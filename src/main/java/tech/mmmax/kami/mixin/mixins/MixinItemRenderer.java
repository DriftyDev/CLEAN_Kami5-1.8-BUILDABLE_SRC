/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.ItemRenderer
 *  net.minecraft.util.EnumHandSide
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package tech.mmmax.kami.mixin.mixins;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tech.mmmax.kami.api.event.RenderItemEvent;

@Mixin(value={ItemRenderer.class})
public class MixinItemRenderer {
    @Redirect(method={"renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/ItemRenderer;transformSideFirstPerson(Lnet/minecraft/util/EnumHandSide;F)V"))
    public void transformRedirect(ItemRenderer renderer, EnumHandSide hand, float y) {
        RenderItemEvent event = new RenderItemEvent(0.56f, -0.52f + y * -0.6f, -0.72f, -0.56f, -0.52f + y * -0.6f, -0.72f, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (hand == EnumHandSide.RIGHT) {
            GlStateManager.translate((double)event.getMainX(), (double)event.getMainY(), (double)event.getMainZ());
            GlStateManager.scale((double)event.getMainHandScaleX(), (double)event.getMainHandScaleY(), (double)event.getMainHandScaleZ());
            GlStateManager.rotate((float)((float)event.getMainRAngel()), (float)((float)event.getMainRx()), (float)((float)event.getMainRy()), (float)((float)event.getMainRz()));
        } else {
            GlStateManager.translate((double)event.getOffX(), (double)event.getOffY(), (double)event.getOffZ());
            GlStateManager.scale((double)event.getOffHandScaleX(), (double)event.getOffHandScaleY(), (double)event.getOffHandScaleZ());
            GlStateManager.rotate((float)((float)event.getOffRAngel()), (float)((float)event.getOffRx()), (float)((float)event.getOffRy()), (float)((float)event.getOffRz()));
        }
    }
}

