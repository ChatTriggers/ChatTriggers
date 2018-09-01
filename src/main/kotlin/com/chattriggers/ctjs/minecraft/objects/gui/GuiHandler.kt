package com.chattriggers.ctjs.minecraft.objects.gui

import com.chattriggers.ctjs.utils.kotlin.KotlinListener
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

@KotlinListener
object GuiHandler {
    private val GUIs: MutableMap<GuiScreen, Int> = mutableMapOf()

    fun openGui(gui: GuiScreen) {
        this.GUIs[gui] = 1
    }

    fun clearGuis() {
        this.GUIs.clear()
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        this.GUIs.forEach {
            if (it.value == 0) {
                Minecraft.getMinecraft().displayGuiScreen(it.key)
                this.GUIs[it.key] = -1
            } else {
                this.GUIs[it.key] = 0
            }
        }

        this.GUIs.entries.removeIf {
            it.value == -1
        }
    }
}