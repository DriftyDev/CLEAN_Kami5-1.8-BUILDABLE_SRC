/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketConfirmTeleport
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package tech.mmmax.kami.impl.features.modules.combat;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tech.mmmax.kami.api.event.PacketEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class Desync
extends Module {
    Value<Boolean> sync = new ValueBuilder().withDescriptor("Sync").withValue(false).register(this);
    boolean cancel = false;
    List<CPacketConfirmTeleport> packets = new ArrayList<CPacketConfirmTeleport>();

    public Desync() {
        super("Desync", Feature.Category.Combat);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (event.getPacket() instanceof CPacketConfirmTeleport) {
            if (!this.cancel) {
                event.setCanceled(true);
                this.cancel = true;
                for (CPacketConfirmTeleport p : new ArrayList<CPacketConfirmTeleport>(this.packets)) {
                    mc.getConnection().sendPacket((Packet)p);
                }
                this.cancel = false;
                this.packets.add((CPacketConfirmTeleport)event.getPacket());
            } else {
                this.packets.remove((CPacketConfirmTeleport)event.getPacket());
            }
        }
        if ((event.getPacket() instanceof CPacketPlayerTryUseItem || event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) && this.sync.getValue().booleanValue()) {
            this.cancel = true;
            for (CPacketConfirmTeleport p : new ArrayList<CPacketConfirmTeleport>(this.packets)) {
                mc.getConnection().sendPacket((Packet)p);
            }
            this.cancel = false;
        }
    }
}

