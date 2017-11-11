package com.chattriggers.ctjs.listeners;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.libs.ChatLib;
import com.chattriggers.ctjs.triggers.TriggerType;
import lombok.Getter;
import com.chattriggers.ctjs.utils.console.Console;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@Getter
public class ChatListener {
    @Getter
    private ArrayList<String> chatHistory = new ArrayList<>();

    @SubscribeEvent
    public void onReceiveChat(ClientChatReceivedEvent event) {
        if (event.type == 0 || event.type == 1) {
            // normal Chat Message
            TriggerType.CHAT.triggerAll(ChatLib.getChatMessage(event, false), event);

            // print to console
            if (CTJS.getInstance().getConfig().getPrintChatToConsole()) {
                Console.getConsole().out.println("[CHAT] " + ChatLib.replaceFormatting(ChatLib.getChatMessage(event, true)));
            }

            // save to chatHistory
            chatHistory.add(ChatLib.getChatMessage(event, true));
            if (chatHistory.size() > 1000) chatHistory.remove(0);
        }
    }

    public void addMessageToChatHistory(String msg) {
        chatHistory.add(msg);
    }
}