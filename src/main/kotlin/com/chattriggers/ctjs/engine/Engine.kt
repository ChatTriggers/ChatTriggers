package com.chattriggers.ctjs.engine

import java.util.Deque
import java.util.ArrayDeque
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

object Engine {
    internal val loaders = object : ThreadLocal<Deque<ILoader>>() {
        override fun initialValue() = ArrayDeque<ILoader>()
    }

    fun getLoader() = loaders.get().last

    @OptIn(ExperimentalContracts::class)
    internal inline fun <T> withLoader(loader: ILoader, crossinline block: () -> T): T {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }

        val deque = loaders.get()
        deque.addLast(loader)
        return try {
            block()
        } finally {
            deque.removeLast()
        }
    }
}