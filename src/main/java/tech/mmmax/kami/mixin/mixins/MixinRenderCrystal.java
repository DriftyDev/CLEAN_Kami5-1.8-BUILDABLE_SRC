/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.renderer.entity.RenderEnderCrystal
 *  net.minecraft.entity.Entity
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package tech.mmmax.kami.mixin.mixins;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tech.mmmax.kami.api.event.RenderEntityEvent;

@Mixin(value={RenderEnderCrystal.class}, priority=0x7FFFFFFE)
public class MixinRenderCrystal {
    @Redirect(method={"doRender"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void renderModelBaseHook(ModelBase modelBase, Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        RenderEntityEvent event = new RenderEntityEvent(modelBase, entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            modelBase.render(entityIn, limbSwing, event.limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }
}

