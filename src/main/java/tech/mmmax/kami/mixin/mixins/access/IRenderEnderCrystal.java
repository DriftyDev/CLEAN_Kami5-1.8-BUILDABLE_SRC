/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.renderer.entity.RenderEnderCrystal
 */
package tech.mmmax.kami.mixin.mixins.access;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={RenderEnderCrystal.class})
public interface IRenderEnderCrystal {
    @Accessor(value="modelEnderCrystalNoBase")
    public ModelBase getModelEnderCrystalNoBase();

    @Accessor(value="modelEnderCrystalNoBase")
    public void setModelEnderCrystalNoBase(ModelBase var1);
}

