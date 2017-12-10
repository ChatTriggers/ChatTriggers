package com.chattriggers.ctjs.libs;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class EventLib {
    public static int getButton(MouseEvent event) {
        return event.button;
    }

    public static RenderGameOverlayEvent.ElementType getType(RenderGameOverlayEvent event) {
        return event.type;
    }

    public static int getType(ClientChatReceivedEvent event) {
        return event.type;
    }
}
