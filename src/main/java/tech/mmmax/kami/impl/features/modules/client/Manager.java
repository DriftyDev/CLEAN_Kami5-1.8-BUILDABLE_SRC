/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.client;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;
import tech.mmmax.kami.impl.KamiMod;

public class Manager
extends Module {
    Value<String> clientName = new ValueBuilder().withDescriptor("Client Name").withValue(KamiMod.NAME).register(this);

    public Manager() {
        super("Manager", Feature.Category.Client);
        this.setEnabled(true);
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        KamiMod.NAME = this.clientName.getValue();
        KamiMod.updateName();
    }
}

