/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package tech.mmmax.kami.mixin.mixins;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tech.mmmax.kami.api.event.PushOutOfBlocksEvent;

@Mixin(value={Entity.class})
public class MixinEntity {
    @Inject(method={"pushOutOfBlocks"}, at={@At(value="HEAD")}, cancellable=true)
    public void pushOutOfBlocks(double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        PushOutOfBlocksEvent event = new PushOutOfBlocksEvent();
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCanceled()) {
            cir.cancel();
        }
    }
}

