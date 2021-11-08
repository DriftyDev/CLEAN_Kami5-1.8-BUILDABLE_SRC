/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraftforge.event.entity.living.LivingEvent$LivingJumpEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.player;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.event.MoveEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.player.PlayerUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;
import tech.mmmax.kami.mixin.mixins.access.IMinecraft;
import tech.mmmax.kami.mixin.mixins.access.ITimer;

public class Strafe
extends Module {
    Value<Number> airSpeed = new ValueBuilder().withDescriptor("Air Speed").withValue(1.2).withRange(1, 1.3).register(this);
    Value<Number> downSpeed = new ValueBuilder().withDescriptor("Down Speed").withValue(1.2).withRange(1, 1.3).register(this);
    Value<Number> groundSpeed = new ValueBuilder().withDescriptor("Ground Speed").withValue(1.2).withRange(1, 1.3).register(this);
    Value<Number> jumpSpeed = new ValueBuilder().withDescriptor("Jump Speed").withValue(1.2).withRange(1, 1.3).register(this);
    Value<Boolean> useTimer = new ValueBuilder().withDescriptor("Use Timer").withValue(false).register(this);
    Value<Number> timerAmount = new ValueBuilder().withDescriptor("Timer Amount").withValue(1.2).withRange(1, 1.3).register(this);
    float oldTickLength;
    ITimer timer;
    boolean jumpBoosting = false;

    public Strafe() {
        super("Strafe", Feature.Category.Player);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.timer = (ITimer)((IMinecraft)mc).getTimer();
        this.oldTickLength = this.timer.getTickLength();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (this.useTimer.getValue().booleanValue()) {
            this.timer.setTickLength(this.oldTickLength);
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (this.useTimer.getValue().booleanValue()) {
            this.timer.setTickLength(this.oldTickLength / this.timerAmount.getValue().floatValue());
        } else {
            this.timer.setTickLength(this.oldTickLength);
        }
        if (PlayerUtils.isMoving((EntityLivingBase)Strafe.mc.player) && Strafe.mc.player.onGround) {
            Strafe.mc.player.jump();
        }
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (PlayerUtils.isMoving((EntityLivingBase)Strafe.mc.player)) {
            float speed = Strafe.mc.player.onGround ? this.groundSpeed.getValue().floatValue() : (this.jumpBoosting ? this.jumpSpeed.getValue().floatValue() : (event.y > 0.0 ? this.airSpeed.getValue().floatValue() : this.downSpeed.getValue().floatValue()));
            double[] motion = PlayerUtils.forward(PlayerUtils.getDefaultMoveSpeed() * (double)speed);
            event.x = motion[0];
            event.z = motion[1];
        } else {
            event.x = 0.0;
            event.z = 0.0;
        }
        if (this.jumpBoosting) {
            this.jumpBoosting = false;
        }
    }

    @SubscribeEvent
    public void onJump(LivingEvent.LivingJumpEvent event) {
        this.jumpBoosting = true;
    }
}

