package com.chattriggers.jsct.libs;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ChatLib {
    public static void chat(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
    }
}
