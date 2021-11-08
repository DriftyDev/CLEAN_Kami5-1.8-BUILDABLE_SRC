/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.world.World
 *  net.minecraftforge.common.ForgeHooks
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package tech.mmmax.kami.mixin.mixins;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tech.mmmax.kami.api.event.PickBlockEvent;

@Mixin(value={ForgeHooks.class})
public class MixinForgeHooks {
    @Inject(method={"onPickBlock"}, at={@At(value="HEAD")}, cancellable=true, remap=false)
    private static void onPickBlock(RayTraceResult target, EntityPlayer player, World world, CallbackInfoReturnable<Boolean> cir) {
        PickBlockEvent event = new PickBlockEvent();
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCanceled()) {
            cir.cancel();
        }
    }
}

