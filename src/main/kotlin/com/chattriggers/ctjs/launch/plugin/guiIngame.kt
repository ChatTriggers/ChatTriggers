package com.chattriggers.ctjs.launch.plugin

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.triggers.TriggerType
import dev.falsehonesty.asmhelper.dsl.At
import dev.falsehonesty.asmhelper.dsl.InjectionPoint
import dev.falsehonesty.asmhelper.dsl.code.CodeBlock.Companion.methodReturn
import dev.falsehonesty.asmhelper.dsl.inject
import net.minecraft.client.renderer.GlStateManager

fun injectGuiIngame() {
    injectRenderScoreboard()
}

fun injectRenderScoreboard() = inject {
    className = "net/minecraft/client/gui/GuiIngame"
    methodName = "renderScoreboard"
    methodDesc = "(Lnet/minecraft/scoreboard/ScoreObjective;Lnet/minecraft/client/gui/ScaledResolution;)V"
    at = At(InjectionPoint.HEAD)

    methodMaps = mapOf("func_180475_a" to "renderScoreboard")

    codeBlock {
        code {
            val event = CancellableEvent()
            TriggerType.RenderScoreboard.triggerAll(event)

            if (event.isCancelled())
                methodReturn()
        }
    }
}

