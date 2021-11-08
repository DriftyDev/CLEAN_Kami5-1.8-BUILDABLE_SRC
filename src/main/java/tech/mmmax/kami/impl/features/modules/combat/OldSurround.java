/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.server.SPacketBlockChange
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 *  net.minecraftforge.event.entity.living.LivingEvent$LivingJumpEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.combat;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.event.PostPacketEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.Timer;
import tech.mmmax.kami.api.utils.player.InventoryUtils;
import tech.mmmax.kami.api.utils.world.BlockUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class OldSurround
extends Module {
    Timer timeSystem = new Timer();
    Timer disableTimer = new Timer();
    Value<Number> ecThreshold = new ValueBuilder().withDescriptor("EChest Threshold").withValue(0.8).withRange(0.0, 1.0).register(this);
    Value<Boolean> trigger = new ValueBuilder().withDescriptor("Trigger").withValue(true).register(this);
    Value<Boolean> disableJump = new ValueBuilder().withDescriptor("Disable on Jump").withValue(true).register(this);
    Value<Boolean> center = new ValueBuilder().withDescriptor("Center").withValue(true).register(this);
    Value<Number> delay = new ValueBuilder().withDescriptor("Delay").withValue(3).withRange(0, 200).register(this);
    Value<Number> triggerDelay = new ValueBuilder().withDescriptor("Trigger Delay").withValue(3).withRange(1, 2000).register(this);
    Value<Number> blocksPerTick = new ValueBuilder().withDescriptor("Blocks Per Tick").withValue(2).withRange(1, 10).register(this);
    Value<Boolean> floor = new ValueBuilder().withDescriptor("Floor").withValue(false).register(this);
    Value<Boolean> dynamic = new ValueBuilder().withDescriptor("Dynamic").withValue(false).register(this);
    Value<Boolean> sneak = new ValueBuilder().withDescriptor("Sneak").withValue(true).register(this);
    Value<Boolean> antiCity = new ValueBuilder().withDescriptor("Anti City").withValue(false).register(this);
    Value<Boolean> offhandSwap = new ValueBuilder().withDescriptor("Offhand Swap").withValue(false).register(this);
    List<BlockPos> placedBlocks = new ArrayList<BlockPos>();
    BlockPos[] offsets = new BlockPos[]{new BlockPos(0, -1, 0), new BlockPos(1, -1, 0), new BlockPos(0, -1, 1), new BlockPos(-1, -1, 0), new BlockPos(0, -1, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0), new BlockPos(0, 0, -1)};

    public OldSurround() {
        super("OldSurround", Feature.Category.Combat);

        this.timeSystem.setDelay((delay.getValue()).longValue());

        this.disableTimer.setDelay((triggerDelay.getValue()).longValue());
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (NullUtils.nullCheck()) {
            return;
        }
        this.timeSystem.resetDelay();
        if (this.center.getValue().booleanValue()) {
            OldSurround.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position((double)this.getCenterOfBlock().getX() + 0.5, OldSurround.mc.player.posY, (double)this.getCenterOfBlock().getZ() + 0.5, OldSurround.mc.player.onGround));
            OldSurround.mc.player.setPosition((double)this.getCenterOfBlock().getX() + 0.5, OldSurround.mc.player.posY, (double)this.getCenterOfBlock().getZ() + 0.5);
        }
        this.placedBlocks.clear();
        this.disableTimer.resetDelay();
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        int oldSlot = OldSurround.mc.player.inventory.currentItem;
        Item offhandItem = OldSurround.mc.player.getHeldItemOffhand().getItem();
        if (InventoryUtils.getHotbarItemSlot(Item.getItemFromBlock((Block)Blocks.OBSIDIAN)) == -1) {
            this.setEnabled(false);
            return;
        }
        if (!OldSurround.mc.player.onGround && this.disableJump.getValue().booleanValue()) {
            this.setEnabled(false);
            return;
        }
        if (this.timeSystem.isPassed()) {
            int blocksInTick = 0;
            int hotbarSlot = InventoryUtils.getHotbarItemSlot(Item.getItemFromBlock((Block)Blocks.OBSIDIAN));
            if (hotbarSlot == -1) {
                return;
            }
            BlockPos[] offsetArray = this.dynamic.getValue() != false ? this.getDynamicOffsets() : this.addToPlayer(this.offsets);
            List<BlockPos> verifiedPositions = this.getPlacableSurrounds(offsetArray);
            if (verifiedPositions.isEmpty()) {
                return;
            }
            if (this.sneak.getValue().booleanValue()) {
                mc.getConnection().sendPacket((Packet)new CPacketEntityAction((Entity)OldSurround.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            }
            if (this.offhandSwap.getValue().booleanValue()) {
                InventoryUtils.moveItemToOffhand(Item.getItemFromBlock((Block)Blocks.OBSIDIAN));
            } else {
                InventoryUtils.switchToSlotGhost(hotbarSlot);
            }
            for (BlockPos pos : verifiedPositions) {
                if (pos == null) break;
                if (!OldSurround.mc.world.getBlockState(pos).getMaterial().isReplaceable() || !BlockUtils.placeBlock(pos, false, this.offhandSwap.getValue() != false ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND)) continue;
                this.placedBlocks.add(pos);
                if (++blocksInTick <= this.blocksPerTick.getValue().intValue()) continue;
                break;
            }
            if (this.offhandSwap.getValue().booleanValue()) {
                InventoryUtils.moveItemToOffhand(offhandItem);
            } else {
                InventoryUtils.switchToSlotGhost(oldSlot);
            }
            this.timeSystem.resetDelay();
            if (this.sneak.getValue().booleanValue()) {
                mc.getConnection().sendPacket((Packet)new CPacketEntityAction((Entity)OldSurround.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            }
        }
        if (this.disableTimer.isPassed() && this.trigger.getValue().booleanValue()) {
            this.setEnabled(false);
        }
    }

    @SubscribeEvent
    public void onPostPacket(PostPacketEvent event) {
        SPacketBlockChange packet;
        if (NullUtils.nullCheck()) {
            return;
        }
        if (event.getPacket() instanceof SPacketBlockChange && this.antiCity.getValue().booleanValue() && (packet = (SPacketBlockChange)event.getPacket()).getBlockState().getBlock() == Blocks.AIR && OldSurround.mc.player.getDistance((double)packet.getBlockPosition().getX(), (double)packet.getBlockPosition().getY(), (double)packet.getBlockPosition().getZ()) <= 2.0) {
            this.onUpdate(null);
        }
    }

    List<BlockPos> getPlacableSurrounds(BlockPos[] offsets) {
        ArrayList<BlockPos> allowed = new ArrayList<BlockPos>();
        for (BlockPos pos : offsets) {
            if (pos == null || !OldSurround.mc.world.getBlockState(pos).getMaterial().isReplaceable()) continue;
            boolean allow = true;
            for (Entity entity : OldSurround.mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos))) {
                if (!(entity instanceof EntityPlayer)) continue;
                allow = false;
                break;
            }
            if (!allow) continue;
            allowed.add(pos);
        }
        return allowed;
    }

    @SubscribeEvent
    public void onJump(LivingEvent.LivingJumpEvent e) {
        if (e.getEntity().equals((Object)OldSurround.mc.player)) {
            if (this.disableJump.getValue().booleanValue()) {
                this.setEnabled(false);
            }
        } else if (e.getEntity() == OldSurround.mc.player && this.disableJump.getValue().booleanValue()) {
            this.setEnabled(false);
        }
    }

    BlockPos[] getDynamicOffsets() {
        int xOffset = 0;
        BlockPos[] positions = new BlockPos[1000];
        BlockPos playerPos = this.getPlayerPos();
        double borderMax = 0.7;
        double borderMin = 0.3;
        double decimalX = OldSurround.mc.player.posX - Math.floor(OldSurround.mc.player.posX);
        double decimalZ = OldSurround.mc.player.posZ - Math.floor(OldSurround.mc.player.posZ);
        int n = decimalX >= borderMax ? 1 : (xOffset = decimalX <= borderMin ? -1 : 0);
        int zOffset = decimalZ >= borderMax ? 1 : (decimalZ <= borderMin ? -1 : 0);
        int i = 0;
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            if (facing == EnumFacing.EAST) {
                positions[i] = playerPos.add(decimalX >= borderMax ? facing.getXOffset() : 0, -1, 0);
                positions[++i] = playerPos.add(decimalX >= borderMax ? facing.getXOffset() * 2 : facing.getXOffset(), -1, 0);
                positions[++i] = playerPos.add(decimalX >= borderMax ? facing.getXOffset() * 2 : facing.getXOffset(), 0, 0);
                ++i;
                if (zOffset != 0) {
                    positions[i] = playerPos.add(decimalX >= borderMax ? facing.getXOffset() : 0, -1, zOffset);
                    positions[++i] = playerPos.add(decimalX >= borderMax ? facing.getXOffset() * 2 : facing.getXOffset(), -1, zOffset);
                    positions[++i] = playerPos.add(decimalX >= borderMax ? facing.getXOffset() * 2 : facing.getXOffset(), 0, zOffset);
                    ++i;
                }
            }
            if (facing == EnumFacing.WEST) {
                positions[i] = playerPos.add(decimalX <= borderMin ? facing.getXOffset() : 0, -1, 0);
                positions[++i] = playerPos.add(decimalX <= borderMin ? facing.getXOffset() * 2 : facing.getXOffset(), -1, 0);
                positions[++i] = playerPos.add(decimalX <= borderMin ? facing.getXOffset() * 2 : facing.getXOffset(), 0, 0);
                ++i;
                if (zOffset != 0) {
                    positions[i] = playerPos.add(decimalX <= borderMin ? facing.getXOffset() : 0, -1, zOffset);
                    positions[++i] = playerPos.add(decimalX <= borderMin ? facing.getXOffset() * 2 : facing.getXOffset(), -1, zOffset);
                    positions[++i] = playerPos.add(decimalX <= borderMin ? facing.getXOffset() * 2 : facing.getXOffset(), 0, zOffset);
                    ++i;
                }
            }
            if (facing == EnumFacing.NORTH) {
                positions[i] = playerPos.add(0, -1, decimalZ <= borderMin ? facing.getZOffset() : 0);
                positions[++i] = playerPos.add(0, -1, decimalZ <= borderMin ? facing.getZOffset() * 2 : facing.getZOffset());
                positions[++i] = playerPos.add(0, 0, decimalZ <= borderMin ? facing.getZOffset() * 2 : facing.getZOffset());
                ++i;
                if (xOffset != 0) {
                    positions[i] = playerPos.add(xOffset, -1, decimalZ <= borderMin ? facing.getZOffset() : 0);
                    positions[++i] = playerPos.add(xOffset, -1, decimalZ <= borderMin ? facing.getZOffset() * 2 : facing.getZOffset());
                    positions[++i] = playerPos.add(xOffset, 0, decimalZ <= borderMin ? facing.getZOffset() * 2 : facing.getZOffset());
                    ++i;
                }
            }
            if (facing != EnumFacing.SOUTH) continue;
            positions[i] = playerPos.add(0, -1, decimalZ >= borderMax ? facing.getZOffset() : 0);
            positions[++i] = playerPos.add(0, -1, decimalZ >= borderMax ? facing.getZOffset() * 2 : facing.getZOffset());
            positions[++i] = playerPos.add(0, 0, decimalZ >= borderMax ? facing.getZOffset() * 2 : facing.getZOffset());
            ++i;
            if (xOffset == 0) continue;
            positions[i] = playerPos.add(xOffset, -1, decimalZ >= borderMax ? facing.getZOffset() : 0);
            positions[++i] = playerPos.add(xOffset, -1, decimalZ >= borderMax ? facing.getZOffset() * 2 : facing.getZOffset());
            positions[++i] = playerPos.add(xOffset, 0, decimalZ >= borderMax ? facing.getZOffset() * 2 : facing.getZOffset());
            ++i;
        }
        return positions;
    }

    BlockPos[] addToPlayer(BlockPos[] offsets) {
        BlockPos[] positions = new BlockPos[1000];
        int i = 0;
        for (BlockPos b : offsets) {
            if (b == null) continue;
            positions[i] = this.getPlayerPos().add((Vec3i)b);
            ++i;
        }
        return positions;
    }

    BlockPos getCenterOfBlock() {
        double newX = Math.floor(OldSurround.mc.player.posX) + 0.5;
        double newZ = Math.floor(OldSurround.mc.player.posZ) + 0.5;
        return new BlockPos(newX, OldSurround.mc.player.posY, newZ);
    }

    BlockPos getPlayerPos() {
        double decimalPoint = OldSurround.mc.player.posY - Math.floor(OldSurround.mc.player.posY);
        return new BlockPos(this.floor.getValue() != false ? Math.floor(OldSurround.mc.player.posX) : OldSurround.mc.player.posX, decimalPoint > this.ecThreshold.getValue().doubleValue() ? Math.floor(OldSurround.mc.player.posY) + 1.0 : Math.floor(OldSurround.mc.player.posY), this.floor.getValue() != false ? Math.floor(OldSurround.mc.player.posZ) : OldSurround.mc.player.posZ);
    }
}

