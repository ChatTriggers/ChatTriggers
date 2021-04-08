package com.chattriggers.ctjs.launch.plugin

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.kotlin.ITextComponent
import dev.falsehonesty.asmhelper.dsl.At
import dev.falsehonesty.asmhelper.dsl.InjectionPoint
import dev.falsehonesty.asmhelper.dsl.code.CodeBlock.Companion.iReturn
import dev.falsehonesty.asmhelper.dsl.code.CodeBlock.Companion.methodReturn
import dev.falsehonesty.asmhelper.dsl.inject
import dev.falsehonesty.asmhelper.dsl.instructions.*
import net.minecraft.item.ItemStack
import org.lwjgl.input.Keyboard

fun injectGuiScreen() {
    injectSendChatMessage()
    injectHandleKeyboardInput()
    injectMouseClick()
    injectMouseRelease()
    injectMouseDrag()
    injectTextComponentClick()
    injectTextComponentHover()
    injectRenderTooltip()
}

fun injectSendChatMessage() = inject {
    className = "net/minecraft/client/gui/GuiScreen"
    methodName = "sendChatMessage"
    methodDesc = "(Ljava/lang/String;Z)V"
    at = At(InjectionPoint.HEAD)

    methodMaps = mapOf("func_175281_b" to "sendChatMessage")

    codeBlock {
        val local1 = shadowLocal<String>()

        code {
            val event = CancellableEvent()
            TriggerType.MESSAGE_SENT.triggerAll(local1, event)

            if (event.isCancelled())
                methodReturn()
        }
    }
}

fun injectHandleKeyboardInput() = inject {
    className = "net/minecraft/client/gui/GuiScreen"
    methodName = "handleKeyboardInput"
    methodDesc = "()V"

    at = At(
        InjectionPoint.INVOKE(
            Descriptor(
                "net/minecraft/client/gui/GuiScreen",
                "keyTyped",
                "(CI)V"
            )
        )
    )

    methodMaps = mapOf(
        "func_146282_l" to "handleKeyboardInput",
        "func_73869_a" to "keyTyped"
    )

    codeBlock {
        val local0 = shadowLocal<Any>()

        code {
            val event = CancellableEvent()
            TriggerType.GUI_KEY.triggerAll(
                Keyboard.getEventCharacter(),
                Keyboard.getEventKey(),
                local0,
                event
            )

            if (event.isCancelled())
                methodReturn()
        }
    }
}

fun injectMouseClick() = inject {
    className = "net/minecraft/client/gui/GuiScreen"
    methodName = "handleMouseInput"
    methodDesc = "()V"

    at = At(
        InjectionPoint.INVOKE(
            Descriptor(
                "net/minecraft/client/gui/GuiScreen",
                "mouseClicked",
                "(III)V"
            )
        )
    )

    methodMaps = mapOf(
        "func_146274_d" to "handleMouseInput",
        "func_73864_a" to "mouseClicked"
    )

    codeBlock {
        val local0 = shadowLocal<Any>()
        val local1 = shadowLocal<Int>()
        val local2 = shadowLocal<Int>()
        val local3 = shadowLocal<Int>()

        code {
            val event = CancellableEvent()
            TriggerType.GUI_MOUSE_CLICK.triggerAll(local1, local2, local3, local0, event)
            if (event.isCancelled())
                methodReturn()
        }
    }
}

fun injectMouseRelease() = inject {
    className = "net/minecraft/client/gui/GuiScreen"
    methodName = "handleMouseInput"
    methodDesc = "()V"

    at = At(
        InjectionPoint.INVOKE(
            Descriptor(
                "net/minecraft/client/gui/GuiScreen",
                "mouseReleased",
                "(III)V"
            )
        )
    )

    methodMaps = mapOf(
        "func_146274_d" to "handleMouseInput",
        "func_146286_b" to "mouseReleased"
    )

    codeBlock {
        val local0 = shadowLocal<Any>()
        val local1 = shadowLocal<Int>()
        val local2 = shadowLocal<Int>()
        val local3 = shadowLocal<Int>()

        code {
            val event = CancellableEvent()
            TriggerType.GUI_MOUSE_RELEASE.triggerAll(local1, local2, local3, local0, event)
            if (event.isCancelled())
                methodReturn()
        }
    }
}

fun injectMouseDrag() = inject {
    className = "net/minecraft/client/gui/GuiScreen"
    methodName = "handleMouseInput"
    methodDesc = "()V"

    at = At(
        InjectionPoint.INVOKE(
            Descriptor(
                "net/minecraft/client/gui/GuiScreen",
                "mouseClickMove",
                "(IIIJ)V"
            )
        )
    )

    methodMaps = mapOf(
        "func_146274_d" to "handleMouseInput",
        "func_146273_a" to "mouseClickMove"
    )

    codeBlock {
        val local0 = shadowLocal<Any>()
        val local1 = shadowLocal<Int>()
        val local2 = shadowLocal<Int>()
        val local3 = shadowLocal<Int>()

        code {
            val event = CancellableEvent()
            TriggerType.GUI_MOUSE_DRAG.triggerAll(local1, local2, local3, local0, event)
            if (event.isCancelled())
                methodReturn()
        }
    }
}

fun injectTextComponentClick() = inject {
    className = "net/minecraft/client/gui/GuiScreen"
    methodName = "handleComponentClick"
    methodDesc = "(L$ICHAT_COMPONENT;)Z"
    at = At(InjectionPoint.HEAD)

    methodMaps = mapOf("func_175276_a" to "handleComponentClick")

    codeBlock {
        val local1 = shadowLocal<ITextComponent?>()

        code {
            val event = CancellableEvent()
            TriggerType.CHAT_COMPONENT_CLICKED.triggerAll(local1?.let(::TextComponent), event)
            if (event.isCancelled())
                iReturn(0)
        }
    }
}

fun injectTextComponentHover() = inject {
    className = "net/minecraft/client/gui/GuiScreen"
    methodName = "handleComponentHover"
    methodDesc = "(L$ICHAT_COMPONENT;II)V"
    at = At(InjectionPoint.HEAD)

    methodMaps = mapOf("func_175272_a" to "handleComponentHover")

    codeBlock {
        val local1 = shadowLocal<ITextComponent?>()
        val local2 = shadowLocal<Int>()
        val local3 = shadowLocal<Int>()

        code {
            val event = CancellableEvent()
            TriggerType.CHAT_COMPONENT_HOVERED.triggerAll(local1?.let(::TextComponent), local2, local3, event)
            if (event.isCancelled())
                methodReturn()
        }
    }
}

fun injectRenderTooltip() = inject {
    className = "net/minecraft/client/gui/GuiScreen"
    methodName = "renderToolTip"
    methodDesc = "(L$ITEM_STACK;II)V"

    at = At(
        InjectionPoint.INVOKE(
            Descriptor(
                ITEM_STACK,
                "getTooltip",
                "(L$ENTITY_PLAYER;Z)Ljava/util/List;"
            )
        ),
        before = false,
        shift = 1
    )

    methodMaps = mapOf(
        "func_146285_a" to "renderToolTip",
        "func_82840_a" to "getTooltip"
    )

    codeBlock {
        val local1 = shadowLocal<ItemStack>()
        val local4 = shadowLocal<List<String>>()

        code {
            val event = CancellableEvent()
            TriggerType.TOOLTIP.triggerAll(local4, Item(local1), event)
            if (event.isCancelled())
                methodReturn()
        }
    }
}
