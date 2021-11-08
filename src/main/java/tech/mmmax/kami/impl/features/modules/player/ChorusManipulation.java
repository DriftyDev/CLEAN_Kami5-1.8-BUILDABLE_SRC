/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketConfirmTeleport
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Text
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package tech.mmmax.kami.impl.features.modules.player;

import java.util.LinkedList;
import java.util.Queue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tech.mmmax.kami.api.event.PacketEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;
import tech.mmmax.kami.impl.features.modules.client.HudColors;
import tech.mmmax.kami.impl.gui.ClickGui;

public class ChorusManipulation
extends Module {
    ScaledResolution sr;
    Queue<CPacketPlayer> packets = new LinkedList<CPacketPlayer>();
    Queue<CPacketConfirmTeleport> tpPackets = new LinkedList<CPacketConfirmTeleport>();
    Value<Boolean> cancel = new ValueBuilder().withDescriptor("Cancel").withValue(false).register(this);
    public Value<Number> xPos = new ValueBuilder().withDescriptor("X Pos").withValue(100).withRange(0, 1000).register(this);
    public Value<Number> yPos = new ValueBuilder().withDescriptor("Y Pos").withValue(10).withRange(0, 1000).register(this);
    AxisAlignedBB render;

    public ChorusManipulation() {
        super("Blink", Feature.Category.Player);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook && this.cancel.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayer) {
            this.packets.add((CPacketPlayer)event.getPacket());
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketConfirmTeleport) {
            this.tpPackets.add((CPacketConfirmTeleport)event.getPacket());
            event.setCanceled(true);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        while (!this.packets.isEmpty()) {
            mc.getConnection().sendPacket((Packet)this.packets.poll());
        }
        while (!this.tpPackets.isEmpty()) {
            mc.getConnection().sendPacket((Packet)this.tpPackets.poll());
        }
        this.render = null;
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (this.render != null) {
            // empty if block
        }
    }

    @SubscribeEvent
    public void draw(RenderGameOverlayEvent.Text event) {
        this.sr = new ScaledResolution(mc);
        this.xPos.setMax(this.sr.getScaledWidth());
        this.yPos.setMax(this.sr.getScaledHeight());
        ClickGui.CONTEXT.getRenderer().renderText("Currently Blinked", this.xPos.getValue().floatValue(), this.yPos.getValue().floatValue(), HudColors.getTextColor(this.yPos.getValue().intValue()), ClickGui.CONTEXT.getColorScheme().doesTextShadow());
    }
}

