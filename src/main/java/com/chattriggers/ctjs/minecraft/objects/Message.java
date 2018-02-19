package com.chattriggers.ctjs.minecraft.objects;

import com.chattriggers.ctjs.minecraft.libs.ChatLib;
import lombok.Getter;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.Collections;

public class Message {
    @Getter
    private IChatComponent chatMessage;
    private ArrayList<Object> messageParts;
    @Getter
    private int chatLineId;

    public Message(Object... messages) {
        messageParts = new ArrayList<>();

        Collections.addAll(messageParts, messages);
        parseMessages(messages);

        this.chatLineId = -1;
    }

    /**
     * Sets the chat line ID. Useful for deleting messages by ID.
     *
     * @param id the ID of the message
     * @return the message for method chaining
     */
    public Message setChatLineId(int id) {
        this.chatLineId = id;

        return this;
    }

    /**
     * Sets a part of the message (defined by the splits made in the constructor)
     *
     * @param part    the index of the part to change
     * @param message the new message to replace with
     * @return the message for method chaining
     */
    public Message setMessagePart(int part, Object message) {
        messageParts.set(part, message);
        parseMessages(messageParts);

        return this;
    }

    /**
     * Get an exact copy of the message
     *
     * @return the copy of the message
     */
    public Message copy() {
        if (chatLineId != -1) {
            return new Message(chatLineId, messageParts);
        }

        return new Message(messageParts);
    }

    private void parseMessages(Object... messages) {
        chatMessage = new ChatComponentText("");

        for (Object message : messages) {
            if (message instanceof String) {
                ChatComponentText cct = new ChatComponentText(ChatLib.addColor((String) message));
                cct.setChatStyle(new ChatStyle().setParentStyle(null));

                chatMessage.appendSibling(cct);
            } else if (message instanceof IChatComponent) {
                chatMessage.appendSibling((IChatComponent) message);
            }
        }
    }
}
