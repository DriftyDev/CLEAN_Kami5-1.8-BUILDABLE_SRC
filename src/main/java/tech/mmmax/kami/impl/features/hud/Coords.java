/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Text
 */
package tech.mmmax.kami.impl.features.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.text.DecimalFormat;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import tech.mmmax.kami.api.feature.hud.HudComponent;
import tech.mmmax.kami.impl.features.modules.client.HudColors;
import tech.mmmax.kami.impl.gui.ClickGui;

public class Coords
extends HudComponent {
    DecimalFormat df = new DecimalFormat("#.#");

    public Coords() {
        super("Coords");
    }

    private String getFacing(String in) {
        String facing = Coords.getTitle(in);
        String add = in.equalsIgnoreCase("North") ? " (-Z)" : (in.equalsIgnoreCase("East") ? " (+X)" : (in.equalsIgnoreCase("South") ? " (+Z)" : (in.equalsIgnoreCase("West") ? " (-X)" : " ERROR")));
        return facing + add;
    }

    public static String getTitle(String in) {
        in = Character.toUpperCase(in.toLowerCase().charAt(0)) + in.toLowerCase().substring(1);
        return in;
    }

    @Override
    public void draw(RenderGameOverlayEvent.Text event) {
        super.draw(event);
        boolean inHell = Coords.mc.world.getBiome(Coords.mc.player.getPosition()).getBiomeName().equals("Hell");
        int k = Coords.mc.currentScreen instanceof GuiChat ? 14 : 0;
        ClickGui.CONTEXT.getRenderer().renderText(this.getFacing(Coords.mc.player.getHorizontalFacing().getName().toUpperCase()), ((Number)this.xPos.getValue()).floatValue(), ((Number)this.yPos.getValue()).floatValue() - (float)ClickGui.CONTEXT.getRenderer().getTextHeight("XYZ") - (float)k, HudColors.getTextColor(((Number)this.yPos.getValue()).intValue()), ClickGui.CONTEXT.getColorScheme().doesTextShadow());
        if (inHell) {
            ClickGui.CONTEXT.getRenderer().renderText("XYZ " + ChatFormatting.WHITE + this.df.format(Coords.mc.player.posX) + ", " + this.df.format(Coords.mc.player.posY) + ", " + this.df.format(Coords.mc.player.posZ) + ChatFormatting.RESET + " (" + ChatFormatting.WHITE + this.df.format(Coords.mc.player.posX * 7.0) + ", " + this.df.format(Coords.mc.player.posZ * 7.0) + ChatFormatting.RESET + ")", ((Number)this.xPos.getValue()).floatValue(), ((Number)this.yPos.getValue()).floatValue() - (float)k, HudColors.getTextColor(((Number)this.yPos.getValue()).intValue()), ClickGui.CONTEXT.getColorScheme().doesTextShadow());
        } else {
            ClickGui.CONTEXT.getRenderer().renderText("XYZ " + ChatFormatting.WHITE + this.df.format(Coords.mc.player.posX) + ", " + this.df.format(Coords.mc.player.posY) + ", " + this.df.format(Coords.mc.player.posZ) + ChatFormatting.RESET + " (" + ChatFormatting.WHITE + this.df.format(Coords.mc.player.posX / 7.0) + ", " + this.df.format(Coords.mc.player.posZ / 7.0) + ChatFormatting.RESET + ")", ((Number)this.xPos.getValue()).floatValue(), ((Number)this.yPos.getValue()).floatValue() - (float)k, HudColors.getTextColor(((Number)this.yPos.getValue()).intValue()), ClickGui.CONTEXT.getColorScheme().doesTextShadow());
        }
    }
}

