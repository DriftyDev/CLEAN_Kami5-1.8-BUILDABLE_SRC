/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.impl.features.commands;

import tech.mmmax.kami.api.command.Command;
import tech.mmmax.kami.api.management.FriendManager;
import tech.mmmax.kami.api.utils.chat.ChatMessage;
import tech.mmmax.kami.api.utils.chat.ChatUtils;

public class Friend
extends Command {
    public Friend() {
        super("Friend", "Friends someone", new String[]{"f", "friend"});
    }

    @Override
    public void run(String[] args) {
        if (args.length > 2) {
            if (args[1].equalsIgnoreCase("add")) {
                FriendManager.INSTANCE.getFriends().add(new tech.mmmax.kami.api.friends.Friend(args[2]));
                ChatUtils.sendMessage(new ChatMessage("Added friend with ign: " + args[2], false, 0));
            } else if (args[1].equalsIgnoreCase("del")) {
                FriendManager.INSTANCE.getFriends().remove(new tech.mmmax.kami.api.friends.Friend(args[2]));
                ChatUtils.sendMessage(new ChatMessage("Removed friend with ign: " + args[2], false, 0));
            } else {
                ChatUtils.sendMessage(new ChatMessage("Invalid format", false, 0));
            }
        } else {
            ChatUtils.sendMessage(new ChatMessage("Invalid format", false, 0));
        }
    }
}

