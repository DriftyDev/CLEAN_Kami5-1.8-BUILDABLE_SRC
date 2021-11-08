/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  org.lwjgl.opengl.GL11
 */
package tech.mmmax.kami.impl.features.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.render.RenderUtil;
import tech.mmmax.kami.api.utils.world.HoleUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class HoleEsp
extends Module {
    Value<String> page = new ValueBuilder().withDescriptor("Page").withValue("Data").withModes("Data", "Colors", "Void").register(this);
    Value<String> glowMode = new ValueBuilder().withDescriptor("Glow Mode").withValue("Fade").withModes("Fade", "Fog", "None").withPageParent(this.page).withPage("Data").register(this);
    Value<Number> lineWidth = new ValueBuilder().withDescriptor("Line Width").withValue(Float.valueOf(2.0f)).withRange(0.1, 5).withPageParent(this.page).withPage("Data").register(this);
    Value<Number> height = new ValueBuilder().withDescriptor("Height").withValue(1).withRange(0, 2).withPageParent(this.page).withPage("Data").register(this);
    Value<Number> range = new ValueBuilder().withDescriptor("Range").withValue(5.0).withRange(1.0, 30.0).withPageParent(this.page).withPage("Data").register(this);
    Value<Boolean> doubles = new ValueBuilder().withDescriptor("Doubles").withValue(true).withPageParent(this.page).withPage("Data").register(this);
    Value<Color> bedrockFill = new ValueBuilder().withDescriptor("Bedrock Fill").withValue(new Color(0, 255, 0, 100)).withPageParent(this.page).withPage("Colors").register(this);
    Value<Color> bedrockLine = new ValueBuilder().withDescriptor("Bedrock Line").withValue(new Color(255, 255, 255, 255)).withPageParent(this.page).withPage("Colors").register(this);
    Value<Color> obbyFill = new ValueBuilder().withDescriptor("Obby Fill").withValue(new Color(0, 255, 218, 100)).withPageParent(this.page).withPage("Colors").register(this);
    Value<Color> obbyLine = new ValueBuilder().withDescriptor("Obby Line").withValue(new Color(255, 255, 255, 255)).withPageParent(this.page).withPage("Colors").register(this);
    Value<Color> doubleFill = new ValueBuilder().withDescriptor("Double Fill").withValue(new Color(255, 0, 11, 100)).withPageParent(this.page).withPage("Colors").register(this);
    Value<Color> doubleLine = new ValueBuilder().withDescriptor("Double Line").withValue(new Color(255, 255, 255, 255)).withPageParent(this.page).withPage("Colors").register(this);
    Value<Color> bedrockFill2 = new ValueBuilder().withDescriptor("Bedrock Fill2").withValue(new Color(0, 255, 0, 0)).withPageParent(this.page).withPage("Colors").register(this);
    Value<Color> bedrockLine2 = new ValueBuilder().withDescriptor("Bedrock Line2").withValue(new Color(255, 255, 255, 0)).withPageParent(this.page).withPage("Colors").register(this);
    Value<Color> obbyFill2 = new ValueBuilder().withDescriptor("Obby Fill2").withValue(new Color(0, 255, 218, 0)).withPageParent(this.page).withPage("Colors").register(this);
    Value<Color> obbyLine2 = new ValueBuilder().withDescriptor("Obby Line2").withValue(new Color(255, 255, 255, 0)).withPageParent(this.page).withPage("Colors").register(this);
    Value<Color> doubleFill2 = new ValueBuilder().withDescriptor("Double Fill2").withValue(new Color(255, 0, 11, 0)).withPageParent(this.page).withPage("Colors").register(this);
    Value<Color> doubleLine2 = new ValueBuilder().withDescriptor("Double Line2").withValue(new Color(255, 255, 255, 0)).withPageParent(this.page).withPage("Colors").register(this);
    Value<Boolean> wireFrame = new ValueBuilder().withDescriptor("Wire Frame").withValue(false).withPageParent(this.page).withPage("Colors").register(this);
    Value<Boolean> voidHoles = new ValueBuilder().withDescriptor("Void Holes").withValue(false).withPageParent(this.page).withPage("Void").register(this);
    Value<Color> voidSafeFill = new ValueBuilder().withDescriptor("Void Safe Fill").withValue(new Color(170, 0, 255, 25)).withPageParent(this.page).withPage("Void").register(this);
    Value<Color> voidSafeLine = new ValueBuilder().withDescriptor("Void Safe Line").withValue(new Color(170, 0, 255, 255)).withPageParent(this.page).withPage("Void").register(this);
    Value<Color> voidFill = new ValueBuilder().withDescriptor("Void Fill").withValue(new Color(255, 1, 242, 25)).withPageParent(this.page).withPage("Void").register(this);
    Value<Color> voidLine = new ValueBuilder().withDescriptor("Void Line").withValue(new Color(255, 0, 251, 255)).withPageParent(this.page).withPage("Void").register(this);
    Value<Number> voidHeight = new ValueBuilder().withDescriptor("Void Height").withValue(0.2).withRange(0.1, 2.0).withPageParent(this.page).withPage("Void").register(this);
    Value<Boolean> voidWireFrame = new ValueBuilder().withDescriptor("Wire Frame", "voidWireFrame").withValue(false).withPageParent(this.page).withPage("Void").register(this);
    ExecutorService service = Executors.newCachedThreadPool();
    volatile List<HoleUtils.Hole> holes = new ArrayList<HoleUtils.Hole>();
    volatile List<BlockPos> voidPositions = new ArrayList<BlockPos>();

    public HoleEsp() {
        super("Hole ESP", Feature.Category.Render);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        this.service.submit(() -> {
            this.holes = HoleUtils.getHoles(this.range.getValue().floatValue(), HoleEsp.mc.player.getPosition(), this.doubles.getValue());
            if (this.voidHoles.getValue().booleanValue()) {
                this.voidPositions = this.getVoidHoles();
            }
        });
    }

    @SubscribeEvent
    public void onRender3d(RenderWorldLastEvent event) {
        for (HoleUtils.Hole hole : this.holes) {
            Color outlineColor2 = null;
            Color outlineColor = null;
            Color fillColor2 = null;
            Color fillColor = null;
            GL11.glLineWidth((float)RenderUtil.getInterpolatedLinWid((float)HoleEsp.mc.player.getDistance((double)hole.pos1.getX(), (double)hole.pos1.getY(), (double)hole.pos1.getZ()), this.lineWidth.getValue().floatValue(), this.lineWidth.getValue().floatValue()));
            AxisAlignedBB holeBB = hole.doubleHole ? new AxisAlignedBB((double)hole.pos1.getX(), (double)hole.pos1.getY(), (double)hole.pos1.getZ(), (double)(hole.pos2.getX() + 1), (double)(hole.pos2.getY() + 1), (double)(hole.pos2.getZ() + 1)) : new AxisAlignedBB(hole.pos1);
            holeBB = new AxisAlignedBB(holeBB.minX, holeBB.minY, holeBB.minZ, holeBB.maxX, holeBB.minY + this.height.getValue().doubleValue(), holeBB.maxZ);
            Color color = hole.bedrock ? this.bedrockFill.getValue() : (fillColor = hole.doubleHole ? this.doubleFill.getValue() : this.obbyFill.getValue());
            Color color2 = hole.bedrock ? this.bedrockFill2.getValue() : (fillColor2 = hole.doubleHole ? this.doubleFill2.getValue() : this.obbyFill2.getValue());
            Color color3 = hole.bedrock ? this.bedrockLine.getValue() : (outlineColor = hole.doubleHole ? this.doubleLine.getValue() : this.obbyLine.getValue());
            Color color4 = hole.bedrock ? this.bedrockLine2.getValue() : (outlineColor2 = hole.doubleHole ? this.doubleLine2.getValue() : this.obbyLine2.getValue());
            if (this.glowMode.getValue().equals("Fade")) {
                RenderUtil.renderBB(7, holeBB, fillColor, fillColor2);
                if (this.wireFrame.getValue().booleanValue()) {
                    RenderUtil.renderBBWireFrame(3, holeBB, outlineColor, outlineColor2);
                    continue;
                }
                RenderUtil.renderBB(3, holeBB, outlineColor, outlineColor2);
                continue;
            }
            if (this.glowMode.getValue().equals("None")) {
                RenderUtil.renderBB(7, holeBB, fillColor, fillColor);
                if (this.wireFrame.getValue().booleanValue()) {
                    RenderUtil.renderBBWireFrame(3, holeBB, outlineColor, outlineColor);
                    continue;
                }
                RenderUtil.renderBB(3, holeBB, outlineColor, outlineColor);
                continue;
            }
            if (!this.glowMode.getValue().equals("Fog")) continue;
            RenderUtil.renderBBFog(holeBB, fillColor, fillColor2);
            if (this.wireFrame.getValue().booleanValue()) {
                RenderUtil.renderBBWireFrame(3, holeBB, outlineColor, outlineColor2);
                continue;
            }
            RenderUtil.renderBB(3, holeBB, outlineColor, outlineColor2);
        }
        if (this.voidHoles.getValue().booleanValue()) {
            for (BlockPos pos : this.voidPositions) {
                boolean safe = HoleEsp.mc.world.getBlockState(pos).getBlock() != Blocks.AIR;
                Color fill = safe ? this.voidSafeFill.getValue() : this.voidFill.getValue();
                Color line = safe ? this.voidSafeLine.getValue() : this.voidLine.getValue();
                AxisAlignedBB bb = new AxisAlignedBB(pos);
                bb = new AxisAlignedBB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.minY + this.voidHeight.getValue().doubleValue(), bb.maxZ);
                RenderUtil.renderBB(7, bb, fill, fill);
                if (this.wireFrame.getValue().booleanValue()) {
                    RenderUtil.renderBBWireFrame(3, bb, line, line);
                    continue;
                }
                RenderUtil.renderBB(3, bb, line, line);
            }
        }
    }

    public List<BlockPos> getVoidHoles() {
        ArrayList<BlockPos> voids = new ArrayList<BlockPos>();
        for (int x = -this.range.getValue().intValue(); x < this.range.getValue().intValue(); ++x) {
            for (int z = -this.range.getValue().intValue(); z < this.range.getValue().intValue(); ++z) {
                BlockPos pos = new BlockPos(HoleEsp.mc.player.posX + (double)x, 0.0, HoleEsp.mc.player.posZ + (double)z);
                if (HoleEsp.mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK) continue;
                voids.add(pos);
            }
        }
        return voids;
    }
}

