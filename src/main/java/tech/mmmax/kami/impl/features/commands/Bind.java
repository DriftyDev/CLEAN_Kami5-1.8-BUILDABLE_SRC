/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package tech.mmmax.kami.impl.features.commands;

import org.lwjgl.input.Keyboard;
import tech.mmmax.kami.api.command.Command;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.management.FeatureManager;
import tech.mmmax.kami.api.utils.chat.ChatMessage;
import tech.mmmax.kami.api.utils.chat.ChatUtils;

public class Bind
extends Command {
    public Bind() {
        super("Bind", "binds a module", new String[]{"bind", "b"});
    }

    @Override
    public void run(String[] args) {
        if (args.length > 2) {
            for (Feature feature : FeatureManager.INSTANCE.getFeatures()) {
                Module module;
                String modName;
                if (!(feature instanceof Module) || !(modName = (module = (Module)feature).getName().replace(" ", "")).equalsIgnoreCase(args[1])) continue;
                module.getBind().setKey(Keyboard.getKeyIndex((String)args[2].toUpperCase()));
                ChatUtils.sendMessage(new ChatMessage("Bound " + module.getName() + " to " + Keyboard.getKeyName((int)module.getBind().getKey()), false, 0));
            }
        } else {
            ChatUtils.sendMessage(new ChatMessage("Please input a valid command", false, 0));
        }
    }
}

