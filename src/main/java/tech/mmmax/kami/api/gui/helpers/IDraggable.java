/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.api.gui.helpers;

import tech.mmmax.kami.api.gui.helpers.MouseHelper;
import tech.mmmax.kami.api.gui.helpers.Rect;

public interface IDraggable {
    default public void drag(Rect dims, int dragX, int dragY, MouseHelper mouse) {
        dims.setX(mouse.getX() - dragX);
        dims.setY(mouse.getY() - dragY);
    }
}

