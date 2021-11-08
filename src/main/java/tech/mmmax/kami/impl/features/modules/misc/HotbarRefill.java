/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.misc;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.player.InventoryUtils;

public class HotbarRefill
extends Module {
    Item[] cache = new Item[9];

    public HotbarRefill() {
        super("HotbarRefill", Feature.Category.Misc);
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (HotbarRefill.mc.currentScreen != null) {
            return;
        }
        if (HotbarRefill.mc.player.isDead) {
            this.cache = new Item[9];
            return;
        }
        int index = 0;
        for (Item item : this.cache) {
            if (item != null && !item.equals(Item.getItemFromBlock((Block)Blocks.AIR)) && HotbarRefill.mc.player.inventory.getStackInSlot(index).isEmpty()) {
                try {
                    int slot = this.getSlot(item);
                    if (slot != -1) {
                        InventoryUtils.moveItem(slot, index);
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            ++index;
        }
        for (int i = 0; i < 9; ++i) {
            this.cache[i] = HotbarRefill.mc.player.inventory.getStackInSlot(i).getItem();
        }
    }

    int getSlot(Item item) {
        int slot = -1;
        for (int i = 45; i > 9; --i) {
            if (!HotbarRefill.mc.player.inventory.getStackInSlot(i).getItem().equals(item)) continue;
            slot = i;
            break;
        }
        return slot;
    }
}

