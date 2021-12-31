package com.chattriggers.ctjs.minecraft.listeners

import net.minecraft.util.ActionResult

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

    fun actionResult() = if (isCanceled()) ActionResult.FAIL else ActionResult.PASS
}
