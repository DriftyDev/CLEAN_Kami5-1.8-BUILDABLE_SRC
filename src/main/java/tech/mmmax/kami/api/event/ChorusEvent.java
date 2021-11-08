/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package tech.mmmax.kami.api.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ChorusEvent
extends Event {
    final EntityLivingBase entityLivingBase;
    double x;
    double y;
    double z;
    boolean successful;

    public ChorusEvent(EntityLivingBase entityLivingBase, double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.entityLivingBase = entityLivingBase;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public boolean isSuccessful() {
        return this.successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public EntityLivingBase getEntityLivingBase() {
        return this.entityLivingBase;
    }

    public boolean isCancelable() {
        return true;
    }
}

