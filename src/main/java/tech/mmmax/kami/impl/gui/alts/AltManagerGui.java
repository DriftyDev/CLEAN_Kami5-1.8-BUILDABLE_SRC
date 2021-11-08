/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiTextField
 */
package tech.mmmax.kami.impl.gui.alts;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import tech.mmmax.kami.api.utils.AltUtils;

public class AltManagerGui
extends GuiScreen {
    public static AltManagerGui INSTANCE;
    public static final int BUTTON_WIDTH = 100;
    public static final int BUTTON_HEIGHT = 20;
    GuiTextField username;
    GuiTextField token;
    GuiTextField password;

    public void initGui() {
        super.initGui();
        this.username = new GuiTextField(0, this.fontRenderer, (this.width - 100) / 2, (this.height - 20) / 2, 100, 20);
        this.token = new GuiTextField(1, this.fontRenderer, (this.width - 100) / 2, (this.height - 20) / 2 + 20, 100, 20);
        this.password = new GuiTextField(2, this.fontRenderer, (this.width - 100) / 2, (this.height - 20) / 2 + 40, 100, 20);
        this.username.setText("Username");
        this.token.setText("Token");
        this.password.setText("Password");
        this.addButton(new GuiButton(3, (this.width - 100) / 2, (this.height - 20) / 2 + 60, 100, 20, "Login"));
        this.addButton(new GuiButton(4, (this.width - 100) / 2, (this.height - 20) / 2 + 80, 100, 20, "Exit"));
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.username.drawTextBox();
        this.token.drawTextBox();
        this.password.drawTextBox();
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.username.mouseClicked(mouseX, mouseY, mouseButton);
        this.token.mouseClicked(mouseX, mouseY, mouseButton);
        this.password.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.username.textboxKeyTyped(typedChar, keyCode);
        this.token.textboxKeyTyped(typedChar, keyCode);
        this.password.textboxKeyTyped(typedChar, keyCode);
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 3) {
            AltUtils.login(this.username.getText(), this.token.getText());
        }
        if (button.id == 4) {
            this.mc.displayGuiScreen(null);
        }
    }
}

