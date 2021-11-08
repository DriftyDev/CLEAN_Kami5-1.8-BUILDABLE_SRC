/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelPlayer
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EnumPlayerModelParts
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.text.TextFormatting
 *  org.lwjgl.opengl.GL11
 */
package tech.mmmax.kami.api.utils.render;

import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import tech.mmmax.kami.api.wrapper.IMinecraft;

public class RenderUtil
implements IMinecraft {
    public static Tessellator tessellator = Tessellator.getInstance();
    public static BufferBuilder builder = tessellator.getBuffer();

    public static float getInterpolatedLinWid(float distance, float line, float lineFactor) {
        return line * lineFactor / distance;
    }

    public static void renderBB(int glMode, AxisAlignedBB bb, Color bottom, Color top) {
        GL11.glShadeModel((int)7425);
        bb = RenderUtil.updateToCamera(bb);
        RenderUtil.prepare();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        builder = tessellator.getBuffer();
        builder.begin(glMode, DefaultVertexFormats.POSITION_COLOR);
        RenderUtil.buildBBBuffer(builder, bb, bottom, top);
        tessellator.draw();
        RenderUtil.release();
        GL11.glShadeModel((int)7424);
    }

    public static void renderBBWireFrame(int glMode, AxisAlignedBB bb, Color bottom, Color top) {
        GL11.glShadeModel((int)7425);
        bb = RenderUtil.updateToCamera(bb);
        RenderUtil.prepare();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        builder = tessellator.getBuffer();
        builder.begin(glMode, DefaultVertexFormats.POSITION_COLOR);
        RenderUtil.buildBBBufferWireFrame(builder, bb, bottom, top);
        tessellator.draw();
        RenderUtil.release();
        GL11.glShadeModel((int)7424);
    }

    public static void drawRectWatermark(int x, int y, int w, int h, int color) {
        Gui.drawRect((int)x, (int)y, (int)(x + w), (int)(y + h), (int)color);
    }

    public static void renderBBFog(AxisAlignedBB bb, Color main, Color center) {
        GL11.glShadeModel((int)7425);
        bb = RenderUtil.updateToCamera(bb);
        RenderUtil.prepare();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        builder = tessellator.getBuffer();
        builder.begin(4, DefaultVertexFormats.POSITION_COLOR);
        RenderUtil.buildBBBufferFog(builder, bb, main, center);
        tessellator.draw();
        RenderUtil.release();
        GL11.glShadeModel((int)7424);
    }

    public static void buildBBBuffer(BufferBuilder builder, AxisAlignedBB bb, Color bottom, Color top) {
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.minY, bb.minZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.minY, bb.minZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.minY, bb.maxZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.minY, bb.maxZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.minY, bb.minZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.maxY, bb.minZ, top);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.maxY, bb.maxZ, top);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.minY, bb.maxZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.minY, bb.maxZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.maxY, bb.maxZ, top);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.maxY, bb.maxZ, top);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.minY, bb.maxZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.minY, bb.maxZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.maxY, bb.maxZ, top);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.maxY, bb.minZ, top);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.minY, bb.minZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.minY, bb.minZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.maxY, bb.minZ, top);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.maxY, bb.minZ, top);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.minY, bb.minZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.maxY, bb.minZ, top);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.maxY, bb.minZ, top);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.maxY, bb.maxZ, top);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.maxY, bb.maxZ, top);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.maxY, bb.minZ, top);
    }

    public static void buildBBBufferWireFrame(BufferBuilder builder, AxisAlignedBB bb, Color bottom, Color top) {
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.minY, bb.minZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.minY, bb.minZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.minY, bb.maxZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.minY, bb.minZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.minY, bb.minZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.minY, bb.maxZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.minY, bb.maxZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.minY, bb.minZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.minY, bb.minZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.maxY, bb.minZ, top);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.maxY, bb.maxZ, top);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.minY, bb.maxZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.minY, bb.maxZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.maxY, bb.maxZ, top);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.maxY, bb.maxZ, top);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.minY, bb.maxZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.minY, bb.maxZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.maxY, bb.maxZ, top);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.maxY, bb.minZ, top);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.minY, bb.minZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.minY, bb.minZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.maxY, bb.minZ, top);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.maxY, bb.minZ, top);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.minY, bb.minZ, bottom);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.maxY, bb.minZ, top);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.maxY, bb.minZ, top);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.maxY, bb.maxZ, top);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.maxY, bb.minZ, top);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.maxY, bb.minZ, top);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.maxY, bb.minZ, top);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.maxY, bb.maxZ, top);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.maxY, bb.maxZ, top);
    }

    public static void buildBBBufferFog(BufferBuilder builder, AxisAlignedBB bb, Color main, Color center) {
        double centerX = (bb.maxX - bb.minX) / 2.0;
        double centerY = (bb.maxY - bb.minY) / 2.0;
        double centerZ = (bb.maxZ - bb.minZ) / 2.0;
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.minY, bb.minZ, main);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.minY, bb.minZ, main);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.minY, bb.maxZ, main);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.minY, bb.minZ, main);
        RenderUtil.addBuilderVertex(builder, bb.minX + centerX, bb.maxY, bb.minZ + centerZ, center);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.minY, bb.minZ, main);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.minY, bb.minZ, main);
        RenderUtil.addBuilderVertex(builder, bb.minX + centerX, bb.maxY, bb.minZ + centerZ, center);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.minY, bb.maxZ, main);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.minY, bb.maxZ, main);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.minY, bb.maxZ, main);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.minY, bb.minZ, main);
        RenderUtil.addBuilderVertex(builder, bb.maxX, bb.minY, bb.maxZ, main);
        RenderUtil.addBuilderVertex(builder, bb.minX + centerX, bb.maxY, bb.minZ + centerZ, center);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.minY, bb.maxZ, main);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.minY, bb.maxZ, main);
        RenderUtil.addBuilderVertex(builder, bb.minX + centerX, bb.maxY, bb.minZ + centerZ, center);
        RenderUtil.addBuilderVertex(builder, bb.minX, bb.minY, bb.minZ, main);
    }

    public static void addBuilderVertex(BufferBuilder bufferBuilder, double x, double y, double z, Color color) {
        bufferBuilder.pos(x, y, z).color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f).endVertex();
    }

    public static void prepare() {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.depthMask((boolean)false);
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glBlendFunc((int)770, (int)771);
    }

    public static void release() {
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
        GL11.glEnable((int)3553);
        GL11.glPolygonMode((int)1032, (int)6914);
    }

    public static AxisAlignedBB updateToCamera(AxisAlignedBB bb) {
        return new AxisAlignedBB(bb.minX - RenderUtil.mc.getRenderManager().viewerPosX, bb.minY - RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ - RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX - RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY - RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ - RenderUtil.mc.getRenderManager().viewerPosZ);
    }

    public static Vec3d updateToCamera(Vec3d vec) {
        return new Vec3d(vec.x - RenderUtil.mc.getRenderManager().viewerPosX, vec.y - RenderUtil.mc.getRenderManager().viewerPosY, vec.z - RenderUtil.mc.getRenderManager().viewerPosZ);
    }

    public static void renderEntity(EntityLivingBase entity, ModelBase modelBase, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (mc.getRenderManager() == null) {
            return;
        }
        if (modelBase instanceof ModelPlayer) {
            ModelPlayer modelPlayer = (ModelPlayer)modelBase;
            modelPlayer.bipedHeadwear.showModel = false;
            modelPlayer.bipedBodyWear.showModel = false;
            modelPlayer.bipedLeftLegwear.showModel = false;
            modelPlayer.bipedRightLegwear.showModel = false;
            modelPlayer.bipedLeftArmwear.showModel = false;
            modelPlayer.bipedRightArmwear.showModel = false;
        }
        float partialTicks = mc.getRenderPartialTicks();
        double x = entity.posX - RenderUtil.mc.getRenderManager().viewerPosX;
        double y = entity.posY - RenderUtil.mc.getRenderManager().viewerPosY;
        double z = entity.posZ - RenderUtil.mc.getRenderManager().viewerPosZ;
        GlStateManager.pushMatrix();
        if (entity.isSneaking()) {
            y -= 0.125;
        }
        RenderUtil.renderLivingAt(x, y, z);
        RenderUtil.prepareRotations(entity);
        float f4 = RenderUtil.prepareScale(entity, scale);
        float yaw = RenderUtil.handleRotationFloat(entity, partialTicks);
        GlStateManager.enableAlpha();
        modelBase.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
        modelBase.setRotationAngles(limbSwing, limbSwingAmount, 0.0f, yaw, entity.rotationPitch, f4, (Entity)entity);
        modelBase.render((Entity)entity, limbSwing, limbSwingAmount, 0.0f, yaw, entity.rotationPitch, f4);
        GlStateManager.popMatrix();
    }

    public static void renderLivingAt(double x, double y, double z) {
        GlStateManager.translate((float)((float)x), (float)((float)y), (float)((float)z));
    }

    public static float prepareScale(EntityLivingBase entity, float scale) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale((float)-1.0f, (float)-1.0f, (float)1.0f);
        double widthX = entity.getRenderBoundingBox().maxX - entity.getRenderBoundingBox().minX;
        double widthZ = entity.getRenderBoundingBox().maxZ - entity.getRenderBoundingBox().minZ;
        GlStateManager.scale((double)((double)scale + widthX), (double)(scale * entity.height), (double)((double)scale + widthZ));
        float f = 0.0625f;
        GlStateManager.translate((float)0.0f, (float)-1.501f, (float)0.0f);
        return f;
    }

    public static void prepareRotations(EntityLivingBase entityLivingBase) {
        GlStateManager.rotate((float)(180.0f - entityLivingBase.rotationYaw), (float)0.0f, (float)1.0f, (float)0.0f);
    }

    public static float handleRotationFloat(EntityLivingBase livingBase, float partialTicks) {
        return livingBase.rotationYawHead;
    }

    public static void applyRotations(EntityLivingBase entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
        GlStateManager.rotate((float)(180.0f - rotationYaw), (float)0.0f, (float)1.0f, (float)0.0f);
        if (entityLiving.deathTime > 0) {
            float f = ((float)entityLiving.deathTime + partialTicks - 1.0f) / 20.0f * 1.6f;
            if ((f = MathHelper.sqrt((float)f)) > 1.0f) {
                f = 1.0f;
            }
            GlStateManager.rotate((float)f, (float)0.0f, (float)0.0f, (float)1.0f);
        } else {
            String s = TextFormatting.getTextWithoutFormattingCodes((String)entityLiving.getName());
            if (s != null && ("Dinnerbone".equals(s) || "Grumm".equals(s)) && (!(entityLiving instanceof EntityPlayer) || ((EntityPlayer)entityLiving).isWearing(EnumPlayerModelParts.CAPE))) {
                GlStateManager.translate((float)0.0f, (float)(entityLiving.height + 0.1f), (float)0.0f);
                GlStateManager.rotate((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            }
        }
    }

    public static void glBillboard(float x, float y, float z, float scale2) {
        float scale = scale2;
        GlStateManager.translate((double)((double)x - RenderUtil.mc.getRenderManager().viewerPosX), (double)((double)y - RenderUtil.mc.getRenderManager().viewerPosY), (double)((double)z - RenderUtil.mc.getRenderManager().viewerPosZ));
        GlStateManager.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(-RenderUtil.mc.player.rotationYaw), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)RenderUtil.mc.player.rotationPitch, (float)(RenderUtil.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f), (float)0.0f, (float)0.0f);
        GlStateManager.scale((float)(-scale), (float)(-scale), (float)scale);
    }

    public static void glBillboardDistanceScaled(float x, float y, float z, EntityPlayer player, float scale) {
        RenderUtil.glBillboard(x, y, z, 0.01f * scale);
        int distance = (int)player.getDistance((double)x, (double)y, (double)z);
        float scaleDistance = (float)distance / 2.0f / (2.0f + (2.0f - scale));
        if (scaleDistance < 1.0f) {
            scaleDistance = 1.0f;
        }
        GlStateManager.scale((float)scaleDistance, (float)scaleDistance, (float)scaleDistance);
    }

    public static void drawGradientSideways(double leftpos, double top, double right, double bottom, int col1, int col2) {
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(col1 & 0xFF) / 255.0f;
        float f5 = (float)(col2 >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(col2 >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(col2 >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(col2 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)7);
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
        GL11.glVertex2d((double)leftpos, (double)top);
        GL11.glVertex2d((double)leftpos, (double)bottom);
        GL11.glColor4f((float)f6, (float)f7, (float)f8, (float)f5);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
    }
}

