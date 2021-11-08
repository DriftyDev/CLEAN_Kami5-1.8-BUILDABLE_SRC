/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.server.SPacketEntityMetadata
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$MouseInputEvent
 *  org.lwjgl.input.Mouse
 */
package tech.mmmax.kami.impl.features.modules.misc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;
import tech.mmmax.kami.api.event.PacketEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class MultiTask
extends Module {
    public static MultiTask INSTANCE;
    public Value<Boolean> cancel = new ValueBuilder().withDescriptor("Cancel").withValue(true).register(this);
    Value<Boolean> debug = new ValueBuilder().withDescriptor("Debug").withValue(false).register(this);

    public MultiTask() {
        super("MultiTask", Feature.Category.Misc);
        INSTANCE = this;
    }

    public void onPacket(PacketEvent event) {
        CPacketPlayerDigging packet;
        if (NullUtils.nullCheck()) {
            return;
        }
        if (event.getPacket() instanceof CPacketPlayerDigging && (packet = (CPacketPlayerDigging)event.getPacket()).getAction() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM && Mouse.isButtonDown((int)0)) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityMetadata) {
            // empty if block
        }
    }

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event) {
        if (Mouse.getEventButtonState() && MultiTask.mc.player != null && MultiTask.mc.objectMouseOver.typeOfHit.equals((Object)RayTraceResult.Type.ENTITY) && MultiTask.mc.player.isHandActive() && (MultiTask.mc.gameSettings.keyBindAttack.isPressed() || Mouse.getEventButton() == MultiTask.mc.gameSettings.keyBindAttack.getKeyCode())) {
            MultiTask.mc.playerController.attackEntity((EntityPlayer)MultiTask.mc.player, MultiTask.mc.objectMouseOver.entityHit);
            MultiTask.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }
}

