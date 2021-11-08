/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiMainMenu
 *  net.minecraft.client.gui.GuiScreen
 */
package tech.mmmax.kami.mixin.mixins;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value={GuiMainMenu.class})
public class MixinGuiMainMenu
extends GuiScreen {
}

