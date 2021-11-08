/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.item.Item
 *  net.minecraft.network.play.server.SPacketSoundEffect
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.event.PacketEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.chat.ChatMessage;
import tech.mmmax.kami.api.utils.chat.ChatUtils;
import tech.mmmax.kami.api.utils.player.InventoryUtils;
import tech.mmmax.kami.api.utils.render.RenderUtil;
import tech.mmmax.kami.api.utils.world.BlockUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class ChorusAura
extends Module {
    Value<String> page = new ValueBuilder().withDescriptor("Page").withValue("Offsets").withModes("Offsets", "Ranges", "Render").register(this);
    Value<Boolean> feetBlock = new ValueBuilder().withDescriptor("Feet Block").withValue(false).withPageParent(this.page).withPage("Offsets").register(this);
    Value<Boolean> headBlock = new ValueBuilder().withDescriptor("Head Block").withValue(false).withPageParent(this.page).withPage("Offsets").register(this);
    Value<String> trapMode = new ValueBuilder().withDescriptor("Trap Mode").withValue("City").withModes("City", "None").withPageParent(this.page).withPage("Offsets").register(this);
    Value<Number> placeRange = new ValueBuilder().withDescriptor("Place Range").withValue(6).withRange(1, 10).withPageParent(this.page).withPage("Ranges").register(this);
    Value<Color> fillColor = new ValueBuilder().withDescriptor("Fill Color").withValue(new Color(255, 255, 255, 100)).withPageParent(this.page).withPage("Render").register(this);
    Value<Color> lineColor = new ValueBuilder().withDescriptor("Line Color").withValue(new Color(255, 255, 255, 255)).withPageParent(this.page).withPage("Render").register(this);
    Value<Number> renderTime = new ValueBuilder().withDescriptor("Render Time").withValue(1000).withRange(100, 5000).withPageParent(this.page).withPage("Render").register(this);
    List<BlockPos> offsets = new ArrayList<BlockPos>();
    List<BlockPos> renderOffsets = new ArrayList<BlockPos>();
    long startTime = 0L;
    BlockPos[] surroundOffsets = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0), new BlockPos(0, 0, -1)};

    public ChorusAura() {
        super("ChorusAura", Feature.Category.Combat);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.offsets.clear();
        this.renderOffsets.clear();
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        SPacketSoundEffect packet;
        if (event.getPacket() instanceof SPacketSoundEffect && ((packet = (SPacketSoundEffect)event.getPacket()).getSound().equals(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT) || packet.getSound().equals(SoundEvents.ENTITY_ENDERMEN_TELEPORT))) {
            this.offsets = this.getOffsets(new BlockPos(packet.getX(), packet.getY(), packet.getZ()));
            int slot = InventoryUtils.getHotbarItemSlot(Item.getItemFromBlock((Block)Blocks.OBSIDIAN));
            if (slot == -1) {
                return;
            }
            int oldSlot = ChorusAura.mc.player.inventory.currentItem;
            boolean switched = false;
            this.startTime = System.currentTimeMillis();
            int i = 0;
            for (BlockPos pos : this.offsets) {
                if (!(ChorusAura.mc.player.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()) <= this.placeRange.getValue().doubleValue())) continue;
                if (!switched) {
                    InventoryUtils.switchToSlotGhost(slot);
                    switched = true;
                }
                this.renderOffsets.add(pos);
                if (!BlockUtils.placeBlock(pos, true)) continue;
                ChatUtils.sendMessage(new ChatMessage("placed " + i++, false, 0));
            }
            if (switched) {
                InventoryUtils.switchToSlotGhost(oldSlot);
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (System.currentTimeMillis() - this.startTime > this.renderTime.getValue().longValue()) {
            this.renderOffsets.clear();
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        for (BlockPos pos : new ArrayList<BlockPos>(this.renderOffsets)) {
            AxisAlignedBB bb = new AxisAlignedBB(pos);
            RenderUtil.renderBB(7, bb, this.fillColor.getValue(), this.fillColor.getValue());
            RenderUtil.renderBB(3, bb, this.lineColor.getValue(), this.lineColor.getValue());
        }
    }

    List<BlockPos> getOffsets(BlockPos pos) {
        ArrayList<BlockPos> offsets = new ArrayList<BlockPos>();
        if (this.trapMode.getValue().equals("City")) {
            offsets.addAll(Arrays.asList(this.getCityOffsets(pos)));
        }
        if (this.feetBlock.getValue().booleanValue()) {
            offsets.add(pos);
        }
        if (this.headBlock.getValue().booleanValue()) {
            offsets.add(pos.add(0, 1, 0));
        }
        return offsets;
    }

    BlockPos[] getCityOffsets(BlockPos pos) {
        BlockPos p = this.getSurroundedBlock(pos.add(0, -1, 0));
        if (p == null) {
            return new BlockPos[0];
        }
        return new BlockPos[]{p.add(0, 1, 0), pos.add(1, 1, 0), pos.add(1, 1, 1), pos.add(0, 1, 1), pos.add(-1, 1, 1), pos.add(-1, 1, 0), pos.add(-1, 1, -1), pos.add(0, 1, -1), pos.add(1, 2, 0), pos.add(0, 2, 0)};
    }

    public BlockPos getSurroundedBlock(BlockPos feet) {
        for (BlockPos offset : this.surroundOffsets) {
            IBlockState blockState = ChorusAura.mc.world.getBlockState(feet.add((Vec3i)offset));
            if (blockState.getMaterial().isReplaceable()) continue;
            return feet.add((Vec3i)offset);
        }
        return null;
    }
}

