package com.chattriggers.ctjs.launch.plugin

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.triggers.TriggerType
import me.falsehonesty.asmhelper.dsl.At
import me.falsehonesty.asmhelper.dsl.InjectionPoint
import me.falsehonesty.asmhelper.dsl.inject
import me.falsehonesty.asmhelper.dsl.instructions.Descriptor
import me.falsehonesty.asmhelper.dsl.writers.asm
import net.minecraft.item.ItemStack

fun makeGuiTransformers() {
//    injectTooltipTrigger()
}

private fun injectTooltipTrigger() = inject {
    // net/minecraft/client/gui/GuiScreen.renderToolTip(Lnet/minecraft/item/ItemStack;II)V
    className = "net/minecraft/client/gui/GuiScreen"
    methodName = "renderToolTip"
    methodDesc = "(Lnet/minecraft/item/ItemStack;II)V"

    // net/minecraft/item/Item;getFontRenderer(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/client/gui/FontRenderer;
    at = At(
        InjectionPoint.INVOKE(
        Descriptor("net/minecraft/item/Item", "getFontRenderer", "(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/client/gui/FontRenderer;")
    ), before = false, shift = 1)

    codeBlock {
        val local4 = shadowLocal<List<String>>()
        val local1 = shadowLocal<ItemStack>()

        code {
            val event = CancellableEvent()
            TriggerType.TOOLTIP.triggerAll(
                local4,
                Item(local1),
                event
            )
            if (event.isCancelled()) asm { methodReturn() }
        }
    }
}