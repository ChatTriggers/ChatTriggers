package com.chattriggers.ctjs.launch.plugin

import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.triggers.TriggerType
import dev.falsehonesty.asmhelper.dsl.At
import dev.falsehonesty.asmhelper.dsl.InjectionPoint
import dev.falsehonesty.asmhelper.dsl.code.CodeBlock.Companion.methodReturn
import dev.falsehonesty.asmhelper.dsl.inject
import dev.falsehonesty.asmhelper.dsl.instructions.Descriptor
import net.minecraft.client.Minecraft
import net.minecraft.client.shader.Framebuffer
import net.minecraft.util.ScreenShotHelper
import java.io.File

fun injectMinecraft() {
    injectDispatchKeypresses()
    injectDisplayGuiScreen()
    injectStartGame()
}

fun injectDispatchKeypresses() = inject {
    className = "net/minecraft/client/Minecraft"
    methodName = "dispatchKeypresses"
    methodDesc = "()V"

    at = At(
        InjectionPoint.INVOKE(
            Descriptor(
                "net/minecraft/client/gui/GuiIngame",
                "getChatGUI",
                "()Lnet/minecraft/client/gui/GuiNewChat;"
            ),
            ordinal = 1
        ),
        before = true,
        shift = -2
    )

    fieldMaps = mapOf(
        "mcDataDir" to "field_71412_D",
        "displayWidth" to "field_71443_c",
        "displayHeight" to "field_71440_d",
        "framebufferMc" to "field_147124_at"
    )

    methodMaps = mapOf(
        "saveScreenshot" to "func_148260_a",
        "func_146158_b" to "getChatGUI",
        "func_152348_aa" to "dispatchKeypresses"
    )

    codeBlock {
        val mcDataDir = shadowField<File>()
        val displayWidth = shadowField<Int>()
        val displayHeight = shadowField<Int>()
        val framebufferMc = shadowField<Framebuffer>()

        code {
            val chatComponent = ScreenShotHelper.saveScreenshot(mcDataDir, displayWidth, displayHeight, framebufferMc)
            if (chatComponent != null)
                TextComponent(chatComponent).chat()
            methodReturn()
        }
    }

//    insnList {
//        invokeStatic("net/minecraft/util/ScreenShotHelper", "saveScreenshot", "(L$FILE;IIL$FRAME_BUFFER;)L$ICHAT_COMPONENT;") {
//            getLocalField(className, "mcDataDir", "L$FILE;")
//            getLocalField(className, "displayWidth", "I")
//            getLocalField(className, "displayHeight", "I")
//            getLocalField(className, "framebufferMc", "L$FRAME_BUFFER;")
//        }
//        val chatComponent = astore()
//        load(chatComponent)
//
//        ifClause(JumpCondition.NULL) {
//            createInstance("com/chattriggers/ctjs/minecraft/objects/message/TextComponent", "(L$ICHAT_COMPONENT;)V") {
//                load(chatComponent)
//            }
//            invokeVirtual("com/chattriggers/ctjs/minecraft/objects/message/TextComponent", "chat", "()V")
//        }
//
//        methodReturn()
//    }
}

fun injectDisplayGuiScreen() = inject {
    className = "net/minecraft/client/Minecraft"
    methodName = "displayGuiScreen"
    methodDesc = "(Lnet/minecraft/client/gui/GuiScreen;)V"

    at = At(
        InjectionPoint.INVOKE(
            Descriptor(
                "net/minecraft/client/gui/GuiScreen",
                "onGuiClosed",
                "()V"
            )
        )
    )

    methodMaps = mapOf(
        "func_147108_a" to "displayGuiScreen",
        "onGuiClosed" to "func_146281_b"
    )

    codeBlock {
        code {
            TriggerType.GuiClosed.triggerAll(Minecraft.getMinecraft().currentScreen)
        }
    }
}

fun injectStartGame() = inject {
    className = "net/minecraft/client/Minecraft"
    methodName = "startGame"
    methodDesc = "()V"
    at = At(InjectionPoint.TAIL)

    methodMaps = mapOf("func_71384_a" to "startGame")

    codeBlock {
        code {
            TriggerType.GameLoad.triggerAll()
        }
    }
}
