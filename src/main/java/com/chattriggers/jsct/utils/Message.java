package com.chattriggers.jsct.utils;

import com.chattriggers.jsct.libs.ChatLib;
import lombok.Getter;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.*;

public class Message {
    @Getter
    private IChatComponent chatMessage;

    public Message(Object... messages) {
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
