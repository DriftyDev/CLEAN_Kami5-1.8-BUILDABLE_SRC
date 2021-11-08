/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Text
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package tech.mmmax.kami.impl.features.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.time.ZonedDateTime;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tech.mmmax.kami.api.feature.hud.HudComponent;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;
import tech.mmmax.kami.impl.KamiMod;
import tech.mmmax.kami.impl.features.modules.client.HudColors;
import tech.mmmax.kami.impl.gui.ClickGui;

public class Welcomer
extends HudComponent {
    Value<Boolean> autoPos = new ValueBuilder().withDescriptor("Auto Pos").withValue(true).register(this);
    Value<String> mode = new ValueBuilder().withDescriptor("Mode").withValue("Extra").withModes("Extra", "No Extra", "Kami5").register(this);
    ZonedDateTime time = ZonedDateTime.now();

    public Welcomer() {
        super("Welcomer");
        
        this.xPos.setActive(autoPos.getValue() == false);
        this.yPos.setActive(autoPos.getValue() == false);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        String string = this.getWelcomeString();
        if (this.autoPos.getValue().booleanValue()) {
            ScaledResolution sr = new ScaledResolution(mc);
            this.xPos.setValue((sr.getScaledWidth() - ClickGui.CONTEXT.getRenderer().getTextWidth(string)) / 2);
            this.yPos.setValue(1);
        }
        ClickGui.CONTEXT.getRenderer().renderText(this.getWelcomeString(), ((Number)this.xPos.getValue()).floatValue(), ((Number)this.yPos.getValue()).floatValue(), HudColors.getTextColor(((Number)this.yPos.getValue()).intValue()), ClickGui.CONTEXT.getColorScheme().doesTextShadow());
    }

    String getWelcomeString() {
        String timer = null;
        String string = this.time.getHour() <= 11 ? "Good Morning " : (this.time.getHour() <= 18 && this.time.getHour() > 11 ? "Good Afternoon " : (timer = this.time.getHour() <= 23 && this.time.getHour() > 18 ? "Good Evening " : ""));
        if (this.mode.getValue().equals("Kami5")) {
            return "Welcome to " + KamiMod.NAME_VERSION_COLORED + ChatFormatting.RESET + Welcomer.mc.player.getName();
        }
        if (this.mode.getValue().equals("Extra")) {
            return timer + Welcomer.mc.player.getName() + ">:^)";
        }
        return timer + Welcomer.mc.player.getName();
    }
}

