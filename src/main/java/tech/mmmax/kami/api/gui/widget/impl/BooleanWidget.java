/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.api.gui.widget.impl;

import tech.mmmax.kami.api.gui.component.IComponent;
import tech.mmmax.kami.api.gui.context.Context;
import tech.mmmax.kami.api.gui.helpers.MouseHelper;
import tech.mmmax.kami.api.gui.helpers.Rect;
import tech.mmmax.kami.api.gui.widget.IWidget;

public class BooleanWidget
implements IWidget<Boolean>,
IComponent {
    String title;
    Rect dims;
    boolean value;

    public BooleanWidget(String title, Rect dims) {
        this.title = title;
        this.dims = dims;
        this.value = true;
    }

    @Override
    public void draw(Context context, MouseHelper mouse) {
        this.getDims().setHeight(context.getMetrics().getButtonHeight());
        context.getRenderer().renderBooleanWidget(this, context, this.getDisplayDims(), mouse);
    }

    @Override
    public void click(Context context, MouseHelper mouse, int button) {
        if (this.getDisplayDims().collideWithMouse(mouse) && button == 0) {
            this.setValue(this.getValue() == false);
        }
    }

    @Override
    public void release(Context context, MouseHelper mouse, int state) {
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

    @Override
    public Boolean getValue() {
        return this.value;
    }

    @Override
    public void setValue(Boolean value) {
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
}

