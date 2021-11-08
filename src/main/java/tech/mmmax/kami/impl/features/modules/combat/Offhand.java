/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Items
 *  net.minecraft.init.MobEffects
 *  net.minecraft.item.Item
 *  net.minecraft.util.EnumHand
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  org.lwjgl.input.Mouse
 */
package tech.mmmax.kami.impl.features.modules.combat;

import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.player.InventoryUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class Offhand
extends Module {
    Value<String> item = new ValueBuilder().withDescriptor("Item").withValue("Totem").withModes("Totem", "Gapple", "Crystal", "Bow").register(this);
    Value<String> weakItem = new ValueBuilder().withDescriptor("Weak Item").withValue("Totem").withModes("Totem", "Gapple", "Crystal", "Bow").register(this);
    Value<Number> totemHealth = new ValueBuilder().withDescriptor("Totem Health").withValue(15).withRange(0, 36).register(this);
    Value<Boolean> swordGap = new ValueBuilder().withDescriptor("Sword Gap").withValue(false).register(this);

    public Offhand() {
        super("Offhand", Feature.Category.Combat);
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (Offhand.mc.currentScreen != null) {
            return;
        }
        if (Offhand.mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() != this.getItem()) {
            InventoryUtils.moveItemToOffhand(this.getItem());
        }
    }

    public Item getItem() {
        Item i = Items.TOTEM_OF_UNDYING;
        if (!Mouse.isButtonDown((int)1) && Offhand.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem().equals(Items.DIAMOND_SWORD) && Offhand.mc.player.getHealth() + Offhand.mc.player.getAbsorptionAmount() > (float)this.totemHealth.getValue().intValue()) {
            i = this.getSelectedItem();
        } else if (this.swordGap.getValue().booleanValue() && Offhand.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem().equals(Items.DIAMOND_SWORD) && Mouse.isButtonDown((int)1)) {
            i = Items.GOLDEN_APPLE;
        } else if (Offhand.mc.player.getHealth() + Offhand.mc.player.getAbsorptionAmount() > (float)this.totemHealth.getValue().intValue()) {
            i = this.getSelectedItem();
        }
        return i;
    }

    public Item getSelectedItem() {
        Item i = null;
        if (!Offhand.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
            if (this.item.getValue().equalsIgnoreCase("totem")) {
                i = Items.TOTEM_OF_UNDYING;
            } else if (this.item.getValue().equals("Crystal")) {
                i = Items.END_CRYSTAL;
            } else if (this.item.getValue().equals("Gapple")) {
                i = Items.GOLDEN_APPLE;
            } else if (this.item.getValue().equals("Bow")) {
                i = Items.BOW;
            } else if (this.item.getValue().equals("String")) {
                i = Items.STRING;
            }
        } else if (this.weakItem.getValue().equalsIgnoreCase("totem")) {
            i = Items.TOTEM_OF_UNDYING;
        } else if (this.weakItem.getValue().equals("Crystal")) {
            i = Items.END_CRYSTAL;
        } else if (this.weakItem.getValue().equals("Gapple")) {
            i = Items.GOLDEN_APPLE;
        } else if (this.weakItem.getValue().equals("Bow")) {
            i = Items.BOW;
        } else if (this.weakItem.getValue().equals("String")) {
            i = Items.STRING;
        }
        return i;
    }
}

