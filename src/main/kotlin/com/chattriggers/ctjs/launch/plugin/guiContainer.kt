package com.chattriggers.ctjs.launch.plugin

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent
import com.chattriggers.ctjs.triggers.TriggerType
import dev.falsehonesty.asmhelper.dsl.At
import dev.falsehonesty.asmhelper.dsl.InjectionPoint
import dev.falsehonesty.asmhelper.dsl.code.CodeBlock.Companion.asm
import dev.falsehonesty.asmhelper.dsl.inject
import dev.falsehonesty.asmhelper.dsl.instructions.Descriptor
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Slot

fun injectGuiContainer() {
    injectDrawForeground()
    injectDrawSlotHighlight()
}

fun injectDrawForeground() = inject {
    className = "net/minecraft/client/gui/inventory/GuiContainer"
    methodName = "drawScreen"
    methodDesc = "(IIF)V"

    at = At(
        InjectionPoint.INVOKE(
            Descriptor(
                "net/minecraft/client/gui/inventory/GuiContainer",
                "drawGuiContainerForegroundLayer",
                "(II)V"
            )
        ),
        before = false
    )

    methodMaps = mapOf(
        "func_148128_a" to "drawScreen",
        "func_146979_b" to "drawGuiContainerForegroundLayer"
    )

    fieldMaps = mapOf("theSlot" to "field_147006_u")

    codeBlock {
        val theSlot = shadowField<Slot?>()

        val local0 = shadowLocal<GuiContainer>()
        val local1 = shadowLocal<Int>()
        val local2 = shadowLocal<Int>()

        code {
            if (theSlot != null) {
                TriggerType.PreItemRender.triggerAll(local1, local2, theSlot, local0)
            }
        }
    }
}

fun injectDrawSlotHighlight() = inject {
    className = "net/minecraft/client/gui/inventory/GuiContainer"
    methodName = "drawScreen"
    methodDesc = "(IIF)V"

    at = At(
        InjectionPoint.INVOKE(
            Descriptor(
                "net/minecraft/client/gui/inventory/GuiContainer",
                "drawGradientRect",
                "(IIIIII)V"
            )
        )
    )

    methodMaps = mapOf(
        "func_148128_a" to "drawScreen",
        "func_73733_a" to "drawGradientRect"
    )

    fieldMaps = mapOf("theSlot" to "field_147006_u")

    codeBlock {
        val theSlot = shadowField<Slot?>()

        val local0 = shadowLocal<GuiContainer>()
        val local1 = shadowLocal<Int>()
        val local2 = shadowLocal<Int>()

        code {
            if (theSlot != null) {
                val event = CancellableEvent()

                TriggerType.RenderSlotHighlight.triggerAll(local1, local2, theSlot, local0, event)

                if (event.isCancelled()) {
                    asm {
                        pop2()
                        int(0)
                        int(0)
                    }
                }
            }
        }
    }
}
