/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketKeepAlive
 */
package tech.mmmax.kami.mixin.mixins.access;

import net.minecraft.network.play.client.CPacketKeepAlive;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={CPacketKeepAlive.class})
public interface ICPacketKeepAlive {
    @Accessor(value="key")
    public void setKey(long var1);

    @Accessor(value="key")
    public long getKey();
}

