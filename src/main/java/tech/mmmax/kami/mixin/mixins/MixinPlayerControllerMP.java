/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.util.EnumHand
 *  org.lwjgl.input.Mouse
 */
package tech.mmmax.kami.mixin.mixins;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.mmmax.kami.impl.features.modules.misc.MultiTask;

@Mixin(value={PlayerControllerMP.class})
public class MixinPlayerControllerMP {
    @Inject(method={"onStoppedUsingItem"}, at={@At(value="HEAD")}, cancellable=true)
    public void onStopItem(EntityPlayer playerIn, CallbackInfo ci) {
        if (MultiTask.INSTANCE.isEnabled() && MultiTask.INSTANCE.cancel.getValue().booleanValue() && playerIn.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.GOLDEN_APPLE && Mouse.isButtonDown((int)0)) {
            ci.cancel();
        }
    }
}

