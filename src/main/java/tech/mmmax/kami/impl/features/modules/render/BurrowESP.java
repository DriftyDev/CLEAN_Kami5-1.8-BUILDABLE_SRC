/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.World
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.management.FriendManager;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.render.RenderUtil;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class BurrowESP
extends Module {
    Value<Color> fill = new ValueBuilder().withDescriptor("Fill").withValue(new Color(255, 62, 62, 25)).register(this);
    Value<Color> line = new ValueBuilder().withDescriptor("Line").withValue(new Color(255, 62, 62, 255)).register(this);
    private final Map<EntityPlayer, BlockPos> burrowedPlayers = new HashMap<EntityPlayer, BlockPos>();

    public BurrowESP() {
        super("Burrow ESP", Feature.Category.Render);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        this.burrowedPlayers.clear();
        this.getPlayers();
    }

    private void getPlayers() {
        for (EntityPlayer entityPlayer : BurrowESP.mc.world.playerEntities) {
            if (entityPlayer == BurrowESP.mc.player || FriendManager.INSTANCE.isFriend((Entity)entityPlayer) || !this.isBurrowed(entityPlayer)) continue;
            this.burrowedPlayers.put(entityPlayer, new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ));
        }
    }

    private void renderBurrowedBlock(BlockPos blockPos) {
        IBlockState blockState = BurrowESP.mc.world.getBlockState(blockPos);
        AxisAlignedBB bb = blockState.getSelectedBoundingBox((World)BurrowESP.mc.world, blockPos);
        RenderUtil.renderBB(7, bb, this.fill.getValue(), this.fill.getValue());
        RenderUtil.renderBB(3, bb, this.line.getValue(), this.line.getValue());
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.burrowedPlayers.clear();
    }

    private void renderblocklambda(Map.Entry entry) {
        this.renderBurrowedBlock((BlockPos)entry.getValue());
    }

    private boolean isBurrowed(EntityPlayer entityPlayer) {
        BlockPos blockPos = new BlockPos(Math.floor(entityPlayer.posX), Math.floor(entityPlayer.posY + 0.2), Math.floor(entityPlayer.posZ));
        return BurrowESP.mc.world.getBlockState(blockPos).getBlock() == Blocks.ENDER_CHEST || BurrowESP.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN || BurrowESP.mc.world.getBlockState(blockPos).getBlock() == Blocks.CHEST;
    }

    @SubscribeEvent
    public void onRender3d(RenderWorldLastEvent event) {
        if (!this.burrowedPlayers.isEmpty()) {
            this.burrowedPlayers.entrySet().forEach(this::renderblocklambda);
        }
    }
}

