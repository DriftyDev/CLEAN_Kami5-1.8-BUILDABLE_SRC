/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.NetworkManager
 *  net.minecraft.network.login.server.SPacketDisconnect
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.network.play.server.SPacketDisconnect
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package tech.mmmax.kami.impl.features.modules.misc;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tech.mmmax.kami.api.event.PacketEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;
import tech.mmmax.kami.mixin.mixins.access.INetHandlerPlayClient;

public class NettyTest
extends Module {
    Value<Boolean> cancelCPacket = new ValueBuilder().withDescriptor("Cancel C").withValue(false).register(this);
    Value<Boolean> cancelKick = new ValueBuilder().withDescriptor("Cancel Kick").withValue(false).register(this);
    Value<Boolean> switchNetty = new ValueBuilder().withDescriptor("Switch Netty").withValue(false).register(this);
    NetworkManager networkManager = null;
    boolean cancel = false;

    public NettyTest() {
        super("NettyTest", Feature.Category.Misc);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (!(event.getPacket() instanceof CPacketChatMessage) || !this.switchNetty.getValue().booleanValue() || this.networkManager != null) {
            // empty if block
        }
        if (event.getTime() == PacketEvent.Time.Send && this.cancelCPacket.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if ((event.getPacket() instanceof net.minecraft.network.play.server.SPacketDisconnect || event.getPacket() instanceof SPacketDisconnect) && this.cancelKick.getValue().booleanValue()) {
            event.setCanceled(true);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (NullUtils.nullCheck()) {
            return;
        }
        if (this.switchNetty.getValue().booleanValue() && this.networkManager != null) {
            ((INetHandlerPlayClient)mc.getConnection()).setNetManager(this.networkManager);
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (NullUtils.nullCheck()) {
            return;
        }
        if (this.switchNetty.getValue().booleanValue()) {
            this.networkManager = mc.getConnection().getNetworkManager();
        }
    }
}

