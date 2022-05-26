package com.chattriggers.ctjs.minecraft.listeners

//#if FABRIC
//$$ import net.minecraft.util.ActionResult
//#endif

class CancellableEvent {
    private var cancelled = false

    @JvmOverloads
    fun setCanceled(newVal: Boolean = true) {
        cancelled = newVal
    }

    @JvmOverloads
    fun setCancelled(newVal: Boolean = true) {
        cancelled = newVal
    }

    fun isCancelable() = true
    fun isCancellable() = true

    fun isCancelled() = cancelled
    fun isCanceled() = cancelled

    //#if FABRIC
    //$$ fun actionResult() = if (cancelled) ActionResult.FAIL else ActionResult.PASS
    //#endif
}
