/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.item.ItemChorusFruit
 *  net.minecraft.item.ItemFood
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package tech.mmmax.kami.mixin.mixins;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemChorusFruit;
import net.minecraft.item.ItemFood;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tech.mmmax.kami.api.event.ChorusEvent;

@Mixin(value={ItemChorusFruit.class})
public class MixinItemChorusFruit
extends ItemFood {
    public MixinItemChorusFruit(int amount, float saturation) {
        super(amount, saturation, false);
    }

    @Redirect(method={"onItemUseFinish"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/EntityLivingBase;attemptTeleport(DDD)Z"))
    public boolean attemptTeleportHook(EntityLivingBase entityLivingBase, double x, double y, double z) {
        ChorusEvent event = new ChorusEvent(entityLivingBase, x, y, z);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            return entityLivingBase.attemptTeleport(x, y, z);
        }
        return event.isSuccessful();
    }
}

