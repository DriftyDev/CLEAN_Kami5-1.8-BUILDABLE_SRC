/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  org.lwjgl.opengl.GL11
 */
package tech.mmmax.kami.impl.features.modules.player;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.MathUtil;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.color.ColorUtil;
import tech.mmmax.kami.api.utils.player.PlayerUtils;
import tech.mmmax.kami.api.utils.player.TargetUtils;
import tech.mmmax.kami.api.utils.render.RenderUtil;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class TargetStrafe
extends Module {
    Value<Number> radius = new ValueBuilder().withDescriptor("Radius").withValue(4).withRange(1, 9).register(this);
    Value<Number> targetRange = new ValueBuilder().withDescriptor("Target Range").withValue(7).withRange(3, 14).register(this);
    Value<Color> lineColor = new ValueBuilder().withDescriptor("Line Color").withValue(new Color(255, 255, 255, 150)).register(this);
    Value<Number> fadedAlpha = new ValueBuilder().withDescriptor("Faded Alpha").withValue(100).withRange(0, 255).register(this);
    Value<Number> fadedMax = new ValueBuilder().withDescriptor("Faded Max").withValue(10).withRange(5, 20).register(this);
    Value<Color> targetColor = new ValueBuilder().withDescriptor("Target Color").withValue(new Color(255, 255, 255, 47)).register(this);
    Value<Number> lineWidth = new ValueBuilder().withDescriptor("Line Width").withValue(2).withRange(0.1, 4).register(this);
    List<Vec3d> circle = new ArrayList<Vec3d>();
    Entity target = null;

    public TargetStrafe() {
        super("Target Strafe", Feature.Category.Player);
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        this.target = TargetUtils.getTarget(this.targetRange.getValue().doubleValue());
        if (this.target == null) {
            return;
        }
        this.circle = this.getCircle();
        if (this.circle == null) {
            return;
        }
        Vec3d closestPos = this.circle.stream().min(Comparator.comparingDouble(pos -> TargetStrafe.mc.player.getDistance(this.target.posX + pos.x * this.radius.getValue().doubleValue(), this.target.posY + pos.y, this.target.posZ + pos.z * this.radius.getValue().doubleValue()))).orElse(new Vec3d(0.0, 0.0, 0.0));
        double speed = PlayerUtils.getDefaultMoveSpeed();
        TargetStrafe.mc.player.motionX = closestPos.x * speed;
        TargetStrafe.mc.player.motionZ = closestPos.z * speed;
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (this.target != null) {
            RenderUtil.renderBB(7, this.target.getRenderBoundingBox(), this.targetColor.getValue(), this.targetColor.getValue());
            if (this.circle != null) {
                GL11.glLineWidth((float)this.lineWidth.getValue().floatValue());
                RenderUtil.prepare();
                GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                RenderUtil.builder = RenderUtil.tessellator.getBuffer();
                RenderUtil.builder.begin(3, DefaultVertexFormats.POSITION_COLOR);
                this.buildBBBuffer(RenderUtil.builder, this.circle, this.lineColor.getValue(), this.fadedAlpha.getValue().intValue());
                RenderUtil.tessellator.draw();
                RenderUtil.release();
                GL11.glLineWidth((float)1.0f);
            }
        }
    }

    void buildBBBuffer(BufferBuilder bb, List<Vec3d> positions, Color color, int fadeAlpha) {
        for (Vec3d pos : positions) {
            Vec3d targetPos = new Vec3d(pos.x * this.radius.getValue().doubleValue(), pos.y, pos.z * this.radius.getValue().doubleValue()).add(this.target.posX, this.target.posY, this.target.posZ);
            Vec3d camPos = RenderUtil.updateToCamera(targetPos);
            double normal = MathUtil.normalize(TargetStrafe.mc.player.getDistance(targetPos.x, targetPos.y, targetPos.z), 0.0, this.fadedMax.getValue().doubleValue());
            normal = MathHelper.clamp((double)normal, (double)0.0, (double)1.0);
            int alpha = (int)(normal * (double)(fadeAlpha - this.lineColor.getValue().getAlpha()) + (double)this.lineColor.getValue().getAlpha());
            RenderUtil.addBuilderVertex(bb, camPos.x, camPos.y, camPos.z, ColorUtil.newAlpha(color, alpha));
        }
    }

    List<Vec3d> getCircle() {
        ArrayList<Vec3d> positions = new ArrayList<Vec3d>();
        for (int i = 0; i <= 360; ++i) {
            double x = Math.sin(Math.toRadians(i));
            double z = -Math.cos(Math.toRadians(i));
            positions.add(new Vec3d(x, 0.0, z));
        }
        return positions;
    }
}

