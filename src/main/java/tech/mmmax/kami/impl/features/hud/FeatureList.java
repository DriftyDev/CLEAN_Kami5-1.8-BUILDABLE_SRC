/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Text
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent
 */
package tech.mmmax.kami.impl.features.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.hud.HudComponent;
import tech.mmmax.kami.api.management.FeatureManager;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;
import tech.mmmax.kami.impl.features.modules.client.HudColors;
import tech.mmmax.kami.impl.gui.ClickGui;

public class FeatureList
extends HudComponent {
    Value<String> alignment = new ValueBuilder().withDescriptor("Alignment").withValue("TopLeft").withModes("TopLeft", "BottomLeft", "TopRight", "BottomRight").register(this);
    Value<Boolean> animations = new ValueBuilder().withDescriptor("Animation").withValue(false).withAction(s -> {}).register(this);

    public FeatureList() {
        super("Feature List");
    }

    @SubscribeEvent
    public void onFastTick(TickEvent event) {
        for (Feature feature : FeatureManager.INSTANCE.getFeatures()) {
            try {
                feature.animation();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void draw(RenderGameOverlayEvent.Text event) {
        super.draw(event);
        ArrayList<Feature> sorted = new ArrayList<Feature>();
        for (Feature feature : FeatureManager.INSTANCE.getFeatures()) {
            if (!feature.visible.getValue().booleanValue() || !feature.isEnabled()) continue;
            sorted.add(feature);
        }
        sorted.sort(Comparator.comparingInt(mod -> {
            int o = ClickGui.CONTEXT.getRenderer().getTextWidth(mod.getDisplayName() + (!mod.getHudInfo().equals("") ? ChatFormatting.GRAY + " [" + ChatFormatting.WHITE + mod.getHudInfo() + ChatFormatting.GRAY + "]" : ""));
            return -o;
        }));
        int offset = 0;
        for (Feature module : sorted) {
            boolean top = this.alignment.getValue().contains("Top");
            String text = module.getDisplayName() + (!module.getHudInfo().equals("") ? ChatFormatting.GRAY + " [" + ChatFormatting.WHITE + module.getHudInfo() + ChatFormatting.GRAY + "]" : "");
            if (this.alignment.getValue().contains("Left")) {
                ClickGui.CONTEXT.getRenderer().renderText(text, ((Number)this.xPos.getValue()).intValue() + ClickGui.CONTEXT.getRenderer().getTextWidth(text), ((Number)this.yPos.getValue()).intValue() + (top ? offset : -offset), HudColors.getTextColor(((Number)this.yPos.getValue()).intValue() + (top ? offset : -offset)), ClickGui.CONTEXT.getColorScheme().doesTextShadow());
                offset += ClickGui.CONTEXT.getRenderer().getTextHeight(text);
            }
            if (!this.alignment.getValue().contains("Right")) continue;
            if (this.animations.getValue().booleanValue()) {
                ClickGui.CONTEXT.getRenderer().renderText(text, ((Number)this.xPos.getValue()).intValue() - module.offset - ClickGui.CONTEXT.getRenderer().getTextWidth(text), ((Number)this.yPos.getValue()).intValue() + (top ? offset : -offset), HudColors.getTextColor(((Number)this.yPos.getValue()).intValue() + (top ? offset : -offset)), ClickGui.CONTEXT.getColorScheme().doesTextShadow());
                offset += ClickGui.CONTEXT.getRenderer().getTextHeight(text);
                continue;
            }
            ClickGui.CONTEXT.getRenderer().renderText(text, ((Number)this.xPos.getValue()).intValue() - ClickGui.CONTEXT.getRenderer().getTextWidth(text), ((Number)this.yPos.getValue()).intValue() + (top ? offset : -offset), HudColors.getTextColor(((Number)this.yPos.getValue()).intValue() + (top ? offset : -offset)), ClickGui.CONTEXT.getColorScheme().doesTextShadow());
            offset += ClickGui.CONTEXT.getRenderer().getTextHeight(text);
        }
    }
}

