/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.impl.gui.components.value;

import tech.mmmax.kami.api.gui.context.Context;
import tech.mmmax.kami.api.gui.helpers.MouseHelper;
import tech.mmmax.kami.api.gui.helpers.Rect;
import tech.mmmax.kami.api.gui.widget.impl.ComboBoxWidget;
import tech.mmmax.kami.api.value.Value;

public class ComboBoxComponent
extends ComboBoxWidget {
    Value<String> value;

    public ComboBoxComponent(Value<String> value) {
        super(value.getName(), value.getValue(), value.getModes(), new Rect(0, 0, 0, 0));
        this.value = value;
    }

    @Override
    public void draw(Context context, MouseHelper mouse) {
        this.setValue(this.value.getValue());
        super.draw(context, mouse);
    }

    @Override
    public void click(Context context, MouseHelper mouse, int button) {
        super.click(context, mouse, button);
    }

    @Override
    public boolean isActive() {
        return this.value.isActive();
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
        this.value.setValue(value);
    }
}

