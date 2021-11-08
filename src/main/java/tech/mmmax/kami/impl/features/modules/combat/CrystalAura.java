/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.init.Items
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.network.play.server.SPacketSoundEffect
 *  net.minecraft.network.play.server.SPacketSpawnObject
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.SoundCategory
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  org.lwjgl.opengl.GL11
 */
package tech.mmmax.kami.impl.features.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;
import tech.mmmax.kami.api.event.PacketEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.Timer;
import tech.mmmax.kami.api.utils.player.InventoryUtils;
import tech.mmmax.kami.api.utils.player.RotationUtil;
import tech.mmmax.kami.api.utils.player.TargetUtils;
import tech.mmmax.kami.api.utils.render.RenderUtil;
import tech.mmmax.kami.api.utils.world.CrystalUtil;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;
import tech.mmmax.kami.impl.features.modules.misc.SpeedMine;

public class CrystalAura
extends Module {
    Timer placeTimer = new Timer();
    Timer breakTimer = new Timer();
    Value<String> page = new ValueBuilder().withDescriptor("Page").withValue("Render").withModes("Render", "Calc", "Place", "Break", "Delays", "Dev").register(this);
    Value<Number> minDamage = new ValueBuilder().withDescriptor("Min Damage").withValue(4.0).withRange(0.0, 20.0).register(this);
    Value<Number> maxSelfDamage = new ValueBuilder().withDescriptor("Max Self Damage").withValue(15.0).withRange(0.0, 36d).register(this);
    Value<Boolean> antiSuicide = new ValueBuilder().withDescriptor("Anti Suicide").withValue(false).register(this);
    Value<Number> antiSuicideHealth = new ValueBuilder().withDescriptor("Anti Suicide Health").withValue(36).withRange(0, 36).register(this);
    Value<Number> antiSuicideFactor = new ValueBuilder().withDescriptor("Anti Suicide Factor").withValue(4).withRange(0, 10).register(this);
    Value<Number> lethalHealth = new ValueBuilder().withDescriptor("Lethal Health").withValue(18).withRange(0, 36).register(this);
    Value<Number> lethalMinDmg = new ValueBuilder().withDescriptor("Lethal Min Damage").withValue(2).withRange(0, 36).register(this);
    Value<Number> lethalMaxSelfDmg = new ValueBuilder().withDescriptor("Lethal Max Self DMG").withValue(36).withRange(0, 36).register(this);
    Value<Number> targetRange = new ValueBuilder().withDescriptor("Target Range").withValue(7.0).withRange(3.0, 20.0).register(this);
    Value<Number> range = new ValueBuilder().withDescriptor("Range").withValue(5).withRange(1, 10).register(this);
    Value<Number> wallsRange = new ValueBuilder().withDescriptor("Walls Range").withValue(3).withRange(1, 5).register(this);
    Value<Number> raytraceHits = new ValueBuilder().withDescriptor("Raytrace Hits").withValue(2).withRange(1, 9).register(this);
    Value<Number> shrinkFactor = new ValueBuilder().withDescriptor("Shrink Factor").withValue(0.3).withRange(0d, 1d).register(this);
    Value<Number> breakDelay = new ValueBuilder().withDescriptor("Break Delay").withValue(1).withRange(0, 1000).register(this);
    Value<Number> placeDelay = new ValueBuilder().withDescriptor("Place Delay").withValue(1).withRange(0, 1000).register(this);
    Value<Boolean> antiSurround = new ValueBuilder().withDescriptor("Anti Surround").withValue(false).register(this);
    Value<Number> antiSurroundDelay = new ValueBuilder().withDescriptor("Anti Surround Delay").withValue(1).withRange(0, 1000).register(this);
    Value<Boolean> antiStuck = new ValueBuilder().withDescriptor("Anti Stuck").withValue(false).register(this);
    Value<Boolean> placeCrystals = new ValueBuilder().withDescriptor("Place").withValue(true).register(this);
    Value<Boolean> antiChorus = new ValueBuilder().withDescriptor("Anti Chorus").withValue(false).register(this);
    Value<String> swingMode = new ValueBuilder().withDescriptor("Swing Mode").withValue("Auto").withModes("Auto", "Mainhand", "Offhand", "None").register(this);
    Value<Boolean> one13 = new ValueBuilder().withDescriptor("1.13").withValue(false).register(this);
    Value<Boolean> packetPlace = new ValueBuilder().withDescriptor("Packet Place").withValue(true).register(this);
    Value<String> switchMode = new ValueBuilder().withDescriptor("Switch Mode").withValue("None").withModes("None", "Normal", "Silent").register(this);
    Value<Boolean> breakCrystals = new ValueBuilder().withDescriptor("Break").withValue(true).register(this);
    Value<Boolean> breakRotate = new ValueBuilder().withDescriptor("Break Rotate").withValue(true).register(this);
    Value<Number> breakAttempts = new ValueBuilder().withDescriptor("Break Attempts").withValue(1).withRange(1, 10).register(this);
    Value<Boolean> setDead = new ValueBuilder().withDescriptor("Set Dead").withValue(true).register(this);
    Value<Boolean> inhibit = new ValueBuilder().withDescriptor("Inhibit").withValue(true).register(this);
    Value<Boolean> onlyOwn = new ValueBuilder().withDescriptor("Only Own").withValue(true).register(this);
    Value<Boolean> sequential = new ValueBuilder().withDescriptor("Sequential").withValue(true).register(this);
    Value<Boolean> packetBreak = new ValueBuilder().withDescriptor("Packet Break").withValue(true).register(this);
    Value<Boolean> predict = new ValueBuilder().withDescriptor("Predict").withValue(false).register(this);
    Value<Boolean> smartPredict = new ValueBuilder().withDescriptor("Smart Predict").withValue(true).register(this);
    Value<Number> breakPredictAttempts = new ValueBuilder().withDescriptor("Predict Attempts").withValue(3).withRange(1, 10).register(this);
    Value<Boolean> autoSkip = new ValueBuilder().withDescriptor("Auto Skip").withValue(true).register(this);
    Value<Number> skip = new ValueBuilder().withDescriptor("Predict Skip").withValue(2).withRange(1, 10).register(this);
    Value<Number> startFactor = new ValueBuilder().withDescriptor("Predict Start").withValue(2).withRange(0, 10).register(this);
    Value<Number> add = new ValueBuilder().withDescriptor("Predict Add").withValue(2).withRange(0, 10).register(this);
    Value<Boolean> instaminePoll = new ValueBuilder().withDescriptor("Instamine Poll").withValue(false).register(this);
    Value<Color> fillColorS = new ValueBuilder().withDescriptor("Fill Color").withValue(new Color(0, 0, 0, 100)).register(this);
    Value<Color> lineColorS = new ValueBuilder().withDescriptor("Outline Color").withValue(new Color(255, 255, 255, 255)).register(this);
    EntityLivingBase target;
    CrystalUtil.Crystal placePos;
    List<CrystalUtil.Crystal> oldPlacements = new ArrayList<CrystalUtil.Crystal>();
    int highestID;
    int lastSkip;
    int curAlpha = this.fillColorS.getValue().getAlpha();
    boolean shouldPredict = false;
    int currStuck = 0;
    long lastBroke = System.currentTimeMillis();

    public void handlePage(String page) {
        this.fillColorS.setActive(page.equals("Render"));
        this.lineColorS.setActive(page.equals("Render"));
        this.minDamage.setActive(page.equals("Calc"));
        this.maxSelfDamage.setActive(page.equals("Calc"));
        this.antiSuicide.setActive(page.equals("Calc"));
        this.antiSuicideHealth.setActive(page.equals("Calc"));
        this.antiSuicideFactor.setActive(page.equals("Calc"));
        this.lethalHealth.setActive(page.equals("Calc"));
        this.lethalMinDmg.setActive(page.equals("Calc"));
        this.lethalMaxSelfDmg.setActive(page.equals("Calc"));
        this.targetRange.setActive(page.equals("Calc"));
        this.range.setActive(page.equals("Calc"));
        this.wallsRange.setActive(page.equals("Calc"));
        this.raytraceHits.setActive(page.equals("Calc"));
        this.shrinkFactor.setActive(page.equals("Calc"));
        this.placeCrystals.setActive(page.equals("Place"));
        this.antiChorus.setActive(page.equals("Place"));
        this.swingMode.setActive(page.equals("Place"));
        this.one13.setActive(page.equals("Place"));
        this.packetPlace.setActive(page.equals("Place"));
        this.switchMode.setActive(page.equals("Place"));
        this.breakCrystals.setActive(page.equals("Break"));
        this.breakRotate.setActive(page.equals("Break"));
        this.breakAttempts.setActive(page.equals("Break"));
        this.setDead.setActive(page.equals("Break"));
        this.inhibit.setActive(page.equals("Break"));
        this.onlyOwn.setActive(page.equals("Break"));
        this.sequential.setActive(page.equals("Break"));
        this.packetBreak.setActive(page.equals("Break"));
        this.predict.setActive(page.equals("Break"));
        this.smartPredict.setActive(page.equals("Break") && this.predict.getValue() != false);
        this.breakPredictAttempts.setActive(page.equals("Break") && this.predict.getValue() != false);
        this.autoSkip.setActive(page.equals("Break") && this.predict.getValue() != false);
        this.skip.setActive(page.equals("Break") && this.predict.getValue() != false);
        this.startFactor.setActive(page.equals("Break") && this.predict.getValue() != false);
        this.add.setActive(page.equals("Break") && this.predict.getValue() != false);
        this.breakDelay.setActive(page.equals("Delays"));
        this.placeDelay.setActive(page.equals("Delays"));
        this.antiSurround.setActive(page.equals("Delays"));
        this.antiSurroundDelay.setActive(page.equals("Delays"));
        this.antiStuck.setActive(page.equals("Delays"));
        this.instaminePoll.setActive(page.equals("Dev"));
    }

    public CrystalAura() {
        super("Crystal Aura", Feature.Category.Combat);

        this.handlePage(page.getValue());
    }

    @Override
    public String getHudInfo() {
        return this.target != null ? this.target.getName() + ", " + (double)(System.currentTimeMillis() - this.breakTimer.getStartTime()) / 10.0 + (this.placePos != null ? ", " + (this.antiSuicide.getValue() != false ? this.placePos.getEnemyDamage() - this.placePos.getSelfDamage() : this.placePos.getEnemyDamage()) : "") : "";
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.breakTimer.resetDelay();
        this.breakTimer.setPaused(false);
        this.placeTimer.resetDelay();
        this.placeTimer.setPaused(false);
        this.highestID = 0;
        this.currStuck = 0;
        if (NullUtils.nullCheck()) {
            return;
        }
        RotationUtil.INSTANCE.rotating = false;
        RotationUtil.INSTANCE.resetRotations();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (NullUtils.nullCheck()) {
            return;
        }
        RotationUtil.INSTANCE.rotating = false;
        RotationUtil.INSTANCE.resetRotations();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (RotationUtil.INSTANCE.rotatedYaw == RotationUtil.INSTANCE.yaw && RotationUtil.INSTANCE.rotatedPitch == RotationUtil.INSTANCE.pitch) {
            RotationUtil.INSTANCE.rotating = false;
            RotationUtil.INSTANCE.resetRotations();
        }
        this.target = TargetUtils.getTarget(this.targetRange.getValue().doubleValue());
        if (this.target == null) {
            return;
        }
        boolean antiSurrounding = this.target.motionX == 0.0 && this.target.motionZ == 0.0;
        this.placeTimer.setDelay(this.placeDelay.getValue().longValue());
        this.breakTimer.setDelay(antiSurrounding ? this.antiSurroundDelay.getValue().longValue() : this.breakDelay.getValue().longValue());
        this.doCrystalAura();
    }

    public void doCrystalAura() {
        if (this.placeTimer.isPassed()) {
            if (this.placeCrystals.getValue().booleanValue()) {
                boolean lethal = (double)(this.target.getHealth() + this.target.getAbsorptionAmount()) <= this.lethalHealth.getValue().doubleValue();
                this.placePos = CrystalUtil.getPlacePos((Entity)this.target, this.range.getValue().doubleValue(), this.wallsRange.getValue().doubleValue(), this.one13.getValue(), 0.0, this.antiSuicide.getValue() != false && (double)(CrystalAura.mc.player.getHealth() + CrystalAura.mc.player.getAbsorptionAmount()) <= this.antiSuicideHealth.getValue().doubleValue(), this.antiSuicideFactor.getValue().doubleValue(), lethal ? this.lethalMinDmg.getValue().doubleValue() : this.minDamage.getValue().doubleValue(), lethal ? this.lethalMaxSelfDmg.getValue().doubleValue() : this.maxSelfDamage.getValue().doubleValue(), false, false, this.raytraceHits.getValue().intValue(), this.shrinkFactor.getValue().doubleValue());
                if (this.placePos != null) {
                    if (this.oldPlacements.contains(this.placePos)) {
                        this.oldPlacements.remove(this.placePos);
                    }
                    this.oldPlacements.add(this.placePos);
                    int oldSlot = CrystalAura.mc.player.inventory.currentItem;
                    if (this.instaminePoll.getValue().booleanValue()) {
                        SpeedMine.INSTANCE.pollBreak();
                    }
                    if (CrystalUtil.getCrystalHand() == EnumHand.MAIN_HAND) {
                        switch (this.switchMode.getValue()) {
                            case "Normal": {
                                InventoryUtils.switchToSlot(Items.END_CRYSTAL);
                            }
                            case "Silent": {
                                int crystalSlot = InventoryUtils.getHotbarItemSlot(Items.END_CRYSTAL);
                                if (crystalSlot == -1) {
                                    return;
                                }
                                InventoryUtils.switchToSlotGhost(crystalSlot);
                            }
                        }
                    }
                    if (CrystalAura.mc.player.getHeldItem(CrystalUtil.getCrystalHand()).getItem() == Items.END_CRYSTAL || this.switchMode.getValue().equals("Silent")) {
                        EnumHand hand = this.swingMode.getValue().equals("None") ? null : (this.swingMode.getValue().equals("Auto") ? CrystalUtil.getCrystalHand() : (this.swingMode.getValue().equals("Offhand") ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND));
                        CrystalUtil.placeCrystal(this.placePos.crystalPos, this.packetPlace.getValue(), hand);
                        this.shouldPredict = true;
                    }
                    if (CrystalUtil.getCrystalHand() == EnumHand.MAIN_HAND && this.switchMode.getValue().equals("Silent")) {
                        InventoryUtils.switchToSlotGhost(oldSlot);
                    }
                }
            }
            this.placeTimer.resetDelay();
            this.placeTimer.setPaused(true);
            this.breakTimer.setPaused(false);
            ++this.currStuck;
            if (this.antiStuck.getValue().booleanValue()) {
                return;
            }
        }
        if (this.breakTimer.isPassed() && this.breakCrystals.getValue().booleanValue()) {
            EntityEnderCrystal crystal = CrystalUtil.getCrystalToBreak(this.inhibit.getValue(), this.range.getValue().doubleValue());
            if (this.predict.getValue().booleanValue()) {
                if (!this.smartPredict.getValue().booleanValue() || this.shouldPredict) {
                    int start = this.highestID + this.startFactor.getValue().intValue();
                    for (int i = 0; i < this.breakPredictAttempts.getValue().intValue(); ++i) {
                        int crystalID = start + i * (this.autoSkip.getValue() != false ? this.lastSkip : this.skip.getValue().intValue());
                        EnumHand hand = this.swingMode.getValue().equals("None") ? null : (this.swingMode.getValue().equals("Auto") ? CrystalUtil.getCrystalHand() : (hand = this.swingMode.getValue().equals("Offhand") ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND));
                        if (CrystalUtil.hitCrystals.contains(crystalID)) continue;
                        CrystalUtil.breakCrystal(crystalID, hand);
                    }
                    this.highestID += this.add.getValue().intValue();
                    this.shouldPredict = false;
                }
            } else if (crystal != null) {
                if (this.breakRotate.getValue().booleanValue()) {
                    RotationUtil.INSTANCE.rotating = true;
                    RotationUtil.INSTANCE.rotate(crystal.getPositionVector());
                }
                EnumHand hand = this.swingMode.getValue().equals("None") ? null : (this.swingMode.getValue().equals("Auto") ? CrystalUtil.getCrystalHand() : (this.swingMode.getValue().equals("Offhand") ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND));
                for (int i = 0; i < this.breakAttempts.getValue().intValue(); ++i) {
                    CrystalUtil.breakCrystal(crystal, this.packetBreak.getValue(), hand);
                }
                this.lastBroke = System.currentTimeMillis();
            }
        }
        this.breakTimer.resetDelay();
        this.placeTimer.setPaused(false);
        this.breakTimer.setPaused(true);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (event.getPacket() instanceof SPacketSpawnObject) {
            SPacketSpawnObject packet = (SPacketSpawnObject)event.getPacket();
            if (this.sequential.getValue().booleanValue() && this.target != null && packet.getType() == 51 && CrystalAura.mc.player.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= this.range.getValue().doubleValue() && (!this.onlyOwn.getValue().booleanValue() || this.checkOnlyOwn(packet.getX(), packet.getY(), packet.getZ()))) {
                EnumHand hand = this.swingMode.getValue().equals("None") ? null : (this.swingMode.getValue().equals("Auto") ? CrystalUtil.getCrystalHand() : (this.swingMode.getValue().equals("Offhand") ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND));
                CrystalUtil.breakCrystal(packet.getEntityID(), hand);
                this.lastBroke = System.currentTimeMillis();
                this.breakTimer.resetDelay();
                this.placeTimer.setPaused(false);
                this.breakTimer.setPaused(true);
            }
            this.checkID(packet.getEntityID());
        }
        if (event.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
            if (this.setDead.getValue().booleanValue() && packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (Entity c : CrystalUtil.getLoadedCrystalsInRange(this.range.getValue().doubleValue())) {
                    if (!(c.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0)) continue;
                    c.setDead();
                }
            }
            if (this.antiChorus.getValue().booleanValue() && (packet.getSound() == SoundEvents.ENTITY_ENDERMEN_TELEPORT || packet.getSound() == SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT)) {
                BlockPos pos = new BlockPos(packet.getX(), packet.getY() - 1.0, packet.getZ());
                if (CrystalAura.mc.player.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= this.range.getValue().doubleValue() && CrystalUtil.canPlaceCrystal1(pos, this.one13.getValue(), false) && CrystalUtil.canPlaceCrystal2(pos)) {
                    EnumHand hand = this.swingMode.getValue().equals("None") ? null : (this.swingMode.getValue().equals("Auto") ? CrystalUtil.getCrystalHand() : (this.swingMode.getValue().equals("Offhand") ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND));
                    CrystalUtil.placeCrystal(pos, this.packetPlace.getValue(), hand);
                }
            }
        }
    }

    boolean checkOnlyOwn(double x, double y, double z) {
        CrystalUtil.Crystal crystal = new CrystalUtil.Crystal(new BlockPos(x, y, z), (Entity)this.target, 0.0);
        return (double)crystal.getSelfDamage() < this.maxSelfDamage.getValue().doubleValue() && (double)crystal.getEnemyDamage() > this.minDamage.getValue().doubleValue() && crystal.getSelfDamage() < crystal.getEnemyDamage();
    }

    void checkID(int id) {
        if (id > this.highestID) {
            this.lastSkip = this.highestID - id;
            this.highestID = id;
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (this.target == null || this.placePos == null) {
            return;
        }
        GL11.glLineWidth((float)1.7f);
        RenderUtil.renderBB(7, new AxisAlignedBB(this.placePos.crystalPos), this.fillColorS.getValue(), this.fillColorS.getValue());
        RenderUtil.renderBB(3, new AxisAlignedBB(this.placePos.crystalPos), this.lineColorS.getValue(), this.lineColorS.getValue());
    }

    double normalize(double value, double min, double max) {
        return (value - min) / (max - min);
    }
}

