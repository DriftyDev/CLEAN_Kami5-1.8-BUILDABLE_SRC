/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package tech.mmmax.kami.api.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderSkyEvent
extends Event {
    final float partialTicks;
    double red = 0.0;
    double green = 0.0;
    double blue = 0.0;

    public RenderSkyEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public double getRed() {
        return this.red;
    }

    public double getGreen() {
        return this.green;
    }

    public double getBlue() {
        return this.blue;
    }

    public void setRed(double red) {
        this.red = red;
    }

    public void setGreen(double green) {
        this.green = green;
    }

    public void setBlue(double blue) {
        this.blue = blue;
    }

    public boolean isCancelable() {
        return true;
    }
}

