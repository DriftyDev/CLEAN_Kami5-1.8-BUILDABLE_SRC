/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.impl.gui;

import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.gui.GUI;
import tech.mmmax.kami.api.gui.context.Context;
import tech.mmmax.kami.api.gui.helpers.Rect;
import tech.mmmax.kami.impl.features.modules.client.gui.ClickGuiModule;
import tech.mmmax.kami.impl.gui.components.CategoryFrame;
import tech.mmmax.kami.impl.gui.renderer.Renderer;

public class ClickGui
extends GUI {
    public static Context CONTEXT = new Context(ClickGuiModule.INSTANCE, ClickGuiModule.INSTANCE, new Renderer());
    public static ClickGui INSTANCE;

    public ClickGui() {
        super(CONTEXT);
    }

    @Override
    public void addComponents() {
        super.addComponents();
        int offset = 100;
        for (Feature.Category category : Feature.Category.values()) {
            this.getContext().getComponents().add(new CategoryFrame(category, new Rect(offset, 40, 100, 200)));
            offset += this.getContext().getMetrics().getFrameWidth() + 10;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

