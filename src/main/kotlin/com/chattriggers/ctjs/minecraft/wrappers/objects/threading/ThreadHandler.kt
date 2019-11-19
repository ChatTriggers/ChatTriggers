package com.chattriggers.ctjs.minecraft.wrappers.objects.threading

import java.util.*
import java.util.function.Consumer

class ThreadHandler {
    private val activeThreads: MutableList<WrappedThread>

    init {
        this.activeThreads = ArrayList()
    }

    fun addThread(thread: WrappedThread) =
        this.activeThreads.add(thread)

    fun removeThread(thread: WrappedThread) =
        activeThreads.removeIf { thread1 -> thread1 === thread }

    fun stopThreads() {
        activeThreads.forEach(Consumer<WrappedThread> { it.interrupt() })
        activeThreads.clear()
    }
}