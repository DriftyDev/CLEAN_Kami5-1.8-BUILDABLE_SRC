/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.api.management;

import java.util.ArrayList;
import java.util.List;
import tech.mmmax.kami.api.feature.Feature;

public class FeatureManager {
    public static FeatureManager INSTANCE;
    List<Feature> features = new ArrayList<Feature>();

    public List<Feature> getFeatures() {
        return this.features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }
}

