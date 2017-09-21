package com.chattriggers.jsct.utils;

import com.chattriggers.jsct.libs.ChatLib;
import lombok.Getter;
import net.minecraft.util.*;

import java.util.ArrayList;
import java.util.Collections;

public class Message {
    @Getter
    private IChatComponent chatMessage;
    private ArrayList<Object> messageParts;

    public Message(Object... messages) {
        chatMessage = new ChatComponentText("");
        messageParts = new ArrayList<>();

        Collections.addAll(messageParts, messages);
        parseMessages(messages);
    }

    /**
     * Sets a part of the message (defined by the splits made in the constructor)
     * @param part the index of the part to change
     * @param message the new message to replace with
     */
    public void setMessagePart(int part, Object message) {
        messageParts.set(part, message);
        parseMessages(messageParts);
    }

    /**
     * Get an exact copy of the message
     * @return the copy of the message
     */
    public Message copy() {
        return new Message(messageParts);
    }

    private void parseMessages(Object... messages) {
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
