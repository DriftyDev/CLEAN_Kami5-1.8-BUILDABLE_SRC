/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.ClientChatEvent
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package tech.mmmax.kami.api.management;

import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tech.mmmax.kami.api.command.Command;
import tech.mmmax.kami.api.utils.chat.ChatMessage;
import tech.mmmax.kami.api.utils.chat.ChatUtils;

public class CommandManager {
    public static CommandManager INSTANCE;
    public String PREFIX = "-";
    List<Command> commands = new ArrayList<Command>();

    public CommandManager() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    @SubscribeEvent
    public void onChat(ClientChatEvent event) {
        if (event.getMessage().startsWith(this.PREFIX)) {
            String sub = event.getMessage().substring(1);
            String[] args = sub.split(" ");
            if (args.length > 0) {
                block0: for (Command command : this.commands) {
                    for (String s : command.getAlias()) {
                        if (!s.equalsIgnoreCase(args[0])) continue;
                        command.run(args);
                        continue block0;
                    }
                }
            } else {
                ChatUtils.sendMessage(new ChatMessage("Invalid command", false, 0));
            }
            event.setCanceled(true);
        }
    }

    public List<Command> getCommands() {
        return this.commands;
    }
}

