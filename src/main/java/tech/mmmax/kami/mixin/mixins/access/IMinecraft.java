/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.Timer
 */
package tech.mmmax.kami.mixin.mixins.access;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={Minecraft.class})
public interface IMinecraft {
    @Accessor(value="timer")
    public Timer getTimer();
}

