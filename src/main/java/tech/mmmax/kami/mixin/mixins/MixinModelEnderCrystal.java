/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelEnderCrystal
 *  net.minecraft.client.model.ModelRenderer
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package tech.mmmax.kami.mixin.mixins;

import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tech.mmmax.kami.api.event.CrystalBlockRenderEvent;

@Mixin(value={ModelEnderCrystal.class})
public class MixinModelEnderCrystal {
    @Redirect(method={"render"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/model/ModelRenderer;render(F)V", ordinal=1))
    public void rendeHook1(ModelRenderer modelRenderer, float scale) {
        CrystalBlockRenderEvent event = new CrystalBlockRenderEvent(modelRenderer, scale, 0);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            modelRenderer.render(scale);
        }
    }

    @Redirect(method={"render"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/model/ModelRenderer;render(F)V", ordinal=2))
    public void rendeHook2(ModelRenderer modelRenderer, float scale) {
        CrystalBlockRenderEvent event = new CrystalBlockRenderEvent(modelRenderer, scale, 1);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            modelRenderer.render(scale);
        }
    }

    @Redirect(method={"render"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/model/ModelRenderer;render(F)V", ordinal=3))
    public void rendeHook3(ModelRenderer modelRenderer, float scale) {
        CrystalBlockRenderEvent event = new CrystalBlockRenderEvent(modelRenderer, scale, 2);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            modelRenderer.render(scale);
        }
    }
}

