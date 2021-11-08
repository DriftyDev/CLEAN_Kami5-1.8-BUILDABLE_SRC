/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.MathUtil;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.Timer;
import tech.mmmax.kami.api.utils.color.ColorUtil;
import tech.mmmax.kami.api.utils.player.InventoryUtils;
import tech.mmmax.kami.api.utils.player.RotationUtil;
import tech.mmmax.kami.api.utils.player.TargetUtils;
import tech.mmmax.kami.api.utils.render.RenderUtil;
import tech.mmmax.kami.api.utils.world.BlockUtils;
import tech.mmmax.kami.api.utils.world.HoleUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class HoleFill
extends Module {
    Value<String> switchMode = new ValueBuilder().withDescriptor("Switch Mode", "switchMode").withValue("Ghost").withModes("Normal", "Ghost", "Require").register(this);
    Value<Number> range = new ValueBuilder().withDescriptor("Range").withValue(5.0).withRange(1.0, 10.0).register(this);
    Value<Number> wallRange = new ValueBuilder().withDescriptor("Wall Range").withValue(3.0).withRange(1.0, 10.0).register(this);
    Value<Number> delay = new ValueBuilder().withDescriptor("Delay", "delay").withValue(1).withRange(0, 1000).register(this);
    Value<Number> blocksPerTick = new ValueBuilder().withDescriptor("Blocks Per Tick", "blocksPerTick").withValue(1).withRange(1, 10).register(this);
    Value<Boolean> disableAfter = new ValueBuilder().withDescriptor("Disable", "disable").withValue(true).register(this);
    Value<Boolean> rotate = new ValueBuilder().withDescriptor("Rotate", "rotate").withValue(true).register(this);
    Value<Boolean> doubles = new ValueBuilder().withDescriptor("Doubles").withValue(true).register(this);
    Value<Boolean> smart = new ValueBuilder().withDescriptor("Smart", "smart").withValue(false).register(this);
    Value<Number> smartTargetRange = new ValueBuilder().withDescriptor("Target Range", "targetRange").withValue(5.0).withRange(1.0, 10.0).register(this);
    Value<Number> smartBlockRange = new ValueBuilder().withDescriptor("Smart Block Range", "smartBlockRange").withValue(1.0).withRange(0.3, 4.0).register(this);
    Value<Boolean> noSelfFill = new ValueBuilder().withDescriptor("No Self Fill").withValue(false).register(this);
    Value<Number> selfDist = new ValueBuilder().withDescriptor("Self Dist").withValue(1).withRange(0, 3).register(this);
    Value<Boolean> renderHoles = new ValueBuilder().withDescriptor("Render").withValue(true).register(this);
    Value<Color> fill = new ValueBuilder().withDescriptor("Fill").withValue(new Color(255, 0, 0, 25)).register(this);
    Value<Color> line = new ValueBuilder().withDescriptor("Line").withValue(new Color(255, 0, 0, 255)).register(this);
    Value<Number> fadeTime = new ValueBuilder().withDescriptor("Fade Time").withValue(200).withRange(0, 1000).register(this);
    Timer timeSystem = new Timer();
    List<HoleUtils.Hole> holes = new ArrayList<HoleUtils.Hole>();
    Map<HoleUtils.Hole, Long> renderPositions = new HashMap<HoleUtils.Hole, Long>();
    Entity target;
    long currentTime;

    public HoleFill() {
        super("Hole Fill", Feature.Category.Combat);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.timeSystem.resetDelay();
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        this.currentTime = System.currentTimeMillis();
        this.target = TargetUtils.getTarget(this.smartTargetRange.getValue().doubleValue());
        this.timeSystem.setDelay(this.delay.getValue().longValue());
        int blocksPlaced = 0;
        if (this.timeSystem.isPassed()) {
            this.getHoles();
            if (this.holes == null || this.holes.size() == 0) {
                if (this.disableAfter.getValue().booleanValue()) {
                    this.setEnabled(false);
                }
                return;
            }
            if (this.switchMode.getValue().equalsIgnoreCase("Require") && HoleFill.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() != Item.getItemFromBlock((Block)Blocks.OBSIDIAN)) {
                return;
            }
            int oldSlot = HoleFill.mc.player.inventory.currentItem;
            int blockSlot = InventoryUtils.getHotbarItemSlot(Item.getItemFromBlock((Block)Blocks.OBSIDIAN));
            if (blockSlot == -1) {
                return;
            }
            boolean switched = false;
            for (HoleUtils.Hole hole : this.holes) {
                if (!switched) {
                    this.doSwitch(blockSlot);
                    switched = true;
                }
                this.doRotate(hole.pos1);
                if (hole.doubleHole) {
                    BlockUtils.placeBlock(hole.pos1, true);
                    BlockUtils.placeBlock(hole.pos2, true);
                } else {
                    BlockUtils.placeBlock(hole.pos1, true);
                }
                if (this.renderPositions.containsKey(hole) && this.currentTime - this.renderPositions.get(hole) > this.fadeTime.getValue().longValue()) {
                    this.renderPositions.put(hole, System.currentTimeMillis());
                }
                if (++blocksPlaced < this.blocksPerTick.getValue().intValue()) continue;
                break;
            }
            if (this.switchMode.getValue().equalsIgnoreCase("Ghost") && switched) {
                this.doSwitch(oldSlot);
            }
            this.timeSystem.resetDelay();
        } else {
            if (RotationUtil.INSTANCE.rotating) {
                RotationUtil.INSTANCE.resetRotations();
            }
            RotationUtil.INSTANCE.rotating = false;
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (!this.renderHoles.getValue().booleanValue()) {
            return;
        }
        for (Map.Entry<HoleUtils.Hole, Long> entry : this.renderPositions.entrySet()) {
            int fillAlpha = this.fill.getValue().getAlpha();
            int lineAlpha = this.line.getValue().getAlpha();
            long time = System.currentTimeMillis() - entry.getValue();
            double normal = MathUtil.normalize(time, 0.0, this.fadeTime.getValue().doubleValue());
            normal = MathHelper.clamp((double)normal, (double)0.0, (double)1.0);
            normal = -normal;
            fillAlpha = (int)((double)fillAlpha * (normal += 1.0));
            lineAlpha = (int)((double)lineAlpha * normal);
            Color fillColor = ColorUtil.newAlpha(this.fill.getValue(), fillAlpha);
            Color lineColor = ColorUtil.newAlpha(this.line.getValue(), lineAlpha);
            HoleUtils.Hole hole = entry.getKey();
            AxisAlignedBB bb = hole.doubleHole ? new AxisAlignedBB((double)hole.pos1.getX(), (double)hole.pos1.getY(), (double)hole.pos1.getZ(), (double)(hole.pos2.getX() + 1), (double)(hole.pos2.getY() + 1), (double)(hole.pos2.getZ() + 1)) : new AxisAlignedBB(hole.pos1);
            RenderUtil.renderBB(7, bb, fillColor, fillColor);
            RenderUtil.renderBB(3, bb, lineColor, lineColor);
        }
    }

    public void getHoles() {
        this.loadHoles();
    }

    public void loadHoles() {
        this.holes = HoleUtils.getHoles(this.range.getValue().doubleValue(), HoleFill.mc.player.getPosition(), this.doubles.getValue()).stream().filter(hole -> {
            boolean isAllowedHole = true;
            AxisAlignedBB bb = hole.doubleHole ? new AxisAlignedBB((double)hole.pos1.getX(), (double)hole.pos1.getY(), (double)hole.pos1.getZ(), (double)(hole.pos2.getX() + 1), (double)(hole.pos2.getY() + 1), (double)(hole.pos2.getZ() + 1)) : new AxisAlignedBB(hole.pos1);
            for (Entity e : HoleFill.mc.world.getEntitiesWithinAABB(Entity.class, bb)) {
                isAllowedHole = false;
            }
            return isAllowedHole;
        }).filter(hole -> {
            boolean isAllowedSmart = false;
            if (this.smart.getValue().booleanValue()) {
                if (this.target != null && this.target.getDistance((double)hole.pos1.getX() + 0.5, (double)(hole.pos1.getY() + 1), (double)hole.pos1.getZ() + 0.5) < this.smartBlockRange.getValue().doubleValue()) {
                    isAllowedSmart = true;
                }
            } else {
                isAllowedSmart = true;
            }
            return isAllowedSmart;
        }).filter(hole -> {
            BlockPos pos = hole.pos1.add(0, 1, 0);
            boolean raytrace = HoleFill.mc.world.rayTraceBlocks(BlockUtils.getEyesPos(), new Vec3d((Vec3i)pos)) != null;
            return !raytrace || HoleFill.mc.player.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()) <= this.wallRange.getValue().doubleValue();
        }).collect(Collectors.toList());
    }

    public void doSwitch(int i) {
        if (this.switchMode.getValue().equalsIgnoreCase("Normal")) {
            InventoryUtils.switchToSlot(i);
        }
        if (this.switchMode.getValue().equalsIgnoreCase("Ghost")) {
            InventoryUtils.switchToSlotGhost(i);
        }
    }

    public void doRotate(BlockPos pos) {
        if (this.rotate.getValue().booleanValue()) {
            if (!RotationUtil.INSTANCE.rotating) {
                RotationUtil.INSTANCE.rotating = true;
            }
            RotationUtil.INSTANCE.rotate(new Vec3d((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()));
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}

