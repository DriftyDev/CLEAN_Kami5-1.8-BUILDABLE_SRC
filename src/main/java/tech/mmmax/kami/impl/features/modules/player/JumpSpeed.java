/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.player;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.event.MoveEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.Timer;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class JumpSpeed
extends Module {
    Timer timer = new Timer();
    Value<Number> force = new ValueBuilder().withDescriptor("Force").withValue(5).withRange(0, 20).register(this);
    Value<Number> delay = new ValueBuilder().withDescriptor("Delay").withValue(50).withRange(1, 1000).register(this);
    Value<String> mode = new ValueBuilder().withDescriptor("Mode").withValue("Ground").withModes("Ground", "OffGround", "Always").register(this);
    Value<String> jumpMode = new ValueBuilder().withDescriptor("Jump Mode").withValue("Move").withModes("Move", "Tick").register(this);
    Value<Number> speed = new ValueBuilder().withDescriptor("Speed").withValue(5).withRange(0, 100).register(this);
    Value<String> speedMode = new ValueBuilder().withDescriptor("Speed Mode").withValue("Move").withModes("Move", "Motion").register(this);

    public JumpSpeed() {
        super("JumpSpeed", Feature.Category.Player);

        this.timer.setDelay((delay.getValue()).longValue());
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (JumpSpeed.mc.player.moveForward != 0.0f && JumpSpeed.mc.player.onGround && this.timer.isPassed() && this.jumpMode.getValue().equals("Tick")) {
            JumpSpeed.mc.player.jump();
            this.timer.resetDelay();
        }
    }

    @SubscribeEvent
    public void moveEvent(MoveEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (JumpSpeed.mc.player.isInLava() || JumpSpeed.mc.player.isInWater() || JumpSpeed.mc.player.isOnLadder()) {
            return;
        }
        if (JumpSpeed.mc.player.moveForward != 0.0f) {
            if (JumpSpeed.mc.player.onGround) {
                if (this.timer.isPassed() && this.jumpMode.getValue().equals("Tick")) {
                    JumpSpeed.mc.player.jump();
                    this.timer.resetDelay();
                }
                if (this.speedMode.getValue().equals("Move")) {
                    event.x *= (double)(this.speed.getValue().floatValue() / 50.0f);
                    event.z *= (double)(this.speed.getValue().floatValue() / 50.0f);
                    event.moved = true;
                }
            }
            if (JumpSpeed.mc.player.onGround && this.mode.getValue().equals("Ground") || !JumpSpeed.mc.player.onGround && this.mode.getValue().equals("OffGround") || this.mode.getValue().equals("Always")) {
                event.y = -this.force.getValue().doubleValue();
                event.moved = true;
            }
        }
    }

    @Override
    public String getHudInfo() {
        return this.mode.getValue();
    }
}

