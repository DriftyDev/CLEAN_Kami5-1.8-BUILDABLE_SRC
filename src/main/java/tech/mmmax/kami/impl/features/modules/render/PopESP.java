/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelPlayer
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.network.play.server.SPacketEntityStatus
 *  net.minecraft.util.ResourceLocation
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
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.ResourceLocation;
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
import tech.mmmax.kami.impl.features.modules.misc.FakePlayer;

public class PopESP
extends Module {
    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    Value<Number> fadeStart = new ValueBuilder().withDescriptor("Fade Start").withValue(100).withRange(0, 4000).register(this);
    Value<Number> fadeTime = new ValueBuilder().withDescriptor("Fade Time").withValue(500).withRange(0, 2000).register(this);
    Value<Boolean> self = new ValueBuilder().withDescriptor("Self").withValue(false).register(this);
    Value<Color> fillColorS = new ValueBuilder().withDescriptor("Fill Color").withValue(new Color(15, 100, 255, 100)).register(this);
    Value<Color> lineColorS = new ValueBuilder().withDescriptor("Line Color").withValue(new Color(15, 100, 255, 255)).register(this);
    Value<Boolean> glint = new ValueBuilder().withDescriptor("Glint").withValue(true).register(this);
    Value<Color> glintColor = new ValueBuilder().withDescriptor("Glint Color").withValue(new Color(134, 55, 255, 123)).register(this);
    EntityOtherPlayerMP player = null;
    ModelPlayer playerModel = null;
    long startTime = 0L;

    public PopESP() {
        super("PopESP", Feature.Category.Render);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        SPacketEntityStatus packet;
        if (NullUtils.nullCheck()) {
            return;
        }
        if (event.getPacket() instanceof SPacketEntityStatus && (packet = (SPacketEntityStatus)event.getPacket()).getOpCode() == 35 && packet.getEntity((World)PopESP.mc.world) != null && (this.self.getValue().booleanValue() || packet.getEntity((World)PopESP.mc.world).getEntityId() != PopESP.mc.player.getEntityId())) {
            GameProfile profile = new GameProfile(PopESP.mc.player.getUniqueID(), "");
            this.player = new EntityOtherPlayerMP((World)FakePlayer.mc.world, profile);
            this.player.copyLocationAndAnglesFrom(packet.getEntity((World)PopESP.mc.world));
            this.playerModel = new ModelPlayer(0.0f, false);
            this.startTime = System.currentTimeMillis();
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        GL11.glLineWidth((float)1.0f);
        float[] glintHSB = Color.RGBtoHSB(this.glintColor.getValue().getRed(), this.glintColor.getValue().getGreen(), this.glintColor.getValue().getBlue(), null);
        int lineA = this.lineColorS.getValue().getAlpha();
        int fillA = this.fillColorS.getValue().getAlpha();
        int glintA = this.glintColor.getValue().getAlpha();
        float glintB = glintHSB[2];
        if (System.currentTimeMillis() - this.startTime > this.fadeStart.getValue().longValue()) {
            long time = System.currentTimeMillis() - this.startTime - this.fadeStart.getValue().longValue();
            double normal = this.normalize(time, 0.0, this.fadeTime.getValue().doubleValue());
            normal = MathHelper.clamp((double)normal, (double)0.0, (double)1.0);
            normal = -normal + 1.0;
            lineA = (int)(normal * (double)lineA);
            fillA = (int)(normal * (double)fillA);
            glintA = (int)(normal * (double)glintA);
            glintB = (float)(normal * (double)glintB);
        }
        Color lineColor = ColorUtil.newAlpha(this.lineColorS.getValue(), lineA);
        Color fillColor = ColorUtil.newAlpha(this.fillColorS.getValue(), fillA);
        Color finalGlintColor = ColorUtil.newAlpha(Color.getHSBColor(glintHSB[0], glintHSB[1], glintB), glintA);
        if (this.player != null && this.playerModel != null) {
            RenderUtil.prepare();
            ColorUtil.glColor(fillColor);
            GL11.glPolygonMode((int)1032, (int)6914);
            RenderUtil.renderEntity((EntityLivingBase)this.player, (ModelBase)this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1.0f);
            if (this.glint.getValue().booleanValue()) {
                PopESP.mc.getRenderManager().renderEngine.bindTexture(RES_ITEM_GLINT);
                GL11.glTexCoord3d((double)1.0, (double)1.0, (double)1.0);
                GL11.glEnable((int)3553);
                GL11.glBlendFunc((int)768, (int)771);
                ColorUtil.glColor(finalGlintColor);
                RenderUtil.renderEntity((EntityLivingBase)this.player, (ModelBase)this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1.0f);
                GL11.glBlendFunc((int)770, (int)771);
            }
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

