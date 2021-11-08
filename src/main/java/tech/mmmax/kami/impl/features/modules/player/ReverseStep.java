/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.player;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.event.MoveEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.player.PlayerUtils;
import tech.mmmax.kami.api.utils.world.HoleUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class ReverseStep
extends Module {
    Value<Number> force = new ValueBuilder().withDescriptor("Force").withValue(5).withRange(3, 20).register(this);
    Value<String> mode = new ValueBuilder().withDescriptor("Mode").withValue("Normal").withModes("Normal", "Bypass", "MoveEventCancel").register(this);
    Value<Boolean> Slowwalk = new ValueBuilder().withDescriptor("Slowwalk").withValue(false).register(this);

    public ReverseStep() {
        super("Reverse Step", Feature.Category.Player);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (ReverseStep.mc.player.isInLava() || ReverseStep.mc.player.isInWater() || ReverseStep.mc.player.isOnLadder()) {
            return;
        }
        if ((this.mode.getValue().equals("Normal") || this.mode.getValue().equals("MoveEventCancel")) && ReverseStep.mc.player.onGround) {
            ReverseStep.mc.player.motionY -= this.force.getValue().doubleValue();
        }
        if ((HoleUtils.isBedrockHoles(this.getPlayerPos().down()) || HoleUtils.isObbyHole(this.getPlayerPos().down()) && this.Slowwalk.getValue().booleanValue()) && PlayerUtils.isMoving((EntityLivingBase)ReverseStep.mc.player)) {
            PlayerUtils.setSpeed((EntityLivingBase)ReverseStep.mc.player, 0.05f);
        }
    }

    BlockPos getPlayerPos() {
        double decimalPoint = ReverseStep.mc.player.posY - Math.floor(ReverseStep.mc.player.posY);
        return new BlockPos(ReverseStep.mc.player.posX, decimalPoint > 0.8 ? Math.floor(ReverseStep.mc.player.posY) + 1.0 : Math.floor(ReverseStep.mc.player.posY), ReverseStep.mc.player.posZ);
    }

    @SubscribeEvent
    public void moveEvent(MoveEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (ReverseStep.mc.player.isInLava() || ReverseStep.mc.player.isInWater() || ReverseStep.mc.player.isOnLadder()) {
            return;
        }
        if (this.mode.getValue().equals("MoveEventCancel") && ReverseStep.mc.player.onGround) {
            event.y = 0.0;
        }
        if (this.mode.getValue().equals("Bypass") && ReverseStep.mc.player.onGround && event.y < 0.1) {
            event.y = -this.force.getValue().doubleValue();
            event.moved = true;
        }
    }

    @Override
    public String getHudInfo() {
        return this.mode.getValue();
    }
}

