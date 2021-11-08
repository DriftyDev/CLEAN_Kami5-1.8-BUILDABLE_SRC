/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.impl.features.modules.client;

import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class Optimizer
extends Module {
    public Value<Boolean> entityFrustum = new ValueBuilder().withDescriptor("Entity Frustum").withValue(false).register(this);
    public static Optimizer INSTANCE;

    public Optimizer() {
        super("Optimizer", Feature.Category.Client);
        INSTANCE = this;
    }
}

