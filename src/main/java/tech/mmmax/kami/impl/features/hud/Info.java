/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.resources.I18n
 *  net.minecraft.init.Items
 *  net.minecraft.potion.PotionEffect
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Text
 */
package tech.mmmax.kami.impl.features.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import tech.mmmax.kami.api.feature.hud.HudComponent;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.player.InventoryUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;
import tech.mmmax.kami.impl.features.modules.client.HudColors;
import tech.mmmax.kami.impl.gui.ClickGui;

public class Info
extends HudComponent {
    Value<String> order = new ValueBuilder().withDescriptor("Order").withValue("BottomLeft").withModes("BottomLeft", "BottomRight", "TopRight", "TopLeft").register(this);
    Value<Color> infoColor = new ValueBuilder().withDescriptor("Info Color").withValue(new Color(87, 87, 87)).register(this);
    Value<Boolean> potion = new ValueBuilder().withDescriptor("Potions").withValue(true).register(this);
    int off = 0;

    public Info() {
        super("Info");
    }

    @Override
    public void draw(RenderGameOverlayEvent.Text event) {
        super.draw(event);
        if (NullUtils.nullCheck()) {
            return;
        }
        DecimalFormat minuteFormatter = new DecimalFormat("0");
        DecimalFormat secondsFormatter = new DecimalFormat("00");
        ArrayList<InfoComponent> potions = new ArrayList<InfoComponent>();
        ArrayList<InfoComponent> info = new ArrayList<InfoComponent>();
        for (PotionEffect effect : Info.mc.player.getActivePotionEffects()) {
            double timeS = effect.getDuration() / 20 % 60;
            double timeM = effect.getDuration() / 20 / 60;
            String time = minuteFormatter.format(timeM) + ":" + secondsFormatter.format(timeS);
            String name = I18n.format((String)effect.getEffectName(), (Object[])new Object[0]) + " " + (effect.getAmplifier() + 1) + " " + ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + time + ChatFormatting.GRAY + "]";
            potions.add(new InfoComponent(new Color(effect.getPotion().getLiquidColor()), name));
        }
        info.add(new InfoComponent(null, "FPS " + ChatFormatting.WHITE + Minecraft.getDebugFPS()));
        if (mc.getConnection() != null && mc.getConnection().getPlayerInfo(Info.mc.player.getUniqueID()) != null) {
            info.add(new InfoComponent(null, "PING " + ChatFormatting.WHITE + mc.getConnection().getPlayerInfo(Info.mc.player.getUniqueID()).getResponseTime()));
        }
        int crystals = InventoryUtils.getItemCount(Items.END_CRYSTAL);
        int totems = InventoryUtils.getItemCount(Items.TOTEM_OF_UNDYING);
        int exp = InventoryUtils.getItemCount(Items.EXPERIENCE_BOTTLE);
        info.add(new InfoComponent(null, "CRYSTALS " + ChatFormatting.WHITE + crystals));
        info.add(new InfoComponent(null, "TOTEMS " + ChatFormatting.WHITE + totems));
        info.add(new InfoComponent(null, "XP " + ChatFormatting.WHITE + exp));
        info.sort(Comparator.comparingInt(i -> -ClickGui.CONTEXT.getRenderer().getTextWidth(i.text)));
        this.off = 0;
        if (this.order.getValue().contains("Top")) {
            this.renderInfo(info);
            if (this.potion.getValue().booleanValue()) {
                this.renderPotions(potions);
            }
        } else {
            if (this.potion.getValue().booleanValue()) {
                this.renderPotions(potions);
            }
            this.renderInfo(info);
        }
    }

    public void renderPotions(List<InfoComponent> potions) {
        for (InfoComponent comp : potions) {
            int x = this.order.getValue().contains("Right") ? ((Number)this.xPos.getValue()).intValue() - ClickGui.CONTEXT.getRenderer().getTextWidth(comp.text) : ((Number)this.xPos.getValue()).intValue();
            ClickGui.CONTEXT.getRenderer().renderText(comp.text, x, ((Number)this.yPos.getValue()).intValue() + this.off, comp.color, ClickGui.CONTEXT.getColorScheme().doesTextShadow());
            this.off += this.order.getValue().contains("Top") ? ClickGui.CONTEXT.getRenderer().getTextHeight(comp.text) : -ClickGui.CONTEXT.getRenderer().getTextHeight(comp.text);
        }
    }

    public void renderInfo(List<InfoComponent> info) {
        for (InfoComponent comp : info) {
            int x = this.order.getValue().contains("Right") ? ((Number)this.xPos.getValue()).intValue() - ClickGui.CONTEXT.getRenderer().getTextWidth(comp.text) : ((Number)this.xPos.getValue()).intValue();
            ClickGui.CONTEXT.getRenderer().renderText(comp.text, x, ((Number)this.yPos.getValue()).intValue() + this.off, HudColors.getTextColor(((Number)this.yPos.getValue()).intValue() + this.off), ClickGui.CONTEXT.getColorScheme().doesTextShadow());
            this.off += this.order.getValue().contains("Top") ? ClickGui.CONTEXT.getRenderer().getTextHeight(comp.text) : -ClickGui.CONTEXT.getRenderer().getTextHeight(comp.text);
        }
    }

    static class InfoComponent {
        Color color;
        String text;

        public InfoComponent(Color color, String text) {
            this.color = color;
            this.text = text;
        }
    }
}

