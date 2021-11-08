/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.api.utils.color;

import java.awt.Color;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class RainbowUtil {
    public static RainbowUtil INSTANCE;
    Color color = new Color(0, 0, 0);
    Color start = new Color(0, 0, 0);
    Color end = new Color(0, 0, 0);
    Modes mode = Modes.Rainbow;
    float hue = 0.0f;

    public RainbowUtil() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (this.mode == Modes.Rainbow) {
            this.hue += 1.0f;
            if (this.hue >= 360.0f) {
                this.hue = 0.0f;
            }
        }
    }

    public Color getColor(float sat, float bri) {
        return Color.getHSBColor(this.hue / 360.0f, sat, bri);
    }

    public Color getStart() {
        return this.start;
    }

    public Color getEnd() {
        return this.end;
    }

    public void setStart(Color start) {
        this.start = start;
    }

    public void setEnd(Color end) {
        this.end = end;
    }

    public Modes getMode() {
        return this.mode;
    }

    public void setMode(Modes mode) {
        this.mode = mode;
    }

    static enum Modes {
        SinWave,
        Rainbow;

    }
}

