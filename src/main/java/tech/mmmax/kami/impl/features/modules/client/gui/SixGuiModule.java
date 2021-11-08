/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.impl.features.modules.client.gui;

import java.awt.Color;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.gui.context.Context;
import tech.mmmax.kami.api.gui.helpers.MouseHelper;
import tech.mmmax.kami.api.gui.helpers.Rect;
import tech.mmmax.kami.api.gui.render.IRenderer;
import tech.mmmax.kami.api.gui.theme.IColorScheme;
import tech.mmmax.kami.api.gui.theme.IMetrics;
import tech.mmmax.kami.api.gui.widget.impl.BindWidget;
import tech.mmmax.kami.api.gui.widget.impl.BooleanWidget;
import tech.mmmax.kami.api.gui.widget.impl.ColorWidget;
import tech.mmmax.kami.api.gui.widget.impl.ComboBoxWidget;
import tech.mmmax.kami.api.gui.widget.impl.SliderWidget;
import tech.mmmax.kami.api.gui.widget.impl.TextEntryWidget;
import tech.mmmax.kami.impl.gui.components.module.FeatureButton;

public class SixGuiModule
extends Module
implements IColorScheme,
IMetrics,
IRenderer {
    public SixGuiModule() {
        super("Six Gui", Feature.Category.Client);
    }

    @Override
    public void preRender(Context context) {
    }

    @Override
    public void renderFrameTitle(Context context, Rect rect, MouseHelper mouse, String title, boolean open) {
    }

    @Override
    public void renderFrameOutline(Context context, Rect rect, MouseHelper mouse) {
    }

    @Override
    public void renderFrame(Context context, Rect rect, MouseHelper mouse) {
    }

    @Override
    public void renderBooleanWidget(BooleanWidget widget, Context context, Rect rect, MouseHelper mouse) {
    }

    @Override
    public void renderBindWidget(BindWidget widget, Context context, Rect rect, MouseHelper mouse) {
    }

    @Override
    public void renderFeatureButton(FeatureButton button, Context context, Rect rect, MouseHelper mouse) {
    }

    @Override
    public void renderComboBox(ComboBoxWidget widget, Context context, Rect rect, MouseHelper mouseHelper) {
    }

    @Override
    public void renderSliderWidget(SliderWidget widget, Context context, Rect rect, Rect sliderRect, MouseHelper mouse) {
    }

    @Override
    public void renderColorWidget(ColorWidget widget, Context context, boolean open, Rect headerRect, Rect dims, Rect container, Rect alphaSlider, Rect hueSlider, Rect colorSquare) {
    }

    @Override
    public void renderStringWidget(TextEntryWidget widget, Context context, Rect rect, MouseHelper mouse) {
    }

    @Override
    public int getTextWidth(String text) {
        return 0;
    }

    @Override
    public int getTextHeight(String text) {
        return 0;
    }

    @Override
    public void renderText(String text, float x, float y, Color color, boolean shadow) {
    }

    @Override
    public void renderRect(Rect rect, Color top, Color bottom, IRenderer.RectMode mode, Context context) {
    }

    @Override
    public void scissorRect(Rect dims) {
    }

    @Override
    public Color getMainColor(int pos) {
        return null;
    }

    @Override
    public Color getOutlineColor() {
        return null;
    }

    @Override
    public Color getButtonColor() {
        return null;
    }

    @Override
    public Color getBackgroundColor() {
        return null;
    }

    @Override
    public Color getSecondaryBackgroundColor() {
        return null;
    }

    @Override
    public Color getTertiaryBackgroundColor() {
        return null;
    }

    @Override
    public Color getTextColor() {
        return null;
    }

    @Override
    public Color getTextColorHighlight() {
        return null;
    }

    @Override
    public Color getTextColorActive() {
        return null;
    }

    @Override
    public boolean doesTextShadow() {
        return false;
    }

    @Override
    public int getSpacing() {
        return 0;
    }

    @Override
    public int getBetweenSpacing() {
        return 0;
    }

    @Override
    public int getSettingSpacing() {
        return 0;
    }

    @Override
    public int getFrameSpacing() {
        return 0;
    }

    @Override
    public int getFrameWidth() {
        return 0;
    }

    @Override
    public int getButtonHeight() {
        return 0;
    }

    @Override
    public int getFrameHeight() {
        return 0;
    }

    @Override
    public float getOutlineWidth() {
        return 0.0f;
    }
}

