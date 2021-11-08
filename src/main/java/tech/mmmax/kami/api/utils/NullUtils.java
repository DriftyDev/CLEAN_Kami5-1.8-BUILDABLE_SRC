/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.api.utils;

import tech.mmmax.kami.api.wrapper.IMinecraft;

public class NullUtils
implements IMinecraft {
    public static boolean nullCheck() {
        return NullUtils.mc.player == null || NullUtils.mc.world == null || NullUtils.mc.playerController == null;
    }
}

