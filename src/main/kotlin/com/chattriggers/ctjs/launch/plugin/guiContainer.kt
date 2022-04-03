package com.chattriggers.ctjs.launch.plugin

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Slot
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.kotlin.MCSlot
import dev.falsehonesty.asmhelper.dsl.At
import dev.falsehonesty.asmhelper.dsl.InjectionPoint
import dev.falsehonesty.asmhelper.dsl.code.CodeBlock.Companion.asm
import dev.falsehonesty.asmhelper.dsl.code.CodeBlock.Companion.methodReturn
import dev.falsehonesty.asmhelper.dsl.inject
import dev.falsehonesty.asmhelper.dsl.instructions.Descriptor
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager

fun injectGuiContainer() {
    injectDrawForeground()
    injectDrawSlotHighlight()
    injectDrawSlot()
}

fun injectDrawSlot() = inject {
    className = "net/minecraft/client/gui/inventory/GuiContainer"
    methodName = "drawSlot"
    methodDesc = "(Lnet/minecraft/inventory/Slot;)V"

    at = At(InjectionPoint.HEAD)

    methodMaps = mapOf(
            "func_146977_a" to "drawSlot",
    )

    fieldMaps = mapOf("theSlot" to "field_147006_u")

    codeBlock {

        val local0 = shadowLocal<GuiContainer>()
        val local1 = shadowLocal<MCSlot>()

        code {
            val event = CancellableEvent()

            GlStateManager.pushMatrix()
            TriggerType.DrawSlot.triggerAll(Slot(local1), local0, event)
            GlStateManager.popMatrix()

            if (event.isCancelled()) {
                methodReturn()
            }
        }
    }
}

fun injectDrawForeground() = inject {
    className = "net/minecraft/client/gui/inventory/GuiContainer"
    methodName = "drawScreen"
    methodDesc = "(IIF)V"

    at = At(
        InjectionPoint.INVOKE(
            Descriptor(
                GUI_CONTAINER,
                "drawGuiContainerForegroundLayer",
                "(II)V"
            )
        ),
        before = false
    )

    methodMaps = mapOf(
        "func_73863_a" to "drawScreen",
        "func_146979_b" to "drawGuiContainerForegroundLayer"
    )

    fieldMaps = mapOf("theSlot" to "field_147006_u")

    codeBlock {
        val theSlot = shadowField<MCSlot?>()

        val local0 = shadowLocal<GuiContainer>()
        val local1 = shadowLocal<Int>()
        val local2 = shadowLocal<Int>()

        code {
            if (theSlot != null) {
                GlStateManager.pushMatrix()
                TriggerType.PreItemRender.triggerAll(local1, local2, theSlot, local0)
                GlStateManager.popMatrix()
            }
        }
    }
}

fun injectDrawSlotHighlight() = inject {
    className = GUI_CONTAINER
    methodName = "drawScreen"
    methodDesc = "(IIF)V"

    at = At(
        InjectionPoint.INVOKE(
            Descriptor(
                GUI_CONTAINER,
                "drawGradientRect",
                "(IIIIII)V"
            )
        )
    )

    methodMaps = mapOf(
        "func_73863_a" to "drawScreen",
        "func_73733_a" to "drawGradientRect"
    )

    fieldMaps = mapOf("theSlot" to "field_147006_u")

    codeBlock {
        val theSlot = shadowField<MCSlot?>()

        val local0 = shadowLocal<GuiContainer>()
        val local1 = shadowLocal<Int>()
        val local2 = shadowLocal<Int>()

        code {
            if (theSlot != null) {
                val event = CancellableEvent()

                GlStateManager.pushMatrix()
                TriggerType.RenderSlotHighlight.triggerAll(local1, local2, theSlot, local0, event)
                GlStateManager.popMatrix()

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
