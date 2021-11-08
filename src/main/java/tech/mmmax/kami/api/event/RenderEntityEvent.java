/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.entity.Entity
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package tech.mmmax.kami.api.event;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderEntityEvent
extends Event {
    public ModelBase modelBase;
    public Entity entityIn;
    public float limbSwing;
    public float limbSwingAmount;
    public float ageInTicks;
    public float netHeadYaw;
    public float headPitch;
    public float scale;

    public RenderEntityEvent(ModelBase modelBase, Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.modelBase = modelBase;
        this.entityIn = entityIn;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.ageInTicks = ageInTicks;
        this.netHeadYaw = netHeadYaw;
        this.headPitch = headPitch;
        this.scale = scale;
    }

    public boolean isCancelable() {
        return true;
    }
}

