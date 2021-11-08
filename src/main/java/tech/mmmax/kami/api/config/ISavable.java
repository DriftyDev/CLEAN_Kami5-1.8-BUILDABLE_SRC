/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.api.config;

import java.util.Map;

public interface ISavable {
    public void load(Map<String, Object> var1);

    public Map<String, Object> save();

    public String getFileName();

    public String getDirName();
}

