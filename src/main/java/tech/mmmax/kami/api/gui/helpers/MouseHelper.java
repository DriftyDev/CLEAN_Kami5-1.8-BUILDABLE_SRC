/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.api.gui.helpers;

public class MouseHelper {
    int x;
    int y;

    public MouseHelper(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

