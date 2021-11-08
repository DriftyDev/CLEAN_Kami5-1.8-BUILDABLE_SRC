/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.math.MathHelper
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
package tech.mmmax.kami.impl.features.modules.client.gui;

import java.awt.Color;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
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
import tech.mmmax.kami.api.utils.MathUtil;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.color.ColorUtil;
import tech.mmmax.kami.api.utils.render.font.CFontRenderer;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;
import tech.mmmax.kami.impl.features.modules.client.FontModule;
import tech.mmmax.kami.impl.gui.ClickGui;
import tech.mmmax.kami.impl.gui.components.module.FeatureButton;

public class SecondGuiModule
extends Module
implements IColorScheme,
IMetrics,
IRenderer {
    public static CFontRenderer fontRenderer = new CFontRenderer(FontModule.INSTANCE.font);
    Value<Boolean> textShadow = new ValueBuilder().withDescriptor("Text Shadow").withValue(true).register(this);
    Value<Color> textColor = new ValueBuilder().withDescriptor("Text Color").withValue(new Color(255, 255, 255, 255)).register(this);
    Value<Color> color = new ValueBuilder().withDescriptor("Color").withValue(new Color(0, 179, 42, 255)).register(this);
    Value<Color> headerColor = new ValueBuilder().withDescriptor("Header Color").withValue(new Color(0, 0, 0, 150)).register(this);
    Value<Color> headerLine = new ValueBuilder().withDescriptor("Header Line").withValue(new Color(0, 179, 42, 255)).register(this);
    Value<Number> headerLineWidth = new ValueBuilder().withDescriptor("Header Line Width").withValue(1.5).withRange(1, 5).register(this);
    Value<Boolean> centerHeader = new ValueBuilder().withDescriptor("Center Header").withValue(false).register(this);
    Value<Color> backgroundColor = new ValueBuilder().withDescriptor("Background Color").withValue(new Color(76, 76, 76, 150)).register(this);
    Value<Color> backgroundSecondary = new ValueBuilder().withDescriptor("Background Second").withValue(new Color(33, 33, 33, 150)).register(this);
    Value<Color> backgroundTertiary = new ValueBuilder().withDescriptor("Background Tert").withValue(new Color(3, 3, 3, 174)).register(this);
    Value<Color> outlineColor = new ValueBuilder().withDescriptor("Outline Color").withValue(new Color(0, 0, 0, 255)).register(this);
    Value<Number> width = new ValueBuilder().withDescriptor("Width").withValue(100).withRange(60, 200).register(this);
    Value<Number> gradientSize = new ValueBuilder().withDescriptor("Gradient Size").withValue(100).withRange(0, 600).register(this);
    Value<Color> bgColor = new ValueBuilder().withDescriptor("Background").withValue(new Color(0, 0, 0, 25)).register(this);

    public SecondGuiModule() {
        super("Second Gui", Feature.Category.Client);
    }

    @Override
    public void onEnable() {
        if (NullUtils.nullCheck()) {
            return;
        }
        super.onEnable();
        ClickGui.INSTANCE.enterGui(this, this, this);
        this.setEnabled(false);
    }

    @Override
    public void preRender(Context context) {
        GlStateManager.disableAlpha();
        this.renderRect(new Rect(0, context.getScaledResolution().getScaledHeight() - this.gradientSize.getValue().intValue(), context.getScaledResolution().getScaledWidth(), this.gradientSize.getValue().intValue()), this.bgColor.getValue(), context.getColorScheme().getMainColor(0), IRenderer.RectMode.Fill, context);
        this.renderRect(new Rect(0, 0, context.getScaledResolution().getScaledWidth(), context.getScaledResolution().getScaledHeight() - this.gradientSize.getValue().intValue()), this.bgColor.getValue(), this.bgColor.getValue(), IRenderer.RectMode.Fill, context);
        GlStateManager.enableAlpha();
    }

    @Override
    public void renderFrameTitle(Context context, Rect rect, MouseHelper mouse, String title, boolean open) {
        GL11.glLineWidth((float)this.headerLineWidth.getValue().intValue());
        context.getRenderer().renderRect(rect, this.headerColor.getValue(), this.headerColor.getValue(), IRenderer.RectMode.Fill, context);
        context.getRenderer().renderRect(rect, this.headerLine.getValue(), this.headerLine.getValue(), IRenderer.RectMode.Outline, context);
        int centerY = (rect.getHeight() - context.getRenderer().getTextHeight(title)) / 2;
        int centerX = (rect.getWidth() - context.getRenderer().getTextWidth(title)) / 2;
        context.getRenderer().renderText(title, rect.getX() + (this.centerHeader.getValue() != false ? centerX : 2), rect.getY() + centerY, context.getColorScheme().getTextColor(), context.getColorScheme().doesTextShadow());
        GL11.glLineWidth((float)1.0f);
    }

    @Override
    public void renderFrameOutline(Context context, Rect rect, MouseHelper mouse) {
    }

    @Override
    public void renderFrame(Context context, Rect rect, MouseHelper mouse) {
        context.getRenderer().renderRect(rect, context.getColorScheme().getBackgroundColor(), context.getColorScheme().getBackgroundColor(), IRenderer.RectMode.Fill, context);
    }

    @Override
    public void renderBooleanWidget(BooleanWidget widget, Context context, Rect rect, MouseHelper mouse) {
        Color c = widget.getValue() != false ? context.getColorScheme().getMainColor(rect.getY()) : context.getColorScheme().getTertiaryBackgroundColor();
        context.getRenderer().renderRect(rect, c, c, IRenderer.RectMode.Fill, context);
        if (widget.getValue().booleanValue()) {
            context.getRenderer().renderRect(rect, context.getColorScheme().getOutlineColor(), context.getColorScheme().getOutlineColor(), IRenderer.RectMode.Outline, context);
        }
        int centerY = (rect.getHeight() - context.getRenderer().getTextHeight(widget.getTitle())) / 2;
        context.getRenderer().renderText(widget.getTitle(), rect.getX() + 2, rect.getY() + centerY, context.getColorScheme().getTextColor(), context.getColorScheme().doesTextShadow());
    }

    @Override
    public void renderBindWidget(BindWidget widget, Context context, Rect rect, MouseHelper mouse) {
        context.getRenderer().renderRect(rect, context.getColorScheme().getTertiaryBackgroundColor(), context.getColorScheme().getTertiaryBackgroundColor(), IRenderer.RectMode.Fill, context);
        float centerY = (float)(rect.getHeight() - context.getRenderer().getTextHeight(widget.getTitle())) / 2.0f + 1.0f;
        String keyName = widget.getValue().getKey() != -1 ? Keyboard.getKeyName((int)widget.getValue().getKey()) : "NONE";
        String text = widget.isBinding() ? "Binding..." : "Bind: " + keyName;
        context.getRenderer().renderText(text, rect.getX() + 2, (float)rect.getY() + centerY, context.getColorScheme().getTextColor(), context.getColorScheme().doesTextShadow());
    }

    @Override
    public void renderFeatureButton(FeatureButton widget, Context context, Rect rect, MouseHelper mouse) {
        Color color = widget.getValue() != false ? context.getColorScheme().getMainColor(0) : context.getColorScheme().getSecondaryBackgroundColor();
        this.renderRect(rect, color, color, IRenderer.RectMode.Fill, context);
        if (widget.getValue().booleanValue()) {
            this.renderRect(rect, this.getOutlineColor(), this.getOutlineColor(), IRenderer.RectMode.Outline, context);
        }
        int centerY = (rect.getHeight() - this.getTextHeight(widget.getTitle())) / 2 + 1;
        this.renderText(widget.getTitle(), rect.getX() + 2, rect.getY() + centerY, context.getColorScheme().getTextColor(), context.getColorScheme().doesTextShadow());
    }

    @Override
    public void renderComboBox(ComboBoxWidget widget, Context context, Rect rect, MouseHelper mouseHelper) {
        this.renderRect(rect, context.getColorScheme().getTertiaryBackgroundColor(), context.getColorScheme().getTertiaryBackgroundColor(), IRenderer.RectMode.Fill, context);
        String text = widget.getTitle() + ": " + widget.getValue();
        int centerY = (rect.getHeight() - this.getTextHeight(text)) / 2 + 1;
        this.renderText(text, rect.getX() + 2, rect.getY() + centerY, context.getColorScheme().getTextColor(), context.getColorScheme().doesTextShadow());
    }

    @Override
    public void renderSliderWidget(SliderWidget widget, Context context, Rect rect, Rect sliderRect, MouseHelper mouse) {
        this.renderRect(rect, context.getColorScheme().getTertiaryBackgroundColor(), context.getColorScheme().getTertiaryBackgroundColor(), IRenderer.RectMode.Fill, context);
        this.renderRect(sliderRect, context.getColorScheme().getMainColor(0), context.getColorScheme().getMainColor(0), IRenderer.RectMode.Fill, context);
        this.renderRect(sliderRect, context.getColorScheme().getOutlineColor(), context.getColorScheme().getOutlineColor(), IRenderer.RectMode.Outline, context);
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        String text = widget.getTitle() + ": " + df.format(widget.getValue().doubleValue());
        int centerY = (rect.getHeight() - this.getTextHeight(text)) / 2 + 1;
        this.renderText(text, rect.getX() + 2, rect.getY() + centerY, context.getColorScheme().getTextColor(), context.getColorScheme().doesTextShadow());
    }

    @Override
    public void renderColorWidget(ColorWidget widget, Context context, boolean open, Rect headerRect, Rect dims, Rect container, Rect alphaSlider, Rect hueSlider, Rect colorSquare) {
        int sliderWidth = 2;
        this.renderRect(open ? dims : headerRect, context.getColorScheme().getTertiaryBackgroundColor(), context.getColorScheme().getTertiaryBackgroundColor(), IRenderer.RectMode.Fill, context);
        String text = widget.getTitle();
        int centerY = (headerRect.getHeight() - this.getTextHeight(text)) / 2 + 1;
        String openString = open ? "-" : "+";
        int rightX = headerRect.getWidth() - this.getTextWidth(openString) - 2;
        this.renderText(text, headerRect.getX() + 2, headerRect.getY() + centerY, context.getColorScheme().getTextColor(), context.getColorScheme().doesTextShadow());
        this.renderText(openString, headerRect.getX() + rightX, headerRect.getY() + centerY, widget.getValue(), context.getColorScheme().doesTextShadow());
        if (open) {
            int hueSegments = 30;
            GL11.glShadeModel((int)7425);
            GlStateManager.disableTexture2D();
            GL11.glLineWidth((float)(hueSlider.getHeight() * 2));
            GL11.glBegin((int)3);
            for (int i = 0; i <= hueSegments; ++i) {
                double normal = MathUtil.normalize(i, 0.0, hueSegments);
                Color color = Color.getHSBColor((float)normal, 1.0f, 1.0f);
                ColorUtil.glColor(color);
                GL11.glVertex2f((float)((float)((double)hueSlider.getX() + normal * (double)hueSlider.getWidth())), (float)((float)hueSlider.getY() + (float)hueSlider.getHeight() / 2.0f));
            }
            GL11.glEnd();
            GlStateManager.enableTexture2D();
            GL11.glLineWidth((float)1.0f);
            float[] hsb = Color.RGBtoHSB(widget.getValue().getRed(), widget.getValue().getGreen(), widget.getValue().getBlue(), null);
            int hueOffset = (int)(hsb[0] * (float)hueSlider.getWidth());
            hueOffset = MathHelper.clamp((int)hueOffset, (int)0, (int)hueSlider.getWidth());
            Rect huePicker = new Rect(hueSlider.getX() + hueOffset - sliderWidth / 2, hueSlider.getY(), sliderWidth, hueSlider.getHeight());
            this.renderRect(huePicker, new Color(255, 255, 255), new Color(255, 255, 255), IRenderer.RectMode.Fill, context);
            GlStateManager.enableBlend();
            GL11.glBlendFunc((int)770, (int)771);
            GlStateManager.disableTexture2D();
            GL11.glShadeModel((int)7425);
            GL11.glBegin((int)7);
            ColorUtil.glColor(new Color(255, 255, 255));
            GL11.glVertex2f((float)colorSquare.getX(), (float)colorSquare.getY());
            ColorUtil.glColor(new Color(255, 255, 255));
            GL11.glVertex2f((float)colorSquare.getX(), (float)(colorSquare.getY() + colorSquare.getHeight()));
            ColorUtil.glColor(Color.getHSBColor(hsb[0], 1.0f, 1.0f));
            GL11.glVertex2f((float)(colorSquare.getX() + colorSquare.getWidth()), (float)(colorSquare.getY() + colorSquare.getHeight()));
            ColorUtil.glColor(Color.getHSBColor(hsb[0], 1.0f, 1.0f));
            GL11.glVertex2f((float)(colorSquare.getX() + colorSquare.getWidth()), (float)colorSquare.getY());
            GL11.glEnd();
            GL11.glBegin((int)7);
            ColorUtil.glColor(new Color(0, 0, 0, 24));
            GL11.glVertex2f((float)colorSquare.getX(), (float)colorSquare.getY());
            ColorUtil.glColor(new Color(0, 0, 0, 255));
            GL11.glVertex2f((float)colorSquare.getX(), (float)(colorSquare.getY() + colorSquare.getHeight()));
            ColorUtil.glColor(new Color(0, 0, 0, 255));
            GL11.glVertex2f((float)(colorSquare.getX() + colorSquare.getWidth()), (float)(colorSquare.getY() + colorSquare.getHeight()));
            ColorUtil.glColor(new Color(0, 0, 0, 24));
            GL11.glVertex2f((float)(colorSquare.getX() + colorSquare.getWidth()), (float)colorSquare.getY());
            GL11.glEnd();
            GlStateManager.enableTexture2D();
            int pickerSize = 2;
            int pickerOffsetX = MathHelper.clamp((int)((int)(hsb[1] * (float)colorSquare.getWidth())), (int)0, (int)colorSquare.getWidth());
            int pickerOffsetY = MathHelper.clamp((int)((int)(hsb[2] * (float)colorSquare.getHeight())), (int)0, (int)colorSquare.getHeight());
            Rect pickerRect = new Rect(colorSquare.getX() + pickerOffsetX - pickerSize / 2, colorSquare.getY() + colorSquare.getHeight() - pickerOffsetY - pickerSize / 2, pickerSize, pickerSize);
            this.renderRect(pickerRect, new Color(255, 255, 255), new Color(255, 255, 255), IRenderer.RectMode.Fill, context);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glBegin((int)7);
            ColorUtil.glColor(ColorUtil.newAlpha(widget.getValue(), 24));
            GL11.glVertex2f((float)alphaSlider.getX(), (float)alphaSlider.getY());
            ColorUtil.glColor(ColorUtil.newAlpha(widget.getValue(), 255));
            GL11.glVertex2f((float)alphaSlider.getX(), (float)(alphaSlider.getY() + alphaSlider.getHeight()));
            ColorUtil.glColor(ColorUtil.newAlpha(widget.getValue(), 255));
            GL11.glVertex2f((float)(alphaSlider.getX() + alphaSlider.getWidth()), (float)(alphaSlider.getY() + alphaSlider.getHeight()));
            ColorUtil.glColor(ColorUtil.newAlpha(widget.getValue(), 24));
            GL11.glVertex2f((float)(alphaSlider.getX() + alphaSlider.getWidth()), (float)alphaSlider.getY());
            GL11.glEnd();
            GlStateManager.enableTexture2D();
            double alphaNormal = MathHelper.clamp((double)MathUtil.normalize(widget.getValue().getAlpha(), 0.0, 255.0), (double)0.0, (double)1.0);
            int alphaOffset = (int)(alphaNormal * (double)alphaSlider.getHeight());
            Rect alphaPicker = new Rect(alphaSlider.getX(), alphaSlider.getY() + alphaOffset - sliderWidth / 2, alphaSlider.getWidth(), sliderWidth);
            this.renderRect(alphaPicker, new Color(255, 255, 255), new Color(255, 255, 255), IRenderer.RectMode.Fill, context);
        }
    }

    @Override
    public void renderStringWidget(TextEntryWidget widget, Context context, Rect rect, MouseHelper mouse) {
        this.renderRect(rect, context.getColorScheme().getTertiaryBackgroundColor(), context.getColorScheme().getTertiaryBackgroundColor(), IRenderer.RectMode.Fill, context);
        String renderText = widget.typing ? widget.getValue() + "|" : widget.getValue();
        int centerY = (rect.getHeight() - this.getTextHeight(renderText)) / 2;
        this.renderText(renderText, rect.getX() + 2, rect.getY() + centerY + 1, context.getColorScheme().getTextColor(), context.getColorScheme().doesTextShadow());
    }

    @Override
    public int getTextWidth(String text) {
        return FontModule.INSTANCE.isEnabled() ? fontRenderer.getStringWidth(text) : SecondGuiModule.mc.fontRenderer.getStringWidth(text);
    }

    @Override
    public int getTextHeight(String text) {
        return FontModule.INSTANCE.isEnabled() ? fontRenderer.getStringHeight(text) + 1 : SecondGuiModule.mc.fontRenderer.FONT_HEIGHT;
    }

    @Override
    public void renderText(String text, float x, float y, Color color, boolean shadow) {
        if (FontModule.INSTANCE.isEnabled()) {
            if (fontRenderer.getFont().getSize() != FontModule.INSTANCE.fontSize.getValue().intValue()) {
                FontModule.INSTANCE.font = FontModule.INSTANCE.font.deriveFont((float)FontModule.INSTANCE.fontSize.getValue().intValue());
                fontRenderer.setFont(FontModule.INSTANCE.font);
            }
            if (shadow) {
                fontRenderer.drawStringWithShadow(text, x, y, color.getRGB());
            } else {
                fontRenderer.drawString(text, x, y, color.getRGB());
            }
        } else if (shadow) {
            SecondGuiModule.mc.fontRenderer.drawStringWithShadow(text, x, y, color.getRGB());
        } else {
            SecondGuiModule.mc.fontRenderer.drawString(text, (int)x, (int)y, color.getRGB());
        }
    }

    @Override
    public void renderRect(Rect rect, Color color, Color bottom, IRenderer.RectMode mode, Context context) {
        if (mode == IRenderer.RectMode.Fill) {
            GlStateManager.enableBlend();
            GL11.glShadeModel((int)7425);
            GlStateManager.disableTexture2D();
            GL11.glBegin((int)7);
            ColorUtil.glColor(bottom);
            GL11.glVertex2f((float)rect.getX(), (float)(rect.getY() + rect.getHeight()));
            GL11.glVertex2f((float)(rect.getX() + rect.getWidth()), (float)(rect.getY() + rect.getHeight()));
            ColorUtil.glColor(color);
            GL11.glVertex2f((float)(rect.getX() + rect.getWidth()), (float)rect.getY());
            GL11.glVertex2f((float)rect.getX(), (float)rect.getY());
            GL11.glEnd();
            GlStateManager.enableTexture2D();
        }
        if (mode == IRenderer.RectMode.Outline) {
            GlStateManager.disableTexture2D();
            ColorUtil.glColor(color);
            GL11.glBegin((int)2);
            GL11.glVertex2f((float)rect.getX(), (float)rect.getY());
            GL11.glVertex2f((float)((float)(rect.getX() + rect.getWidth()) + context.getMetrics().getOutlineWidth() / 2.0f), (float)rect.getY());
            GL11.glVertex2f((float)((float)(rect.getX() + rect.getWidth()) + context.getMetrics().getOutlineWidth() / 2.0f), (float)(rect.getY() + rect.getHeight()));
            GL11.glVertex2f((float)rect.getX(), (float)(rect.getY() + rect.getHeight()));
            GL11.glEnd();
            GlStateManager.enableTexture2D();
        }
    }

    @Override
    public void scissorRect(Rect dims) {
        ScaledResolution sr = new ScaledResolution(mc);
        double factor = sr.getScaleFactor();
        GL11.glScissor((int)((int)((double)dims.getX() * factor)), (int)(sr.getScaledHeight() - (dims.getY() + dims.getHeight())), (int)((int)((double)dims.getWidth() * factor)), (int)((int)((double)dims.getHeight() * factor)));
    }

    @Override
    public Color getMainColor(int pos) {
        return this.color.getValue();
    }

    @Override
    public Color getOutlineColor() {
        return this.outlineColor.getValue();
    }

    @Override
    public Color getButtonColor() {
        return this.color.getValue();
    }

    @Override
    public Color getBackgroundColor() {
        return this.backgroundColor.getValue();
    }

    @Override
    public Color getSecondaryBackgroundColor() {
        return this.backgroundSecondary.getValue();
    }

    @Override
    public Color getTertiaryBackgroundColor() {
        return this.backgroundTertiary.getValue();
    }

    @Override
    public Color getTextColor() {
        return this.textColor.getValue();
    }

    @Override
    public Color getTextColorHighlight() {
        return this.textColor.getValue();
    }

    @Override
    public Color getTextColorActive() {
        return this.textColor.getValue();
    }

    @Override
    public boolean doesTextShadow() {
        return this.textShadow.getValue();
    }

    @Override
    public int getSpacing() {
        return 1;
    }

    @Override
    public int getBetweenSpacing() {
        return 1;
    }

    @Override
    public int getSettingSpacing() {
        return 1;
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
        return 14;
    }

    @Override
    public int getFrameHeight() {
        return 15;
    }

    @Override
    public float getOutlineWidth() {
        return 0.0f;
    }
}

