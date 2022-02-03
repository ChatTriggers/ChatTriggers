package com.chattriggers.ctjs.minecraft.objects.keybind

import com.chattriggers.ctjs.minecraft.wrappers.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

internal object KeyBindHandler {
    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    private val keyBinds = mutableListOf<KeyBind>()

    fun registerKeyBind(keyBind: KeyBind) {
        keyBinds.add(keyBind)
    }

    fun clearKeyBinds() = keyBinds.clear()

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (World.getWorld() == null || event.phase == TickEvent.Phase.END)
            return

        keyBinds.forEach(KeyBind::onTick)
    }

    @SubscribeEvent
    fun onKeyInput(event: InputEvent.KeyInputEvent) {
        keyBinds.forEach(KeyBind::onKeyInput)
    }
}
