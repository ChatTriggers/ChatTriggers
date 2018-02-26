package com.chattriggers.ctjs.minecraft.listeners;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.minecraft.wrappers.Chat;
import com.chattriggers.ctjs.minecraft.wrappers.Events;
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
        if (Events.getType(event) == 0 || Events.getType(event) == 1) {
            // save to chatHistory
            chatHistory.add(Chat.getChatMessage(event, true));
            if (chatHistory.size() > 1000) chatHistory.remove(0);

            // normal Chat Message
            TriggerType.CHAT.triggerAll(Chat.getChatMessage(event, false), event);

            // print to console
            if (CTJS.getInstance().getConfig().getPrintChatToConsole().value) {
                Console.getConsole().out.println("[CHAT] " + Chat.replaceFormatting(Chat.getChatMessage(event, true)));
            }
        }
    }

    public void addMessageToChatHistory(String msg) {
        chatHistory.add(msg);
        if (chatHistory.size() > 1000) {chatHistory.remove(0);}
    }
}