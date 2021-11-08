/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.api.gui.flow.impl;

import tech.mmmax.kami.api.gui.component.IComponent;
import tech.mmmax.kami.api.gui.context.Context;
import tech.mmmax.kami.api.gui.flow.Flow;
import tech.mmmax.kami.api.gui.helpers.Rect;

public class GridFlow
extends Flow {
    int rows;
    int columns;
    boolean autoHeight = false;
    boolean autoColumns = true;

    public GridFlow(Rect dims, int rows, int columns, int level) {
        super(dims, level);
        this.rows = rows;
        this.columns = columns;
    }

    public int getRows() {
        return this.rows;
    }

    public int getColumns() {
        return this.columns;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public boolean doesAutoHeight() {
        return this.autoHeight;
    }

    public void setAutoHeight(boolean autoHeight) {
        this.autoHeight = autoHeight;
    }

    public boolean doesAutoColumns() {
        return this.autoColumns;
    }

    public void setAutoColumns(boolean autoColumns) {
        this.autoColumns = autoColumns;
    }

    @Override
    public void positionComponents(Context context) {
        super.positionComponents(context);
        int index = 0;
        int indexInColumn = 0;
        int componentWidth = (this.getDims().getWidth() - (this.columns - 1) * context.getMetrics().getBetweenSpacing()) / this.columns;
        int componentHeight = (this.getDims().getHeight() - (this.rows - 1) * context.getMetrics().getBetweenSpacing()) / this.rows;
        for (IComponent component : this.getComponents()) {
            int componentX = componentWidth * indexInColumn + (indexInColumn - 1) * context.getMetrics().getBetweenSpacing();
            component.getDims().setX(this.getDims().getX() + componentX);
            component.getDims().setWidth(componentWidth);
            component.getDims().setY(this.getDims().getY() + index * componentHeight + context.getMetrics().getBetweenSpacing());
            if (this.autoHeight) {
                component.getDims().setHeight(componentHeight);
            }
            if (++indexInColumn != this.columns) continue;
            indexInColumn = 0;
            ++index;
        }
    }
}

