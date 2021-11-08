/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerAbilities
 *  net.minecraft.network.play.server.SPacketPlayerAbilities
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.misc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.event.PacketEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.Timer;
import tech.mmmax.kami.api.utils.chat.ChatMessage;
import tech.mmmax.kami.api.utils.chat.ChatUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class Disabler
extends Module {
    Timer timer = new Timer();
    Value<String> mode = new ValueBuilder().withDescriptor("Mode").withValue("NCP").withModes("NCP", "AAC").register(this);
    Value<Boolean> debug = new ValueBuilder().withDescriptor("Debug").withValue(false).register(this);
    Value<Boolean> constant = new ValueBuilder().withDescriptor("Constant").withValue(false).register(this);
    Value<Number> delay = new ValueBuilder().withDescriptor("Delay").withValue(200).withRange(0, 2000).register(this);

    public Disabler() {
        super("Disabler", Feature.Category.Misc);

        this.timer.setDelay((delay.getValue()).longValue());
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (NullUtils.nullCheck() || event == null) {
            return;
        }
        if (event.getPacket() instanceof SPacketPlayerAbilities) {
            SPacketPlayerAbilities sPacketPlayerAbilities = (SPacketPlayerAbilities)event.getPacket();
        }
        if (this.mode.getValue().equals("NCP") && event.getPacket() instanceof CPacketPlayerAbilities) {
            CPacketPlayerAbilities packet = (CPacketPlayerAbilities)event.getPacket();
            packet.setAllowFlying(true);
            packet.setInvulnerable(true);
            packet.setWalkSpeed(3.0f);
            packet.setCreativeMode(true);
            if (this.debug.getValue().booleanValue()) {
                ChatUtils.sendMessage(new ChatMessage("Editing player abilities", false, 0));
            }
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (this.mode.getValue().equals("NCP") && this.constant.getValue().booleanValue() && this.timer.isPassed()) {
            CPacketPlayerAbilities packet = new CPacketPlayerAbilities(Disabler.mc.player.capabilities);
            mc.getConnection().sendPacket((Packet)packet);
            this.timer.resetDelay();
        }
    }

    @Override
    public String getHudInfo() {
        return this.mode.getValue();
    }
}

