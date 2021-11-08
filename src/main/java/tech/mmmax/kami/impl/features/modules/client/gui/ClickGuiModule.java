/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.impl.features.modules.client.gui;

import java.awt.Color;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.gui.theme.IColorScheme;
import tech.mmmax.kami.api.gui.theme.IMetrics;
import tech.mmmax.kami.api.utils.color.RainbowUtil;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;
import tech.mmmax.kami.impl.gui.ClickGui;
import tech.mmmax.kami.impl.gui.renderer.Renderer;

public class ClickGuiModule
extends Module
implements IColorScheme,
IMetrics {
    public static ClickGuiModule INSTANCE;
    Value<Boolean> textShadow = new ValueBuilder().withDescriptor("Text Shadow").withValue(false).register(this);
    public Value<Number> rainbowSat = new ValueBuilder().withDescriptor("Sat").withValue(1).withRange(0, 1).register(this);
    public Value<Number> rainbowBri = new ValueBuilder().withDescriptor("Bri").withValue(1).withRange(0, 1).register(this);
    Value<Boolean> rainbow = new ValueBuilder().withDescriptor("Rainbow").withValue(false).register(this);
    Value<Color> color = new ValueBuilder().withDescriptor("Color").withValue(new Color(32, 159, 220)).register(this);
    Value<Color> outlineColor = new ValueBuilder().withDescriptor("Outline Color").withValue(new Color(32, 159, 220)).register(this);
    Value<Number> frameText = new ValueBuilder().withDescriptor("Frame Text").withValue(255).withRange(0, 255).register(this);
    Value<Number> text = new ValueBuilder().withDescriptor("Text").withValue(0).withRange(0, 255).register(this);
    Value<Number> frameBackground = new ValueBuilder().withDescriptor("Background").withValue(255).withRange(0, 255).register(this);
    Value<Number> frameBackgroundAlpha = new ValueBuilder().withDescriptor("Background Alpha").withValue(150).withRange(0, 255).register(this);
    Value<Number> backgroundSecondary = new ValueBuilder().withDescriptor("Background S").withValue(255).withRange(0, 255).register(this);
    Value<Number> backgroundSecondaryAlpha = new ValueBuilder().withDescriptor("Background S Alpha").withValue(150).withRange(0, 255).register(this);
    Value<Number> backgroundTertiary = new ValueBuilder().withDescriptor("Background T").withValue(255).withRange(0, 255).register(this);
    Value<Number> backgroundTertiaryAlpha = new ValueBuilder().withDescriptor("Background T Alpha").withValue(150).withRange(0, 255).register(this);
    Value<Number> width = new ValueBuilder().withDescriptor("Width").withValue(120).withRange(80, 200).register(this);
    Value<Number> spacing = new ValueBuilder().withDescriptor("Spacing").withValue(2).withRange(0, 4).register(this);
    Value<Number> inBSpacing = new ValueBuilder().withDescriptor("In Spacing").withValue(1).withRange(0, 4).register(this);

    public ClickGuiModule() {
        super("ClickGUI", Feature.Category.Client);
        this.getBind().setKey(22);
        INSTANCE = this;

        this.rainbowSat.setActive(rainbow.getValue());
        this.rainbowBri.setActive(rainbow.getValue());
    }

    @Override
    public void onEnable() {
        super.onEnable();
        ClickGui.INSTANCE.enterGui(this, this, new Renderer());
        this.setEnabled(false);
    }

    @Override
    public Color getMainColor(int pos) {
        return this.rainbow.getValue() != false ? RainbowUtil.INSTANCE.getColor(this.rainbowSat.getValue().floatValue(), this.rainbowBri.getValue().floatValue()) : this.color.getValue();
    }

    @Override
    public Color getOutlineColor() {
        return this.rainbow.getValue() != false ? RainbowUtil.INSTANCE.getColor(this.rainbowSat.getValue().floatValue(), this.rainbowBri.getValue().floatValue()) : this.outlineColor.getValue();
    }

    @Override
    public Color getButtonColor() {
        return this.getMainColor(0);
    }

    @Override
    public Color getBackgroundColor() {
        return new Color(this.frameBackground.getValue().intValue(), this.frameBackground.getValue().intValue(), this.frameBackground.getValue().intValue(), this.frameBackgroundAlpha.getValue().intValue());
    }

    @Override
    public Color getSecondaryBackgroundColor() {
        return new Color(this.backgroundSecondary.getValue().intValue(), this.backgroundSecondary.getValue().intValue(), this.backgroundSecondary.getValue().intValue(), this.backgroundSecondaryAlpha.getValue().intValue());
    }

    @Override
    public Color getTertiaryBackgroundColor() {
        return new Color(this.backgroundTertiary.getValue().intValue(), this.backgroundTertiary.getValue().intValue(), this.backgroundTertiary.getValue().intValue(), this.backgroundTertiaryAlpha.getValue().intValue());
    }

    @Override
    public Color getTextColor() {
        return new Color(this.text.getValue().intValue(), this.text.getValue().intValue(), this.text.getValue().intValue(), 255);
    }

    @Override
    public Color getTextColorHighlight() {
        return new Color(this.frameText.getValue().intValue(), this.frameText.getValue().intValue(), this.frameText.getValue().intValue());
    }

    @Override
    public Color getTextColorActive() {
        return this.getMainColor(0);
    }

    @Override
    public boolean doesTextShadow() {
        return this.textShadow.getValue();
    }

    @Override
    public int getSpacing() {
        return this.spacing.getValue().intValue();
    }

    @Override
    public int getBetweenSpacing() {
        return this.inBSpacing.getValue().intValue();
    }

    @Override
    public int getSettingSpacing() {
        return 0;
    }

    @Override
    public int getFrameSpacing() {
        return this.getSpacing();
    }

    @Override
    public int getFrameWidth() {
        return this.width.getValue().intValue();
    }

    @Override
    public int getButtonHeight() {
        return 12;
    }

    @Override
    public int getFrameHeight() {
        return this.getButtonHeight() + 2;
    }

    @Override
    public float getOutlineWidth() {
        return 1.0f;
    }
}

