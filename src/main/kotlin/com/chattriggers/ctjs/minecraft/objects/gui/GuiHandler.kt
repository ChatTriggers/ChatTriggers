package com.chattriggers.ctjs.minecraft.objects.gui

import com.chattriggers.ctjs.minecraft.wrappers.Client
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

object GuiHandler {
    private var pendingGui: GuiScreen? = null

    fun openGui(gui: GuiScreen) {
        pendingGui = gui
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase == TickEvent.Phase.END)
            return

        if (pendingGui != null) {
            Client.getMinecraft().displayGuiScreen(pendingGui!!)
            pendingGui = null
        }
    }
}
