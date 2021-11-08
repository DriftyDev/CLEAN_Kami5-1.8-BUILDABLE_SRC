/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  org.lwjgl.input.Mouse
 */
package tech.mmmax.kami.impl.features.modules.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.management.FriendManager;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.chat.ChatMessage;
import tech.mmmax.kami.api.utils.chat.ChatUtils;
import tech.mmmax.kami.api.utils.player.InventoryUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class MiddleClick
extends Module {
    Value<Boolean> friend = new ValueBuilder().withDescriptor("Friend").withValue(true).register(this);
    Value<Boolean> pearl = new ValueBuilder().withDescriptor("Pearl").withValue(true).register(this);
    Value<Boolean> xp = new ValueBuilder().withDescriptor("XP").withValue(true).register(this);
    Value<Boolean> offhandSwap = new ValueBuilder().withDescriptor("Offhand Swap").withValue(false).register(this);
    boolean hasPressed = false;
    Entity pointed;

    public MiddleClick() {
        super("Middle Click", Feature.Category.Player);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (Mouse.isButtonDown((int)2)) {
            int oldSlot;
            this.pointed = MiddleClick.mc.getRenderManager().pointedEntity;
            if (!this.hasPressed) {
                if (this.friend.getValue().booleanValue() && this.pointed != null) {
                    if (FriendManager.INSTANCE.isFriend(this.pointed)) {
                        FriendManager.INSTANCE.removeFriend(this.pointed);
                        ChatUtils.sendMessage(new ChatMessage("Removed " + this.pointed.getName() + " from friends", false, 0));
                    } else {
                        FriendManager.INSTANCE.addFriend(this.pointed);
                        ChatUtils.sendMessage(new ChatMessage("Added " + this.pointed.getName() + " from friends", false, 0));
                    }
                }
                if ((this.pointed == null || this.pointed instanceof EntityEnderCrystal) && this.pearl.getValue().booleanValue() && this.allowPearl()) {
                    oldSlot = MiddleClick.mc.player.inventory.currentItem;
                    int pearlSlot = InventoryUtils.getHotbarItemSlot(Items.ENDER_PEARL);
                    if (pearlSlot == -1 && !this.offhandSwap.getValue().booleanValue()) {
                        ChatUtils.sendMessage(new ChatMessage("No pearls in hotbar", false, 0));
                        this.hasPressed = true;
                        return;
                    }
                    Item oldItem = MiddleClick.mc.player.getHeldItemOffhand().getItem();
                    if (this.offhandSwap.getValue().booleanValue()) {
                        InventoryUtils.moveItemToOffhand(Items.ENDER_PEARL);
                    } else {
                        InventoryUtils.switchToSlotGhost(pearlSlot);
                    }
                    MiddleClick.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(this.offhandSwap.getValue() != false ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND));
                    if (this.offhandSwap.getValue().booleanValue()) {
                        InventoryUtils.moveItemToOffhand(oldItem);
                    } else {
                        InventoryUtils.switchToSlotGhost(oldSlot);
                    }
                }
            }
            if (this.xp.getValue().booleanValue() && this.allowExp()) {
                oldSlot = MiddleClick.mc.player.inventory.currentItem;
                int xpSlot = InventoryUtils.getHotbarItemSlot(Items.EXPERIENCE_BOTTLE);
                if (xpSlot == -1) {
                    this.hasPressed = true;
                    return;
                }
                InventoryUtils.switchToSlotGhost(xpSlot);
                MiddleClick.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                InventoryUtils.switchToSlotGhost(oldSlot);
            }
            this.hasPressed = true;
        } else {
            this.hasPressed = false;
        }
    }

    boolean allowPearl() {
        RayTraceResult mouseOver = MiddleClick.mc.objectMouseOver;
        return mouseOver == null || mouseOver.typeOfHit == RayTraceResult.Type.MISS;
    }

    boolean allowExp() {
        RayTraceResult mouseOver = MiddleClick.mc.objectMouseOver;
        return mouseOver != null && (mouseOver.typeOfHit == RayTraceResult.Type.BLOCK || this.pointed instanceof EntityEnderCrystal);
    }
}

