package com.chattriggers.ctjs.launch.plugin

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent
import com.chattriggers.ctjs.minecraft.wrappers.inventory.Item
import com.chattriggers.ctjs.triggers.TriggerType
import dev.falsehonesty.asmhelper.dsl.At
import dev.falsehonesty.asmhelper.dsl.InjectionPoint
import dev.falsehonesty.asmhelper.dsl.code.CodeBlock.Companion.methodReturn
import dev.falsehonesty.asmhelper.dsl.inject
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.item.ItemStack

fun injectRenderItem() {
    injectRenderItemAndEffectIntoGUI()
    injectRenderItemOverlayIntoGUI()
}

fun injectRenderItemAndEffectIntoGUI() = inject {
    className = "net/minecraft/client/renderer/entity/RenderItem"
    methodName = "renderItemAndEffectIntoGUI"
    methodDesc = "(Lnet/minecraft/item/ItemStack;II)V"
    at = At(InjectionPoint.HEAD)

    methodMaps = mapOf("func_180450_b" to "renderItemAndEffectIntoGUI")

    codeBlock {
        val local1 = shadowLocal<ItemStack?>()
        val local2 = shadowLocal<Int>()
        val local3 = shadowLocal<Int>()

        code {
            if (local1 != null) {
                val event = CancellableEvent()

                GlStateManager.pushMatrix()
                TriggerType.RenderItemIntoGui.triggerAll(Item(local1), local2, local3, event)
                GlStateManager.popMatrix()

                if (event.isCanceled())
                    methodReturn()
            }
        }
    }
}

fun injectRenderItemOverlayIntoGUI() = inject {
    className = "net/minecraft/client/renderer/entity/RenderItem"
    methodName = "renderItemOverlayIntoGUI"
    methodDesc = "(Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V"
    at = At(InjectionPoint.HEAD)

    methodMaps = mapOf("func_180453_a" to "renderItemOverlayIntoGUI")

    codeBlock {
        val local2 = shadowLocal<ItemStack?>()
        val local3 = shadowLocal<Int>()
        val local4 = shadowLocal<Int>()

        code {
            if (local2 != null) {
                val event = CancellableEvent()

                GlStateManager.pushMatrix()
                TriggerType.RenderItemOverlayIntoGui.triggerAll(Item(local2), local3, local4, event)
                GlStateManager.popMatrix()

                if (event.isCanceled())
                    methodReturn()
            }
        }
    }
}