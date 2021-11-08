/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.impl.gui.components.value;

import tech.mmmax.kami.api.gui.context.Context;
import tech.mmmax.kami.api.gui.helpers.MouseHelper;
import tech.mmmax.kami.api.gui.widget.impl.SliderWidget;
import tech.mmmax.kami.api.value.Value;

public class SliderComponent
extends SliderWidget {
    Value<Number> val;

    public SliderComponent(Value<Number> value) {
        super(value.getName(), value.getValue(), value.getMin(), value.getMax());
        this.val = value;
    }

    @Override
    public void draw(Context context, MouseHelper mouse) {
        super.draw(context, mouse);
        this.setValue(this.val.getValue());
        this.min = this.val.getMin();
        this.max = this.val.getMax();
    }

    @Override
    public void setValue(Number value) {
        super.setValue(value);
        this.val.setValue(value);
    }

    @Override
    public boolean isActive() {
        return this.val.isActive();
    }
}

