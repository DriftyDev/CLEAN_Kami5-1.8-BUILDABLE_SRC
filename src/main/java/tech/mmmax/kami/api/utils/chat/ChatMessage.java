/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.api.utils.chat;

public class ChatMessage {
    int messageID;
    boolean override;
    String text;

    public ChatMessage(String text, boolean override, int messageID) {
        this.text = text;
        this.override = override;
        this.messageID = messageID;
    }

    public String getText() {
        return this.text;
    }

    public int getMessageID() {
        return this.messageID;
    }

    public boolean doesOverride() {
        return this.override;
    }
}

