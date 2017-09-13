package com.chattriggers.jsct.listeners;

import com.chattriggers.jsct.triggers.Trigger;
import com.chattriggers.jsct.triggers.TriggerRegister;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Copyright (c) FalseHonesty 2017
 */
public class ChatListener {
    @SubscribeEvent
    public void onReceiveChat(ClientChatReceivedEvent e) {
        if (e.type == 0) {
            //Normal Chat Message
            for (Trigger trigger : TriggerRegister.TriggerTypes.CHAT.getTriggers()) {
                trigger.trigger(e.message.getUnformattedText());
            }
        }
    }
}