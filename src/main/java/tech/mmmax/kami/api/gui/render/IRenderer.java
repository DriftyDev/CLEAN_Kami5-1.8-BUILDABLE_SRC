/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.api.gui.render;

import java.awt.Color;
import tech.mmmax.kami.api.gui.context.Context;
import tech.mmmax.kami.api.gui.helpers.MouseHelper;
import tech.mmmax.kami.api.gui.helpers.Rect;
import tech.mmmax.kami.api.gui.widget.impl.BindWidget;
import tech.mmmax.kami.api.gui.widget.impl.BooleanWidget;
import tech.mmmax.kami.api.gui.widget.impl.ColorWidget;
import tech.mmmax.kami.api.gui.widget.impl.ComboBoxWidget;
import tech.mmmax.kami.api.gui.widget.impl.SliderWidget;
import tech.mmmax.kami.api.gui.widget.impl.TextEntryWidget;
import tech.mmmax.kami.impl.gui.components.module.FeatureButton;

public interface IRenderer {
    public void preRender(Context var1);

    public void renderFrameTitle(Context var1, Rect var2, MouseHelper var3, String var4, boolean var5);

    public void renderFrameOutline(Context var1, Rect var2, MouseHelper var3);

    public void renderFrame(Context var1, Rect var2, MouseHelper var3);

    public void renderBooleanWidget(BooleanWidget var1, Context var2, Rect var3, MouseHelper var4);

    public void renderBindWidget(BindWidget var1, Context var2, Rect var3, MouseHelper var4);

    public void renderFeatureButton(FeatureButton var1, Context var2, Rect var3, MouseHelper var4);

    public void renderComboBox(ComboBoxWidget var1, Context var2, Rect var3, MouseHelper var4);

    public void renderSliderWidget(SliderWidget var1, Context var2, Rect var3, Rect var4, MouseHelper var5);

    public void renderColorWidget(ColorWidget var1, Context var2, boolean var3, Rect var4, Rect var5, Rect var6, Rect var7, Rect var8, Rect var9);

    public void renderStringWidget(TextEntryWidget var1, Context var2, Rect var3, MouseHelper var4);

    public int getTextWidth(String var1);

    public int getTextHeight(String var1);

    public void renderText(String var1, float var2, float var3, Color var4, boolean var5);

    public void renderRect(Rect var1, Color var2, Color var3, RectMode var4, Context var5);

    public void scissorRect(Rect var1);

    public static enum RectMode {
        Fill,
        Outline;

    }
}

