/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.combat;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.player.InventoryUtils;
import tech.mmmax.kami.api.utils.player.TargetUtils;
import tech.mmmax.kami.api.utils.world.BlockUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class Platformer
extends Module {
    Value<Number> blocksPerTick = new ValueBuilder().withDescriptor("Blocks Per Tick").withValue(10).withRange(1, 10).register(this);
    Value<String> mode = new ValueBuilder().withDescriptor("Mode").withValue("Forward").withModes("Forward", "Square").register(this);
    Value<Number> size = new ValueBuilder().withDescriptor("Size").withValue(2).withRange(1, 4).register(this);
    Value<Number> motionFactor = new ValueBuilder().withDescriptor("Motion Factor").withValue(2).withRange(1, 5).register(this);
    Value<Number> targetRange = new ValueBuilder().withDescriptor("Target Range").withValue(10).withRange(2, 15).register(this);
    Value<Number> range = new ValueBuilder().withDescriptor("Range").withValue(4).withRange(1, 9).register(this);
    Entity target;

    public Platformer() {
        super("Platformer", Feature.Category.Combat);
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        this.target = TargetUtils.getTarget(this.targetRange.getValue().doubleValue());
        if (this.target == null) {
            return;
        }
        boolean switched = false;
        int blocksInTick = 0;
        int oldSlot = Platformer.mc.player.inventory.currentItem;
        int blockSlot = InventoryUtils.getHotbarItemSlot(Item.getItemFromBlock((Block)Blocks.OBSIDIAN));
        if (blockSlot == -1) {
            return;
        }
        List<BlockPos> posList = this.getPositions(this.target);
        for (BlockPos pos : posList) {
            if (!BlockUtils.canPlaceBlock(pos)) continue;
            if (!switched) {
                InventoryUtils.switchToSlotGhost(blockSlot);
                switched = true;
            }
            BlockUtils.placeBlock(pos, true);
            if (++blocksInTick <= this.blocksPerTick.getValue().intValue()) continue;
            break;
        }
        if (switched) {
            InventoryUtils.switchToSlotGhost(oldSlot);
        }
    }

    List<BlockPos> getPositions(Entity target) {
        ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        if (this.mode.getValue().equals("Forward")) {
            for (int i = 1; i <= this.size.getValue().intValue(); ++i) {
                BlockPos pos = new BlockPos(target.posX + target.motionX * this.motionFactor.getValue().doubleValue() * (double)i, target.posY - 1.0, target.posZ + target.motionZ * this.motionFactor.getValue().doubleValue() * (double)i);
                if (!(Platformer.mc.player.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()) <= this.range.getValue().doubleValue())) continue;
                positions.add(pos);
            }
        }
        if (this.mode.getValue().equals("Square")) {
            for (int x = -this.size.getValue().intValue(); x < this.size.getValue().intValue(); ++x) {
                for (int z = -this.size.getValue().intValue(); z < this.size.getValue().intValue(); ++z) {
                    BlockPos pos = new BlockPos(target.posX + (double)x, target.posY - 1.0, target.posZ + (double)z);
                    if (!(Platformer.mc.player.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()) <= this.range.getValue().doubleValue())) continue;
                    positions.add(pos);
                }
            }
        }
        return positions;
    }

    @Override
    public String getHudInfo() {
        return this.mode.getValue();
    }
}

