/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketConfirmTeleport
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketPlayer$PositionRotation
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.player;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.event.MoveEvent;
import tech.mmmax.kami.api.event.PacketEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.player.PlayerUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class PacketFly
extends Module {
    Value<String> mode = new ValueBuilder().withDescriptor("Mode").withValue("Clip").withModes("Clip", "Smooth").register(this);
    Value<Number> reduction = new ValueBuilder().withDescriptor("Reduction").withValue(0.1).withRange(0.0, 0.5).register(this);
    Value<Number> reductionY = new ValueBuilder().withDescriptor("Reduction Y").withValue(0.1).withRange(0.0, 0.5).register(this);
    Value<Number> attempts = new ValueBuilder().withDescriptor("Attempts").withValue(5).withRange(0, 10).register(this);
    Value<String> syncMode = new ValueBuilder().withDescriptor("Sync Mode").withValue("None").withModes("None", "Fast", "Final").register(this);
    Value<String> bandMode = new ValueBuilder().withDescriptor("RubberBand Mode").withValue("Normal").withModes("Normal", "Cancel", "Smooth").register(this);
    Value<Boolean> bandSync = new ValueBuilder().withDescriptor("Band Sync").withValue(false).register(this);
    Value<Number> speed = new ValueBuilder().withDescriptor("Speed").withValue(3).withRange(1, 10).register(this);
    Value<Boolean> autoSpeed = new ValueBuilder().withDescriptor("Auto Speed").withValue(true).register(this);

    public PacketFly() {
        super("PacketFly", Feature.Category.Player);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
            if (this.bandMode.getValue().equals("Cancel")) {
                mc.getConnection().sendPacket((Packet)new CPacketConfirmTeleport(packet.getTeleportId()));
                mc.getConnection().sendPacket((Packet)new CPacketPlayer.PositionRotation(packet.getX(), packet.getY(), packet.getZ(), PacketFly.mc.player.rotationYaw, PacketFly.mc.player.rotationPitch, false));
                if (this.bandSync.getValue().booleanValue()) {
                    PacketFly.mc.player.setPosition(packet.getX(), packet.getY(), packet.getZ());
                }
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        PacketFly.mc.player.motionX = 0.0;
        PacketFly.mc.player.motionY = 0.0;
        PacketFly.mc.player.motionZ = 0.0;
        if (this.mode.getValue().equals("Clip")) {
            double[] forward = PlayerUtils.forward(this.getSpeed());
            for (int i = 0; i < this.attempts.getValue().intValue(); ++i) {
                this.sendPackets(PacketFly.mc.player.posX + forward[0], PacketFly.mc.player.posY + this.getUpMovement(), PacketFly.mc.player.posZ + forward[1]);
                if (!this.syncMode.getValue().equals("Fast")) continue;
                PacketFly.mc.player.setPosition(PacketFly.mc.player.posX + forward[0], PacketFly.mc.player.posY + this.getUpMovement(), PacketFly.mc.player.posZ + forward[1]);
            }
            if (this.syncMode.getValue().equals("Final")) {
                PacketFly.mc.player.setPosition(PacketFly.mc.player.posX + forward[0], PacketFly.mc.player.posY + this.getUpMovement(), PacketFly.mc.player.posZ + forward[1]);
            }
        }
    }

    double getUpMovement() {
        return (double)(PacketFly.mc.gameSettings.keyBindJump.isKeyDown() ? 1 : (PacketFly.mc.gameSettings.keyBindSneak.isKeyDown() ? -1 : 0)) * this.getSpeed();
    }

    public void sendPackets(double x, double y, double z) {
        mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(x, y, z, PacketFly.mc.player.onGround));
        mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(0.0, 1337.0, 0.0, PacketFly.mc.player.onGround));
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (this.mode.getValue().equals("Smooth")) {
            double[] forward = PlayerUtils.forward(this.getSpeed());
            for (int i = 0; i < this.attempts.getValue().intValue(); ++i) {
                this.sendPackets(PacketFly.mc.player.posX + forward[0], PacketFly.mc.player.posY + this.getUpMovement(), PacketFly.mc.player.posZ + forward[1]);
                if (!this.syncMode.getValue().equals("Fast")) continue;
                PacketFly.mc.player.setPosition(PacketFly.mc.player.posX + forward[0], PacketFly.mc.player.posY + this.getUpMovement(), PacketFly.mc.player.posZ + forward[1]);
            }
            if (this.syncMode.getValue().equals("Final")) {
                PacketFly.mc.player.setPosition(PacketFly.mc.player.posX + forward[0], PacketFly.mc.player.posY + this.getUpMovement(), PacketFly.mc.player.posZ + forward[1]);
            }
        }
        event.x = event.x * this.reduction.getValue().doubleValue() / 10.0;
        event.z = event.z * this.reduction.getValue().doubleValue() / 10.0;
        event.y = event.y * this.reductionY.getValue().doubleValue() / 10.0;
    }

    double getSpeed() {
        return this.autoSpeed.getValue() != false ? PlayerUtils.getDefaultMoveSpeed() / 10.0 : this.speed.getValue().doubleValue() / 100.0;
    }

    @Override
    public String getHudInfo() {
        return this.mode.getValue();
    }
}

