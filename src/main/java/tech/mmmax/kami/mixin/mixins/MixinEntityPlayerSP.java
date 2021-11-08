/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.entity.MoverType
 *  net.minecraft.stats.RecipeBook
 *  net.minecraft.stats.StatisticsManager
 *  net.minecraft.world.World
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package tech.mmmax.kami.mixin.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.MoverType;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.mmmax.kami.api.event.MoveEvent;
import tech.mmmax.kami.api.event.PostMoveEvent;

@Mixin(value={EntityPlayerSP.class}, priority=0x7FFFFFF5)
public class MixinEntityPlayerSP
extends AbstractClientPlayer {
    @Shadow
    public float renderArmPitch;

    public MixinEntityPlayerSP(Minecraft p_i47378_1_, World p_i47378_2_, NetHandlerPlayClient p_i47378_3_, StatisticsManager p_i47378_4_, RecipeBook p_i47378_5_) {
        super(p_i47378_2_, p_i47378_3_.getGameProfile());
    }

    @Inject(method={"move"}, at={@At(value="HEAD")}, cancellable=true)
    public void move(MoverType type, double x, double y, double z, CallbackInfo ci) {
        MoveEvent event = new MoveEvent(type, x, y, z);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCanceled()) {
            ci.cancel();
            return;
        }
        if (event.x != x || event.y != y || event.z != z) {
            super.move(type, event.x, event.y, event.z);
            ci.cancel();
        }
    }

    @Inject(method={"move"}, at={@At(value="TAIL")}, cancellable=true)
    public void postMove(MoverType type, double x, double y, double z, CallbackInfo ci) {
        PostMoveEvent event = new PostMoveEvent(x, y, z);
        MinecraftForge.EVENT_BUS.post((Event)event);
    }
}

