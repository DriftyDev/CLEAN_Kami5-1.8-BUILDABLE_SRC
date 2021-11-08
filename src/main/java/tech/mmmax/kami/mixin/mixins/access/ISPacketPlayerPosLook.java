/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 */
package tech.mmmax.kami.mixin.mixins.access;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={SPacketPlayerPosLook.class})
public interface ISPacketPlayerPosLook {
    @Accessor(value="yaw")
    public void setYaw(float var1);

    @Accessor(value="pitch")
    public void setPitch(float var1);

    @Accessor(value="x")
    public void setX(double var1);

    @Accessor(value="y")
    public void setY(double var1);

    @Accessor(value="z")
    public void setZ(double var1);
}

