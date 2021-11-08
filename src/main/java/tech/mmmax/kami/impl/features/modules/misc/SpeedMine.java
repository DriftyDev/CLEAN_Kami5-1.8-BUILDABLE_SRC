/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.ItemPickaxe
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.World
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$LeftClickBlock
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.misc;

import java.awt.Color;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.event.PacketEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.Timer;
import tech.mmmax.kami.api.utils.chat.ChatMessage;
import tech.mmmax.kami.api.utils.chat.ChatUtils;
import tech.mmmax.kami.api.utils.player.InventoryUtils;
import tech.mmmax.kami.api.utils.render.RenderUtil;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class SpeedMine
extends Module {
    Timer breakTimer = new Timer();
    Timer switchTimer = new Timer();
    public static SpeedMine INSTANCE;
    Value<Boolean> render = new ValueBuilder().withDescriptor("Render").withValue(true).register(this);
    Value<Boolean> animateFill = new ValueBuilder().withDescriptor("Animate Fill").withValue(true).register(this);
    Value<Boolean> animateOutline = new ValueBuilder().withDescriptor("Animate Line").withValue(false).register(this);
    Value<Number> delay = new ValueBuilder().withDescriptor("Delay").withValue(200).withRange(20, 2000).register(this);
    Value<Number> breakAttempts = new ValueBuilder().withDescriptor("Break Attempts").withValue(2).withRange(1, 5).register(this);
    Value<Boolean> autoBreakGhostSwitch = new ValueBuilder().withDescriptor("Ghost Switch AB").withValue(false).register(this);
    Value<Boolean> autoBreak = new ValueBuilder().withDescriptor("AutoBreak").withValue(false).register(this);
    Value<Boolean> noBed = new ValueBuilder().withDescriptor("No Bed").withValue(false).register(this);
    Value<Number> stopCount = new ValueBuilder().withDescriptor("Stop count").withValue(2).withRange(1, 4).register(this);
    Value<Boolean> cancel = new ValueBuilder().withDescriptor("Cancel").withValue(false).register(this);
    Value<String> mode = new ValueBuilder().withDescriptor("Mode").withValue("Packet").withModes("Packet", "Instant", "InstantBypass").register(this);
    Value<Boolean> unbreak = new ValueBuilder().withDescriptor("Unbreak").withValue(false).register(this);
    Value<Boolean> ghostSwitch = new ValueBuilder().withDescriptor("Ghost Switch").withValue(true).register(this);
    Value<Boolean> ghostSwitchPacket = new ValueBuilder().withDescriptor("Ghost Switch Packet").withValue(true).register(this);
    Value<Boolean> debug = new ValueBuilder().withDescriptor("Debug").withValue(false).register(this);
    Value<Color> outline = new ValueBuilder().withDescriptor("Outline").withValue(new Color(255, 255, 255, 255)).register(this);
    Value<Color> fill = new ValueBuilder().withDescriptor("Fill").withValue(new Color(158, 50, 255, 81)).register(this);
    BlockPos breaking;
    boolean packetCancel = false;
    EnumFacing face;
    int currentBreakAttempts = 0;
    boolean firstBreak = false;
    long startTime = 0L;

    public SpeedMine() {
        super("SpeedMine", Feature.Category.Misc);
        INSTANCE = this;

        this.breakTimer.setDelay((delay.getValue()).longValue());

        this.delay.setActive(autoBreak.getValue());
        this.autoBreakGhostSwitch.setActive(autoBreak.getValue());

        this.autoBreak.setActive((mode.getValue()).contains("Instant"));
        this.delay.setActive((mode.getValue()).contains("Instant"));
        this.autoBreakGhostSwitch.setActive((mode.getValue()).contains("Instant"));
        this.stopCount.setActive((mode.getValue()).contains("Instant"));
        this.animateFill.setActive((mode.getValue()).equals("Packet"));
        this.animateOutline.setActive((mode.getValue()).equals("Packet"));
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.currentBreakAttempts = 0;
    }

    @SubscribeEvent
    public void onBlockClick(PlayerInteractEvent.LeftClickBlock event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (this.canBreak(event.getPos())) {
            if (this.breaking == null || !this.breaking.equals((Object)event.getPos())) {
                this.startTime = System.currentTimeMillis();
            }
            if (this.mode.getValue().equals("Packet")) {
                if (this.breaking != null && this.unbreak.getValue().booleanValue()) {
                    mc.getConnection().sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.breaking, this.face));
                    mc.getConnection().sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.breaking, this.face));
                }
                mc.getConnection().sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFace()));
                mc.getConnection().sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFace()));
                float breakTime = SpeedMine.mc.world.getBlockState(event.getPos()).getBlockHardness((World)SpeedMine.mc.world, event.getPos()) * 20.0f * 2.0f;
                this.switchTimer.setDelay((long)breakTime);
                this.switchTimer.resetDelay();
            }
            if (this.mode.getValue().equals("Instant")) {
                if (this.breaking == null || !this.breaking.equals((Object)event.getPos())) {
                    this.packetCancel = false;
                    mc.getConnection().sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFace()));
                    this.currentBreakAttempts = 0;
                    this.packetCancel = true;
                    this.firstBreak = true;
                }
                for (int i = 0; i < this.stopCount.getValue().intValue(); ++i) {
                    mc.getConnection().sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFace()));
                }
                if (!this.firstBreak) {
                    ++this.currentBreakAttempts;
                }
            }
            if (this.mode.getValue().equals("InstantBypass")) {
                if (this.breaking == null || !this.breaking.equals((Object)event.getPos())) {
                    this.packetCancel = false;
                    mc.getConnection().sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFace()));
                    this.currentBreakAttempts = 0;
                    this.packetCancel = true;
                    this.firstBreak = true;
                }
                for (int i = 0; i < this.stopCount.getValue().intValue(); ++i) {
                    mc.getConnection().sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFace()));
                }
                mc.getConnection().sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, event.getPos(), event.getFace()));
                if (!this.firstBreak) {
                    ++this.currentBreakAttempts;
                }
            }
            this.breaking = event.getPos();
            this.face = event.getFace();
            if (this.cancel.getValue().booleanValue()) {
                event.setCanceled(true);
            }
        }
    }

    public void pollBreak() {
        if (!this.switchTimer.isPassed()) {
            return;
        }
        this.switchTimer.resetDelay();
        int pickSlot = -1;
        int oldSlot = SpeedMine.mc.player.inventory.currentItem;
        if (this.ghostSwitch.getValue().booleanValue() && this.mode.getValue().equals("Packet") || this.autoBreakGhostSwitch.getValue().booleanValue() && this.mode.getValue().contains("Instant")) {
            for (int i = 0; i < 9; ++i) {
                if (!(SpeedMine.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemPickaxe)) continue;
                pickSlot = i;
            }
            if (pickSlot != -1) {
                InventoryUtils.switchToSlotGhost(pickSlot);
            }
        }
        if (this.ghostSwitchPacket.getValue().booleanValue()) {
            mc.getConnection().sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.breaking, this.face));
        }
        if (this.ghostSwitch.getValue().booleanValue() && pickSlot != -1) {
            InventoryUtils.switchToSlotGhost(oldSlot);
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (this.breaking != null) {
            if (SpeedMine.mc.world.getBlockState(this.breaking).getBlock() == Blocks.AIR) {
                if (this.mode.getValue().equals("Packet")) {
                    this.breaking = null;
                }
                this.currentBreakAttempts = 0;
                this.firstBreak = false;
                return;
            }
            if (this.mode.getValue().contains("Instant") && this.face != null && this.breakTimer.isPassed()) {
                if (this.autoBreak.getValue().booleanValue()) {
                    int oldSlot = SpeedMine.mc.player.inventory.currentItem;
                    int pickSlot = this.getPickSlot();
                    if (pickSlot == -1) {
                        return;
                    }
                    if (this.noBed.getValue().booleanValue() && SpeedMine.mc.world.getBlockState(this.breaking).getBlock() == Blocks.BED) {
                        return;
                    }
                    if (this.autoBreakGhostSwitch.getValue().booleanValue()) {
                        InventoryUtils.switchToSlotGhost(pickSlot);
                    }
                    if (this.autoBreakGhostSwitch.getValue().booleanValue() || SpeedMine.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemPickaxe) {
                        if (this.currentBreakAttempts <= this.breakAttempts.getValue().intValue()) {
                            int i;
                            if (this.mode.getValue().equals("Instant")) {
                                for (i = 0; i < this.stopCount.getValue().intValue(); ++i) {
                                    mc.getConnection().sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.breaking, this.face));
                                }
                                ++this.currentBreakAttempts;
                            }
                            if (this.mode.getValue().equals("InstantBypass")) {
                                for (i = 0; i < this.stopCount.getValue().intValue(); ++i) {
                                    mc.getConnection().sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.breaking, this.face));
                                }
                                mc.getConnection().sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.breaking, this.face));
                                ++this.currentBreakAttempts;
                            }
                        } else if ((this.mode.getValue().equals("Instant") || this.mode.getValue().equals("InstantBypass")) && !this.firstBreak) {
                            ChatUtils.sendMessage(new ChatMessage("Restarting block break", false, 0));
                            mc.getConnection().sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.breaking, this.face));
                            this.breaking = null;
                            this.currentBreakAttempts = 0;
                        }
                    }
                    if (this.autoBreakGhostSwitch.getValue().booleanValue()) {
                        InventoryUtils.switchToSlotGhost(oldSlot);
                    }
                }
                this.breakTimer.resetDelay();
            }
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if ((this.mode.getValue().equals("Instant") || this.mode.getValue().equals("InstantBypass")) && event.getPacket() instanceof CPacketPlayerDigging && ((CPacketPlayerDigging)event.getPacket()).getAction() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK && this.packetCancel) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (this.breaking != null && this.render.getValue().booleanValue()) {
            if (SpeedMine.mc.world.getBlockState(this.breaking).getBlock() == Blocks.AIR && this.mode.getValue().equals("Packet")) {
                this.breaking = null;
                return;
            }
            AxisAlignedBB fillBB = new AxisAlignedBB(this.breaking);
            AxisAlignedBB lineBB = new AxisAlignedBB(this.breaking);
            if (this.mode.getValue().equals("Packet")) {
                float breakTime = SpeedMine.mc.world.getBlockState(this.breaking).getBlockHardness((World)SpeedMine.mc.world, this.breaking) * 20.0f * 2.0f;
                double shrinkFactor = this.normalize(System.currentTimeMillis() - this.startTime, 0.0, breakTime);
                shrinkFactor = MathHelper.clamp((double)shrinkFactor, (double)0.0, (double)1.0);
                if (this.animateFill.getValue().booleanValue()) {
                    fillBB = fillBB.shrink(shrinkFactor);
                }
                if (this.animateOutline.getValue().booleanValue()) {
                    lineBB = lineBB.shrink(shrinkFactor);
                }
            }
            RenderUtil.renderBB(7, fillBB, this.fill.getValue(), this.fill.getValue());
            RenderUtil.renderBB(3, lineBB, this.outline.getValue(), this.outline.getValue());
        }
    }

    @Override
    public String getHudInfo() {
        return this.mode.getValue();
    }

    boolean canBreak(BlockPos pos) {
        IBlockState blockState = SpeedMine.mc.world.getBlockState(pos);
        Block block = blockState.getBlock();
        return block.getBlockHardness(blockState, (World)SpeedMine.mc.world, pos) != -1.0f;
    }

    int getPickSlot() {
        int pickSlot = -1;
        for (int i = 0; i < 9; ++i) {
            if (!(SpeedMine.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemPickaxe)) continue;
            pickSlot = i;
            break;
        }
        return pickSlot;
    }

    double normalize(double value, double min, double max) {
        return (value - min) / (max - min);
    }
}

