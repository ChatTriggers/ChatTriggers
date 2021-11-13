package com.chattriggers.ctjs.minecraft.objects.gui

import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

@External
object GuiHandler {
    private val GUIs: MutableMap<GuiScreen, Int> = mutableMapOf()

    fun openGui(gui: GuiScreen) {
        GUIs[gui] = 1
    }

    fun clearGuis() {
        GUIs.clear()
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase == TickEvent.Phase.END)
            return

        GUIs.forEach {
            if (it.value == 0) {
                Minecraft.getMinecraft().displayGuiScreen(it.key)
                GUIs[it.key] = -1
            } else {
                GUIs[it.key] = 0
            }
        }

        GUIs.entries.removeIf {
            it.value == -1
        }
    }
}
