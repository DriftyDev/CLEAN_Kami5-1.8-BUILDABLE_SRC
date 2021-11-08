/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Text
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package tech.mmmax.kami.api.feature.hud;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;
import tech.mmmax.kami.api.wrapper.IMinecraft;

public class HudComponent
extends Feature
implements IMinecraft {
    public Value<Number> xPos = new ValueBuilder().withDescriptor("X Pos").withValue(100).withRange(0, 1000).register(this);
    public Value<Number> yPos = new ValueBuilder().withDescriptor("Y Pos").withValue(10).withRange(0, 1000).register(this);
    ScaledResolution sr;

    public HudComponent(String name) {
        super(name, Feature.Category.Hud, Feature.FeatureType.Hud);
    }

    @SubscribeEvent
    public void draw(RenderGameOverlayEvent.Text event) {
        this.sr = new ScaledResolution(mc);
        this.xPos.setMax(this.sr.getScaledWidth());
        this.yPos.setMax(this.sr.getScaledHeight());
    }
}

