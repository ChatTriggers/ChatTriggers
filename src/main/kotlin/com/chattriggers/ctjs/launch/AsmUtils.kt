package com.chattriggers.ctjs.launch

import com.chattriggers.ctjs.engine.module.ModuleManager
import jdk.internal.org.objectweb.asm.tree.InsnList
import me.falsehonesty.asmhelper.dsl.At
import me.falsehonesty.asmhelper.dsl.instructions.InsnListBuilder
import net.minecraft.crash.CrashReportCategory

object AsmUtils {
    fun addCrashSectionCallable(crashReport: CrashReportCategory) {
        crashReport.addCrashSectionCallable("ct.js modules") {
            ModuleManager.cachedModules.toString()
        }
    }
}

fun inject(
    _className: String,
    _at: At,
    _methodName: String,
    _methodDesc: String,
    _fieldMaps: Map<String, String>,
    _methodMaps: Map<String, String>,
    _insnList: (InsnListBuilder) -> Unit
) {
    me.falsehonesty.asmhelper.dsl.inject {
        className = _className
        methodName = _methodName
        methodDesc = _methodDesc
        at = _at
        fieldMaps = _fieldMaps
        methodMaps = _methodMaps

        insnList {
            _insnList(this)
        }
    }
}