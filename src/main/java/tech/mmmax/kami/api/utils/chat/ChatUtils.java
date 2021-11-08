/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 */
package tech.mmmax.kami.api.utils.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import tech.mmmax.kami.api.utils.chat.ChatMessage;
import tech.mmmax.kami.api.wrapper.IMinecraft;
import tech.mmmax.kami.impl.KamiMod;

public class ChatUtils
implements IMinecraft {
    public static String CLIENT_NAME = ChatFormatting.GRAY + "[" + ChatFormatting.RESET + KamiMod.NAME_VERSION_COLORED + ChatFormatting.GRAY + "]" + ChatFormatting.RESET;

    public static void sendMessage(ChatMessage message) {
        CLIENT_NAME = ChatFormatting.GRAY + "[" + ChatFormatting.RESET + KamiMod.NAME_VERSION_COLORED + ChatFormatting.RESET + ChatFormatting.GRAY + "]" + ChatFormatting.RESET;
        if (mc == null || ChatUtils.mc.player == null || ChatUtils.mc.world == null || ChatUtils.mc.ingameGUI == null) {
            return;
        }
        if (message.doesOverride()) {
            ChatUtils.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((ITextComponent)new TextComponentString(CLIENT_NAME + " " + message.text), message.getMessageID());
        } else {
            ChatUtils.mc.ingameGUI.getChatGUI().printChatMessage((ITextComponent)new TextComponentString(CLIENT_NAME + " " + message.text));
        }
    }

    public static String hephaestus(String string) {
        String str = string;
        str = str.replace("a", "\u1d00");
        str = str.replace("b", "\u0299");
        str = str.replace("c", "\u1d04");
        str = str.replace("d", "\u1d05");
        str = str.replace("e", "\u1d07");
        str = str.replace("f", "\u0493");
        str = str.replace("g", "\u0262");
        str = str.replace("h", "\u029c");
        str = str.replace("i", "\u026a");
        str = str.replace("j", "\u1d0a");
        str = str.replace("k", "\u1d0b");
        str = str.replace("l", "\u029f");
        str = str.replace("m", "\u1d0d");
        str = str.replace("n", "\u0274");
        str = str.replace("o", "\u1d0f");
        str = str.replace("p", "\u1d18");
        str = str.replace("q", "\u01eb");
        str = str.replace("r", "\u0280");
        str = str.replace("s", "\u0455");
        str = str.replace("t", "\u1d1b");
        str = str.replace("u", "\u1d1c");
        str = str.replace("v", "\u1d20");
        str = str.replace("w", "\u1d21");
        str = str.replace("x", "\u0445");
        str = str.replace("y", "\u028f");
        str = str.replace("z", "\u1d22");
        str = str.replace("|", "\u23d0");
        return str;
    }
}

