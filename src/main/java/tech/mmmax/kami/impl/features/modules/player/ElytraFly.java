/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.util.math.MathHelper
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.player;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.event.PacketEvent;
import tech.mmmax.kami.api.event.TravelEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;
import tech.mmmax.kami.mixin.mixins.access.ICPacketPlayer;
import tech.mmmax.kami.mixin.mixins.access.IMinecraft;
import tech.mmmax.kami.mixin.mixins.access.ITimer;

public class ElytraFly
extends Module {
    public static ElytraFly INSTANCE;
    Value<Number> speed = new ValueBuilder().withDescriptor("Speed").withValue(1.8).withRange(0d, 10d).register(this);
    Value<Number> glideSpeed = new ValueBuilder().withDescriptor("Glide Speed").withValue(0).withRange(0, 1).register(this);
    Value<Number> idleSpeed = new ValueBuilder().withDescriptor("Idle Down Speed").withValue(0).withRange(0, 1).register(this);
    Value<Boolean> openTimer = new ValueBuilder().withDescriptor("Open Timer", "Open Timer").withValue(true).register(this);
    Value<Boolean> hover = new ValueBuilder().withDescriptor("Hover", "Hover").withValue(true).register(this);
    boolean cancelling = true;
    private double hoverTarget;
    private boolean hoverState;
    public static boolean flyUp;
    public static boolean autoFly;
    public float packetYaw;

    public ElytraFly() {
        super("Elytra Fly", Feature.Category.Player);
        INSTANCE = this;
        this.hoverTarget = -1.0;
        this.hoverState = false;
        this.packetYaw = 0.0f;
    }

    @SubscribeEvent
    public void onMove(TravelEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        ITimer timer = (ITimer)((IMinecraft)mc).getTimer();
        if (ElytraFly.mc.player != null) {
            if (!ElytraFly.mc.player.isElytraFlying()) {
                if (this.openTimer.getValue().booleanValue() && !ElytraFly.mc.player.onGround && ElytraFly.mc.player.motionY < -0.04) {
                    CPacketEntityAction packet4 = new CPacketEntityAction((Entity)ElytraFly.mc.player, CPacketEntityAction.Action.START_FALL_FLYING);
                    ElytraFly.mc.player.connection.sendPacket((Packet)packet4);
                    timer.setTickLength(200.0f);
                    event.setCanceled(true);
                }
            } else {
                boolean doHover;
                timer.setTickLength(50.0f);
                if (this.hoverTarget < 0.0) {
                    this.hoverTarget = ElytraFly.mc.player.posY;
                }
                boolean moveForward = ElytraFly.mc.gameSettings.keyBindForward.isKeyDown();
                boolean moveBackward = ElytraFly.mc.gameSettings.keyBindBack.isKeyDown();
                boolean moveLeft = ElytraFly.mc.gameSettings.keyBindLeft.isKeyDown();
                boolean moveRight = ElytraFly.mc.gameSettings.keyBindRight.isKeyDown();
                boolean moveUp = ElytraFly.mc.gameSettings.keyBindJump.isKeyDown();
                boolean moveDown = ElytraFly.mc.gameSettings.keyBindSneak.isKeyDown();
                float moveForwardFactor = moveForward ? 1.0f : -1.0f;
                float yawDeg = ElytraFly.mc.player.rotationYaw;
                if (moveLeft && (moveForward || moveBackward)) {
                    yawDeg -= 40.0f * moveForwardFactor;
                } else if (moveRight && (moveForward || moveBackward)) {
                    yawDeg += 40.0f * moveForwardFactor;
                } else if (moveLeft) {
                    yawDeg -= 90.0f;
                } else if (moveRight) {
                    yawDeg += 90.0f;
                }
                if (moveBackward) {
                    yawDeg -= 180.0f;
                }
                this.packetYaw = yawDeg;
                float yaw = (float)Math.toRadians(yawDeg);
                float pitch = (float)Math.toRadians(ElytraFly.mc.player.rotationPitch);
                double d8 = Math.sqrt(ElytraFly.mc.player.motionX * ElytraFly.mc.player.motionX + ElytraFly.mc.player.motionZ * ElytraFly.mc.player.motionZ);
                this.hoverState = this.hoverState ? ElytraFly.mc.player.posY < this.hoverTarget + 0.1 : ElytraFly.mc.player.posY < this.hoverTarget + 0.0;
                boolean bl = doHover = this.hoverState && this.hover.getValue() != false;
                if (moveUp || moveForward || moveBackward || moveLeft || moveRight || autoFly) {
                    if ((moveUp || doHover || flyUp) && d8 > 1.0) {
                        if (ElytraFly.mc.player.motionX == 0.0 && ElytraFly.mc.player.motionZ == 0.0) {
                            ElytraFly.mc.player.motionY = this.glideSpeed.getValue().doubleValue();
                        } else {
                            double d6 = 1.0;
                            double d10 = d8 * 0.2 * 0.04;
                            ElytraFly.mc.player.motionY += d10 * 3.2;
                            ElytraFly.mc.player.motionX -= (double)(-MathHelper.sin((float)yaw)) * d10 / d6;
                            ElytraFly.mc.player.motionZ -= (double)MathHelper.cos((float)yaw) * d10 / d6;
                            if (d6 > 0.0) {
                                ElytraFly.mc.player.motionX += ((double)(-MathHelper.sin((float)yaw)) / d6 * d8 - ElytraFly.mc.player.motionX) * 0.3;
                                ElytraFly.mc.player.motionZ += ((double)MathHelper.cos((float)yaw) / d6 * d8 - ElytraFly.mc.player.motionZ) * 0.3;
                            }
                            ElytraFly.mc.player.motionX *= (double)0.99f;
                            ElytraFly.mc.player.motionY *= (double)0.98f;
                            ElytraFly.mc.player.motionZ *= (double)0.99f;
                        }
                    } else {
                        ElytraFly.mc.player.motionX = (double)(-MathHelper.sin((float)yaw)) * this.speed.getValue().doubleValue();
                        ElytraFly.mc.player.motionY = -this.idleSpeed.getValue().doubleValue();
                        ElytraFly.mc.player.motionZ = (double)MathHelper.cos((float)yaw) * this.speed.getValue().doubleValue();
                    }
                } else {
                    ElytraFly.mc.player.motionX = 0.0;
                    ElytraFly.mc.player.motionY = 0.0;
                    ElytraFly.mc.player.motionZ = 0.0;
                }
                if (moveDown) {
                    ElytraFly.mc.player.motionY = -this.glideSpeed.getValue().doubleValue();
                }
                if (moveUp || moveDown) {
                    this.hoverTarget = ElytraFly.mc.player.posY;
                }
                event.setCanceled(true);
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (NullUtils.nullCheck()) {
            return;
        }
        ITimer timer = (ITimer)((IMinecraft)mc).getTimer();
        timer.setTickLength(50.0f);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (NullUtils.nullCheck()) {
            return;
        }
        this.hoverTarget = -1.0;
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (event.getTime() == PacketEvent.Time.Send && event.getPacket() instanceof CPacketPlayer) {
            if (!ElytraFly.mc.player.isElytraFlying()) {
                return;
            }
            CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            ((ICPacketPlayer)((CPacketPlayer)event.getPacket())).setPitch(0.0f);
            ((ICPacketPlayer)((CPacketPlayer)event.getPacket())).setYaw(this.packetYaw);
        }
        if (event.getTime() == PacketEvent.Time.Receive && event.getPacket() instanceof CPacketEntityAction) {
            if (ElytraFly.mc.player == null || !ElytraFly.mc.player.isElytraFlying()) {
                return;
            }
            CPacketEntityAction packet = (CPacketEntityAction) event.getPacket();
            if (packet.getAction() == CPacketEntityAction.Action.START_FALL_FLYING) {
                this.hoverTarget = ElytraFly.mc.player.posY + 0.35;
            }
        }
    }
}

