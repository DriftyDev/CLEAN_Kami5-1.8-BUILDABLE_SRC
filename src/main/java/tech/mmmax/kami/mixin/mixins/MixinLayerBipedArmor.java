/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBiped
 *  net.minecraft.client.renderer.entity.layers.LayerBipedArmor
 *  net.minecraft.inventory.EntityEquipmentSlot
 */
package tech.mmmax.kami.mixin.mixins;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.mmmax.kami.impl.features.modules.render.Chams;

@Mixin(value={LayerBipedArmor.class})
public class MixinLayerBipedArmor {
    @Inject(method={"setModelSlotVisible"}, at={@At(value="HEAD")}, cancellable=true)
    public void setModelSlotVisible(ModelBiped model, EntityEquipmentSlot slotIn, CallbackInfo ci) {
        if (Chams.INSTANCE.isEnabled() && Chams.INSTANCE.noArmor.getValue().booleanValue()) {
            ci.cancel();
            switch (slotIn) {
                case HEAD: {
                    model.bipedHead.showModel = false;
                    model.bipedHeadwear.showModel = false;
                }
                case CHEST: {
                    model.bipedBody.showModel = false;
                    model.bipedRightArm.showModel = false;
                    model.bipedLeftArm.showModel = false;
                }
                case LEGS: {
                    model.bipedBody.showModel = false;
                    model.bipedRightLeg.showModel = false;
                    model.bipedLeftLeg.showModel = false;
                }
                case FEET: {
                    model.bipedRightLeg.showModel = false;
                    model.bipedLeftLeg.showModel = false;
                }
            }
        }
    }
}

