package com.chattriggers.ctjs.listeners;

import com.chattriggers.ctjs.libs.ChatLib;
import com.chattriggers.ctjs.triggers.TriggerRegister;
import lombok.Getter;
import net.minecraft.util.IChatComponent;
import com.chattriggers.ctjs.utils.console.Console;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@Getter
public class ChatListener {
    @SubscribeEvent
    public void onReceiveChat(ClientChatReceivedEvent event) {
        if (event.type == 0 || event.type == 1) {
            //Normal Chat Message
            TriggerRegister.TriggerTypes.triggerAllOfType(
                    TriggerRegister.TriggerTypes.CHAT,
                    event.message.getUnformattedText(),
                    event
            );

            Console.getConsole().out.println("[CHAT] " + ChatLib.replaceFormatting(event.message.getFormattedText()));
        }
    }
}