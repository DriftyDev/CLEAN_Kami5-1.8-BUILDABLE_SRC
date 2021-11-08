/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.ChatAllowedCharacters
 */
package tech.mmmax.kami.api.gui.widget.impl;

import net.minecraft.util.ChatAllowedCharacters;
import tech.mmmax.kami.api.gui.component.IComponent;
import tech.mmmax.kami.api.gui.context.Context;
import tech.mmmax.kami.api.gui.helpers.MouseHelper;
import tech.mmmax.kami.api.gui.helpers.Rect;
import tech.mmmax.kami.api.gui.widget.IWidget;

public class TextEntryWidget
implements IWidget<String>,
IComponent {
    Rect dims;
    String value;
    public boolean typing = false;

    public TextEntryWidget(Rect dims, String value) {
        this.dims = dims;
        this.value = value;
    }

    @Override
    public void draw(Context context, MouseHelper mouse) {
        if (this.typing && !this.getDims().collideWithMouse(mouse)) {
            this.typing = false;
        }
        this.getDims().setHeight(context.getMetrics().getButtonHeight());
        context.getRenderer().renderStringWidget(this, context, this.getDims(), mouse);
    }

    @Override
    public void click(Context context, MouseHelper mouse, int button) {
        if (this.getDims().collideWithMouse(mouse)) {
            this.typing = !this.typing;
        }
    }

    @Override
    public void release(Context context, MouseHelper mouse, int state) {
    }

    @Override
    public void key(Context context, int key, char character) {
        if (this.typing) {
            if (key == 28) {
                this.typing = false;
                return;
            }
            if (key == 14) {
                try {
                    this.setValue(this.getValue().substring(0, this.getValue().length() - 1));
                }
                catch (Exception exception) {
                    // empty catch block
                }
                return;
            }
            if (ChatAllowedCharacters.isAllowedCharacter((char)character)) {
                this.setValue(this.getValue() + character);
                return;
            }
        }
    }

    @Override
    public int getLevel() {
        return 3;
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

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public void setTitle(String title) {
    }

    @Override
    public Rect getDisplayDims() {
        return this.getDims();
    }
}

