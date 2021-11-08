/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.network.packet;

import org.json.simple.JSONObject;

public abstract class Packet {
    public JSONObject getHeader() {
        JSONObject object = new JSONObject();
        object.put("type", this.getClass().getName());
        return object;
    }

    public abstract void fromJsonObject(JSONObject var1);

    public abstract JSONObject readPacketData();

    public abstract void handlePacket();
}

