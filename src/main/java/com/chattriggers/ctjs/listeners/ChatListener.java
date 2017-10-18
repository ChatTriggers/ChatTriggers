package com.chattriggers.ctjs.listeners;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.libs.ChatLib;
import com.chattriggers.ctjs.triggers.TriggerType;
import lombok.Getter;
import com.chattriggers.ctjs.utils.console.Console;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Getter
public class ChatListener {
    @SubscribeEvent
    public void onReceiveChat(ClientChatReceivedEvent event) {
        if (event.type == 0 || event.type == 1) {
            //Normal Chat Message
            TriggerType.CHAT.triggerAll(event.message.getUnformattedText(), event);

            if (CTJS.getInstance().getConfig().getPrintChatToConsole()) {
                Console.getConsole().out.println("[CHAT] " + ChatLib.replaceFormatting(event.message.getFormattedText()));
            }
        }
    }
}