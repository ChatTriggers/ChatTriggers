package com.chattriggers.ctjs.minecraft.wrappers.settings

import com.chattriggers.ctjs.minecraft.wrappers.Client
import net.minecraft.entity.player.EntityPlayer

/**
 * Used from [Settings.chat] to get chat settings
 */
class ChatSettings {
    private val settings = Client.settings.getSettings()

    // show chat
    fun getVisibility() = settings.chatVisibility
    fun setVisibility(visibility: String) {
        when (visibility.toLowerCase()) {
            "hidden" -> settings.chatVisibility = EntityPlayer.EnumChatVisibility.HIDDEN
            "commands", "system" -> settings.chatVisibility = EntityPlayer.EnumChatVisibility.SYSTEM
            else -> settings.chatVisibility = EntityPlayer.EnumChatVisibility.FULL
        }
    }

    // colors
    fun getColors() = settings.chatColours
    fun setColors(toggled: Boolean) {
        settings.chatColours = toggled
    }

    // web links
    fun getWebLinks() = settings.chatLinks
    fun setWebLinks(toggled: Boolean) {
        settings.chatLinks = toggled
    }

    // opacity
    fun getOpacity() = settings.chatOpacity
    fun setOpacity(opacity: Float) {
        settings.chatOpacity = opacity
    }

    // prompt on links
    fun getPromptOnWebLinks() = settings.chatLinksPrompt
    fun setPromptOnWebLinks(toggled: Boolean) {
        settings.chatLinksPrompt = toggled
    }

    // scale

    // focused height

    // unfocused height

    // width

    // reduced debug info
}