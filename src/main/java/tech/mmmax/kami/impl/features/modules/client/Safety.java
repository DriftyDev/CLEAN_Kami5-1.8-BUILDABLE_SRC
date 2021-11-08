/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.world.HoleUtils;

public class Safety
extends Module {
    SafetyMode safety;

    public Safety() {
        super("Safety", Feature.Category.Client);
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (Safety.mc.world.getBlockState(Safety.mc.player.getPosition()).getMaterial().isSolid()) {
            this.safety = SafetyMode.SAFE;
            return;
        }
        if (HoleUtils.isHole(Safety.mc.player.getPosition())) {
            this.safety = SafetyMode.SAFE;
            return;
        }
        this.safety = SafetyMode.UNSAFE;
    }

    @Override
    public String getHudInfo() {
        return this.safety != null ? this.safety.toString() : "";
    }

    static enum SafetyMode {
        SAFE(ChatFormatting.GREEN),
        UNSAFE(ChatFormatting.RED);

        ChatFormatting color;

        private SafetyMode(ChatFormatting color) {
            this.color = color;
        }

        public String toString() {
            return this.color.toString() + super.toString();
        }
    }
}

