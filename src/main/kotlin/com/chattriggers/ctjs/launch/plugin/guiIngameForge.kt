package com.chattriggers.ctjs.launch.plugin

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent
import com.chattriggers.ctjs.triggers.TriggerType
import dev.falsehonesty.asmhelper.dsl.At
import dev.falsehonesty.asmhelper.dsl.InjectionPoint
import dev.falsehonesty.asmhelper.dsl.code.CodeBlock.Companion.methodReturn
import dev.falsehonesty.asmhelper.dsl.inject
import dev.falsehonesty.asmhelper.dsl.instructions.Descriptor

fun injectGuiIngameForge() {
    injectRenderTitle()
}

fun injectRenderTitle() = inject {
    className = "net/minecraftforge/client/GuiIngameForge"
    methodName = "renderTitle"
    methodDesc = "(IIF)V"
    at = At(
        InjectionPoint.INVOKE(
            Descriptor(
                "net/minecraft/client/renderer/GlStateManager",
                "pushMatrix",
                "()V",
            ),
            ordinal = 0,
        )
    )

    fieldMaps = mapOf(
        "displayedTitle" to "field_175201_x",
        "displayedSubTitle" to "field_175200_y",
    )

    methodMaps = mapOf("func_179094_E" to "pushMatrix")

    codeBlock {
        val displayedTitle = shadowField<String>()
        val displayedSubTitle = shadowField<String>()

        code {
            if (displayedTitle.isNotEmpty() && displayedSubTitle.isNotEmpty()) {
                val event = CancellableEvent()

                TriggerType.RenderTitle.triggerAll(displayedTitle, displayedSubTitle, event)

                if (event.isCancelled())
                    methodReturn()
            }

        }
    }
}
