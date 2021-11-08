/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package tech.mmmax.kami.api.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class StepEvent
extends Event {
    public double height = 0.0;

    public StepEvent(double height) {
        this.height = height;
    }
}

