package com.chattriggers.ctjs.minecraft.wrappers.utils

@Suppress("unused")
abstract class WrappedThread {
    abstract fun start()

    // Provide the following methods as no-ops to avoid breaking
    // changes, as this class use to extend Thread
    fun run() {}
    fun stop() {}
    fun interrupt() {}
    fun isInterrupted() = false
    fun destroy() {}
    fun isAlive() = true
    fun suspend() {}
    fun resume() {}
    fun getId() = 0L

    companion object {
        @JvmStatic
        @JvmOverloads
        fun sleep(millis: Long, nanos: Int = 0) = Thread.sleep(millis, nanos)

        @JvmStatic
        fun currentThread(): Thread = Thread.currentThread()
    }
}
