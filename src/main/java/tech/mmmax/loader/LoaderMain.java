/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.relauncher.IFMLLoadingPlugin
 */
package tech.mmmax.loader;

import java.util.Map;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

public class LoaderMain
implements IFMLLoadingPlugin {
    public LoaderMain() {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.kami.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
    }

    public String[] getASMTransformerClass() {
        return new String[0];
    }

    public String getModContainerClass() {
        return null;
    }

    public String getSetupClass() {
        return null;
    }

    public void injectData(Map<String, Object> data) {
    }

    public String getAccessTransformerClass() {
        return null;
    }
}

