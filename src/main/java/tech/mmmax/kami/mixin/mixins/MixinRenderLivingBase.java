/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$Profile
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.client.renderer.entity.RenderLivingBase
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package tech.mmmax.kami.mixin.mixins;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.mmmax.kami.api.event.RenderEntityEvent;

@Mixin(value={RenderLivingBase.class}, priority=-1)
public class MixinRenderLivingBase<T extends EntityLivingBase>
extends Render<T> {
    @Shadow
    protected ModelBase mainModel;

    public MixinRenderLivingBase(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
        super(renderManagerIn);
        this.mainModel = modelBaseIn;
        this.shadowSize = shadowSizeIn;
    }

    @Inject(method={"renderModel"}, at={@At(value="HEAD")}, cancellable=true)
    void doRender(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, CallbackInfo ci) {
        boolean flag = true;
        boolean flag1 = !flag && !entityIn.isInvisibleToPlayer((EntityPlayer)Minecraft.getMinecraft().player);
        RenderEntityEvent event = new RenderEntityEvent(this.mainModel, (Entity)entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        if (flag || flag1) {
            if (!this.bindEntityTexture(entityIn)) {
                return;
            }
            if (flag1) {
                GlStateManager.enableBlendProfile((GlStateManager.Profile)GlStateManager.Profile.TRANSPARENT_MODEL);
            }
            MinecraftForge.EVENT_BUS.post((Event)event);
            if (flag1) {
                GlStateManager.disableBlendProfile((GlStateManager.Profile)GlStateManager.Profile.TRANSPARENT_MODEL);
            }
        }
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Nullable
    protected ResourceLocation getEntityTexture(T entity) {
        return null;
    }
}

