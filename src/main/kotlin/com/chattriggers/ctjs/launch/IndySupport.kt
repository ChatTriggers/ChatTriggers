package com.chattriggers.ctjs.launch

import com.chattriggers.ctjs.engine.module.ModuleManager
import org.mozilla.javascript.Undefined
import java.lang.invoke.*

object IndySupport {
    private var invocationInvalidator = SwitchPoint()
    private var areLoadersConfigured = false

    // A dummy handle which matches the expected type of an exposed ASM function, but just returns Undefined
    val DUMMY_INVOKE_HANDLE: MethodHandle = MethodHandles.dropArguments(
        MethodHandles.constant(Any::class.java, Undefined.instance),
        0,
        Array<Any?>::class.java,
    )

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
        val targetHandle = if (areLoadersConfigured) {
            // If we are loaded, then make an initial lookup to the target function. This is where we want
            // our bytecode invoke to actually point to.
            ModuleManager.asmInvokeLookup(moduleName, functionID)
        } else {
            // Otherwise, we are in the middle of a load (or the user has called /ct unload for some reason).
            // In this case, there's nothing we can really do, so we point to a dummy handle which will return
            // the Undefined sentinel.
            DUMMY_INVOKE_HANDLE
        }

        // When we reload, we need to re-resolve all js invocation targets because our old script engine has
        // been thrown away and recreated, plus the user may have changed code in their targeted function
        val initTarget = callSite.target

        // This switch point will be our indicator to know if the user has reloaded. As soon as the user reloads,
        // this switch should get flipped, immediately making the call site link to the fallback MethodHandle, which
        // in this case is this very method, which will let it re-run the lookup and such. The "flip" happens during
        // the invocation of invalidateInvocations below.
        val guardedTarget = invocationInvalidator.guardWithTest(targetHandle, initTarget)

        // We now have a target that is very fast to call back into the target method, and
        // can survive reloads perfectly well, so we want our call site to now point to said target.
        callSite.target = guardedTarget

        // But since we're actually still being invoked currently, we need to still need to do what the user
        // expects and call back into their target function.
        return targetHandle.invoke(args)
    }

    // Calling this causes the next invocation of any particular invokedynamic instruction to
    // rebind (i.e. call into initInvokeJS above)
    fun invalidateInvocations(areLoadersConfigured: Boolean) {
        this.areLoadersConfigured = areLoadersConfigured
        SwitchPoint.invalidateAll(arrayOf(invocationInvalidator))
        invocationInvalidator = SwitchPoint()
    }
}
