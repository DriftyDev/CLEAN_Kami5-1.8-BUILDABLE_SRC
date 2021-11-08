/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.api.gui.widget.impl;

import tech.mmmax.kami.api.gui.component.IComponent;
import tech.mmmax.kami.api.gui.context.Context;
import tech.mmmax.kami.api.gui.helpers.MouseHelper;
import tech.mmmax.kami.api.gui.helpers.Rect;
import tech.mmmax.kami.api.gui.widget.IWidget;

public class ComboBoxWidget
implements IComponent,
IWidget<String> {
    String title;
    String[] modes;
    String theValue;
    Rect dims;

    public ComboBoxWidget(String title, String theValue, String[] modes, Rect dims) {
        this.title = title;
        this.modes = modes;
        this.theValue = theValue;
        this.dims = dims;
    }

    @Override
    public void draw(Context context, MouseHelper mouse) {
        this.getDims().setHeight(context.getMetrics().getButtonHeight());
        context.getRenderer().renderComboBox(this, context, this.getDisplayDims(), mouse);
    }

    @Override
    public void click(Context context, MouseHelper mouse, int button) {
        if (this.getDisplayDims().collideWithMouse(mouse)) {
            int current = 0;
            int index = 0;
            System.out.println("balls");
            for (String s : this.modes) {
                if (s.equals(this.getValue())) {
                    current = index;
                }
                ++index;
            }
            int amount = 1;
            if (current + amount > this.modes.length - 1) {
                this.setValue(this.modes[0]);
            } else {
                this.setValue(this.modes[current + amount]);
            }
        }
    }

    @Override
    public void release(Context context, MouseHelper mouse, int state) {
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

    @Override
    public String getValue() {
        return this.theValue;
    }

    @Override
    public void setValue(String value) {
        this.theValue = value;
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
    public int getLevel() {
        return 3;
    }
}

