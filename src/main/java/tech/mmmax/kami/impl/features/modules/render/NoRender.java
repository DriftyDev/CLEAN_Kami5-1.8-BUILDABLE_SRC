/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.impl.features.modules.render;

import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class NoRender
extends Module {
    public static NoRender INSTANCE;
    Value<Boolean> blockInside = new ValueBuilder().withDescriptor("Block Inside").withValue(true).register(this);

    public NoRender() {
        super("No Render", Feature.Category.Render);
        INSTANCE = this;
    }
}

