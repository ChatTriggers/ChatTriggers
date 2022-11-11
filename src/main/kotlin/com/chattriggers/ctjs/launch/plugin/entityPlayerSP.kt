package com.chattriggers.ctjs.launch.plugin

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent
import com.chattriggers.ctjs.triggers.TriggerType
import dev.falsehonesty.asmhelper.dsl.At
import dev.falsehonesty.asmhelper.dsl.InjectionPoint
import dev.falsehonesty.asmhelper.dsl.code.CodeBlock
import dev.falsehonesty.asmhelper.dsl.inject

fun injectEntityPlayerSP() {
    injectSendChatMessage()
}

fun injectSendChatMessage() = inject {
    className = "net/minecraft/client/entity/EntityPlayerSP"
    methodName = "sendChatMessage"
    methodDesc = "(Ljava/lang/String;)V"
    at = At(InjectionPoint.HEAD)

    methodMaps = mapOf("func_71165_d" to "sendChatMessage")

    codeBlock {
        val local1 = shadowLocal<String>()

        code {
            val event = CancellableEvent()
            TriggerType.MessageSent.triggerAll(local1, event)

            if (event.isCancelled())
                CodeBlock.methodReturn()
        }
    }
}
