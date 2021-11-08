/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.player;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;

public class Sprint
extends Module {
    public Sprint() {
        super("Sprint", Feature.Category.Player);
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent tickEvent) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (Sprint.mc.player == null || Sprint.mc.world == null) {
            return;
        }
        if (Sprint.mc.player.moveForward > 0.0f) {
            Sprint.mc.player.setSprinting(true);
        }
    }
}

