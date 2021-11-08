/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.impl.gui.components.value;

import tech.mmmax.kami.api.gui.context.Context;
import tech.mmmax.kami.api.gui.helpers.MouseHelper;
import tech.mmmax.kami.api.gui.helpers.Rect;
import tech.mmmax.kami.api.gui.widget.impl.BooleanWidget;
import tech.mmmax.kami.api.value.Value;

public class BooleanComponent
extends BooleanWidget {
    Value<Boolean> value;

    public BooleanComponent(Value<Boolean> booleanValue) {
        super(booleanValue.getName(), new Rect(0, 0, 0, 0));
        this.value = booleanValue;
    }

    @Override
    public void draw(Context context, MouseHelper mouse) {
        this.setValue(this.value.getValue());
        super.draw(context, mouse);
    }

    @Override
    public void setValue(Boolean value) {
        super.setValue(value);
        this.value.setValue(value);
    }

    @Override
    public boolean isActive() {
        return this.value.isActive();
    }
}

