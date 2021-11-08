/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiIngame
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package tech.mmmax.kami.mixin.mixins;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.mmmax.kami.api.event.RenderCrosshairEvent;

@Mixin(value={GuiIngame.class})
public class MixinGuiIngame {
    @Inject(method={"renderAttackIndicator"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderAttackIndicatorHook(float partialTicks, ScaledResolution sr, CallbackInfo ci) {
        RenderCrosshairEvent event = new RenderCrosshairEvent(partialTicks, sr);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}

