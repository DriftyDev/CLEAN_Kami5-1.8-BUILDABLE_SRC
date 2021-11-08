/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.api.value;

import java.util.function.Consumer;
import tech.mmmax.kami.api.gui.component.IComponent;

public class Value<Type> {
    String name;
    String tag;
    Type min;
    Type max;
    String[] modes;
    boolean active = true;
    IComponent component;
    Consumer<Value<Type>> action;
    boolean enabled;
    Type value;

    public Type getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public String getTag() {
        return this.tag;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public IComponent getComponent() {
        return this.component;
    }

    public void setComponent(IComponent component) {
        this.component = component;
    }

    public void setValue(Type value) {
        this.value = value;
        if (this.action != null) {
            this.action.accept(this);
        }
    }

    public Type getMin() {
        return this.min;
    }

    public Type getMax() {
        return this.max;
    }

    public void setMin(Type min) {
        this.min = min;
    }

    public void setMax(Type max) {
        this.max = max;
    }

    public String[] getModes() {
        return this.modes;
    }

    public void setModes(String[] modes) {
        this.modes = modes;
    }

    public Consumer<Value<Type>> getAction() {
        return this.action;
    }

    public void setAction(Consumer<Value<Type>> action) {
        this.action = action;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

