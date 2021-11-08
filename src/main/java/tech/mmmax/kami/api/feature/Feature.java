/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.MinecraftForge
 */
package tech.mmmax.kami.api.feature;

import java.awt.Color;
import java.util.*;

import net.minecraftforge.common.MinecraftForge;
import tech.mmmax.kami.api.config.ISavable;
import tech.mmmax.kami.api.management.SavableManager;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class Feature
implements ISavable {
    String name;
    boolean enabled;
    FeatureType type;
    List<Value<?>> values;
    Category category;
    public Value<Boolean> visible;
    public Value<String> displayName;
    public int offset;
    public boolean sliding;

    public Feature(String name, Category category, FeatureType type) {
        this.name = name;
        this.enabled = false;
        this.type = type;
        this.values = new ArrayList();
        this.category = category;
        SavableManager.INSTANCE.getSavables().add(this);
        this.visible = new ValueBuilder().withDescriptor("Visible").withValue(true).register(this);
        this.displayName = new ValueBuilder().withDescriptor("Name").withValue(this.getName()).register(this);
    }

    public void animation() {
        this.sliding = (float)this.offset != 0.0f;
        if (this.sliding) {
            ++this.offset;
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return this.displayName.getValue();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (this.enabled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }

    public void toggle() {
        this.setEnabled(!this.isEnabled());
    }

    public FeatureType getType() {
        return this.type;
    }

    public void setType(FeatureType type) {
        this.type = type;
    }

    public List<Value<?>> getValues() {
        return this.values;
    }

    public void setValues(List<Value<?>> values) {
        this.values = values;
    }

    public String getHudInfo() {
        return "";
    }

    @Override
    public void load(Map<String, Object> objects) {
        Object e = objects.get("enabled");
        if (e != null) {
            this.setEnabled((Boolean)e);
        }
        this.setEnabled(objects.get("enabled") != null ? ((Boolean)objects.get("enabled")).booleanValue() : this.isEnabled());
        Iterator values = this.getValues().iterator();

        while(values.hasNext()) {
            Value value = (Value)values.next();
            Object o = objects.get(value.getTag());
            if (o != null) {
                try {
                    if (value.getValue() instanceof Color) {
                        Map<String, Object> map = (Map)o;
                        Color colorVal = new Color((Integer)map.get("red"), (Integer)map.get("green"), (Integer)map.get("blue"), (Integer)map.get("alpha"));
                        value.setValue(colorVal);
                    } else {
                        value.setValue(o);
                    }
                } catch (Exception ec) {
                }
            }
        }
    }

    @Override
    public Map<String, Object> save() {
        HashMap<String, Object> toSave = new HashMap<String, Object>();
        toSave.put("enabled", this.enabled);
        for (Value<?> value : this.getValues()) {
            if (value.getValue() instanceof Color) {
                Value<?> val = value;
                HashMap<String, Integer> color = new HashMap<String, Integer>();
                color.put("red", ((Color)val.getValue()).getRed());
                color.put("green", ((Color)val.getValue()).getGreen());
                color.put("blue", ((Color)val.getValue()).getBlue());
                color.put("alpha", ((Color)val.getValue()).getAlpha());
                toSave.put(value.getTag(), color);
                continue;
            }
            toSave.put(value.getTag(), value.getValue());
        }
        return toSave;
    }

    @Override
    public String getFileName() {
        return this.getName() + ".yml";
    }

    @Override
    public String getDirName() {
        return "features";
    }

    public static enum Category {
        Client,
        Player,
        Combat,
        Misc,
        Render,
        Hud;

    }

    public static enum FeatureType {
        Module,
        Hud;

    }
}

