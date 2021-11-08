/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.MoverType
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.world.World
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package tech.mmmax.kami.mixin.mixins;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tech.mmmax.kami.api.event.TravelEvent;
import tech.mmmax.kami.impl.features.modules.render.NoRender;

@Mixin(value={EntityPlayer.class})
public abstract class MixinEntityPlayer
extends EntityLivingBase {
    public MixinEntityPlayer(World worldIn) {
        super(worldIn);
    }

    @Inject(method={"isEntityInsideOpaqueBlock"}, at={@At(value="HEAD")}, cancellable=true)
    public void isEntityInsideOpaqueBlockHook(CallbackInfoReturnable<Boolean> info) {
        if (NoRender.INSTANCE.isEnabled()) {
            info.setReturnValue(false);
        }
    }

    @Inject(method={"travel"}, at={@At(value="HEAD")}, cancellable=true)
    public void travel(float strafe, float vertical, float forward, CallbackInfo info) {
        TravelEvent event = new TravelEvent();
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCanceled()) {
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            info.cancel();
        }
    }
}

