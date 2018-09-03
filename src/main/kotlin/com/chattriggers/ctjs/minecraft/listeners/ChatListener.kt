package com.chattriggers.ctjs.minecraft.listeners

import com.chattriggers.ctjs.engine.ModuleManager
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.EventLib
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.config.Config
import com.chattriggers.ctjs.utils.kotlin.KotlinListener
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@KotlinListener
object ChatListener {
    val chatHistory = mutableListOf<String>()

    @SubscribeEvent
    fun onReceiveChat(event: ClientChatReceivedEvent) {
        val type = EventLib.getType(event)

        when (type) {
            in 0..1 -> {
                // save to chatHistory
                chatHistory += ChatLib.getChatMessage(event, true)
                if (chatHistory.size > 1000) chatHistory.removeAt(0)

                // normal Chat Message
                TriggerType.CHAT.triggerAll(ChatLib.getChatMessage(event, false), event)

                // print to console
                if (Config.printChatToConsole) {
                    ModuleManager.generalConsole.out.println("[CHAT] " + ChatLib.replaceFormatting(ChatLib.getChatMessage(event, true)))
                }
            }
            2 -> TriggerType.ACTION_BAR.triggerAll(ChatLib.getChatMessage(event, false), event)
        }
    }
}