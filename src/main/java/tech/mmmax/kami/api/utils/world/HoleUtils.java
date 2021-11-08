/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 */
package tech.mmmax.kami.api.utils.world;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import tech.mmmax.kami.api.utils.world.BlockUtils;
import tech.mmmax.kami.api.wrapper.IMinecraft;

public class HoleUtils
implements IMinecraft {
    public static BlockPos[] holeOffsets = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1), new BlockPos(0, -1, 0)};

    public static boolean isHole(BlockPos pos) {
        boolean isHole = false;
        int amount = 0;
        for (BlockPos p : holeOffsets) {
            if (HoleUtils.mc.world.getBlockState(pos.add((Vec3i)p)).getMaterial().isReplaceable()) continue;
            ++amount;
        }
        if (amount == 5) {
            isHole = true;
        }
        return isHole;
    }

    public static boolean isObbyHole(BlockPos pos) {
        boolean isHole = true;
        int bedrock = 0;
        for (BlockPos off : holeOffsets) {
            Block b = HoleUtils.mc.world.getBlockState(pos.add((Vec3i)off)).getBlock();
            if (!HoleUtils.isSafeBlock(pos.add((Vec3i)off))) {
                isHole = false;
                continue;
            }
            if (b != Blocks.OBSIDIAN && b != Blocks.ENDER_CHEST && b != Blocks.ANVIL) continue;
            ++bedrock;
        }
        if (HoleUtils.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock() != Blocks.AIR || HoleUtils.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock() != Blocks.AIR) {
            isHole = false;
        }
        if (bedrock < 1) {
            isHole = false;
        }
        return isHole;
    }

    public static boolean isBedrockHoles(BlockPos pos) {
        boolean isHole = true;
        for (BlockPos off : holeOffsets) {
            Block b = HoleUtils.mc.world.getBlockState(pos.add((Vec3i)off)).getBlock();
            if (b == Blocks.BEDROCK) continue;
            isHole = false;
        }
        if (HoleUtils.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock() != Blocks.AIR || HoleUtils.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock() != Blocks.AIR) {
            isHole = false;
        }
        return isHole;
    }

    public static Hole isDoubleHole(BlockPos pos) {
        if (HoleUtils.checkOffset(pos, 1, 0)) {
            return new Hole(false, true, pos, pos.add(1, 0, 0));
        }
        if (HoleUtils.checkOffset(pos, 0, 1)) {
            return new Hole(false, true, pos, pos.add(0, 0, 1));
        }
        return null;
    }

    public static boolean checkOffset(BlockPos pos, int offX, int offZ) {
        return HoleUtils.mc.world.getBlockState(pos).getBlock() == Blocks.AIR && HoleUtils.mc.world.getBlockState(pos.add(offX, 0, offZ)).getBlock() == Blocks.AIR && HoleUtils.isSafeBlock(pos.add(0, -1, 0)) && HoleUtils.isSafeBlock(pos.add(offX, -1, offZ)) && HoleUtils.isSafeBlock(pos.add(offX * 2, 0, offZ * 2)) && HoleUtils.isSafeBlock(pos.add(-offX, 0, -offZ)) && HoleUtils.isSafeBlock(pos.add(offZ, 0, offX)) && HoleUtils.isSafeBlock(pos.add(-offZ, 0, -offX)) && HoleUtils.isSafeBlock(pos.add(offX, 0, offZ).add(offZ, 0, offX)) && HoleUtils.isSafeBlock(pos.add(offX, 0, offZ).add(-offZ, 0, -offX));
    }

    static boolean isSafeBlock(BlockPos pos) {
        return HoleUtils.mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN || HoleUtils.mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK || HoleUtils.mc.world.getBlockState(pos).getBlock() == Blocks.ENDER_CHEST;
    }

    public static List<Hole> getHoles(double range, BlockPos playerPos, boolean doubles) {
        ArrayList<Hole> holes = new ArrayList<Hole>();
        List<BlockPos> circle = BlockUtils.getSphere(range, playerPos, true, false);
        for (BlockPos pos : circle) {
            Hole dh;
            if (HoleUtils.mc.world.getBlockState(pos).getBlock() != Blocks.AIR) continue;
            if (HoleUtils.isObbyHole(pos)) {
                holes.add(new Hole(false, false, pos));
                continue;
            }
            if (HoleUtils.isBedrockHoles(pos)) {
                holes.add(new Hole(true, false, pos));
                continue;
            }
            if (!doubles || (dh = HoleUtils.isDoubleHole(pos)) == null || HoleUtils.mc.world.getBlockState(dh.pos1.add(0, 1, 0)).getBlock() != Blocks.AIR && HoleUtils.mc.world.getBlockState(dh.pos2.add(0, 1, 0)).getBlock() != Blocks.AIR) continue;
            holes.add(dh);
        }
        return holes;
    }

    public static class Hole {
        public boolean bedrock;
        public boolean doubleHole;
        public BlockPos pos1;
        public BlockPos pos2;

        public Hole(boolean bedrock, boolean doubleHole, BlockPos pos1, BlockPos pos2) {
            this.bedrock = bedrock;
            this.doubleHole = doubleHole;
            this.pos1 = pos1;
            this.pos2 = pos2;
        }

        public Hole(boolean bedrock, boolean doubleHole, BlockPos pos1) {
            this.bedrock = bedrock;
            this.doubleHole = doubleHole;
            this.pos1 = pos1;
        }
    }
}

