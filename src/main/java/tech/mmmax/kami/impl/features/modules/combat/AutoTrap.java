/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityArmorStand
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.combat;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.Timer;
import tech.mmmax.kami.api.utils.player.InventoryUtils;
import tech.mmmax.kami.api.utils.player.TargetUtils;
import tech.mmmax.kami.api.utils.world.BlockUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class AutoTrap
extends Module {
    Timer timer = new Timer();
    Value<Number> delay = new ValueBuilder().withDescriptor("Delay").withValue(0).withRange(0, 1000).register(this);
    Value<String> trapMode = new ValueBuilder().withDescriptor("Mode").withValue("Top").withModes("Full", "City", "Top", "None").register(this);
    Value<Number> blocksPerTick = new ValueBuilder().withDescriptor("BPT").withValue(10).withRange(0, 10).register(this);
    Value<Number> targetRange = new ValueBuilder().withDescriptor("Target Range").withValue(5.0).withRange(1.0, 10.0).register(this);
    Value<Boolean> disable = new ValueBuilder().withDescriptor("Disable").withValue(false).register(this);
    Entity target;
    public BlockPos[] fullOffsets = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(1, 1, 0), new BlockPos(0, 1, 1), new BlockPos(-1, 1, 0), new BlockPos(0, 1, -1), new BlockPos(1, 2, 0), new BlockPos(0, 2, 0)};
    public BlockPos[] cityOffsets = new BlockPos[]{new BlockPos(1, 1, 0), new BlockPos(1, 1, 1), new BlockPos(0, 1, 1), new BlockPos(-1, 1, 1), new BlockPos(-1, 1, 0), new BlockPos(-1, 1, -1), new BlockPos(0, 1, -1), new BlockPos(-1, 1, -1), new BlockPos(1, 2, 0), new BlockPos(0, 2, 0)};
    BlockPos[] surroundOffsets = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 0)};

    public AutoTrap() {
        super("Auto Trap", Feature.Category.Combat);

        this.timer.setDelay((delay.getValue()).intValue());
    }

    BlockPos getPlayerPos(Entity player) {
        double decimalPoint = player.posY - Math.floor(player.posY);
        return new BlockPos(player.posX, decimalPoint > 0.8 ? Math.floor(player.posY) + 1.0 : Math.floor(player.posY), player.posZ);
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (this.timer.isPassed()) {
            this.target = TargetUtils.getTarget(this.targetRange.getValue().doubleValue());
            if (this.target == null) {
                return;
            }
            if (AutoTrap.mc.world.getBlockState(this.getPlayerPos(this.target).add(0, 2, 0)).getMaterial().isSolid() && this.disable.getValue().booleanValue()) {
                this.setEnabled(false);
                return;
            }
            int blocksInTick = 0;
            boolean switched = false;
            int oldSlot = AutoTrap.mc.player.inventory.currentItem;
            int slot = InventoryUtils.getHotbarItemSlot(Item.getItemFromBlock((Block)Blocks.OBSIDIAN));
            if (slot == -1) {
                return;
            }
            if (!this.trapMode.getValue().equalsIgnoreCase("None")) {
                BlockPos[] offsets;
                for (BlockPos pos : offsets = this.trapMode.getValue().equalsIgnoreCase("Full") ? this.offsetBlocks(this.fullOffsets, this.getPlayerPos(this.target)) : (this.trapMode.getValue().equalsIgnoreCase("City") ? this.offsetBlocks(this.cityOffsets, this.getPlayerPos(this.target)) : this.getObbyToHead(this.getPlayerPos(this.target)))) {
                    if (!this.canPlaceBlock(pos)) continue;
                    if (!switched) {
                        InventoryUtils.switchToSlotGhost(slot);
                        switched = true;
                    }
                    BlockUtils.placeBlock(pos, true);
                    if (++blocksInTick > this.blocksPerTick.getValue().intValue()) break;
                }
                InventoryUtils.switchToSlotGhost(oldSlot);
                this.timer.resetDelay();
            }
        }
    }

    boolean canPlaceBlock(BlockPos pos) {
        boolean allow = true;
        if (!AutoTrap.mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
            allow = false;
        }
        for (Entity entity : AutoTrap.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos))) {
            if (entity instanceof EntityArmorStand) continue;
            allow = false;
            break;
        }
        return allow;
    }

    BlockPos[] offsetBlocks(BlockPos[] toOffset, BlockPos offsetPlace) {
        BlockPos[] offsets = new BlockPos[toOffset.length];
        int index = 0;
        for (BlockPos blockPos : toOffset) {
            offsets[index] = offsetPlace.add((Vec3i)blockPos);
            ++index;
        }
        return offsets;
    }

    public BlockPos[] getObbyToHead(BlockPos feet) {
        ArrayList<BlockPos> obbyToHead = new ArrayList<BlockPos>();
        BlockPos head = feet.add((Vec3i)new BlockPos(0, 1, 0));
        if (this.getSurroundedBlock(head) != null) {
            obbyToHead.add(this.getSurroundedBlock(head).add((Vec3i)new BlockPos(0, 1, 0)));
            obbyToHead.add(head.add((Vec3i)new BlockPos(0, 1, 0)));
        } else if (this.getSurroundedBlock(feet) != null) {
            obbyToHead.add(this.getSurroundedBlock(feet).add((Vec3i)new BlockPos(0, 1, 0)));
        } else if (this.getSurroundedBlock(feet.add(0, -1, 0)) != null) {
            obbyToHead.add(this.getSurroundedBlock(feet.add((Vec3i)new BlockPos(0, -1, 0))).add((Vec3i)new BlockPos(0, 1, 0)));
        }
        BlockPos[] blocks = new BlockPos[obbyToHead.size()];
        return obbyToHead.toArray(blocks);
    }

    public BlockPos getSurroundedBlock(BlockPos feet) {
        for (BlockPos offset : this.surroundOffsets) {
            IBlockState blockState = AutoTrap.mc.world.getBlockState(feet.add((Vec3i)offset));
            if (blockState.getMaterial().isReplaceable()) continue;
            return feet.add((Vec3i)offset);
        }
        return null;
    }
}

