package com.chattriggers.ctjs.launch.plugin

import me.falsehonesty.asmhelper.dsl.At
import me.falsehonesty.asmhelper.dsl.InjectionPoint
import me.falsehonesty.asmhelper.dsl.inject
import me.falsehonesty.asmhelper.dsl.instructions.*


fun injectMinecraft() = inject {
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

    insnList {
        invokeStatic("net/minecraft/util/ScreenShotHelper", "saveScreenshot", "(L$FILE;IIL$FRAME_BUFFER;)L$ICHAT_COMPONENT;") {
            getLocalField(className, "mcDataDir", "L$FILE;")
            getLocalField(className, "displayWidth", "I")
            getLocalField(className, "displayHeight", "I")
            getLocalField(className, "framebufferMc", "L$FRAME_BUFFER;")
        }
        val chatComponent = astore()
        load(chatComponent)

        ifClause(JumpCondition.NULL) {
            createInstance("com/chattriggers/ctjs/minecraft/objects/message/TextComponent", "(L$ICHAT_COMPONENT;)V") {
                load(chatComponent)
            }
            invokeVirtual("com/chattriggers/ctjs/minecraft/objects/message/TextComponent", "chat", "()V")
        }

        methodReturn()
    }
}