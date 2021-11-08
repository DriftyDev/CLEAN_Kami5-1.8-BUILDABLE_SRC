/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.impl.gui.components;

import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.gui.component.impl.FrameComponent;
import tech.mmmax.kami.api.gui.helpers.Rect;
import tech.mmmax.kami.api.management.FeatureManager;
import tech.mmmax.kami.impl.gui.components.module.FeatureButton;

public class CategoryFrame
extends FrameComponent {
    Feature.Category category;

    public CategoryFrame(Feature.Category category, Rect dims) {
        super(category.toString(), dims);
        this.category = category;
        for (Feature feature : FeatureManager.INSTANCE.getFeatures()) {
            if (feature.getCategory() != this.category) continue;
            this.getFlow().getComponents().add(new FeatureButton(feature, new Rect(0, 0, 0, 0)));
        }
    }

    public Feature.Category getCategory() {
        return this.category;
    }

    public void setCategory(Feature.Category category) {
        this.category = category;
    }
}

