/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.render;

import java.awt.Color;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.event.RenderSkyEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;
import tech.mmmax.kami.impl.gui.ClickGui;

public class CustomSky
extends Module {
    Value<Number> time = new ValueBuilder().withDescriptor("Time").withValue(1000).withRange(0, 10000).register(this);
    Value<Number> red = new ValueBuilder().withDescriptor("Red").withValue(255).withRange(0, 255).register(this);
    Value<Number> green = new ValueBuilder().withDescriptor("Green").withValue(255).withRange(0, 255).register(this);
    Value<Number> blue = new ValueBuilder().withDescriptor("Blue").withValue(255).withRange(0, 255).register(this);
    Value<String> mode = new ValueBuilder().withDescriptor("Mode").withValue("Auto").withModes("Auto", "Custom", "Time").register(this);

    public CustomSky() {
        super("Custom Sky", Feature.Category.Render);
        this.time.setActive(((String)mode.getValue()).equals("Time"));
        this.red.setActive(((String)mode.getValue()).equals("Custom"));
        this.green.setActive(((String)mode.getValue()).equals("Custom"));
        this.blue.setActive(((String)mode.getValue()).equals("Custom"));
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (this.mode.getValue().equals("Time")) {
            CustomSky.mc.world.setWorldTime(this.time.getValue().longValue());
        }
    }

    @SubscribeEvent
    public void onRenderSky(RenderSkyEvent event) {
        if (!this.mode.getValue().equals(this.time)) {
            Color color = this.mode.getValue().equals("Custom") ? new Color(this.red.getValue().intValue(), this.green.getValue().intValue(), this.blue.getValue().intValue()) : ClickGui.CONTEXT.getColorScheme().getMainColor(0);
            event.setRed(color.getRed());
            event.setGreen(color.getGreen());
            event.setBlue(color.getBlue());
            event.setCanceled(true);
        }
    }
}

