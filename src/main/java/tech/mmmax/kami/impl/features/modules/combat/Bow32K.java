/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Items
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumHand
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package tech.mmmax.kami.impl.features.modules.combat;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tech.mmmax.kami.api.event.PacketEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.chat.ChatMessage;
import tech.mmmax.kami.api.utils.chat.ChatUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class Bow32K
extends Module {
    Value<Number> height = new ValueBuilder().withDescriptor("Height").withValue(10).withRange(0, 200).register(this);
    Value<Number> increment = new ValueBuilder().withDescriptor("Increment").withValue(0.06).withRange(0.01, 0.2).register(this);
    Value<Boolean> bounds = new ValueBuilder().withDescriptor("Bounds").withValue(false).register(this);
    List<CPacketPlayerDigging> packets = new ArrayList<CPacketPlayerDigging>();

    public Bow32K() {
        super("Bow32K", Feature.Category.Combat);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (event.getPacket() instanceof CPacketPlayerDigging) {
            CPacketPlayerDigging packet = (CPacketPlayerDigging)event.getPacket();
            if (packet.getAction() != CPacketPlayerDigging.Action.RELEASE_USE_ITEM) {
                return;
            }
            if (Bow32K.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.BOW) {
                for (double d = this.increment.getValue().doubleValue(); d < this.height.getValue().doubleValue(); d += this.increment.getValue().doubleValue()) {
                    mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(Bow32K.mc.player.posX, Bow32K.mc.player.posY + d, Bow32K.mc.player.posZ, false));
                    if (!this.bounds.getValue().booleanValue()) continue;
                    mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(Bow32K.mc.player.posX, 1337.0, Bow32K.mc.player.posZ, false));
                }
                ChatUtils.sendMessage(new ChatMessage("Attempting to boost arrows", false, 0));
            }
        }
    }
}

