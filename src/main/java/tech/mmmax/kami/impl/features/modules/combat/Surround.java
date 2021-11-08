/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.server.SPacketBlockChange
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.event.PacketEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.Timer;
import tech.mmmax.kami.api.utils.player.InventoryUtils;
import tech.mmmax.kami.api.utils.render.RenderUtil;
import tech.mmmax.kami.api.utils.world.BlockUtils;
import tech.mmmax.kami.api.utils.world.CrystalUtil;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class Surround
extends Module {
    Timer timer = new Timer();
    Value<String> page = new ValueBuilder().withDescriptor("Page").withValue("Delays").withModes("Delays", "Placements", "Render", "Break").register(this);
    Value<Number> delay = new ValueBuilder().withDescriptor("Delay").withValue(0).withRange(0, 1000).register(this);
    Value<Number> blocksPerTick = new ValueBuilder().withDescriptor("BPT").withValue(20).withRange(1, 50).register(this);
    Value<Number> retryAmount = new ValueBuilder().withDescriptor("Retry Amount").withValue(20).withRange(1, 50).register(this);
    Value<Boolean> multiThread = new ValueBuilder().withDescriptor("Threading").withValue(false).register(this);
    Value<String> mode = new ValueBuilder().withDescriptor("Mode").withValue("Dynamic").withModes("Dynamic", "Normal", "Calc").register(this);
    Value<Number> allowHealth = new ValueBuilder().withDescriptor("Allow Health").withValue(5).withRange(0, 36).register(this);
    Value<Number> calcRange = new ValueBuilder().withDescriptor("Calc Range").withValue(3).withRange(0, 10).register(this);
    Value<Number> wallRange = new ValueBuilder().withDescriptor("Wall Range").withValue(3).withRange(0, 10).register(this);
    Value<Number> raytraceHits = new ValueBuilder().withDescriptor("Raytrace Hits").withValue(2).withRange(1, 9).register(this);
    Value<Number> shrinkFactor = new ValueBuilder().withDescriptor("Shrink Factor").withValue(0.3).withRange(0d, 1d).register(this);
    Value<Boolean> oneThirteen = new ValueBuilder().withDescriptor("1.13", "oneThirteen").withValue(false).register(this);
    Value<Boolean> antiPhase = new ValueBuilder().withDescriptor("Clip Extend").withValue(true).register(this);
    Value<Number> clipTries = new ValueBuilder().withDescriptor("Clip Tries").withValue(2).withRange(1, 10).register(this);
    Value<Boolean> predict = new ValueBuilder().withDescriptor("Predict").withValue(false).register(this);
    Value<Boolean> center = new ValueBuilder().withDescriptor("Center").withValue(false).register(this);
    Value<Boolean> centerBounds = new ValueBuilder().withDescriptor("Center Bounds").withValue(false).register(this);
    Value<Boolean> jumpDisable = new ValueBuilder().withDescriptor("Jump Disable").withValue(true).register(this);
    Value<Color> activeFillColor = new ValueBuilder().withDescriptor("Active Fill Color").withValue(new Color(0, 200, 12, 20)).register(this);
    Value<Color> activeLineColor = new ValueBuilder().withDescriptor("Active Line Color").withValue(new Color(0, 200, 12, 255)).register(this);
    double startY = 0.0;
    List<BlockPos> activeBlocks = new ArrayList<BlockPos>();
    boolean shouldPredict = false;
    List<BlockPos> offsets = new ArrayList<BlockPos>();

    public Surround() {
        super("Surround", Feature.Category.Combat);

        this.timer.setDelay((delay.getValue()).longValue());

        this.handlePage(page.getValue());
    }

    void handlePage(String page) {
        this.delay.setActive(page.equals("Delays"));
        this.blocksPerTick.setActive(page.equals("Delays"));
        this.retryAmount.setActive(page.equals("Delays"));
        this.multiThread.setActive(page.equals("Placements"));
        this.mode.setActive(page.equals("Placements"));
        this.allowHealth.setActive(page.equals("Placements") && this.mode.getValue().equals("Calc"));
        this.calcRange.setActive(page.equals("Placements") && this.mode.getValue().equals("Calc"));
        this.oneThirteen.setActive(page.equals("Placements") && this.mode.getValue().equals("Calc"));
        this.wallRange.setActive(page.equals("Placements") && this.mode.getValue().equals("Calc"));
        this.raytraceHits.setActive(page.equals("Placements") && this.mode.getValue().equals("Calc"));
        this.shrinkFactor.setActive(page.equals("Placements") && this.mode.getValue().equals("Calc"));
        this.antiPhase.setActive(page.equals("Placements"));
        this.clipTries.setActive(page.equals("Placements"));
        this.predict.setActive(page.equals("Placements"));
        this.center.setActive(page.equals("Placements"));
        this.centerBounds.setActive(page.equals("Placements"));
        this.jumpDisable.setActive(page.equals("Placements"));
        this.activeFillColor.setActive(page.equals("Render"));
        this.activeLineColor.setActive(page.equals("Render"));
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        this.shouldPredict = true;
        if (!(!this.jumpDisable.getValue().booleanValue() || Surround.mc.player.isInLava() || Surround.mc.player.isInWater() || Surround.mc.player.onGround && Surround.mc.player.posY == this.startY)) {
            this.setEnabled(false);
            return;
        }
        if (this.timer.isPassed()) {
            this.activeBlocks.clear();
            boolean switched = false;
            int oldSlot = Surround.mc.player.inventory.currentItem;
            int blockSlot = this.getSlot();
            if (blockSlot == -1) {
                this.setEnabled(false);
                return;
            }
            new Thread(() -> {
                this.offsets = this.getOffsets();
            }).start();
            int blocksInTick = 0;
            block0: for (int i = 0; i < this.retryAmount.getValue().intValue(); ++i) {
                if (!this.multiThread.getValue().booleanValue()) {
                    this.offsets = this.getOffsets();
                }
                for (BlockPos pos : this.offsets) {
                    if (blocksInTick > this.blocksPerTick.getValue().intValue()) continue block0;
                    if (!this.canPlaceBlock(pos)) continue;
                    this.activeBlocks.add(pos);
                    if (!switched) {
                        InventoryUtils.switchToSlotGhost(blockSlot);
                        switched = true;
                    }
                    BlockUtils.placeBlock(pos, true);
                    ++blocksInTick;
                }
            }
            if (switched) {
                InventoryUtils.switchToSlotGhost(oldSlot);
            }
            this.timer.resetDelay();
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof SPacketBlockChange && this.predict.getValue().booleanValue()) {
            SPacketBlockChange packet = (SPacketBlockChange)event.getPacket();
            for (BlockPos pos : this.getOffsets()) {
                if (!this.shouldPredict || !pos.equals((Object)packet.getBlockPosition()) || packet.getBlockState().getBlock() != Blocks.AIR) continue;
                int oldSlot = Surround.mc.player.inventory.currentItem;
                int blockSlot = this.getSlot();
                if (blockSlot == -1) {
                    return;
                }
                InventoryUtils.switchToSlotGhost(blockSlot);
                BlockUtils.placeBlock(pos, true);
                InventoryUtils.switchToSlotGhost(oldSlot);
                this.shouldPredict = false;
                break;
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        for (BlockPos pos : this.activeBlocks) {
            RenderUtil.renderBB(7, new AxisAlignedBB(pos), this.activeFillColor.getValue(), this.activeFillColor.getValue());
            RenderUtil.renderBB(3, new AxisAlignedBB(pos), this.activeLineColor.getValue(), this.activeLineColor.getValue());
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (NullUtils.nullCheck()) {
            return;
        }
        if (this.center.getValue().booleanValue()) {
            double x = (double)Surround.mc.player.getPosition().getX() + 0.5;
            double z = (double)Surround.mc.player.getPosition().getZ() + 0.5;
            mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(x, Surround.mc.player.posY, z, Surround.mc.player.onGround));
            if (this.centerBounds.getValue().booleanValue()) {
                mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(x, 1337.0, z, Surround.mc.player.onGround));
            }
            Surround.mc.player.setPosition(x, Surround.mc.player.posY, z);
        }
        this.startY = Surround.mc.player.posY;
    }

    int getSlot() {
        int slot = -1;
        slot = InventoryUtils.getHotbarItemSlot(Item.getItemFromBlock((Block)Blocks.OBSIDIAN));
        if (slot == -1) {
            slot = InventoryUtils.getHotbarItemSlot(Item.getItemFromBlock((Block)Blocks.ENDER_CHEST));
        }
        return slot;
    }

    boolean canPlaceBlock(BlockPos pos) {
        boolean allow = true;
        if (!Surround.mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
            allow = false;
        }
        for (Entity entity : Surround.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityPlayer)) continue;
            allow = false;
            break;
        }
        return allow;
    }

    List<BlockPos> getOffsets() {
        BlockPos playerPos = this.getPlayerPos();
        ArrayList<BlockPos> offsets = new ArrayList<BlockPos>();
        switch (this.mode.getValue()) {
            case "Dynamic": {
                int z;
                int x;
                double decimalX = Math.abs(Surround.mc.player.posX) - Math.floor(Math.abs(Surround.mc.player.posX));
                double decimalZ = Math.abs(Surround.mc.player.posZ) - Math.floor(Math.abs(Surround.mc.player.posZ));
                int offX = this.calcOffset(decimalX);
                int offZ = this.calcOffset(decimalZ);
                int lengthXPos = this.calcLength(decimalX, false);
                int lengthXNeg = this.calcLength(decimalX, true);
                int lengthZPos = this.calcLength(decimalZ, false);
                int lengthZNeg = this.calcLength(decimalZ, true);
                ArrayList<BlockPos> tempOffsets = new ArrayList<BlockPos>();
                offsets.addAll(this.getOverlapPos());
                for (x = 1; x < lengthXPos + 1; ++x) {
                    tempOffsets.add(this.addToPlayer(playerPos, x, 0.0, 1 + lengthZPos));
                    tempOffsets.add(this.addToPlayer(playerPos, x, 0.0, -(1 + lengthZNeg)));
                }
                for (x = 0; x <= lengthXNeg; ++x) {
                    tempOffsets.add(this.addToPlayer(playerPos, -x, 0.0, 1 + lengthZPos));
                    tempOffsets.add(this.addToPlayer(playerPos, -x, 0.0, -(1 + lengthZNeg)));
                }
                for (z = 1; z < lengthZPos + 1; ++z) {
                    tempOffsets.add(this.addToPlayer(playerPos, 1 + lengthXPos, 0.0, z));
                    tempOffsets.add(this.addToPlayer(playerPos, -(1 + lengthXNeg), 0.0, z));
                }
                for (z = 0; z <= lengthZNeg; ++z) {
                    tempOffsets.add(this.addToPlayer(playerPos, 1 + lengthXPos, 0.0, -z));
                    tempOffsets.add(this.addToPlayer(playerPos, -(1 + lengthXNeg), 0.0, -z));
                }
                for (BlockPos pos2 : tempOffsets) {
                    if (!this.hasSurroundingBlock(pos2)) {
                        offsets.add(pos2.add(0, -1, 0));
                    }
                    offsets.add(pos2);
                }
                break;
            }
            case "Calc": {
                List<BlockPos> positions = CrystalUtil.getAvailablePositions(this.calcRange.getValue().doubleValue(), this.wallRange.getValue().doubleValue(), this.oneThirteen.getValue(), false, this.raytraceHits.getValue().intValue(), this.shrinkFactor.getValue().doubleValue());
                positions.sort(Comparator.comparingDouble(pos -> Surround.mc.player.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ())));
                for (BlockPos pos3 : positions) {
                    double damage = CrystalUtil.calculateDamage((double)pos3.getX() + 0.5, pos3.getY() + 1, (double)pos3.getZ() + 0.5, (Entity)Surround.mc.player, 0.0);
                    if (!(damage > this.allowHealth.getValue().doubleValue())) continue;
                    offsets.add(pos3.add(0, 1, 0));
                }
                break;
            }
            case "Normal": {
                offsets.add(playerPos.add(0, -1, 0));
                for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                    if (!this.hasSurroundingBlock(playerPos.add(facing.getXOffset(), 0, facing.getZOffset()))) {
                        offsets.add(playerPos.add(facing.getXOffset(), -1, facing.getZOffset()));
                    }
                    offsets.add(playerPos.add(facing.getXOffset(), 0, facing.getZOffset()));
                }
                break;
            }
        }
        return offsets;
    }

    boolean hasSurroundingBlock(BlockPos pos) {
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (Surround.mc.world.getBlockState(pos.offset(facing)).getBlock() == Blocks.AIR) continue;
            return true;
        }
        return false;
    }

    BlockPos addToPlayer(BlockPos playerPos, double x, double y, double z) {
        if (playerPos.getX() < 0) {
            x = -x;
        }
        if (playerPos.getY() < 0) {
            y = -y;
        }
        if (playerPos.getZ() < 0) {
            z = -z;
        }
        return playerPos.add(x, y, z);
    }

    int calcLength(double decimal, boolean negative) {
        if (negative) {
            return decimal <= 0.3 ? 1 : 0;
        }
        return decimal >= 0.7 ? 1 : 0;
    }

    boolean isOverlapping(int offsetX, int offsetZ) {
        boolean overlapping = false;
        double decimalX = Surround.mc.player.posX - Math.floor(Surround.mc.player.posX);
        decimalX = Math.abs(decimalX);
        double decimalZ = Surround.mc.player.posZ - Math.floor(Surround.mc.player.posZ);
        decimalZ = Math.abs(decimalZ);
        if (offsetX > 0 && decimalX > 0.7) {
            overlapping = true;
        }
        if (offsetX < 0 && decimalX < 0.3) {
            overlapping = true;
        }
        if (offsetZ > 0 && decimalZ >= 0.7) {
            overlapping = true;
        }
        if (offsetZ < 0 && decimalZ < 0.3) {
            overlapping = true;
        }
        return overlapping;
    }

    List<BlockPos> getOverlapPos() {
        ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        double decimalX = Surround.mc.player.posX - Math.floor(Surround.mc.player.posX);
        double decimalZ = Surround.mc.player.posZ - Math.floor(Surround.mc.player.posZ);
        int offX = this.calcOffset(decimalX);
        int offZ = this.calcOffset(decimalZ);
        positions.add(this.getPlayerPos());
        for (int x = 0; x <= Math.abs(offX); ++x) {
            for (int z = 0; z <= Math.abs(offZ); ++z) {
                int properX = x * offX;
                int properZ = z * offZ;
                positions.add(this.getPlayerPos().add(properX, -1, properZ));
            }
        }
        return positions;
    }

    int calcOffset(double dec) {
        return dec >= 0.7 ? 1 : (dec <= 0.3 ? -1 : 0);
    }

    BlockPos getPlayerPos() {
        double decimalPoint = Surround.mc.player.posY - Math.floor(Surround.mc.player.posY);
        return new BlockPos(Surround.mc.player.posX, decimalPoint > 0.8 ? Math.floor(Surround.mc.player.posY) + 1.0 : Math.floor(Surround.mc.player.posY), Surround.mc.player.posZ);
    }

    boolean checkForEntities(BlockPos pos) {
        Iterator iterator = Surround.mc.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos)).iterator();
        if (iterator.hasNext()) {
            Entity e = (Entity)iterator.next();
            return false;
        }
        return true;
    }
}

