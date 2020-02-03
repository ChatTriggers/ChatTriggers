package com.chattriggers.ctjs.launch.plugin

import me.falsehonesty.asmhelper.dsl.At
import me.falsehonesty.asmhelper.dsl.InjectionPoint
import me.falsehonesty.asmhelper.dsl.inject
import me.falsehonesty.asmhelper.dsl.instructions.Descriptor
import me.falsehonesty.asmhelper.dsl.instructions.invokeKOBjectFunction

fun injectGuiMainMenu() = inject {
    className = "net/minecraft/client/gui/GuiMainMenu"
    methodName = "drawScreen"
    methodDesc = "(IIF)V"

    at = At(
        InjectionPoint.INVOKE(
            Descriptor(
                "net/minecraftforge/client/ForgeHooksClient",
                "renderMainMenu",
                "(Lnet/minecraft/client/gui/GuiMainMenu;Lnet/minecraft/client/gui/FontRenderer;II)V"
            )
        )
    )

    insnList {
        invokeKOBjectFunction("com/chattriggers/ctjs/utils/UpdateChecker", "drawUpdateMessage", "()V")
    }
}