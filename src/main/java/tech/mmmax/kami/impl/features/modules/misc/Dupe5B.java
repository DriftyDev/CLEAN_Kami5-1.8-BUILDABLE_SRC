/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemShulkerBox
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketCloseWindow
 *  net.minecraft.util.EnumHand
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.misc;

import net.minecraft.item.ItemShulkerBox;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class Dupe5B
extends Module {
    Value<Number> dropCount = new ValueBuilder().withDescriptor("Drop Count").withValue(1).withRange(1, 60).register(this);
    Value<Boolean> dropAll = new ValueBuilder().withDescriptor("Drop All").withValue(false).register(this);
    Value<Boolean> shulkerCheck = new ValueBuilder().withDescriptor("Shulker Check").withValue(false).register(this);

    public Dupe5B() {
        super("5BDupe", Feature.Category.Misc);
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (Dupe5B.mc.player.getHeldItem(EnumHand.MAIN_HAND).getCount() > 1 && (!this.shulkerCheck.getValue().booleanValue() || Dupe5B.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemShulkerBox)) {
            mc.displayGuiScreen(null);
            mc.getConnection().sendPacket((Packet)new CPacketCloseWindow());
            for (int i = 0; i < this.dropCount.getValue().intValue(); ++i) {
                Dupe5B.mc.player.dropItem(false);
            }
            if (this.dropAll.getValue().booleanValue()) {
                Dupe5B.mc.player.dropItem(true);
            }
        }
    }
}

