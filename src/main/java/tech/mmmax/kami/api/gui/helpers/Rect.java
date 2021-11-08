/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.api.gui.helpers;

import tech.mmmax.kami.api.gui.helpers.MouseHelper;

public class Rect {
    int x;
    int y;
    int width;
    int height;

    public Rect(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean collideWithMouse(MouseHelper mouse) {
        return mouse.getX() >= this.getX() && mouse.getX() <= this.getX() + this.getWidth() && mouse.getY() >= this.getY() && mouse.getY() <= this.getY() + this.getHeight();
    }
}

