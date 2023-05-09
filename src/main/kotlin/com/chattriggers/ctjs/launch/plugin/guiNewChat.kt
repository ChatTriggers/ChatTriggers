package com.chattriggers.ctjs.launch.plugin

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.listeners.ClientListener
import com.chattriggers.ctjs.printToConsole
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.Config
import com.chattriggers.ctjs.utils.kotlin.MCITextComponent
import dev.falsehonesty.asmhelper.dsl.At
import dev.falsehonesty.asmhelper.dsl.InjectionPoint
import dev.falsehonesty.asmhelper.dsl.code.CodeBlock.Companion.methodReturn
import dev.falsehonesty.asmhelper.dsl.inject
import net.minecraftforge.client.event.ClientChatReceivedEvent

fun injectGuiNewChat() = inject {
    className = "net/minecraft/client/gui/GuiNewChat"
    methodName = "printChatMessageWithOptionalDeletion"
    methodDesc = "(L$ICHAT_COMPONENT;I)V"

    at = At(InjectionPoint.HEAD)

    methodMaps = mapOf(
        "func_146234_a" to "printChatMessageWithOptionalDeletion"
    )

    codeBlock {
        val local1 = shadowLocal<MCITextComponent>()

        code {
            val event = ClientChatReceivedEvent(0, local1)
            // save to chatHistory
            ClientListener.chatHistory += ChatLib.getChatMessage(event, true)
            if (ClientListener.chatHistory.size > 1000) ClientListener.chatHistory.removeAt(0)

            // client-side and normal Chat Messages
            TriggerType.Chat.triggerAll(ChatLib.getChatMessage(event, false), event, true)

            // print to console
            if (Config.printChatToConsole) {
                "[CHAT] ${ChatLib.replaceFormatting(ChatLib.getChatMessage(event, true))}".printToConsole()
            }

            if (event.isCanceled) {
                methodReturn()
            }
        }
    }
}
