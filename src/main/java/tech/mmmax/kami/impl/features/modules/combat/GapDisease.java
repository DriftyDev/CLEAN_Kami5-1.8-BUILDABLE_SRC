/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.combat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.event.GapEatEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.Timer;
import tech.mmmax.kami.api.utils.player.InventoryUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class GapDisease
extends Module {
    Timer switchBackTimer = new Timer();
    Value<String> mode = new ValueBuilder().withDescriptor("Mode").withValue("Instant").withModes("Instant", "Packet", "Normal").register(this);
    Value<Number> delay = new ValueBuilder().withDescriptor("Delay").withValue(0).withRange(0, 100).register(this);
    int oldSlot = -1;

    public GapDisease() {
        super("GapDisease", Feature.Category.Combat);

        this.switchBackTimer.setDelay((delay.getValue()).longValue());
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (this.oldSlot != -1 && this.switchBackTimer.isPassed()) {
            if (this.mode.getValue().equals("Packet")) {
                InventoryUtils.switchToSlotGhost(this.oldSlot);
                this.switchBackTimer.resetDelay();
                this.switchBackTimer.setPaused(true);
            }
            if (this.mode.getValue().equals("Normal")) {
                InventoryUtils.switchToSlot(this.oldSlot);
                this.switchBackTimer.resetDelay();
                this.switchBackTimer.setPaused(true);
            }
        }
    }

    @SubscribeEvent
    public void onEat(GapEatEvent event) {
        if (this.mode.getValue().equals("Instant")) {
            InventoryUtils.switchToSlotGhost(GapDisease.mc.player.inventory.currentItem);
        }
        if (this.mode.getValue().equals("Packet")) {
            this.oldSlot = GapDisease.mc.player.inventory.currentItem;
            InventoryUtils.switchToSlotGhost(this.getSlot());
            this.switchBackTimer.resetDelay();
            this.switchBackTimer.setPaused(false);
            if (this.delay.getValue().longValue() == 0L) {
                InventoryUtils.switchToSlotGhost(this.oldSlot);
                this.switchBackTimer.setPaused(true);
            }
        }
        if (this.mode.getValue().equals("Normal")) {
            this.oldSlot = GapDisease.mc.player.inventory.currentItem;
            InventoryUtils.switchToSlot(this.getSlot());
            this.switchBackTimer.resetDelay();
            this.switchBackTimer.setPaused(false);
        }
    }

    int getSlot() {
        if (GapDisease.mc.player.inventory.currentItem == 9) {
            return GapDisease.mc.player.inventory.currentItem - 1;
        }
        return GapDisease.mc.player.inventory.currentItem + 1;
    }
}

