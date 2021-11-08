/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.api.gui.flow;

import java.util.ArrayList;
import java.util.List;
import tech.mmmax.kami.api.gui.component.IComponent;
import tech.mmmax.kami.api.gui.context.Context;
import tech.mmmax.kami.api.gui.helpers.MouseHelper;
import tech.mmmax.kami.api.gui.helpers.Rect;

public abstract class Flow
implements IComponent {
    public List<IComponent> components;
    public Rect dims;
    int level;

    public Flow(Rect dims, int level) {
        this.dims = dims;
        this.components = new ArrayList<IComponent>();
        this.level = level;
    }

    public void positionComponents(Context context) {
    }

    @Override
    public void draw(Context context, MouseHelper mouse) {
        for (IComponent component : this.getComponents()) {
            if (!component.isActive()) continue;
            component.draw(context, mouse);
        }
    }

    @Override
    public void click(Context context, MouseHelper mouse, int button) {
        for (IComponent component : this.getComponents()) {
            if (!component.isActive()) continue;
            component.click(context, mouse, button);
        }
    }

    @Override
    public void release(Context context, MouseHelper mouse, int state) {
        for (IComponent component : this.getComponents()) {
            if (!component.isActive()) continue;
            component.release(context, mouse, state);
        }
    }

    @Override
    public void key(Context context, int key, char character) {
        for (IComponent component : this.getComponents()) {
            if (!component.isActive()) continue;
            component.key(context, key, character);
        }
    }

    @Override
    public Rect getDims() {
        return this.dims;
    }

    @Override
    public boolean isDraggable() {
        return false;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    public List<IComponent> getComponents() {
        return this.components;
    }

    @Override
    public int getLevel() {
        return this.level;
    }
}

