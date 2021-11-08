/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package tech.mmmax.kami.impl.features.modules.misc;

import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tech.mmmax.kami.api.event.PacketEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.chat.ChatUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;
import tech.mmmax.kami.impl.KamiMod;
import tech.mmmax.kami.mixin.mixins.access.ICPacketChat;

public class ChatSuffix
extends Module {
    Value<String> mode = new ValueBuilder().withDescriptor("Mode").withValue(KamiMod.NAME).register(this);
    String[] filters = new String[]{".", "/", ",", ":", "`", "-"};

    public ChatSuffix() {
        super("Chat Suffix", Feature.Category.Misc);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (event.getTime() == PacketEvent.Time.Send && event.getPacket() instanceof CPacketChatMessage && this.allowMessage(((CPacketChatMessage)event.getPacket()).getMessage())) {
            ((ICPacketChat)((CPacketChatMessage)event.getPacket())).setMessage(((CPacketChatMessage)event.getPacket()).getMessage() + ChatUtils.hephaestus(" | " + this.mode.getValue().toLowerCase()));
        }
    }

    boolean allowMessage(String message) {
        boolean allow = true;
        for (String s : this.filters) {
            if (!message.startsWith(s)) continue;
            allow = false;
            break;
        }
        return allow;
    }
}

