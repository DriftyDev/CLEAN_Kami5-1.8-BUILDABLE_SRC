/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.event.MoveEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.Timer;
import tech.mmmax.kami.api.utils.player.PlayerUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class YPort
extends Module {
    Timer forceTimer = new Timer();
    Timer jumpTimer = new Timer();
    Value<Number> speed = new ValueBuilder().withDescriptor("Speed").withValue(0.07).withRange(0.01, 0.3).register(this);
    Value<Number> forceDelay = new ValueBuilder().withDescriptor("Force Delay").withValue(1).withRange(1, 1000).register(this);
    Value<Number> jumpDelay = new ValueBuilder().withDescriptor("Jump Delay").withValue(1).withRange(1, 1000).register(this);
    Value<String> forceMode = new ValueBuilder().withDescriptor("Force Mode").withValue("Tick").withModes("Tick", "Move", "MoveMotion").register(this);
    Value<Number> force = new ValueBuilder().withDescriptor("Force").withValue(1).withRange(0, 5).register(this);
    Value<Number> height = new ValueBuilder().withDescriptor("Height").withValue(3).withRange(1, 5).register(this);

    public YPort() {
        super("YPort", Feature.Category.Player);

        this.forceTimer.setDelay((forceDelay.getValue()).longValue());

        this.jumpTimer.setDelay((jumpDelay.getValue()).longValue());
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (PlayerUtils.isMoving((EntityLivingBase)YPort.mc.player)) {
            if (YPort.mc.player.onGround) {
                if (this.jumpTimer.isPassed()) {
                    YPort.mc.player.jump();
                    PlayerUtils.setSpeed((EntityLivingBase)YPort.mc.player, PlayerUtils.getDefaultMoveSpeed() + this.speed.getValue().doubleValue());
                    this.jumpTimer.resetDelay();
                }
            } else if (this.forceMode.getValue().equals("Tick") && (double)YPort.mc.player.fallDistance <= this.height.getValue().doubleValue() && this.forceTimer.isPassed()) {
                YPort.mc.player.motionY -= this.force.getValue().doubleValue();
                this.forceTimer.resetDelay();
            }
        }
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (PlayerUtils.isMoving((EntityLivingBase)YPort.mc.player) && !YPort.mc.player.onGround && (double)YPort.mc.player.fallDistance <= this.height.getValue().doubleValue()) {
            if (this.forceMode.getValue().equals("Move") && this.forceTimer.isPassed()) {
                event.y = -this.force.getValue().doubleValue();
                this.forceTimer.resetDelay();
                event.moved = true;
            }
            if (this.forceMode.getValue().equals("MoveMotion") && this.forceTimer.isPassed()) {
                YPort.mc.player.motionY -= this.force.getValue().doubleValue();
                this.forceTimer.resetDelay();
                event.moved = true;
            }
        }
    }

    boolean isInHeight() {
        for (double y = 0.0; y < this.height.getValue().doubleValue() + 0.5; y += 0.01) {
            if (YPort.mc.world.getCollisionBoxes((Entity)YPort.mc.player, YPort.mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) continue;
            return true;
        }
        return false;
    }

    @Override
    public String getHudInfo() {
        return this.forceMode.getValue();
    }
}

