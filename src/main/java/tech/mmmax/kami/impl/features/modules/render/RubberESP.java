/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelPlayer
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.World
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.opengl.GL11
 */
package tech.mmmax.kami.impl.features.modules.render;

import com.mojang.authlib.GameProfile;
import java.awt.Color;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import tech.mmmax.kami.api.event.PacketEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.color.ColorUtil;
import tech.mmmax.kami.api.utils.render.RenderUtil;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class RubberESP
extends Module {
    Value<Color> fillColorSet = new ValueBuilder().withDescriptor("Fill Color").withValue(new Color(47, 0, 255, 150)).register(this);
    Value<Color> lineColorSet = new ValueBuilder().withDescriptor("Line Color").withValue(new Color(255, 255, 255, 255)).register(this);
    Value<String> mode = new ValueBuilder().withDescriptor("Mode").withValue("Box").withModes("Box", "Model").register(this);
    Value<Number> fadeStart = new ValueBuilder().withDescriptor("Fade Start").withValue(100).withRange(0, 4000).register(this);
    Value<Number> fadeTime = new ValueBuilder().withDescriptor("Fade Time").withValue(500).withRange(0, 2000).register(this);
    EntityOtherPlayerMP player = null;
    AxisAlignedBB playerBox = null;
    ModelPlayer playerModel = null;
    long startTime = 0L;

    public RubberESP() {
        super("RubberESP", Feature.Category.Render);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
            if (this.mode.getValue().equals("Box")) {
                AxisAlignedBB playerBB = RubberESP.mc.player.getRenderBoundingBox();
                double widthX = (playerBB.maxX - playerBB.minX) / 2.0;
                double height = playerBB.maxY - playerBB.minY;
                double widthZ = (playerBB.maxZ - playerBB.minZ) / 2.0;
                this.playerBox = new AxisAlignedBB(packet.getX() - widthX, packet.getY(), packet.getZ() - widthZ, packet.getX() + widthX, packet.getY() + height, packet.getZ() + widthZ);
                this.player = null;
            }
            if (this.mode.getValue().equals("Model")) {
                GameProfile profile = new GameProfile(RubberESP.mc.player.getUniqueID(), "");
                this.player = new EntityOtherPlayerMP((World)RubberESP.mc.world, profile);
                this.player.setPositionAndRotation(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch());
                this.playerModel = new ModelPlayer(0.0f, false);
                this.playerBox = null;
            }
            this.startTime = System.currentTimeMillis();
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        GL11.glLineWidth((float)1.0f);
        int lineA = this.lineColorSet.getValue().getAlpha();
        int fillA = this.fillColorSet.getValue().getAlpha();
        if (System.currentTimeMillis() - this.startTime > this.fadeStart.getValue().longValue()) {
            long time = System.currentTimeMillis() - this.startTime - this.fadeStart.getValue().longValue();
            double normal = this.normalize(time, 0.0, this.fadeTime.getValue().doubleValue());
            normal = MathHelper.clamp((double)normal, (double)0.0, (double)1.0);
            normal = -normal + 1.0;
            lineA = (int)(normal * (double)lineA);
            fillA = (int)(normal * (double)fillA);
        }
        Color lineColor = ColorUtil.newAlpha(this.lineColorSet.getValue(), lineA);
        Color fillColor = ColorUtil.newAlpha(this.fillColorSet.getValue(), fillA);
        if (this.playerBox != null && this.mode.getValue().equals("Box")) {
            RenderUtil.renderBB(7, this.playerBox, fillColor, fillColor);
            RenderUtil.renderBB(3, this.playerBox, lineColor, lineColor);
        }
        if (this.player != null && this.mode.getValue().equals("Model")) {
            RenderUtil.prepare();
            ColorUtil.glColor(fillColor);
            GL11.glPolygonMode((int)1032, (int)6914);
            RenderUtil.renderEntity((EntityLivingBase)this.player, (ModelBase)this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1.0f);
            ColorUtil.glColor(lineColor);
            GL11.glPolygonMode((int)1032, (int)6913);
            RenderUtil.renderEntity((EntityLivingBase)this.player, (ModelBase)this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1.0f);
            GL11.glPolygonMode((int)1032, (int)6914);
            RenderUtil.release();
        }
    }

    double normalize(double value, double min, double max) {
        return (value - min) / (max - min);
    }
}

