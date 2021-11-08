/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelRenderer
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package tech.mmmax.kami.api.event;

import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class CrystalBlockRenderEvent
extends Event {
    public ModelRenderer renderer;
    public float scale;
    public int box;

    public CrystalBlockRenderEvent(ModelRenderer renderer, float scale, int box) {
        this.renderer = renderer;
        this.scale = scale;
        this.box = box;
    }

    public boolean isCancelable() {
        return true;
    }
}

