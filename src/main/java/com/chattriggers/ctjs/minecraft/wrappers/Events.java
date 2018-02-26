package com.chattriggers.ctjs.minecraft.wrappers;

import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;

public class Events {
    /**
     * Gets the button clicked in the MouseEvent that is passed in
     *
     * @param event a MouseEvent
     * @return the button clicked (0 for left click, 1 for middle click, 2 for right click, etc.)
     */
    public static int getButton(MouseEvent event) {
        return event.button;
    }

    /**
     * Gets the state of the button passed in
     *
     * @param event a MouseEvent
     * @return the state of the button true for pressed, false for unpressed
     */
    public static Boolean getButtonState(MouseEvent event) {
        return event.buttonstate;
    }

    /**
     * Gets the type of the overlay event i.e. air, armor, crosshair, chat, bosshealth, etc.
     *
     * @param event the render overlay event you want information on
     * @return the type of the event
     */
    public static RenderGameOverlayEvent.ElementType getType(RenderGameOverlayEvent event) {
        return event.type;
    }

    /**
     * Gets the type of the chat event that was received
     *
     * @param event a chat event
     * @return the type of the event, 0 for standard chat message, 1 for system message displayed as standard text
     */
    public static int getType(ClientChatReceivedEvent event) {
        return event.type;
    }

    /**
     * Gets the message from a chat event
     *
     * @param event a chat event
     * @return the message from the event
     */
    public static IChatComponent getMessage(ClientChatReceivedEvent event) {
        return event.message;
    }

    /**
     * Gets the name of a sound that was played
     *
     * @param event a sound event
     * @return the name of the sound that was played
     */
    public static String getName(PlaySoundEvent event) {
        return event.name;
    }

    /**
     * Gets the Mod ID from a config changed event
     *
     * @param event a config changed event
     * @return the mod id
     */
    public static String getModId(ConfigChangedEvent.OnConfigChangedEvent event) {
        return event.modID;
    }
}
