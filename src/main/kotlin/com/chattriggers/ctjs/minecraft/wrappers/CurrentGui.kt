package com.chattriggers.ctjs.minecraft.wrappers

import net.minecraft.client.gui.GuiScreen

/**
 * Used from [Client.currentGui] to get the currently open GUI properties
 */
class CurrentGui {
    /**
     * @return the Java class name of the currently open gui, for example, "GuiChest"
     */
    fun getClassName(): String = get()?.javaClass?.simpleName ?: "null"

    /**
     * @return the Minecraft gui class that is currently open
     */
    fun get(): GuiScreen? = Client.getMinecraft().currentScreen

    /**
     * Closes the currently open gui
     */
    fun close() {
        Player.getPlayer()?.closeScreen()
    }
}