/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreen
 *  org.lwjgl.input.Mouse
 */
package tech.mmmax.kami.api.gui;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import tech.mmmax.kami.api.gui.component.IComponent;
import tech.mmmax.kami.api.gui.context.Context;
import tech.mmmax.kami.api.gui.helpers.MouseHelper;
import tech.mmmax.kami.api.gui.render.IRenderer;
import tech.mmmax.kami.api.gui.theme.IColorScheme;
import tech.mmmax.kami.api.gui.theme.IMetrics;

public class GUI
extends GuiScreen {
    Context context;
    int scroll = 13;

    public GUI(Context context) {
        this.context = context;
        this.addComponents();
    }

    public void addComponents() {
    }

    public Context getContext() {
        return this.context;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        MouseHelper mouse = new MouseHelper(mouseX, mouseY);
        this.context.setPartialTicks(partialTicks);
        int dWheel = Mouse.getDWheel();
        dWheel = Integer.compare(dWheel, 0);
        dWheel *= this.scroll;
        this.getContext().getRenderer().preRender(this.getContext());
        if (this.context.getHovering(mouse) != null) {
            this.context.getHovering(mouse).getDims().setY(this.context.getHovering(mouse).getDims().getY() + dWheel);
        }
        for (IComponent component : this.context.getComponents()) {
            if (!component.isActive()) continue;
            component.draw(this.context, mouse);
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        MouseHelper mouse = new MouseHelper(mouseX, mouseY);
        IComponent hovered = this.context.getHovering(mouse);
        if (hovered != null && hovered.isActive()) {
            hovered.click(this.context, mouse, mouseButton);
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        MouseHelper mouse = new MouseHelper(mouseX, mouseY);
        for (IComponent component : this.context.getComponents()) {
            if (!component.isActive()) continue;
            component.release(this.context, mouse, state);
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        for (IComponent component : this.context.getComponents()) {
            if (!component.isActive()) continue;
            component.key(this.context, keyCode, typedChar);
        }
    }

    public void enterGui(IColorScheme colorScheme, IMetrics metrics, IRenderer renderer) {
        this.context.setColorScheme(colorScheme);
        this.context.setMetrics(metrics);
        this.context.setRenderer(renderer);
        this.mc = Minecraft.getMinecraft();
        this.mc.displayGuiScreen((GuiScreen)this);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }
}

