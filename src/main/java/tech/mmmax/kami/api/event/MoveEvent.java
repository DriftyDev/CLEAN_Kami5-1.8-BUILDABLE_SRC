/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.MoverType
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package tech.mmmax.kami.api.event;

import net.minecraft.entity.MoverType;
import net.minecraftforge.fml.common.eventhandler.Event;

public class MoveEvent
extends Event {
    public MoverType type;
    public double x;
    public double y;
    public double z;
    public boolean moved = false;

    public MoveEvent(MoverType type, double x, double y, double z) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean isCancelable() {
        return true;
    }
}

