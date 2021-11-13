package com.chattriggers.ctjs.engine.langs.js.impl

import com.chattriggers.ctjs.engine.langs.js.objects.ASMGlobalObject
import com.chattriggers.ctjs.engine.langs.js.objects.CTExtensions
import com.chattriggers.ctjs.engine.langs.js.objects.ModuleGlobalObject
import com.reevajs.reeva.runtime.builtins.Builtin
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles

enum class CTBuiltin(clazz: Class<*>, name: String, override val debugName: String = name) : Builtin {
    StringAddColor(CTExtensions::class.java, "stringAddColor"),
    StringAddFormatting(CTExtensions::class.java, "stringAddFormatting"),
    StringRemoveFormatting(CTExtensions::class.java, "stringRemoveFormatting"),
    StringReplaceFormatting(CTExtensions::class.java, "stringReplaceFormatting"),
    NumberEaseOut(CTExtensions::class.java, "numberEaseOut"),
    NumberEaseColor(CTExtensions::class.java, "numberEaseColor"),
    GlobalSync(ASMGlobalObject::class.java, "sync"),
    GlobalPrint(ASMGlobalObject::class.java, "print"),
    GlobalSetTimeout(ModuleGlobalObject::class.java, "setTimeout"),
    GlobalSetImmediate(ModuleGlobalObject::class.java, "setImmediate"),
    GlobalCancel(ModuleGlobalObject::class.java, "cancel"),
    GlobalRegister(ModuleGlobalObject::class.java, "register"),
    GlobalEaseOut(ModuleGlobalObject::class.java, "easeOut"),
    GlobalEaseColor(ModuleGlobalObject::class.java, "easeColor");

    override val handle: MethodHandle = MethodHandles.publicLookup().findStatic(clazz, name, Builtin.METHOD_TYPE)
}
