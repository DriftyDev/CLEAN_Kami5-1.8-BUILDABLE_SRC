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

public class PacketEvent
extends Event {
    public Packet<?> packet;
    public Time time;

    public PacketEvent(Packet<?> packet, Time time) {
        this.packet = packet;
        this.time = time;
    }

    public Time getTime() {
        return this.time;
    }

    public Packet<?> getPacket() {
        return this.packet;
    }

    public boolean isCancelable() {
        return true;
    }

    public static enum Time {
        Send,
        Receive;

    }
}

