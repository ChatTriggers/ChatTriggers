package com.chattriggers.ctjs.minecraft.listeners;

import com.chattriggers.ctjs.minecraft.libs.ChatLib;
import com.chattriggers.ctjs.minecraft.libs.EventLib;
import com.chattriggers.ctjs.triggers.TriggerType;
import com.chattriggers.ctjs.utils.config.Config;
import com.chattriggers.ctjs.utils.console.Console;
import lombok.Getter;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@Getter
public class ChatListener {
    @Getter
    private static ChatListener instance;

    @Getter
    private ArrayList<String> chatHistory = new ArrayList<>();

    public ChatListener() {
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onReceiveChat(ClientChatReceivedEvent event) {
        if (EventLib.getType(event) == 0 || EventLib.getType(event) == 1) {
            // save to chatHistory
            chatHistory.add(ChatLib.getChatMessage(event, true));
            if (chatHistory.size() > 1000) chatHistory.remove(0);

            // normal Chat Message
            TriggerType.CHAT.triggerAll(ChatLib.getChatMessage(event, false), event);

            // print to console
            if (Config.getInstance().getPrintChatToConsole().value) {
                Console.getInstance().out.println("[CHAT] " + ChatLib.replaceFormatting(ChatLib.getChatMessage(event, true)));
            }
        }
    }

    public void addMessageToChatHistory(String msg) {
        chatHistory.add(msg);
        if (chatHistory.size() > 1000) {chatHistory.remove(0);}
    }
}