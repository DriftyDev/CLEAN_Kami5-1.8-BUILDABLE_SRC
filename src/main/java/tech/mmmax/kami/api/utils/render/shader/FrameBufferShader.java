/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.shader.Framebuffer
 *  org.lwjgl.opengl.GL11
 */
package tech.mmmax.kami.api.utils.render.shader;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import tech.mmmax.kami.api.utils.render.shader.Shader;
import tech.mmmax.kami.api.wrapper.IMinecraft;

public abstract class FrameBufferShader
extends Shader
implements IMinecraft {
    private static Framebuffer framebuffer;

    public FrameBufferShader(String fragmentShader) {
        super(fragmentShader);
        framebuffer = this.setupFrameBuffer(framebuffer);
    }

    public void startDraw(float partialTicks) {
        GlStateManager.enableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        FrameBufferShader.mc.gameSettings.entityShadows = false;
    }

    public void stopDraw() {
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        mc.getFramebuffer().bindFramebuffer(true);
        FrameBufferShader.mc.entityRenderer.disableLightmap();
        RenderHelper.disableStandardItemLighting();
        FrameBufferShader.mc.entityRenderer.setupOverlayRendering();
        this.drawFramebuffer(framebuffer);
        FrameBufferShader.mc.entityRenderer.disableLightmap();
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    public Framebuffer setupFrameBuffer(Framebuffer frameBuffer) {
        if (frameBuffer != null) {
            frameBuffer.deleteFramebuffer();
        }
        frameBuffer = new Framebuffer(FrameBufferShader.mc.displayWidth, FrameBufferShader.mc.displayHeight, true);
        return frameBuffer;
    }

    public void drawFramebuffer(Framebuffer framebuffer) {
        this.startShader();
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        GL11.glBindTexture((int)3553, (int)framebuffer.framebufferTexture);
        GL11.glBegin((int)7);
        GL11.glTexCoord2d((double)0.0, (double)1.0);
        GL11.glVertex2d((double)0.0, (double)0.0);
        GL11.glTexCoord2d((double)0.0, (double)0.0);
        GL11.glVertex2d((double)0.0, (double)scaledResolution.getScaledHeight());
        GL11.glTexCoord2d((double)1.0, (double)0.0);
        GL11.glVertex2d((double)scaledResolution.getScaledWidth(), (double)scaledResolution.getScaledHeight());
        GL11.glTexCoord2d((double)1.0, (double)1.0);
        GL11.glVertex2d((double)scaledResolution.getScaledWidth(), (double)0.0);
        GL11.glEnd();
        this.stopShader();
    }
}

