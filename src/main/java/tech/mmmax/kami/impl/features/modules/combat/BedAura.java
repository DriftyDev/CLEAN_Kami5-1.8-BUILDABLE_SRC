/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.tileentity.TileEntityBed
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.event.PacketEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.Timer;
import tech.mmmax.kami.api.utils.player.InventoryUtils;
import tech.mmmax.kami.api.utils.player.PlayerUtils;
import tech.mmmax.kami.api.utils.player.TargetUtils;
import tech.mmmax.kami.api.utils.render.RenderUtil;
import tech.mmmax.kami.api.utils.world.BlockUtils;
import tech.mmmax.kami.api.utils.world.CrystalUtil;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;
import tech.mmmax.kami.mixin.mixins.access.ICPacketPlayer;

public class BedAura
extends Module {
    Timer placeTimer = new Timer();
    Timer breakTimer = new Timer();
    Value<String> page = new ValueBuilder().withDescriptor("Page").withValue("Ranges").withModes("Ranges", "Calcs", "Place", "Break", "Render").register(this);
    Value<Number> targetRange = new ValueBuilder().withDescriptor("Target Range").withValue(10).withRange(0, 20).withPageParent(this.page).withPage("Ranges").register(this);
    Value<Number> range = new ValueBuilder().withDescriptor("Range").withValue(6).withRange(0, 10).withPageParent(this.page).withPage("Ranges").register(this);
    Value<Number> minDamage = new ValueBuilder().withDescriptor("Min Damage").withValue(2).withRange(0, 36).withPageParent(this.page).withPage("Calcs").register(this);
    Value<Number> maxSelf = new ValueBuilder().withDescriptor("Max Self").withValue(2).withRange(0, 36).withPageParent(this.page).withPage("Calcs").register(this);
    Value<Boolean> fastCalc = new ValueBuilder().withDescriptor("Fast Calc").withValue(false).withPageParent(this.page).withPage("Calcs").register(this);
    Value<Number> placeDelay = new ValueBuilder().withDescriptor("Place Delay").withValue(0).withRange(0, 1000).withPageParent(this.page).withPage("Place").register(this);
    Value<Boolean> placeSneak = new ValueBuilder().withDescriptor("Sneak").withValue(true).withPageParent(this.page).withPage("Place").register(this);
    Value<Boolean> rotate = new ValueBuilder().withDescriptor("Rotate").withValue(true).withPageParent(this.page).withPage("Place").register(this);
    Value<String> swingMode = new ValueBuilder().withDescriptor("Swing Mode").withValue("Auto").withModes("Auto", "MainHand", "OffHand", "None").withPageParent(this.page).withPage("Place").register(this);
    Value<String> switchMode = new ValueBuilder().withDescriptor("Switch Mode").withValue("Normal").withModes("Normal", "Silent", "None").withPageParent(this.page).withPage("Place").register(this);
    Value<Number> breakDelay = new ValueBuilder().withDescriptor("Break Delay").withValue(0).withRange(0, 1000).withPage("Break").register(this);
    Value<Boolean> breakPredict = new ValueBuilder().withDescriptor("Break Predict").withValue(true).withPageParent(this.page).withPage("Break").register(this);
    Value<Boolean> inhibit = new ValueBuilder().withDescriptor("Inhibit").withValue(true).withPageParent(this.page).withPage("Break").register(this);
    Value<Color> fillColor = new ValueBuilder().withDescriptor("Fill Color").withValue(new Color(255, 255, 255, 100)).withPageParent(this.page).withPage("Render").register(this);
    Value<Color> line = new ValueBuilder().withDescriptor("Line Color").withValue(new Color(255, 255, 255, 255)).withPageParent(this.page).withPage("Render").register(this);
    Value<Color> offsetFill = new ValueBuilder().withDescriptor("Offset Fill Color").withValue(new Color(255, 255, 255, 100)).withPageParent(this.page).withPage("Render").register(this);
    Value<Color> offsetLine = new ValueBuilder().withDescriptor("Offset Line Color").withValue(new Color(255, 255, 255, 255)).withPageParent(this.page).withPage("Render").register(this);
    BlockPos mainRender;
    BlockPos facingRender;
    EntityLivingBase target;
    Vec3d rotating;
    boolean shouldRotate;
    List<TileEntityBed> bedsBroken = new ArrayList<TileEntityBed>();

    public BedAura() {
        super("BedAura", Feature.Category.Combat);

        this.placeTimer.setDelay((placeDelay.getValue()).longValue());

        this.breakTimer.setDelay((breakDelay.getValue()).longValue());
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.placeTimer.resetDelay();
        this.breakTimer.resetDelay();
        this.placeTimer.setPaused(false);
        this.breakTimer.setPaused(true);
        this.bedsBroken.clear();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        this.target = TargetUtils.getTarget(this.targetRange.getValue().doubleValue());
        if (this.target == null) {
            this.shouldRotate = false;
            return;
        }
        if (this.placeTimer.isPassed()) {
            double bestDamage = 0.0;
            EnumFacing bestFacing = null;
            BlockPos bestPos = null;
            for (BlockPos p : BlockUtils.getSphere(this.range.getValue().doubleValue(), BedAura.mc.player.getPosition(), true, false)) {
                EnumFacing facing = null;
                float damage = 0.0f;
                List<EnumFacing> facings = BedAura.getBedPlaceableFaces(p);
                if (facings == null) continue;
                BlockPos pos = p.up();
                for (EnumFacing f : facings) {
                    float selfDamage;
                    float d;
                    if (this.fastCalc.getValue().booleanValue()) {
                        facing = f;
                        d = CrystalUtil.calculateDamage(pos.offset(f).getX(), pos.offset(f).getY(), pos.offset(f).getZ(), (Entity)this.target, 0.0);
                        selfDamage = CrystalUtil.calculateDamage((double)pos.offset(f).getX() + 0.5, (double)pos.offset(f).getY() + 0.5, (double)pos.offset(f).getZ() + 0.5, (Entity)BedAura.mc.player, 0.0);
                        if ((double)selfDamage > this.maxSelf.getValue().doubleValue() || (double)d < this.minDamage.getValue().doubleValue()) continue;
                        if (d > damage) {
                            damage = d;
                            facing = f;
                            break;
                        }
                    }
                    d = CrystalUtil.calculateDamage(pos.offset(f).getX(), pos.offset(f).getY(), pos.offset(f).getZ(), (Entity)this.target, 0.0);
                    selfDamage = CrystalUtil.calculateDamage((double)pos.offset(f).getX() + 0.5, (double)pos.offset(f).getY() + 0.5, (double)pos.offset(f).getZ() + 0.5, (Entity)BedAura.mc.player, 0.0);
                    if ((double)selfDamage > this.maxSelf.getValue().doubleValue() || (double)d < this.minDamage.getValue().doubleValue() || !(d > damage)) continue;
                    damage = d;
                    facing = f;
                }
                if (!((double)damage > bestDamage) || facing == null) continue;
                bestDamage = damage;
                bestFacing = facing;
                bestPos = p;
            }
            if (bestPos != null && bestFacing != null) {
                int slot = InventoryUtils.getHotbarItemSlot(Items.BED);
                if (slot == -1) {
                    this.shouldRotate = false;
                } else {
                    int oldSlot = BedAura.mc.player.inventory.currentItem;
                    this.rotating = BedAura.calculateHitPlace(PlayerUtils.getPlayerPos().offset(bestFacing).up(), bestFacing);
                    this.shouldRotate = true;
                    if (this.switchMode.getValue().equals("Normal") && BedAura.mc.player.getHeldItem(this.getBedHand()).getItem() != Items.BED) {
                        InventoryUtils.switchToSlot(slot);
                        BedAura.mc.playerController.updateController();
                    }
                    if (this.switchMode.getValue().equals("Silent") && BedAura.mc.player.getHeldItem(this.getBedHand()).getItem() != Items.BED) {
                        InventoryUtils.switchToSlotGhost(slot);
                    }
                    if (BedAura.mc.player.getHeldItem(this.getBedHand()).getItem() == Items.BED || this.switchMode.getValue().equals("Silent")) {
                        if (this.placeSneak.getValue().booleanValue() && !BedAura.mc.player.isSneaking()) {
                            mc.getConnection().sendPacket((Packet)new CPacketEntityAction((Entity)BedAura.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                        }
                        mc.getConnection().sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(bestPos, EnumFacing.UP, this.getBedHand(), 0.0f, 0.0f, 0.0f));
                        EnumHand swingArm = this.getSwingHand();
                        if (swingArm != null) {
                            BedAura.mc.player.swingArm(swingArm);
                        }
                        BlockPos pos2 = bestPos.offset(bestFacing);
                        this.mainRender = bestPos.up();
                        this.facingRender = bestPos.up().offset(bestFacing);
                        if (this.placeSneak.getValue().booleanValue()) {
                            mc.getConnection().sendPacket((Packet)new CPacketEntityAction((Entity)BedAura.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                        }
                        if (this.breakPredict.getValue().booleanValue()) {
                            mc.getConnection().sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(bestPos, EnumFacing.UP, this.getBedHand(), 0.0f, 0.0f, 0.0f));
                        }
                    }
                    if (this.switchMode.getValue().equals("Silent") && BedAura.mc.player.getHeldItem(this.getBedHand()).getItem() != Items.BED) {
                        InventoryUtils.switchToSlotGhost(oldSlot);
                    }
                }
            } else {
                this.shouldRotate = false;
            }
            this.breakTimer.resetDelay();
            this.placeTimer.setPaused(true);
            this.breakTimer.setPaused(false);
        }
        if (this.breakTimer.isPassed()) {
            for (TileEntity tileEntity : BedAura.mc.world.loadedTileEntityList) {
                if (!(tileEntity instanceof TileEntityBed) || !(BedAura.mc.player.getDistance((double)tileEntity.getPos().getX(), (double)tileEntity.getPos().getY(), (double)tileEntity.getPos().getZ()) < this.range.getValue().doubleValue())) continue;
                mc.getConnection().sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(tileEntity.getPos(), EnumFacing.UP, this.getBedHand(), 0.0f, 0.0f, 0.0f));
                EnumHand swingArm = this.getSwingHand();
                if (swingArm == null) break;
                BedAura.mc.player.swingArm(swingArm);
                break;
            }
            this.placeTimer.resetDelay();
            this.placeTimer.setPaused(false);
            this.breakTimer.setPaused(true);
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof CPacketPlayer && this.rotating != null && this.shouldRotate && this.rotate.getValue().booleanValue()) {
            CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            ICPacketPlayer inter = (ICPacketPlayer)packet;
            float[] rotationVals = BlockUtils.getNeededRotations(this.rotating);
            inter.setPitch(rotationVals[0]);
            inter.setYaw(rotationVals[1]);
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (this.target != null && this.mainRender != null && this.facingRender != null) {
            AxisAlignedBB mainBB = new AxisAlignedBB((double)this.mainRender.getX(), (double)this.mainRender.getY(), (double)this.mainRender.getZ(), (double)(this.mainRender.getX() + 1), (double)this.mainRender.getY() + 0.5, (double)(this.mainRender.getZ() + 1));
            AxisAlignedBB offBB = new AxisAlignedBB((double)this.facingRender.getX(), (double)this.facingRender.getY(), (double)this.facingRender.getZ(), (double)(this.facingRender.getX() + 1), (double)this.facingRender.getY() + 0.5, (double)(this.facingRender.getZ() + 1));
            RenderUtil.renderBB(7, mainBB, this.fillColor.getValue(), this.fillColor.getValue());
            RenderUtil.renderBB(3, mainBB, this.line.getValue(), this.line.getValue());
            RenderUtil.renderBB(7, offBB, this.offsetFill.getValue(), this.offsetFill.getValue());
            RenderUtil.renderBB(3, offBB, this.offsetLine.getValue(), this.offsetLine.getValue());
        }
    }

    public static Vec3d calculateHitPlace(BlockPos pos, EnumFacing facing) {
        return BlockUtils.getEyesPos().add(new Vec3d(facing.getDirectionVec()));
    }

    EnumHand getSwingHand() {
        return this.swingMode.getValue().equals("Auto") ? this.getBedHand() : (this.swingMode.getValue().equals("MainHand") ? EnumHand.MAIN_HAND : (this.swingMode.getValue().equals("OffHand") ? EnumHand.OFF_HAND : null));
    }

    EnumHand getBedHand() {
        return BedAura.mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() == Items.BED ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
    }

    public static Block getBlock(BlockPos pos) {
        return BedAura.mc.world.getBlockState(pos).getBlock();
    }

    public static List<EnumFacing> getBedPlaceableFaces(BlockPos pos) {
        if (!BedAura.mc.world.getBlockState(pos).getMaterial().isSolid() || BedAura.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock() != Blocks.AIR) {
            return null;
        }
        ArrayList<EnumFacing> facingList = new ArrayList<EnumFacing>();
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            if (!BedAura.mc.world.getBlockState(pos.offset(facing)).getMaterial().isSolid() || BedAura.mc.world.getBlockState(pos.add(0, 1, 0).offset(facing)).getBlock() != Blocks.AIR) continue;
            facingList.add(facing);
        }
        return facingList;
    }
}

