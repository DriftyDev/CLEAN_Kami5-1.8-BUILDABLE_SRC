/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.impl.gui.components.value;

import java.awt.Color;
import tech.mmmax.kami.api.gui.helpers.Rect;
import tech.mmmax.kami.api.gui.widget.impl.ColorWidget;
import tech.mmmax.kami.api.value.Value;

public class ColorComponent
extends ColorWidget {
    Value<Color> colorValue;

    public ColorComponent(Value<Color> colorValue) {
        super(colorValue.getName(), colorValue.getValue(), new Rect(0, 0, 0, 0));
        this.colorValue = colorValue;
    }

    @Override
    public Color getValue() {
        return this.colorValue.getValue();
    }

    @Override
    public void setValue(Color value) {
        this.colorValue.setValue(value);
    }

    @Override
    public boolean isActive() {
        return this.colorValue.isActive();
    }
}

