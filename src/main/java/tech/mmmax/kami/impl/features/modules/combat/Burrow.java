/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketConfirmTeleport
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.combat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.event.PacketEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.chat.ChatMessage;
import tech.mmmax.kami.api.utils.chat.ChatUtils;
import tech.mmmax.kami.api.utils.player.InventoryUtils;
import tech.mmmax.kami.api.utils.world.BlockUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class Burrow
extends Module {
    Value<String> page = new ValueBuilder().withDescriptor("Page").withValue("Placements").withModes("Placements", "Offsets", "Render").register(this);
    Value<Number> offset = new ValueBuilder().withDescriptor("Offset").withValue(2).withRange(-5, 5).register(this);
    Value<Boolean> bypass = new ValueBuilder().withDescriptor("Bypass").withValue(false).register(this);
    Value<String> block = new ValueBuilder().withDescriptor("Block").withValue("EnderChest").withModes("EnderChest", "Obby", "Smart").register(this);
    Value<Boolean> dynamic = new ValueBuilder().withDescriptor("Dynamic").withValue(false).register(this);
    List<BlockPos> originalPos;
    boolean cancelling = false;
    Queue<CPacketConfirmTeleport> packets = new LinkedList<CPacketConfirmTeleport>();
    boolean placing = false;
    int tick = 0;
    double[] heights = new double[]{0.41999998688698, 0.7531999805212, 1.00133597911214, 1.16610926093821};

    public Burrow() {
        super("Burrow", Feature.Category.Combat);

        this.managePages(page.getValue());
    }

    public void managePages(String page) {
        this.offset.setActive(page.equals("Offsets"));
        this.bypass.setActive(page.equals("Offsets"));
        this.block.setActive(page.equals("Placements"));
        this.dynamic.setActive(page.equals("Placements"));
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.cancelling = false;
        if (NullUtils.nullCheck()) {
            return;
        }
        this.originalPos = new ArrayList<BlockPos>();
        if (this.dynamic.getValue().booleanValue()) {
            this.originalPos = this.getOverlapPos();
        } else {
            this.originalPos.add(this.getPlayerPos());
        }
        for (BlockPos pos : this.originalPos) {
            if (!Burrow.mc.world.getBlockState(new BlockPos(Burrow.mc.player.posX, Burrow.mc.player.posY, Burrow.mc.player.posZ)).getBlock().equals(Blocks.OBSIDIAN) && !this.intersectsWithEntity(pos)) continue;
            this.setEnabled(false);
        }
        this.packets.clear();
        this.tick = 0;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        int old;
        if (NullUtils.nullCheck()) {
            return;
        }
        if (InventoryUtils.getHotbarItemSlot(Item.getItemFromBlock((Block)this.getSelectedBlock())) == -1) {
            this.setEnabled(false);
            ChatUtils.sendMessage(new ChatMessage("Could not find block", false, 1));
            return;
        }
        if (!this.cancelling) {
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + this.heights[0], Burrow.mc.player.posZ, true));
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + this.heights[1], Burrow.mc.player.posZ, true));
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + this.heights[2], Burrow.mc.player.posZ, true));
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + this.heights[3], Burrow.mc.player.posZ, true));
            old = Burrow.mc.player.inventory.currentItem;
            ChatUtils.sendMessage(new ChatMessage("Placing burrow", true, 696969));
            if (!this.bypass.getValue().booleanValue()) {
                InventoryUtils.switchToSlotGhost(InventoryUtils.getHotbarItemSlot(Item.getItemFromBlock((Block)this.getSelectedBlock())));
                for (BlockPos pos : this.originalPos) {
                    BlockUtils.placeBlock(pos, true);
                }
                this.resetPos();
                InventoryUtils.switchToSlotGhost(old);
                this.setEnabled(false);
            } else {
                this.cancelling = true;
                this.resetPos();
            }
        }
        if (this.bypass.getValue().booleanValue() && this.placing) {
            old = Burrow.mc.player.inventory.currentItem;
            InventoryUtils.switchToSlotGhost(InventoryUtils.getHotbarItemSlot(Item.getItemFromBlock((Block)this.getSelectedBlock())));
            for (BlockPos pos : this.originalPos) {
                ChatUtils.sendMessage(new ChatMessage("Placing block", true, Integer.MAX_VALUE));
                BlockUtils.placeBlock(pos, true);
            }
            InventoryUtils.switchToSlotGhost(old);
            this.placing = false;
            while (!this.packets.isEmpty()) {
                mc.getConnection().sendPacket((Packet)this.packets.poll());
                if (!this.packets.isEmpty()) continue;
            }
            this.setEnabled(false);
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (this.bypass.getValue().booleanValue() && this.cancelling) {
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                this.placing = true;
            }
            if (event.getPacket() instanceof CPacketConfirmTeleport) {
                this.packets.add((CPacketConfirmTeleport)event.getPacket());
                event.setCanceled(true);
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public void resetPos() {
        Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + this.offset.getValue().doubleValue(), Burrow.mc.player.posZ, false));
    }

    Block getSelectedBlock() {
        String b = this.block.getValue();
        return b.equals("EnderChest") ? Blocks.ENDER_CHEST : Blocks.OBSIDIAN;
    }

    boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : Burrow.mc.world.loadedEntityList) {
            if (entity.equals((Object)Burrow.mc.player) || entity instanceof EntityItem || !new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) continue;
            return true;
        }
        return false;
    }

    BlockPos getPlayerPos() {
        double decimalPoint = Burrow.mc.player.posY - Math.floor(Burrow.mc.player.posY);
        return new BlockPos(Burrow.mc.player.posX, decimalPoint > 0.8 ? Math.floor(Burrow.mc.player.posY) + 1.0 : Math.floor(Burrow.mc.player.posY), Burrow.mc.player.posZ);
    }

    List<BlockPos> getOverlapPos() {
        ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        double decimalX = Burrow.mc.player.posX - Math.floor(Burrow.mc.player.posX);
        double decimalZ = Burrow.mc.player.posZ - Math.floor(Burrow.mc.player.posZ);
        int offX = this.calcOffset(decimalX);
        int offZ = this.calcOffset(decimalZ);
        positions.add(this.getPlayerPos());
        for (int x = 0; x <= Math.abs(offX); ++x) {
            for (int z = 0; z <= Math.abs(offZ); ++z) {
                int properX = x * offX;
                int properZ = z * offZ;
                positions.add(this.getPlayerPos().add(properX, 0, properZ));
            }
        }
        return positions;
    }

    int calcOffset(double dec) {
        return dec >= 0.7 ? 1 : (dec <= 0.3 ? -1 : 0);
    }
}

