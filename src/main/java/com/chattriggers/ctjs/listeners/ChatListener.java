package com.chattriggers.ctjs.listeners;

import com.chattriggers.ctjs.triggers.TriggerRegister;
import lombok.Getter;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@Getter
public class ChatListener {
    @SubscribeEvent
    public void onReceiveChat(ClientChatReceivedEvent event) {
        if (event.type == 0) {
            //Normal Chat Message
            TriggerRegister.TriggerTypes.triggerAllOfType(
                    TriggerRegister.TriggerTypes.CHAT,
                    event.message.getUnformattedText(),
                    event
            );
        }
    }
}