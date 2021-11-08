/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumActionResult
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.api.utils.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.wrapper.IMinecraft;

public class BlockUtils
implements IMinecraft {
    static List<BlockPos> tickCache = new ArrayList<BlockPos>();

    public BlockUtils() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        tickCache = new ArrayList<BlockPos>();
    }

    public static boolean placeBlock(BlockPos pos, boolean sneak) {
        Block block = BlockUtils.mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        }
        EnumFacing side = BlockUtils.getPlaceableSide(pos);
        if (side == null) {
            return false;
        }
        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        if (!BlockUtils.mc.player.isSneaking()) {
            mc.getConnection().sendPacket((Packet)new CPacketEntityAction((Entity)BlockUtils.mc.player, CPacketEntityAction.Action.START_SNEAKING));
        }
        EnumActionResult action = BlockUtils.mc.playerController.processRightClickBlock(BlockUtils.mc.player, BlockUtils.mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        BlockUtils.mc.player.swingArm(EnumHand.MAIN_HAND);
        mc.getConnection().sendPacket((Packet)new CPacketEntityAction((Entity)BlockUtils.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        tickCache.add(pos);
        return action == EnumActionResult.SUCCESS;
    }

    public static boolean placeBlock(BlockPos pos, boolean sneak, EnumHand hand) {
        Block block = BlockUtils.mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        }
        EnumFacing side = BlockUtils.getPlaceableSide(pos);
        if (side == null) {
            return false;
        }
        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        if (!BlockUtils.mc.player.isSneaking()) {
            mc.getConnection().sendPacket((Packet)new CPacketEntityAction((Entity)BlockUtils.mc.player, CPacketEntityAction.Action.START_SNEAKING));
        }
        EnumActionResult action = BlockUtils.mc.playerController.processRightClickBlock(BlockUtils.mc.player, BlockUtils.mc.world, neighbour, opposite, hitVec, hand);
        BlockUtils.mc.player.swingArm(hand);
        mc.getConnection().sendPacket((Packet)new CPacketEntityAction((Entity)BlockUtils.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        tickCache.add(pos);
        return action == EnumActionResult.SUCCESS;
    }

    public static void breakBlock(BlockPos p) {
        Block block = BlockUtils.mc.world.getBlockState(p).getBlock();
        EnumFacing side = BlockUtils.getPlaceableSide(p);
        BlockUtils.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, p, side));
        BlockUtils.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, p, side));
    }

    public static EnumFacing getPlaceableSide(BlockPos pos) {
        for (EnumFacing side : EnumFacing.values()) {
            IBlockState blockState;
            BlockPos neighbour = pos.offset(side);
            if (!BlockUtils.mc.world.getBlockState(neighbour).getBlock().canCollideCheck(BlockUtils.mc.world.getBlockState(neighbour), false) && tickCache.contains(neighbour) || (blockState = BlockUtils.mc.world.getBlockState(neighbour)).getMaterial().isReplaceable()) continue;
            return side;
        }
        return null;
    }

    public static EnumFacing getPlaceableSideH(BlockPos pos) {
        for (EnumFacing side : EnumFacing.HORIZONTALS) {
            IBlockState blockState;
            BlockPos neighbour = pos.offset(side);
            if (!BlockUtils.mc.world.getBlockState(neighbour).getBlock().canCollideCheck(BlockUtils.mc.world.getBlockState(neighbour), false) || (blockState = BlockUtils.mc.world.getBlockState(neighbour)).getMaterial().isReplaceable()) continue;
            return side;
        }
        return null;
    }

    public static float[] getNeededRotations(Vec3d vec) {
        Vec3d eyesPos = BlockUtils.getEyesPos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{BlockUtils.mc.player.rotationYaw + MathHelper.wrapDegrees((float)(yaw - BlockUtils.mc.player.rotationYaw)), BlockUtils.mc.player.rotationPitch + MathHelper.wrapDegrees((float)(pitch - BlockUtils.mc.player.rotationPitch))};
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(BlockUtils.mc.player.posX, BlockUtils.mc.player.posY + (double)BlockUtils.mc.player.getEyeHeight(), BlockUtils.mc.player.posZ);
    }

    public static List<BlockPos> getSphere(double range, BlockPos pos, boolean sphere, boolean hollow) {
        ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        int cx = pos.getX();
        int cy = pos.getY();
        int cz = pos.getZ();
        int x = cx - (int)range;
        while ((double)x <= (double)cx + range) {
            int z = cz - (int)range;
            while ((double)z <= (double)cz + range) {
                int y = sphere ? cy - (int)range : cy;
                while (true) {
                    double d = y;
                    double d2 = sphere ? (double)cy + range : (double)cy + range;
                    if (!(d < d2)) break;
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (!(!(dist < range * range) || hollow && dist < (range - 1.0) * (range - 1.0))) {
                        BlockPos l = new BlockPos(x, y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return circleblocks;
    }

    public static boolean canPlaceBlock(BlockPos pos) {
        boolean allow;
        block1: {
            Iterator iterator;
            allow = true;
            if (!BlockUtils.mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
                allow = false;
            }
            if (!(iterator = BlockUtils.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos)).iterator()).hasNext()) break block1;
            Entity entity = (Entity)iterator.next();
            allow = false;
        }
        return allow;
    }

    static {
        new BlockUtils();
    }
}

