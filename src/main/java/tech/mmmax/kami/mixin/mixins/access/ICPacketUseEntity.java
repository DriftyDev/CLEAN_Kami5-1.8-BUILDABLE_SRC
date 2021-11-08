/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 */
package tech.mmmax.kami.mixin.mixins.access;

import net.minecraft.network.play.client.CPacketUseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={CPacketUseEntity.class})
public interface ICPacketUseEntity {
    @Accessor(value="entityId")
    public void setEntityId(int var1);

    @Accessor(value="action")
    public void setAction(CPacketUseEntity.Action var1);
}

