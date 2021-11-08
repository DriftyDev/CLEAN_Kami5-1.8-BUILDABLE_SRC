/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.network;

import java.util.Queue;
import tech.mmmax.network.packet.Packet;

public interface INetManager {
    public Queue<Packet> getReceiveQueue();

    public Queue<Packet> getSendQueue();

    public void sendPacket(Packet var1);

    public void receivePacket(Packet var1);

    default public void readQueue(int packetCount) {
        Queue<Packet> queue = this.getReceiveQueue();
        int read = 0;
        while (!queue.isEmpty()) {
            Packet packet = queue.poll();
            this.receivePacket(packet);
            if (packetCount == -1 || ++read < packetCount) continue;
            return;
        }
    }

    default public void sendQueue(int packetCount) {
        Queue<Packet> queue = this.getSendQueue();
        int sent = 0;
        while (!queue.isEmpty()) {
            Packet packet = queue.poll();
            this.sendPacket(packet);
            if (packetCount == -1 || ++sent < packetCount) continue;
            return;
        }
    }
}

