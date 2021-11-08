/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.EntityViewRenderEvent$FOVModifier
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.render;

import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.event.PerspectiveEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class ViewModel
extends Module {
    Value<Number> aspectAmount = new ValueBuilder().withDescriptor("Aspect Amount").withValue(1).withRange(0, 3).register(this);
    Value<Boolean> aspect = new ValueBuilder().withDescriptor("Aspect").withValue(false).register(this);
    Value<Number> fov = new ValueBuilder().withDescriptor("FOV").withValue(130).withRange(60, 170).register(this);
    Value<String> fovMode = new ValueBuilder().withDescriptor("FOV Mode").withValue("Normal").withModes("Normal", "Item").register(this);
    Value<Number> leftX = new ValueBuilder().withDescriptor("Left X").withValue(0).withRange(-2, 2).register(this);
    Value<Number> leftY = new ValueBuilder().withDescriptor("Left Y").withValue(0).withRange(-2, 2).register(this);
    Value<Number> leftZ = new ValueBuilder().withDescriptor("Left Z").withValue(0).withRange(-2, 2).register(this);
    Value<Number> rightX = new ValueBuilder().withDescriptor("Right X").withValue(0).withRange(-2, 2).register(this);
    Value<Number> rightY = new ValueBuilder().withDescriptor("Right Y").withValue(0).withRange(-2, 2).register(this);
    Value<Number> rightZ = new ValueBuilder().withDescriptor("Right Z").withValue(0).withRange(-2, 2).register(this);
    Value<Number> leftAngle = new ValueBuilder().withDescriptor("Left Angle").withValue(0).withRange(-180, 180).register(this);
    Value<Number> leftRotateX = new ValueBuilder().withDescriptor("Left Rotate X").withValue(0).withRange(-1, 1).register(this);
    Value<Number> leftRotateY = new ValueBuilder().withDescriptor("Left Rotate Y").withValue(0).withRange(-1, 1).register(this);
    Value<Number> leftRotateZ = new ValueBuilder().withDescriptor("Left Rotate Z").withValue(0).withRange(-1, 1).register(this);
    Value<Number> rightAngle = new ValueBuilder().withDescriptor("Right Angle").withValue(0).withRange(-180, 180).register(this);
    Value<Number> rightRotateX = new ValueBuilder().withDescriptor("Right Rotate X").withValue(0).withRange(-1, 1).register(this);
    Value<Number> rightRotateY = new ValueBuilder().withDescriptor("Right Rotate Y").withValue(0).withRange(-1, 1).register(this);
    Value<Number> rightRotateZ = new ValueBuilder().withDescriptor("Right Rotate Z").withValue(0).withRange(-1, 1).register(this);
    Value<Number> leftScaleX = new ValueBuilder().withDescriptor("Left Scale X").withValue(0).withRange(-3, 3).register(this);
    Value<Number> leftScaleY = new ValueBuilder().withDescriptor("Left Scale Y").withValue(0).withRange(-3, 3).register(this);
    Value<Number> leftScaleZ = new ValueBuilder().withDescriptor("Left Scale Z").withValue(0).withRange(-3, 3).register(this);
    Value<Number> rightScaleX = new ValueBuilder().withDescriptor("Right Scale X").withValue(0).withRange(-3, 3).register(this);
    Value<Number> rightScaleY = new ValueBuilder().withDescriptor("Right Scale Y").withValue(0).withRange(-3, 3).register(this);
    Value<Number> rightScaleZ = new ValueBuilder().withDescriptor("Right Scale Z").withValue(0).withRange(-3, 3).register(this);

    public ViewModel() {
        super("ViewModel", Feature.Category.Render);

        this.aspectAmount.setActive(aspect.getValue());
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (this.fovMode.getValue().equals("Normal")) {
            ViewModel.mc.gameSettings.fovSetting = this.fov.getValue().intValue();
        }
    }

    @SubscribeEvent
    public void onItemFOV(EntityViewRenderEvent.FOVModifier event) {
        if (this.fovMode.getValue().equals("Item")) {
            event.setFOV((float)this.fov.getValue().intValue());
        }
    }

    @SubscribeEvent
    public void setPerspective(PerspectiveEvent event) {
        if (this.aspect.getValue().booleanValue()) {
            event.setAspect(this.aspectAmount.getValue().floatValue());
        }
    }
}

