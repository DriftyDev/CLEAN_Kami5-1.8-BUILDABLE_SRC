/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraftforge.fml.common.Mod$Instance
 *  net.minecraftforge.fml.common.event.FMLInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPostInitializationEvent
 */
package tech.mmmax.kami.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import tech.mmmax.kami.api.management.SavableManager;
import tech.mmmax.kami.impl.Register;

public class KamiMod {
    public static String NAME = "Kami5";
    public static final String MOD_ID = "kami5";
    public static String VERSION = "1.8";
    public static final String NAME_VERSION = NAME + " " + VERSION;
    public static String NAME_VERSION_COLORED = NAME + ChatFormatting.GRAY + " " + VERSION;
    @Mod.Instance
    public KamiMod INSTANCE;

    public static void updateName() {
        NAME_VERSION_COLORED = NAME + ChatFormatting.GRAY + " " + VERSION;
    }

    public void init(FMLInitializationEvent event) {
        System.out.println("Initialzied");
        Register.INSTANCE = new Register();
        Register.INSTANCE.registerAll();
        ShutdownHook.setup();
    }

    public void postInit(FMLPostInitializationEvent event) {
        SavableManager.INSTANCE.load();
    }

    static class ShutdownHook
    extends Thread {
        ShutdownHook() {
        }

        public static void setup() {
            Runtime.getRuntime().addShutdownHook(new ShutdownHook());
        }

        @Override
        public void run() {
            super.run();
            try {
                SavableManager.INSTANCE.save();
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}

