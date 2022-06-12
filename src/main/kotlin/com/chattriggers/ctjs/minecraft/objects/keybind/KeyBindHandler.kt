package com.chattriggers.ctjs.minecraft.objects.keybind

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.triggers.EventType
import java.util.concurrent.CopyOnWriteArrayList

internal object KeyBindHandler {
    private val keyBinds = CopyOnWriteArrayList<KeyBind>()

    init {
        CTJS.addEventListener(EventType.Tick) {
            if (!World.isLoaded())
                return@addEventListener

            keyBinds.forEach {
                // TODO: This causes null pointers rarely, crashing the game.
                //  Catching solves this for now.
                try {
                    it.onTick()
                } catch (ignored: Exception) {
                }
            }
        }
    }

    fun registerKeyBind(keyBind: KeyBind) {
        keyBinds.add(keyBind)
    }

    fun unregisterKeyBind(keyBind: KeyBind) {
        keyBinds.remove(keyBind)
    }

    fun clearKeyBinds() = keyBinds.clear()

    fun getKeyBinds() = keyBinds
}
