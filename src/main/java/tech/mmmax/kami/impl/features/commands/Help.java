/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.impl.features.commands;

import tech.mmmax.kami.api.command.Command;
import tech.mmmax.kami.api.management.CommandManager;
import tech.mmmax.kami.api.utils.chat.ChatMessage;
import tech.mmmax.kami.api.utils.chat.ChatUtils;

public class Help
extends Command {
    public Help() {
        super("Help", "Shows you all the commands", new String[]{"help", "commands"});
    }

    @Override
    public void run(String[] args) {
        for (Command command : CommandManager.INSTANCE.getCommands()) {
            ChatUtils.sendMessage(new ChatMessage(command.getName() + " - " + command.getDesc(), false, 0));
        }
    }
}

