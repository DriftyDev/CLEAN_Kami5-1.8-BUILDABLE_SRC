/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderItem
 *  net.minecraft.item.ItemStack
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Text
 */
package tech.mmmax.kami.impl.features.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import tech.mmmax.kami.api.feature.hud.HudComponent;
import tech.mmmax.kami.impl.features.modules.client.HudColors;
import tech.mmmax.kami.impl.gui.ClickGui;

public class ArmorHud
extends HudComponent {
    private static RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();

    public ArmorHud() {
        super("Armor Hud");
    }

    @Override
    public void draw(RenderGameOverlayEvent.Text event) {
        this.renderArmorHUD(true);
    }

    public void renderArmorHUD(boolean percent) {
        ScaledResolution resolution = new ScaledResolution(mc);
        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();
        GlStateManager.enableTexture2D();
        int i = width / 2;
        int iteration = 0;
        int y = height - 55 - (ArmorHud.mc.player.isInWater() && ArmorHud.mc.playerController.gameIsSurvivalOrAdventure() ? 10 : 0);
        for (ItemStack is : ArmorHud.mc.player.inventory.armorInventory) {
            ++iteration;
            if (is.isEmpty()) continue;
            int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.enableDepth();
            ArmorHud.itemRender.zLevel = 200.0f;
            itemRender.renderItemAndEffectIntoGUI(is, x, y);
            itemRender.renderItemOverlayIntoGUI(ArmorHud.mc.fontRenderer, is, x, y, "");
            ArmorHud.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            String s = is.getCount() > 1 ? is.getCount() + "" : "";
            ClickGui.CONTEXT.getRenderer().renderText(s, x + 19 - 2 - ClickGui.CONTEXT.getRenderer().getTextWidth(s), y + 9, HudColors.getTextColor(y + 9), true);
            if (!percent) continue;
            float green = ((float)is.getMaxDamage() - (float)is.getItemDamage()) / (float)is.getMaxDamage();
            float red = 1.0f - green;
            int dmg = 100 - (int)(red * 100.0f);
            ClickGui.CONTEXT.getRenderer().renderText(dmg + "", x + 8 - ClickGui.CONTEXT.getRenderer().getTextWidth(dmg + "") / 2, y - 11, HudColors.getTextColor(y + 9), true);
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }
}

