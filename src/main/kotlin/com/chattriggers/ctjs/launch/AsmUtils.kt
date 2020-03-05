package com.chattriggers.ctjs.launch

import com.chattriggers.ctjs.engine.module.ModuleManager
import jdk.internal.org.objectweb.asm.tree.InsnList
import me.falsehonesty.asmhelper.dsl.At
import me.falsehonesty.asmhelper.dsl.instructions.InsnListBuilder
import net.minecraft.crash.CrashReportCategory
import org.mozilla.javascript.Context
import org.mozilla.javascript.NativeJavaObject
import org.mozilla.javascript.Wrapper
import java.lang.invoke.CallSite
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
import java.lang.invoke.MutableCallSite

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
    _insnList: (Wrapper) -> Unit
) {
    me.falsehonesty.asmhelper.dsl.inject {
        className = _className
        methodName = _methodName
        methodDesc = _methodDesc
        at = _at
        fieldMaps = _fieldMaps
        methodMaps = _methodMaps

        insnList {
            _insnList(NativeJavaObject(Context.getScope(), this, InsnListBuilder::class.java))
        }
    }
}