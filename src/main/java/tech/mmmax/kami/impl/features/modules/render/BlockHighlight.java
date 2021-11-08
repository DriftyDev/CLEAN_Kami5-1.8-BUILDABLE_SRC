/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.world.World
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package tech.mmmax.kami.impl.features.modules.render;

import java.awt.Color;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.render.RenderUtil;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class BlockHighlight
extends Module {
    Value<String> mode = new ValueBuilder().withDescriptor("Mode").withValue("Normal").withModes("Normal", "Face", "Square").register(this);
    Value<Number> squareSize = new ValueBuilder().withDescriptor("Square Size").withValue(0.3).withRange(0.1, 1).register(this);
    Value<Color> fill = new ValueBuilder().withDescriptor("Fill").withValue(new Color(255, 62, 62, 25)).register(this);
    Value<Color> line = new ValueBuilder().withDescriptor("Line").withValue(new Color(255, 62, 62, 255)).register(this);
    Value<Boolean> entity = new ValueBuilder().withDescriptor("Entity").withValue(false).register(this);
    Value<Color> entityFill = new ValueBuilder().withDescriptor("Entity Fill").withValue(new Color(255, 62, 62, 25)).register(this);
    Value<Color> entityLine = new ValueBuilder().withDescriptor("Entity Line").withValue(new Color(255, 62, 62, 255)).register(this);

    public BlockHighlight() {
        super("BlockHighlight", Feature.Category.Render);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        RayTraceResult result = BlockHighlight.mc.objectMouseOver;
        if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
            IBlockState blockState = BlockHighlight.mc.world.getBlockState(result.getBlockPos());
            AxisAlignedBB bb = blockState.getSelectedBoundingBox((World)BlockHighlight.mc.world, result.getBlockPos());
            RenderUtil.renderBB(7, bb, this.fill.getValue(), this.fill.getValue());
            RenderUtil.renderBB(3, bb, this.line.getValue(), this.line.getValue());
        } else if (result.typeOfHit == RayTraceResult.Type.ENTITY && this.entity.getValue().booleanValue()) {
            AxisAlignedBB bb = result.entityHit.getRenderBoundingBox();
            RenderUtil.renderBB(7, bb, this.entityFill.getValue(), this.entityFill.getValue());
            RenderUtil.renderBB(3, bb, this.entityLine.getValue(), this.entityLine.getValue());
        }
    }
}

