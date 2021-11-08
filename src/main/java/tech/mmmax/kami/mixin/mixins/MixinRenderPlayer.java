/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.renderer.entity.RenderPlayer
 */
package tech.mmmax.kami.mixin.mixins;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.mmmax.kami.impl.features.modules.render.Nametags;

@Mixin(value={RenderPlayer.class})
public class MixinRenderPlayer {
    @Inject(method={"renderEntityName"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderNametag(AbstractClientPlayer entityIn, double x, double y, double z, String name, double distanceSq, CallbackInfo ci) {
        if (Nametags.INSTANCE.isEnabled()) {
            ci.cancel();
        }
    }
}

