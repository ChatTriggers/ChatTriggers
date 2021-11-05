package com.chattriggers.ctjs.launch.plugin

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent
import com.chattriggers.ctjs.triggers.TriggerType
import dev.falsehonesty.asmhelper.dsl.At
import dev.falsehonesty.asmhelper.dsl.InjectionPoint
import dev.falsehonesty.asmhelper.dsl.code.CodeBlock.Companion.aReturn
import dev.falsehonesty.asmhelper.dsl.code.CodeBlock.Companion.asm
import dev.falsehonesty.asmhelper.dsl.inject

fun injectScreenshotHelper() = inject {
    className = "net/minecraft/util/ScreenShotHelper"
    methodName = "saveScreenshot"
    methodDesc = "(L$FILE;IILnet/minecraft/client/shader/Framebuffer;)L$ICHAT_COMPONENT;"
    at = At(InjectionPoint.HEAD)

    methodMaps = mapOf(
        "getTimestampedPNGFileForDirectory" to "func_74290_a",
        "func_148260_a" to "saveScreenshot"
    )

    codeBlock {
        val local5 = shadowLocal<String>()

        code {
            val event = CancellableEvent()

            asm {
                // Private method
                invokeStatic("net/minecraft/util/ScreenShotHelper", "getTimestampedPNGFileForDirectory", "(L$FILE;)L$FILE;") {
                    createInstance(FILE, "(L$FILE;Ljava/lang/String;)V") {
                        aload(0)
                        ldc("screenshots")
                    }
                }
                invokeVirtual(FILE, "getName", "()Ljava/lang/String;")
                astore(5)
            }

            TriggerType.ScreenshotTaken.triggerAll(local5, event)
            if (event.isCancelled())
                aReturn(null)
        }
    }
}
