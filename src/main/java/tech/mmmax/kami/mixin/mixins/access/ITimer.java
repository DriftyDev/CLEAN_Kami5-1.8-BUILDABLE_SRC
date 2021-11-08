/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Timer
 */
package tech.mmmax.kami.mixin.mixins.access;

import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={Timer.class})
public interface ITimer {
    @Accessor(value="tickLength")
    public float getTickLength();

    @Accessor(value="tickLength")
    public void setTickLength(float var1);
}

