/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.math.MathHelper
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
package tech.mmmax.kami.impl.gui.renderer;

import java.awt.Color;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import tech.mmmax.kami.api.gui.context.Context;
import tech.mmmax.kami.api.gui.helpers.MouseHelper;
import tech.mmmax.kami.api.gui.helpers.Rect;
import tech.mmmax.kami.api.gui.render.IRenderer;
import tech.mmmax.kami.api.gui.widget.impl.BindWidget;
import tech.mmmax.kami.api.gui.widget.impl.BooleanWidget;
import tech.mmmax.kami.api.gui.widget.impl.ColorWidget;
import tech.mmmax.kami.api.gui.widget.impl.ComboBoxWidget;
import tech.mmmax.kami.api.gui.widget.impl.SliderWidget;
import tech.mmmax.kami.api.gui.widget.impl.TextEntryWidget;
import tech.mmmax.kami.api.utils.MathUtil;
import tech.mmmax.kami.api.utils.color.ColorUtil;
import tech.mmmax.kami.api.utils.render.font.CFontRenderer;
import tech.mmmax.kami.api.wrapper.IMinecraft;
import tech.mmmax.kami.impl.features.modules.client.FontModule;
import tech.mmmax.kami.impl.features.modules.client.gui.ClickGuiModule;
import tech.mmmax.kami.impl.gui.components.module.FeatureButton;

