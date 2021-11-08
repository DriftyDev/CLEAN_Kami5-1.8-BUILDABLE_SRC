/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 */
package tech.mmmax.kami.api.gui.widget.impl;

import java.awt.Color;
import net.minecraft.util.math.MathHelper;
import tech.mmmax.kami.api.gui.component.IComponent;
import tech.mmmax.kami.api.gui.context.Context;
import tech.mmmax.kami.api.gui.helpers.MouseHelper;
import tech.mmmax.kami.api.gui.helpers.Rect;
import tech.mmmax.kami.api.gui.widget.IWidget;
import tech.mmmax.kami.api.gui.widget.impl.BooleanWidget;
import tech.mmmax.kami.api.utils.MathUtil;
import tech.mmmax.kami.api.utils.color.ColorUtil;

public class ColorWidget
implements IWidget<Color>,
IComponent {
    Color value;
    String title;
    boolean open = false;
    Rect dims;
    Rect displayDims;
    Rect insideDims;
    Rect pickerRect;
    Rect alphaRect;
    Rect hueRect;
    Rect colorSquare;
    BooleanWidget rainbow;
    BooleanWidget sync;
    boolean draggingHue;
    boolean draggingColor;
    boolean draggingAlpha;

    public ColorWidget(String title, Color value, Rect dims) {
        this.title = title;
        this.value = value;
        this.dims = dims;
        this.displayDims = new Rect(0, 0, 0, 0);
        this.pickerRect = new Rect(0, 0, 0, 0);
        this.alphaRect = new Rect(0, 0, 0, 0);
        this.insideDims = new Rect(0, 0, 0, 0);
        this.hueRect = new Rect(0, 0, 0, 0);
        this.colorSquare = new Rect(0, 0, 0, 0);
    }

    @Override
    public Color getValue() {
        return this.value;
    }

    @Override
    public void setValue(Color value) {
        this.value = value;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Rect getDisplayDims() {
        return this.open ? this.dims : this.displayDims;
    }

    @Override
    public void draw(Context context, MouseHelper mouse) {
        float normal;
        int clampMouseX;
        this.displayDims.setX(this.getDims().getX());
        this.displayDims.setY(this.getDims().getY());
        this.displayDims.setWidth(this.getDims().getWidth());
        this.displayDims.setHeight(context.getMetrics().getButtonHeight());
        float[] hsb = Color.RGBtoHSB(this.getValue().getRed(), this.getValue().getGreen(), this.getValue().getBlue(), null);
        int spacing = 1;
        int alphaSliderWidth = context.getMetrics().getButtonHeight();
        int squareSize = this.insideDims.getWidth() - (alphaSliderWidth + spacing * 3);
        if (this.open) {
            this.insideDims.setX(this.getDims().getX() + context.getMetrics().getSpacing());
            this.insideDims.setY(this.getDims().getY() + this.displayDims.getHeight() + context.getMetrics().getSpacing());
            this.insideDims.setWidth(this.getDims().getWidth() - context.getMetrics().getSpacing() * 2);
            this.insideDims.setHeight(this.getDims().getHeight() - this.displayDims.getHeight() - context.getMetrics().getSpacing() * 2);
            this.hueRect.setX(this.insideDims.getX() + spacing);
            this.hueRect.setWidth(this.insideDims.getWidth() - spacing * 2);
            this.hueRect.setY(this.insideDims.getY() + spacing + squareSize + spacing);
            this.hueRect.setHeight(context.getMetrics().getButtonHeight());
            this.alphaRect.setX(this.colorSquare.getX() + this.colorSquare.getWidth() + spacing);
            this.alphaRect.setY(this.colorSquare.getY());
            this.alphaRect.setWidth(alphaSliderWidth);
            this.alphaRect.setHeight(this.colorSquare.getHeight());
            this.colorSquare.setX(this.insideDims.getX() + spacing);
            this.colorSquare.setY(this.insideDims.getY() + spacing);
            this.colorSquare.setWidth(squareSize);
            this.colorSquare.setHeight(squareSize);
        }
        this.dims.setHeight(this.displayDims.getHeight() + squareSize + this.hueRect.getHeight() + context.getMetrics().getSpacing() * 4);
        if (this.draggingHue) {
            clampMouseX = MathHelper.clamp((int)mouse.getX(), (int)this.hueRect.getX(), (int)(this.hueRect.getX() + this.hueRect.getWidth()));
            normal = (float)MathUtil.normalize(clampMouseX, this.hueRect.getX(), this.hueRect.getX() + this.hueRect.getWidth());
            this.setValue(ColorUtil.newAlpha(Color.getHSBColor(MathHelper.clamp((float)normal, (float)0.0f, (float)1.0f), hsb[1], hsb[2]), this.getValue().getAlpha()));
        }
        if (this.draggingColor) {
            clampMouseX = MathHelper.clamp((int)mouse.getX(), (int)this.colorSquare.getX(), (int)(this.colorSquare.getX() + this.colorSquare.getWidth()));
            float normalX = (float)MathUtil.normalize(clampMouseX, this.colorSquare.getX(), this.colorSquare.getX() + this.colorSquare.getWidth());
            int clampMouseY = MathHelper.clamp((int)mouse.getY(), (int)this.colorSquare.getY(), (int)(this.colorSquare.getY() + this.colorSquare.getHeight()));
            float normalY = (float)MathUtil.normalize(clampMouseY, this.colorSquare.getY(), this.colorSquare.getY() + this.colorSquare.getHeight());
            normalY = -normalY + 1.0f;
            normalY = MathHelper.clamp((float)normalY, (float)0.0f, (float)1.0f);
            this.setValue(ColorUtil.newAlpha(Color.getHSBColor(hsb[0], normalX, normalY), this.getValue().getAlpha()));
        }
        if (this.draggingAlpha) {
            int clampMouseY = MathHelper.clamp((int)mouse.getY(), (int)this.alphaRect.getY(), (int)(this.alphaRect.getY() + this.alphaRect.getHeight()));
            normal = (float)MathUtil.normalize(clampMouseY, this.alphaRect.getY(), this.alphaRect.getY() + this.alphaRect.getHeight());
            this.setValue(ColorUtil.newAlpha(this.getValue(), (int)MathHelper.clamp((float)(normal * 255.0f), (float)0.0f, (float)255.0f)));
        }
        context.getRenderer().renderColorWidget(this, context, this.open, this.displayDims, this.dims, this.insideDims, this.alphaRect, this.hueRect, this.colorSquare);
    }

    @Override
    public void click(Context context, MouseHelper mouse, int button) {
        if (this.displayDims.collideWithMouse(mouse) && button == 1) {
            boolean bl = this.open = !this.open;
        }
        if (this.open) {
            if (this.hueRect.collideWithMouse(mouse)) {
                this.draggingHue = true;
            }
            if (this.colorSquare.collideWithMouse(mouse)) {
                this.draggingColor = true;
            }
            if (this.alphaRect.collideWithMouse(mouse)) {
                this.draggingAlpha = true;
            }
        }
    }

    @Override
    public void release(Context context, MouseHelper mouse, int state) {
        this.draggingHue = false;
        this.draggingColor = false;
        this.draggingAlpha = false;
    }

    @Override
    public void key(Context context, int key, char character) {
    }

    @Override
    public int getLevel() {
        return 3;
    }

    @Override
    public Rect getDims() {
        return this.open ? this.dims : this.displayDims;
    }

    @Override
    public boolean isDraggable() {
        return false;
    }

    @Override
    public boolean isActive() {
        return true;
    }
}

