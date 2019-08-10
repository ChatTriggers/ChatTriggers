package com.chattriggers.ctjs.minecraft.listeners

class CancellableEvent() {
    private var cancelled = false

    @JvmOverloads
    fun setCanceled(newVal: Boolean = true) {
        cancelled = newVal
    }

    @JvmOverloads
    fun setCancelled(newVal: Boolean = true) {
        cancelled = newVal
    }

    fun isCancelled() = cancelled
    fun isCanceled() = cancelled
}