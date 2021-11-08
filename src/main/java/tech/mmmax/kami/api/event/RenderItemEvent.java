/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package tech.mmmax.kami.api.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderItemEvent
extends Event {
    double mainX;
    double mainY;
    double mainZ;
    double offX;
    double offY;
    double offZ;
    double mainRAngel;
    double mainRx;
    double mainRy;
    double mainRz;
    double offRAngel;
    double offRx;
    double offRy;
    double offRz;
    double mainHandScaleX;
    double mainHandScaleY;
    double mainHandScaleZ;
    double offHandScaleX;
    double offHandScaleY;
    double offHandScaleZ;

    public RenderItemEvent(double mainX, double mainY, double mainZ, double offX, double offY, double offZ, double mainRAngel, double mainRx, double mainRy, double mainRz, double offRAngel, double offRx, double offRy, double offRz, double mainHandScaleX, double mainHandScaleY, double mainHandScaleZ, double offHandScaleX, double offHandScaleY, double offHandScaleZ) {
        this.mainX = mainX;
        this.mainY = mainY;
        this.mainZ = mainZ;
        this.offX = offX;
        this.offY = offY;
        this.offZ = offZ;
        this.mainRAngel = mainRAngel;
        this.mainRx = mainRx;
        this.mainRy = mainRy;
        this.mainRz = mainRz;
        this.offRAngel = offRAngel;
        this.offRx = offRx;
        this.offRy = offRy;
        this.offRz = offRz;
        this.mainHandScaleX = mainHandScaleX;
        this.mainHandScaleY = mainHandScaleY;
        this.mainHandScaleZ = mainHandScaleZ;
        this.offHandScaleX = offHandScaleX;
        this.offHandScaleY = offHandScaleY;
        this.offHandScaleZ = offHandScaleZ;
    }

    public void setMainX(double v) {
        this.mainX = v;
    }

    public void setMainY(double v) {
        this.mainY = v;
    }

    public void setMainZ(double v) {
        this.mainZ = v;
    }

    public void setOffX(double v) {
        this.offX = v;
    }

    public void setOffY(double v) {
        this.offY = v;
    }

    public void setOffZ(double v) {
        this.offZ = v;
    }

    public void setOffRAngel(double v) {
        this.offRAngel = v;
    }

    public void setOffRx(double v) {
        this.offRx = v;
    }

    public void setOffRy(double v) {
        this.offRy = v;
    }

    public void setOffRz(double v) {
        this.offRz = v;
    }

    public void setMainRAngel(double v) {
        this.mainRAngel = v;
    }

    public void setMainRx(double v) {
        this.mainRx = v;
    }

    public void setMainRy(double v) {
        this.mainRy = v;
    }

    public void setMainRz(double v) {
        this.mainRz = v;
    }

    public void setMainHandScaleX(double v) {
        this.mainHandScaleX = v;
    }

    public void setMainHandScaleY(double v) {
        this.mainHandScaleY = v;
    }

    public void setMainHandScaleZ(double v) {
        this.mainHandScaleZ = v;
    }

    public void setOffHandScaleX(double v) {
        this.offHandScaleX = v;
    }

    public void setOffHandScaleY(double v) {
        this.offHandScaleY = v;
    }

    public void setOffHandScaleZ(double v) {
        this.offHandScaleZ = v;
    }

    public double getMainX() {
        return this.mainX;
    }

    public double getMainY() {
        return this.mainY;
    }

    public double getMainZ() {
        return this.mainZ;
    }

    public double getOffX() {
        return this.offX;
    }

    public double getOffY() {
        return this.offY;
    }

    public double getOffZ() {
        return this.offZ;
    }

    public double getMainRAngel() {
        return this.mainRAngel;
    }

    public double getMainRx() {
        return this.mainRx;
    }

    public double getMainRy() {
        return this.mainRy;
    }

    public double getMainRz() {
        return this.mainRz;
    }

    public double getOffRAngel() {
        return this.offRAngel;
    }

    public double getOffRx() {
        return this.offRx;
    }

    public double getOffRy() {
        return this.offRy;
    }

    public double getOffRz() {
        return this.offRz;
    }

    public double getMainHandScaleX() {
        return this.mainHandScaleX;
    }

    public double getMainHandScaleY() {
        return this.mainHandScaleY;
    }

    public double getMainHandScaleZ() {
        return this.mainHandScaleZ;
    }

    public double getOffHandScaleX() {
        return this.offHandScaleX;
    }

    public double getOffHandScaleY() {
        return this.offHandScaleY;
    }

    public double getOffHandScaleZ() {
        return this.offHandScaleZ;
    }
}

