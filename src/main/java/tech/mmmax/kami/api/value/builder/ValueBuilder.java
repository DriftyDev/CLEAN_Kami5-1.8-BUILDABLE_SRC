/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.api.value.builder;

import java.util.function.Consumer;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.gui.component.IComponent;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.impl.gui.components.value.ICustomComponent;

public class ValueBuilder<Type>
extends Value<Type> {
    Value<String> parent;
    String page;

    public ValueBuilder<Type> withDescriptor(String name, String tag) {
        this.setName(name);
        this.setTag(tag);
        return this;
    }

    public ValueBuilder<Type> withDescriptor(String name) {
        this.setName(name);
        String camelCase = name.replace(" ", "");
        char[] chars = camelCase.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        camelCase = new String(chars);
        this.setTag(camelCase);
        return this;
    }

    public ValueBuilder<Type> withValue(Type value) {
        this.setValue(value);
        return this;
    }

    public ValueBuilder<Type> withAction(Consumer<Value<Type>> action) {
        this.setAction(action);
        return this;
    }

    public ValueBuilder<Type> withRange(Type min, Type max) {
        this.setMin(min);
        this.setMax(max);
        return this;
    }

    public ValueBuilder<Type> withPageParent(Value<String> parent) {
        this.parent = parent;
        return this;
    }

    public ValueBuilder<Type> withPage(String page) {
        this.page = page;
        return this;
    }

    @Override
    public boolean isActive() {
        if (this.parent != null) {
            return this.parent.getValue().equals(this.page) && super.isActive();
        }
        return super.isActive();
    }

    public ValueBuilder<Type> withModes(String ... modes) {
        this.setModes(modes);
        return this;
    }

    public ValueBuilder<Type> withComponent(IComponent widget) {
        if (!(widget instanceof ICustomComponent)) {
            throw new IllegalArgumentException();
        }
        ICustomComponent comp = (ICustomComponent)((Object)widget);
        comp.setValue(this);
        this.setComponent(widget);
        return this;
    }

    public Value<Type> register(Feature feature) {
        feature.getValues().add(this);
        return this;
    }
}