public class Renderer
implements IRenderer,
IMinecraft {
    static CFontRenderer fontRenderer = new CFontRenderer(FontModule.INSTANCE.font);

    @Override
    public void preRender(Context context) {
    }

    @Override
    public void renderFrameTitle(Context context, Rect rect, MouseHelper mouse, String title, boolean open) {
        this.renderRect(rect, context.getColorScheme().getMainColor(rect.getX() + rect.getY()), context.getColorScheme().getMainColor(rect.getX() + rect.getWidth() + rect.getY() + rect.getHeight()), IRenderer.RectMode.Fill, context);
        int centerX = (rect.getWidth() - this.getTextWidth(title)) / 2;
        int centerY = (rect.getHeight() - this.getTextHeight(title)) / 2 + 1;
        String openString = open ? "-" : "+";
        int rightX = rect.getWidth() - this.getTextWidth(openString) - 2;
        this.renderText(title, rect.getX() + centerX, rect.getY() + centerY, context.getColorScheme().getTextColorHighlight(), context.getColorScheme().doesTextShadow());
        this.renderText(openString, rect.getX() + rightX, rect.getY() + centerY, context.getColorScheme().getTextColorHighlight(), context.getColorScheme().doesTextShadow());
    }

    @Override
    public void renderFrameOutline(Context context, Rect rect, MouseHelper mouse) {
        this.renderRect(rect, ClickGuiModule.INSTANCE.getOutlineColor(), ClickGuiModule.INSTANCE.getOutlineColor(), IRenderer.RectMode.Outline, context);
    }

    @Override
    public void renderFrame(Context context, Rect rect, MouseHelper mouse) {
        this.renderRect(rect, context.getColorScheme().getBackgroundColor(), context.getColorScheme().getBackgroundColor(), IRenderer.RectMode.Fill, context);
    }

    @Override
    public void renderBooleanWidget(BooleanWidget widget, Context context, Rect rect, MouseHelper mouse) {
        Color color = widget.getValue() != false ? context.getColorScheme().getMainColor(rect.getX() + rect.getY()) : context.getColorScheme().getTertiaryBackgroundColor();
        Color color2 = widget.getValue() != false ? context.getColorScheme().getMainColor(rect.getX() + rect.getWidth() + rect.getY() + rect.getHeight()) : context.getColorScheme().getTertiaryBackgroundColor();
        this.renderRect(rect, color, color2, IRenderer.RectMode.Fill, context);
        int centerX = (rect.getWidth() - this.getTextWidth(widget.getTitle())) / 2;
        int centerY = (rect.getHeight() - this.getTextHeight(widget.getTitle())) / 2 + 1;
        this.renderText(widget.getTitle(), rect.getX() + centerX, rect.getY() + centerY, context.getColorScheme().getTextColor(), context.getColorScheme().doesTextShadow());
    }

    @Override
    public void renderBindWidget(BindWidget widget, Context context, Rect rect, MouseHelper mouse) {
        this.renderRect(rect, context.getColorScheme().getTertiaryBackgroundColor(), context.getColorScheme().getTertiaryBackgroundColor(), IRenderer.RectMode.Fill, context);
        float centerY = (float)(rect.getHeight() - this.getTextHeight(widget.getTitle())) / 2.0f + 1.0f;
        String keyName = widget.getValue().getKey() != -1 ? Keyboard.getKeyName((int)widget.getValue().getKey()) : "NONE";
        String text = widget.isBinding() ? "Binding..." : "Bind: " + keyName;
        this.renderText(text, rect.getX() + 2, (float)rect.getY() + centerY, context.getColorScheme().getTextColor(), context.getColorScheme().doesTextShadow());
    }

    @Override
    public void renderFeatureButton(FeatureButton widget, Context context, Rect rect, MouseHelper mouse) {
        Color color = widget.getValue() != false ? context.getColorScheme().getMainColor(0) : context.getColorScheme().getSecondaryBackgroundColor();
        this.renderRect(rect, color, color, IRenderer.RectMode.Fill, context);
        int centerX = (rect.getWidth() - this.getTextWidth(widget.getTitle())) / 2;
        int centerY = (rect.getHeight() - this.getTextHeight(widget.getTitle())) / 2 + 1;
        this.renderText(widget.getTitle(), rect.getX() + centerX, rect.getY() + centerY, context.getColorScheme().getTextColor(), context.getColorScheme().doesTextShadow());
    }

    @Override
    public void renderComboBox(ComboBoxWidget widget, Context context, Rect rect, MouseHelper mouseHelper) {
        this.renderRect(rect, context.getColorScheme().getTertiaryBackgroundColor(), context.getColorScheme().getTertiaryBackgroundColor(), IRenderer.RectMode.Fill, context);
        String text = widget.getTitle() + ": " + widget.getValue();
        int centerX = (rect.getWidth() - this.getTextWidth(text)) / 2;
        int centerY = (rect.getHeight() - this.getTextHeight(text)) / 2 + 1;
        this.renderText(text, rect.getX() + centerX, rect.getY() + centerY, context.getColorScheme().getTextColor(), context.getColorScheme().doesTextShadow());
    }

    @Override
    public void renderSliderWidget(SliderWidget widget, Context context, Rect rect, Rect sliderRect, MouseHelper mouse) {
        this.renderRect(rect, context.getColorScheme().getTertiaryBackgroundColor(), context.getColorScheme().getTertiaryBackgroundColor(), IRenderer.RectMode.Fill, context);
        this.renderRect(sliderRect, context.getColorScheme().getMainColor(0), context.getColorScheme().getMainColor(0), IRenderer.RectMode.Fill, context);
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        String text = widget.getTitle() + ": " + df.format(widget.getValue().doubleValue());
        int centerX = (rect.getWidth() - this.getTextWidth(text)) / 2;
        int centerY = (rect.getHeight() - this.getTextHeight(text)) / 2 + 1;
        this.renderText(text, rect.getX() + centerX, rect.getY() + centerY, context.getColorScheme().getTextColor(), context.getColorScheme().doesTextShadow());
    }

    @Override
    public void renderColorWidget(ColorWidget widget, Context context, boolean open, Rect headerRect, Rect dims, Rect container, Rect alphaSlider, Rect hueSlider, Rect colorSquare) {
        int sliderWidth = 2;
        this.renderRect(open ? dims : headerRect, context.getColorScheme().getTertiaryBackgroundColor(), context.getColorScheme().getTertiaryBackgroundColor(), IRenderer.RectMode.Fill, context);
        String text = widget.getTitle();
        int centerX = (headerRect.getWidth() - this.getTextWidth(text)) / 2;
        int centerY = (headerRect.getHeight() - this.getTextHeight(text)) / 2 + 1;
        String openString = open ? "-" : "+";
        int rightX = headerRect.getWidth() - this.getTextWidth(openString) - 2;
        this.renderText(text, headerRect.getX() + centerX, headerRect.getY() + centerY, context.getColorScheme().getTextColor(), context.getColorScheme().doesTextShadow());
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
        return FontModule.INSTANCE.isEnabled() ? fontRenderer.getStringWidth(text) : Renderer.mc.fontRenderer.getStringWidth(text);
    }

    @Override
    public int getTextHeight(String text) {
        return FontModule.INSTANCE.isEnabled() ? fontRenderer.getStringHeight(text) + 1 : Renderer.mc.fontRenderer.FONT_HEIGHT;
    }

    @Override
    public void renderText(String text, float x, float y, Color color, boolean shadow) {
        if (FontModule.INSTANCE.isEnabled()) {
            if (fontRenderer.getFont().getSize() != FontModule.INSTANCE.fontSize.getValue().intValue()) {
                fontRenderer.setFont(fontRenderer.getFont().deriveFont((float)FontModule.INSTANCE.fontSize.getValue().intValue()));
            }
            if (shadow) {
                fontRenderer.drawStringWithShadow(text, x, y, color.getRGB());
            } else {
                fontRenderer.drawString(text, x, y, color.getRGB());
            }
        } else if (shadow) {
            Renderer.mc.fontRenderer.drawStringWithShadow(text, x, y, color.getRGB());
        } else {
            Renderer.mc.fontRenderer.drawString(text, (int)x, (int)y, color.getRGB());
        }
    }

    @Override
    public void renderRect(Rect rect, Color color, Color bottom, IRenderer.RectMode mode, Context context) {
        if (mode == IRenderer.RectMode.Fill) {
            ColorUtil.glColor(color);
            Gui.drawRect((int)rect.getX(), (int)rect.getY(), (int)(rect.getX() + rect.getWidth()), (int)(rect.getY() + rect.getHeight()), (int)color.getRGB());
        }
        if (mode == IRenderer.RectMode.Outline) {
            GlStateManager.disableTexture2D();
            float red = (float)color.getRed() / 255.0f;
            float green = (float)color.getGreen() / 255.0f;
            float blue = (float)color.getBlue() / 255.0f;
            float alpha = (float)color.getAlpha() / 255.0f;
            GL11.glLineWidth((float)context.getMetrics().getOutlineWidth());
            GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
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
}

