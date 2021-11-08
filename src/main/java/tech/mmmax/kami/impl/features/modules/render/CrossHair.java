/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package tech.mmmax.kami.impl.features.modules.render;

import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tech.mmmax.kami.api.event.RenderCrosshairEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.gui.helpers.Rect;
import tech.mmmax.kami.api.gui.render.IRenderer;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;
import tech.mmmax.kami.impl.gui.ClickGui;

public class CrossHair
extends Module {
    Value<Number> fillRed = new ValueBuilder().withDescriptor("Fill Red").withValue(255).withRange(0, 255).register(this);
    Value<Number> fillGreen = new ValueBuilder().withDescriptor("Fill Green").withValue(255).withRange(0, 255).register(this);
    Value<Number> fillBlue = new ValueBuilder().withDescriptor("Fill Blue").withValue(255).withRange(0, 255).register(this);
    Value<Number> outlineValue = new ValueBuilder().withDescriptor("Outline V").withValue(10).withRange(0, 255).register(this);
    Value<String> fillColorMode = new ValueBuilder().withDescriptor("Fill Mode").withValue("Normal").withModes("Normal", "Custom").register(this);
    Value<String> outlineColorMode = new ValueBuilder().withDescriptor("Outline Mode").withValue("Normal").withModes("Normal", "Custom", "None").register(this);
    Value<Number> width = new ValueBuilder().withDescriptor("Width").withValue(3).withRange(1, 8).register(this);
    Value<Number> length = new ValueBuilder().withDescriptor("Length").withValue(6).withRange(4, 15).register(this);
    Value<Number> dist = new ValueBuilder().withDescriptor("Distance").withValue(4).withRange(2, 8).register(this);
    Value<String> mode = new ValueBuilder().withDescriptor("Mode").withValue("Distance").withModes("Distance", "Close", "Dynamic").register(this);
    Value<Boolean> attackIndicator = new ValueBuilder().withDescriptor("Attack Indicator").withValue(false).register(this);

    public CrossHair() {
        super("Cross Hair", Feature.Category.Render);
        this.fillRed.setActive((fillColorMode.getValue()).equals("Custom"));
        this.fillGreen.setActive((fillColorMode.getValue()).equals("Custom"));
        this.fillBlue.setActive((fillColorMode.getValue()).equals("Custom"));

        this.outlineValue.setActive((outlineColorMode.getValue()).equals("Custom"));

        this.dist.setActive((mode.getValue()).equals("Distance"));
    }

    @SubscribeEvent
    public void onRender(RenderCrosshairEvent event) {
        GlStateManager.disableTexture2D();
        if (this.mode.getValue().equals("Close")) {
            this.renderCloseCrossHair(event.getPartialTicks(), event.getScaledResolution());
        }
        if (this.mode.getValue().equals("Distance")) {
            this.renderDistanceCrosshair(event.getPartialTicks(), event.getScaledResolution());
        }
        GlStateManager.enableTexture2D();
        event.setCanceled(true);
    }

    void renderCloseCrossHair(float partialTicks, ScaledResolution sr) {
        int centerX = sr.getScaledWidth() / 2;
        int centerY = sr.getScaledHeight() / 2;
        Rect vertical = new Rect(centerX - this.width.getValue().intValue() / 2, centerY - this.length.getValue().intValue(), this.width.getValue().intValue(), this.length.getValue().intValue() * 2);
        Rect horizontal = new Rect(centerX - this.length.getValue().intValue(), centerY - this.width.getValue().intValue() / 2, this.length.getValue().intValue() * 2, this.width.getValue().intValue());
        ClickGui.CONTEXT.getRenderer().renderRect(vertical, this.getOutlineColor(), this.getOutlineColor(), IRenderer.RectMode.Outline, ClickGui.CONTEXT);
        ClickGui.CONTEXT.getRenderer().renderRect(horizontal, this.getOutlineColor(), this.getOutlineColor(), IRenderer.RectMode.Outline, ClickGui.CONTEXT);
        ClickGui.CONTEXT.getRenderer().renderRect(vertical, this.getColor(), this.getColor(), IRenderer.RectMode.Fill, ClickGui.CONTEXT);
        ClickGui.CONTEXT.getRenderer().renderRect(horizontal, this.getColor(), this.getColor(), IRenderer.RectMode.Fill, ClickGui.CONTEXT);
    }

    void renderDistanceCrosshair(float partialTicks, ScaledResolution sr) {
        int centerX = sr.getScaledWidth() / 2;
        int centerY = sr.getScaledHeight() / 2;
        Rect top = new Rect(centerX - this.width.getValue().intValue() / 2, centerY - this.length.getValue().intValue() - this.dist.getValue().intValue() / 2, this.width.getValue().intValue(), this.length.getValue().intValue());
        Rect left = new Rect(centerX - this.length.getValue().intValue() - this.dist.getValue().intValue() / 2, centerY - this.width.getValue().intValue() / 2, this.length.getValue().intValue(), this.width.getValue().intValue());
        Rect right = new Rect(centerX + this.dist.getValue().intValue() / 2, centerY - this.width.getValue().intValue() / 2, this.length.getValue().intValue(), this.width.getValue().intValue());
        Rect bottom = new Rect(centerX - this.width.getValue().intValue() / 2, centerY + this.dist.getValue().intValue() / 2, this.width.getValue().intValue(), this.length.getValue().intValue());
        ClickGui.CONTEXT.getRenderer().renderRect(top, this.getOutlineColor(), this.getOutlineColor(), IRenderer.RectMode.Outline, ClickGui.CONTEXT);
        ClickGui.CONTEXT.getRenderer().renderRect(left, this.getOutlineColor(), this.getOutlineColor(), IRenderer.RectMode.Outline, ClickGui.CONTEXT);
        ClickGui.CONTEXT.getRenderer().renderRect(bottom, this.getOutlineColor(), this.getOutlineColor(), IRenderer.RectMode.Outline, ClickGui.CONTEXT);
        ClickGui.CONTEXT.getRenderer().renderRect(right, this.getOutlineColor(), this.getOutlineColor(), IRenderer.RectMode.Outline, ClickGui.CONTEXT);
        ClickGui.CONTEXT.getRenderer().renderRect(top, this.getColor(), this.getColor(), IRenderer.RectMode.Fill, ClickGui.CONTEXT);
        ClickGui.CONTEXT.getRenderer().renderRect(left, this.getColor(), this.getColor(), IRenderer.RectMode.Fill, ClickGui.CONTEXT);
        ClickGui.CONTEXT.getRenderer().renderRect(bottom, this.getColor(), this.getColor(), IRenderer.RectMode.Fill, ClickGui.CONTEXT);
        ClickGui.CONTEXT.getRenderer().renderRect(right, this.getColor(), this.getColor(), IRenderer.RectMode.Fill, ClickGui.CONTEXT);
    }

    Color getColor() {
        switch (this.fillColorMode.getValue()) {
            case "Normal": {
                return ClickGui.CONTEXT.getColorScheme().getMainColor(0);
            }
            case "Custom": {
                return new Color(this.fillRed.getValue().intValue(), this.fillGreen.getValue().intValue(), this.fillBlue.getValue().intValue());
            }
        }
        return new Color(255, 255, 255, 255);
    }

    Color getOutlineColor() {
        switch (this.outlineColorMode.getValue()) {
            case "Normal": {
                return new Color(0, 0, 0, 255);
            }
            case "Custom": {
                return new Color(this.outlineValue.getValue().intValue(), this.outlineValue.getValue().intValue(), this.outlineValue.getValue().intValue(), 255);
            }
            case "None": {
                return new Color(0, 0, 0, 0);
            }
        }
        return new Color(0, 0, 0, 100);
    }
}

