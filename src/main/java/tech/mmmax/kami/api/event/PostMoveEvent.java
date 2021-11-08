/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package tech.mmmax.kami.api.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class PostMoveEvent
extends Event {
    double x;
    double y;
    double z;

    public PostMoveEvent(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }
}

