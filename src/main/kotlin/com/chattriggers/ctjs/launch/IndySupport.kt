package com.chattriggers.ctjs.launch

import com.chattriggers.ctjs.engine.module.ModuleManager
import java.lang.invoke.*

object IndySupport {
    private var invocationInvalidator = SwitchPoint()

    @JvmStatic
    fun bootstrapInvokeJS(
        lookup: MethodHandles.Lookup,
        name: String,
        type: MethodType,
        moduleName: String,
        functionID: String
    ): CallSite {
        val callSite = MutableCallSite(type)

        // At bootstrap, link to an intermediary method [initInvokeJS] that will do the function lookup when the method
        // is actually first called.
        // That intermediary method doesn't meet the required type of (Object[])Object,
        // so we need to meddle with it to make sure it does fit.
        // It requires the call site, module name, and function ID as parameters,
        // which are all constants known to us at this point in time,
        // so we will simply insert said arguments into that handle, leaving us with the desired type.
        val initHandle = MethodHandles.insertArguments(
            lookup.findStatic(
                IndySupport::class.java,
                "initInvokeJS",
                MethodType.methodType(
                    Any::class.java,
                    MutableCallSite::class.java,
                    String::class.java,
                    String::class.java,
                    Array<Any?>::class.java
                )
            ), 0, callSite, moduleName, functionID
        )

        callSite.target = initHandle.asType(type)
        return callSite
    }

    @JvmStatic
    fun initInvokeJS(callSite: MutableCallSite, moduleName: String, functionID: String, args: Array<Any?>): Any? {
        // Make an initial lookup to the target function, this is where we want our bytecode invoke
        // to actually point to.
        val targetHandle = ModuleManager.asmInvokeLookup(moduleName, functionID)

        // Until we /ct load that is. When we reload, we need to re-resolve all js invocation targets
        // because our old script engine has been thrown away and recreated, plus the user
        // may have changed code in their targeted function
        val initTarget = callSite.target

        // This switch point will be our indicator to know if the user has reloaded.
        // As soon as the user reloads, this switch should get flipped, immediately making
        // the call site link to the fallback MethodHandle, which in this case is this very
        // method, which will let it re-run the lookup and such.
        val guardedTarget = invocationInvalidator.guardWithTest(targetHandle, initTarget)

        // We now have a target that is very fast to call back into the target method, and
        // can survive reloads perfectly well, so we want our call site to now point to said target.
        callSite.target = guardedTarget

        // But since we're actually still being invoked currently, we need to still need to do what the user
        // expects and call back into their target function.
        return targetHandle.invokeWithArguments(*args)
    }

    fun invalidateInvocations() {
        SwitchPoint.invalidateAll(arrayOf(invocationInvalidator))
        invocationInvalidator = SwitchPoint()
    }
}
