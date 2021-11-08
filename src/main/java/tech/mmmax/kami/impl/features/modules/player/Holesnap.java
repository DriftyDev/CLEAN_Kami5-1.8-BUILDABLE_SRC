/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.player;

import java.awt.Color;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.event.MoveEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.chat.ChatMessage;
import tech.mmmax.kami.api.utils.chat.ChatUtils;
import tech.mmmax.kami.api.utils.player.PlayerUtils;
import tech.mmmax.kami.api.utils.player.TargetUtils;
import tech.mmmax.kami.api.utils.render.RenderUtil;
import tech.mmmax.kami.api.utils.world.HoleUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;
import tech.mmmax.kami.impl.features.modules.player.Step;

public class Holesnap
extends Module {
    public boolean firsttime = false;
    public boolean cancel = false;
    Value<Number> range = new ValueBuilder().withDescriptor("Range").withValue(3).withRange(1, 6).register(this);
    HoleUtils.Hole pk;
    int stuckTicks;

    public Holesnap() {
        super("Holesnap", Feature.Category.Player);
    }

    @Override
    public void onEnable() {
        if (NullUtils.nullCheck()) {
            return;
        }
        super.onEnable();
        this.stuckTicks = 0;
        this.pk = null;
        this.pk = TargetUtils.getTargetHoleVec3D(this.range.getValue().doubleValue());
        if (this.pk == null) {
            Step.paused = false;
            this.cancel = false;
            ChatUtils.sendMessage(new ChatMessage("[Holesnap] Unable to find hole. Disabling", true, 777722));
            this.toggle();
            return;
        }
    }

    @Override
    public void onDisable() {
        if (NullUtils.nullCheck()) {
            return;
        }
        super.onDisable();
        Holesnap.mc.player.stepHeight = 0.5f;
        Step.paused = false;
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (HoleUtils.isObbyHole(PlayerUtils.getPlayerPos()) || HoleUtils.isBedrockHoles(PlayerUtils.getPlayerPos())) {
            Step.paused = false;
            ChatUtils.sendMessage(new ChatMessage("[Holesnap] Snapped into hole, disabling.", true, 777722));
            this.toggle();
            return;
        }
        if (this.pk != null && Holesnap.mc.world.getBlockState(this.pk.pos1).getBlock() == Blocks.AIR) {
            Step.paused = true;
            this.cancel = true;
            BlockPos it = this.pk.pos1;
            Vec3d playerPos = Holesnap.mc.player.getPositionVector();
            Vec3d targetPos = new Vec3d((double)it.getX() + 0.5, Holesnap.mc.player.posY, (double)it.getZ() + 0.5);
            double yawRad = Math.toRadians(this.getRotationTo((Vec3d)playerPos, (Vec3d)targetPos).x);
            double dist = playerPos.distanceTo(targetPos);
            double speed = Holesnap.mc.player.onGround ? -Math.min(0.2805, dist / 2.0) : -PlayerUtils.getDefaultMoveSpeed() + 0.02;
            event.x = -Math.sin(yawRad) * speed;
            event.z = Math.cos(yawRad) * speed;
            if (Holesnap.mc.player.collidedHorizontally && Holesnap.mc.player.onGround) {
                ++this.stuckTicks;
                Holesnap.mc.player.stepHeight = 2.0f;
            } else {
                this.stuckTicks = 0;
                Holesnap.mc.player.stepHeight = 0.5f;
            }
        } else {
            Step.paused = false;
            this.cancel = false;
            ChatUtils.sendMessage(new ChatMessage("[Holesnap] Hole no longer exists, disabling.", true, 777722));
            this.toggle();
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (this.pk != null) {
            RenderUtil.prepare();
            Vec3d targetPos = new Vec3d((double)this.pk.pos1.getX() + 0.5, (double)this.pk.pos1.getY(), (double)this.pk.pos1.getZ() + 0.5);
            Vec3d startPos = RenderUtil.updateToCamera(Holesnap.mc.player.getPositionVector());
            Vec3d endPos = RenderUtil.updateToCamera(targetPos);
            RenderUtil.builder = RenderUtil.tessellator.getBuffer();
            RenderUtil.builder.begin(1, DefaultVertexFormats.POSITION_COLOR);
            RenderUtil.addBuilderVertex(RenderUtil.builder, startPos.x, startPos.y, startPos.z, Color.WHITE);
            RenderUtil.addBuilderVertex(RenderUtil.builder, endPos.x, endPos.y, endPos.z, Color.WHITE);
            RenderUtil.tessellator.draw();
            RenderUtil.release();
        }
    }

    public double normalizeAngle(Double angleIn) {
        double angle = angleIn;
        if ((angle %= 360.0) >= 180.0) {
            angle -= 360.0;
        }
        if (angle < -180.0) {
            angle += 360.0;
        }
        return angle;
    }

    public Vec2f getRotationTo(Vec3d posTo, Vec3d posFrom) {
        return this.getRotationFromVec(posTo.subtract(posFrom));
    }

    public Vec2f getRotationFromVec(Vec3d vec) {
        double xz = Math.hypot(vec.x, vec.z);
        float yaw = (float)this.normalizeAngle(Math.toDegrees(Math.atan2(vec.z, vec.x)) - 90.0);
        float pitch = (float)this.normalizeAngle(Math.toDegrees(-Math.atan2(vec.y, xz)));
        return new Vec2f(yaw, pitch);
    }

    public static boolean isSafeAir(BlockPos blockPos) {
        return Holesnap.isObbyorBedrock(Holesnap.mc.world.getBlockState(blockPos.north()).getBlock()) && Holesnap.isObbyorBedrock(Holesnap.mc.world.getBlockState(blockPos.east()).getBlock()) && Holesnap.isObbyorBedrock(Holesnap.mc.world.getBlockState(blockPos.west()).getBlock()) && Holesnap.isObbyorBedrock(Holesnap.mc.world.getBlockState(blockPos.south()).getBlock());
    }

    public static boolean isObbyorBedrock(Block block) {
        return block == Blocks.OBSIDIAN || block == Blocks.BEDROCK;
    }

    @Override
    public String getHudInfo() {
        if (this.pk != null) {
            return Holesnap.mc.player.getDistanceSq(this.pk.pos1) + " SAFE";
        }
        return "None";
    }
}

