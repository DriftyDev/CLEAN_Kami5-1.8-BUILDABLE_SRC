/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package tech.mmmax.kami.mixin.mixins.access;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={Entity.class})
public interface IEntity {
    @Accessor(value="inPortal")
    public boolean isInPortal();
}

