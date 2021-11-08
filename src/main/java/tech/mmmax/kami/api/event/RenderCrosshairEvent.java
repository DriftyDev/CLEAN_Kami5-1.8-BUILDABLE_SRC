/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package tech.mmmax.kami.api.event;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderCrosshairEvent
extends Event {
    float partialTicks;
    ScaledResolution sr;

    public RenderCrosshairEvent(float partialTicks, ScaledResolution sr) {
        this.partialTicks = partialTicks;
        this.sr = sr;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public ScaledResolution getScaledResolution() {
        return this.sr;
    }

    public boolean isCancelable() {
        return true;
    }
}

