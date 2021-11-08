/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.renderer.EntityRenderer
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package tech.mmmax.kami.mixin.mixins;

import com.google.common.base.Predicate;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.mmmax.kami.api.event.RenderWorldPass;
import tech.mmmax.kami.impl.features.modules.misc.NoMiningTrace;

@Mixin(value={EntityRenderer.class})
public class MixinEntityRenderer {
    @Inject(method={"renderWorldPass"}, at={@At(value="HEAD")})
    public void renderWorldPassHEAD(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post((Event)new RenderWorldPass.Head());
    }

    @Inject(method={"renderWorldPass"}, at={@At(value="TAIL")})
    public void renderWorldPassTAIL(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post((Event)new RenderWorldPass.Tail());
    }

    @Redirect(method={"getMouseOver"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/multiplayer/WorldClient;getEntitiesInAABBexcluding(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;"))
    public List<Entity> getEntitiesInAABBexcluding(WorldClient worldClient, Entity entityIn, AxisAlignedBB boundingBox, Predicate predicate) {
        if (NoMiningTrace.spoofTrace()) {
            return new ArrayList<Entity>();
        }
        return worldClient.getEntitiesInAABBexcluding(entityIn, boundingBox, predicate);
    }
}

