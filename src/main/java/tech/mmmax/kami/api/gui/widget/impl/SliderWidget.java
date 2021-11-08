/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 */
package tech.mmmax.kami.api.gui.widget.impl;

import net.minecraft.util.math.MathHelper;
import tech.mmmax.kami.api.gui.component.IComponent;
import tech.mmmax.kami.api.gui.context.Context;
import tech.mmmax.kami.api.gui.helpers.MouseHelper;
import tech.mmmax.kami.api.gui.helpers.Rect;
import tech.mmmax.kami.api.gui.widget.IWidget;

public class SliderWidget
implements IWidget<Number>,
IComponent {
    String title;
    Number value;
    public Number min;
    public Number max;
    boolean dragging;
    Rect dims;
    Rect slider;

    public SliderWidget(String title, Number number, Number min, Number max) {
        this.title = title;
        this.value = number;
        this.min = min;
        this.max = max;
        this.dims = new Rect(0, 0, 0, 0);
        this.slider = new Rect(0, 0, 0, 0);
    }

    @Override
    public Number getValue() {
        return this.value;
    }

    @Override
    public void setValue(Number value) {
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
        return this.getDims();
    }

    @Override
    public void draw(Context context, MouseHelper mouse) {
        this.getDims().setHeight(context.getMetrics().getButtonHeight());
        this.slider.setHeight(this.getDims().getHeight());
        this.slider.setX(this.getDims().getX());
        this.slider.setY(this.getDims().getY());
        double sliderWidth = this.normalize(this.getValue().doubleValue(), this.min.doubleValue(), this.max.doubleValue()) * (double)this.getDims().getWidth();
        this.slider.setWidth((int)sliderWidth);
        if (this.dragging) {
            Number newVal = 0;
            Double difference = this.max.doubleValue() - this.min.doubleValue();
            newVal = this.min.doubleValue() + MathHelper.clamp((double)this.normalize(mouse.getX() - this.getDims().getX(), 0.0, this.getDims().getWidth()), (double)0.0, (double)1.0) * difference;
            this.setValue(newVal);
        }
        context.getRenderer().renderSliderWidget(this, context, this.getDisplayDims(), this.slider, mouse);
    }

    @Override
    public void click(Context context, MouseHelper mouse, int button) {
        if (this.getDims().collideWithMouse(mouse) && button == 0) {
            this.dragging = true;
        }
    }

    @Override
    public void release(Context context, MouseHelper mouse, int state) {
        this.dragging = false;
    }

    @Override
    public void key(Context context, int key, char character) {
    }

    @Override
    public Rect getDims() {
        return this.dims;
    }

    @Override
    public boolean isDraggable() {
        return false;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    double normalize(double value, double min, double max) {
        return (value - min) / (max - min);
    }

    @Override
    public int getLevel() {
        return 3;
    }
}

