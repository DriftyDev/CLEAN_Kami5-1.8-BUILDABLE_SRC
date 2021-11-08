/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package tech.mmmax.kami.api.event;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PostPacketEvent
extends Event {
    Packet<?> packet;

    public PostPacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return this.packet;
    }
}

