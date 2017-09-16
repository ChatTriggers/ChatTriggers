package com.chattriggers.jsct.listeners;

import com.chattriggers.jsct.triggers.TriggerRegister;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatListener {
    @SubscribeEvent
    public void onReceiveChat(ClientChatReceivedEvent event) {
        if (event.type == 0) {
            //Normal Chat Message
            TriggerRegister.TriggerTypes.triggerAllOfType(
                    TriggerRegister.TriggerTypes.CHAT,
                    event.message.getUnformattedText()
            );
        }
    }
}