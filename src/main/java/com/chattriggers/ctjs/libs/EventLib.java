package com.chattriggers.ctjs.libs;

import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;

public class EventLib {
    public static int getButton(MouseEvent event) {
        return event.button;
    }

    public static Boolean getButtonState(MouseEvent event) {
        return event.buttonstate;
    }

    public static RenderGameOverlayEvent.ElementType getType(RenderGameOverlayEvent event) {
        return event.type;
    }

    public static int getType(ClientChatReceivedEvent event) {
        return event.type;
    }

    public static IChatComponent getMessage(ClientChatReceivedEvent event) {
        return event.message;
    }

    public static String getName(PlaySoundEvent event) {
        return event.name;
    }

    public static String getModId(ConfigChangedEvent.OnConfigChangedEvent event) {
        return event.modID;
    }
}
