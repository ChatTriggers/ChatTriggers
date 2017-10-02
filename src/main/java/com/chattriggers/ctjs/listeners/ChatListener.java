package com.chattriggers.ctjs.listeners;

import com.chattriggers.ctjs.triggers.TriggerRegister;
import lombok.Getter;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@Getter
public class ChatListener {
    @Getter
    private ArrayList<IChatComponent> chatHistory = new ArrayList<>();

    @SubscribeEvent
    public void onReceiveChat(ClientChatReceivedEvent event) {
        if (event.type == 0) {
            //Normal Chat Message
            TriggerRegister.TriggerTypes.triggerAllOfType(
                    TriggerRegister.TriggerTypes.CHAT,
                    event.message.getUnformattedText(),
                    event
            );

            chatHistory.add(event.message);
            while (chatHistory.size() > 100)
                chatHistory.remove(0);
            System.out.println(chatHistory); // TODO: remove
        }
    }
}