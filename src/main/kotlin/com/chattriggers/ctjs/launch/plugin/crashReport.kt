package com.chattriggers.ctjs.launch.plugin

import com.chattriggers.ctjs.engine.module.ModuleManager
import dev.falsehonesty.asmhelper.dsl.At
import dev.falsehonesty.asmhelper.dsl.InjectionPoint
import dev.falsehonesty.asmhelper.dsl.inject
import net.minecraft.crash.CrashReportCategory

fun injectCrashReport() = inject {
    className = "net/minecraft/crash/CrashReport"
    methodName = "populateEnvironment"
    methodDesc = "()V"
    at = At(InjectionPoint.HEAD)

    fieldMaps = mapOf("theReportCategory" to "field_85061_c")
    methodMaps = mapOf("func_71504_g" to "populateEnvironment")

    codeBlock {
        val theReportCategory = shadowField<CrashReportCategory>()

        code {
            theReportCategory.addCrashSection("ChatTriggers modules", ModuleManager.cachedModules)
        }
    }
}
