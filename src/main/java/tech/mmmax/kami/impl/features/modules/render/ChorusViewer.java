/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.network.play.server.SPacketSoundEffect
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.opengl.GL11
 */
package tech.mmmax.kami.impl.features.modules.render;

import java.awt.Color;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import tech.mmmax.kami.api.event.PacketEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.MathUtil;
import tech.mmmax.kami.api.utils.color.ColorUtil;
import tech.mmmax.kami.api.utils.render.RenderUtil;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class ChorusViewer
extends Module {
    Value<Number> lineWidth = new ValueBuilder().withDescriptor("Line Width").withValue(1).withRange(0.1, 5).register(this);
    Value<Color> fillColor = new ValueBuilder().withDescriptor("Fill Color").withValue(new Color(255, 255, 255, 20)).register(this);
    Value<Color> outlineColor = new ValueBuilder().withDescriptor("Outline Color").withValue(new Color(255, 255, 255, 255)).register(this);
    Value<Boolean> tracer = new ValueBuilder().withDescriptor("Tracer").withValue(true).register(this);
    Value<Color> tracerColor = new ValueBuilder().withDescriptor("Tracer Color").withValue(new Color(255, 255, 255, 150)).register(this);
    Value<Number> tracerFadeTime = new ValueBuilder().withDescriptor("Tracer Fade").withValue(500).withRange(1, 2000).register(this);
    Value<Number> fadeStart = new ValueBuilder().withDescriptor("Fade Start").withValue(500).withRange(1, 2000).register(this);
    Value<Number> fadeTime = new ValueBuilder().withDescriptor("Fade Time").withValue(500).withRange(1, 2000).register(this);
    Value<Boolean> tracerFrustum = new ValueBuilder().withDescriptor("Tracer Frustum").withValue(false).register(this);
    AxisAlignedBB renderBB;
    long startTime;

    public ChorusViewer() {
        super("ChorusViewer", Feature.Category.Render);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        double normal;
        long time;
        if (this.renderBB == null) {
            return;
        }
        GL11.glLineWidth((float)this.lineWidth.getValue().floatValue());
        int lineA = this.outlineColor.getValue().getAlpha();
        int fillA = this.fillColor.getValue().getAlpha();
        int tracerA = this.tracerColor.getValue().getAlpha();
        if (System.currentTimeMillis() - this.startTime > this.fadeStart.getValue().longValue()) {
            time = System.currentTimeMillis() - this.startTime - this.fadeStart.getValue().longValue();
            normal = MathUtil.normalize(time, 0.0, this.fadeTime.getValue().doubleValue());
            normal = MathHelper.clamp((double)normal, (double)0.0, (double)1.0);
            normal = -normal + 1.0;
            lineA = (int)(normal * (double)lineA);
            fillA = (int)(normal * (double)fillA);
        }
        time = System.currentTimeMillis() - this.startTime;
        normal = MathUtil.normalize(time, 0.0, this.tracerFadeTime.getValue().doubleValue());
        normal = MathHelper.clamp((double)normal, (double)0.0, (double)1.0);
        normal = -normal + 1.0;
        tracerA = (int)(normal * (double)lineA);
        Color fill = ColorUtil.newAlpha(this.fillColor.getValue(), fillA);
        Color line = ColorUtil.newAlpha(this.outlineColor.getValue(), lineA);
        Color tracerC = ColorUtil.newAlpha(this.tracerColor.getValue(), tracerA);
        RenderUtil.prepare();
        RenderUtil.renderBB(7, this.renderBB, fill, fill);
        RenderUtil.renderBB(3, this.renderBB, line, line);
        if (this.tracer.getValue().booleanValue()) {
            double centerX = (this.renderBB.maxX - this.renderBB.minX) / 2.0;
            double centerZ = (this.renderBB.maxZ - this.renderBB.minZ) / 2.0;
            Vec3d startPos = RenderUtil.updateToCamera(ChorusViewer.mc.player.getPositionVector());
            Vec3d endPos = RenderUtil.updateToCamera(new Vec3d(this.renderBB.minX + centerX, this.renderBB.minY, this.renderBB.minZ + centerZ));
            if (!this.tracerFrustum.getValue().booleanValue() || new Frustum().isBoundingBoxInFrustum(this.renderBB)) {
                RenderUtil.builder = RenderUtil.tessellator.getBuffer();
                RenderUtil.builder.begin(1, DefaultVertexFormats.POSITION_COLOR);
                RenderUtil.addBuilderVertex(RenderUtil.builder, startPos.x, startPos.y, startPos.z, tracerC);
                RenderUtil.addBuilderVertex(RenderUtil.builder, endPos.x, endPos.y, endPos.z, tracerC);
                RenderUtil.tessellator.draw();
            }
        }
        RenderUtil.release();
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        SPacketSoundEffect packet;
        if (event.getPacket() instanceof SPacketSoundEffect && ((packet = (SPacketSoundEffect)event.getPacket()).getSound() == SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT || packet.getSound() == SoundEvents.ENTITY_ENDERMEN_TELEPORT)) {
            AxisAlignedBB playerBB = ChorusViewer.mc.player.getRenderBoundingBox();
            double widthX = (playerBB.maxX - playerBB.minX) / 2.0;
            double height = playerBB.maxY - playerBB.minY;
            double widthZ = (playerBB.maxZ - playerBB.minZ) / 2.0;
            this.renderBB = new AxisAlignedBB(packet.getX() - widthX, packet.getY(), packet.getZ() - widthZ, packet.getX() + widthX, packet.getY() + height, packet.getZ() + widthZ);
            this.startTime = System.currentTimeMillis();
        }
    }
}

