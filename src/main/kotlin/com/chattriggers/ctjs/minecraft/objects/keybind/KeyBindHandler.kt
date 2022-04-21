package com.chattriggers.ctjs.minecraft.objects.keybind

import com.chattriggers.ctjs.minecraft.wrappers.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.util.concurrent.CopyOnWriteArrayList

internal object KeyBindHandler {
    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    private val keyBinds = CopyOnWriteArrayList<KeyBind>()

    fun registerKeyBind(keyBind: KeyBind) {
        keyBinds.add(keyBind)
    }

    fun unregisterKeyBind(keyBind: KeyBind) {
        keyBinds.remove(keyBind)
    }

    fun clearKeyBinds() = keyBinds.clear()

    fun getKeyBinds() = keyBinds

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!World.isLoaded() || event.phase == TickEvent.Phase.END)
            return

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
