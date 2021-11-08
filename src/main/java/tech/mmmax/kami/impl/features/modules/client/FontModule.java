/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.impl.features.modules.client;

import java.awt.Font;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;
import tech.mmmax.kami.impl.features.modules.client.gui.SecondGuiModule;

public class FontModule
extends Module {
    public static FontModule INSTANCE;
    public Font font = new Font("Arial", 0, 18);
    public Value<Number> fontSize = new ValueBuilder().withDescriptor("Font Size").withValue(18).withRange(10, 25).register(this);
    public Value<Number> fontStyle = new ValueBuilder().withDescriptor("Font Style").withValue(0).withRange(0, 2).register(this);

    public FontModule() {
        super("Font", Feature.Category.Client);
        INSTANCE = this;

        this.font = this.font.deriveFont((fontStyle.getValue()).intValue());
        SecondGuiModule.fontRenderer.setFont(FontModule.INSTANCE.font);
    }
}

