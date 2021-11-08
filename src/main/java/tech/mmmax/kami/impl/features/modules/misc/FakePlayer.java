/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemStack
 *  net.minecraft.world.GameType
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.misc;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.event.PacketEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.Timer;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class FakePlayer
extends Module {
    int eID = -696420;
    EntityPlayer fakePlayer;
    Timer popTimer = new Timer();
    Value<String> page = new ValueBuilder().withDescriptor("Page").withValue("Pop").withModes("Pop", "Movement", "Other").register(this);
    Value<String> popMode = new ValueBuilder().withDescriptor("Pop Mode").withValue("None").withModes("None", "Damage", "Timed").withPageParent(this.page).withPage("Pop").register(this);
    Value<Number> popTime = new ValueBuilder().withDescriptor("Pop Time").withValue(2000).withRange(10, 10000).withPageParent(this.page).withPage("Pop").register(this);
    Value<String> moveMode = new ValueBuilder().withDescriptor("Move Mode").withValue("Step").withModes("Step", "Strafe", "StepStrafe", "None").withPageParent(this.page).withPage("Movement").register(this);
    Value<Number> stepHeight = new ValueBuilder().withDescriptor("Step Height").withValue(2.3).withRange(0.5, 10d).withPageParent(this.page).withPage("Movement").register(this);

    public FakePlayer() {
        super("Fake Player", Feature.Category.Misc);

        this.popTimer.setDelay((popTime.getValue()).longValue());
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (NullUtils.nullCheck() || this.fakePlayer != null) {
            return;
        }
        this.fakePlayer = new EntityOtherPlayerMP((World)FakePlayer.mc.world, new GameProfile(UUID.fromString("8d5951a3-907b-49c1-96b4-04adcb59f58c"), "Stinky"));
        this.fakePlayer.copyLocationAndAnglesFrom((Entity)FakePlayer.mc.player);
        this.fakePlayer.inventory.copyInventory(FakePlayer.mc.player.inventory);
        this.fakePlayer.setGameType(GameType.SURVIVAL);
        FakePlayer.mc.world.spawnEntity((Entity)this.fakePlayer);
        this.eID = this.fakePlayer.getEntityId();
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck() || this.fakePlayer == null) {
            return;
        }
        this.fakePlayer.inventory = FakePlayer.mc.player.inventory;
        if (!this.popMode.getValue().equals("None")) {
            this.fakePlayer.inventory.offHandInventory.set(0, new ItemStack(Items.TOTEM_OF_UNDYING));
        }
        if (!this.popMode.getValue().equals("Timed") || this.popTimer.isPassed()) {
            // empty if block
        }
        if (!this.moveMode.getValue().equals("None")) {
            this.fakePlayer.rotationYaw = FakePlayer.mc.player.rotationYaw;
            this.fakePlayer.moveStrafing = FakePlayer.mc.player.moveStrafing;
            this.fakePlayer.moveForward = FakePlayer.mc.player.moveForward;
            this.fakePlayer.moveVertical = FakePlayer.mc.player.moveVertical;
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (this.fakePlayer != null) {
            FakePlayer.mc.world.removeEntityFromWorld(this.eID);
            this.fakePlayer.setDead();
            this.fakePlayer = null;
        }
    }
}

