package com.chattriggers.ctjs.minecraft.objects.gui

import gg.essential.api.utils.GuiUtil
import net.minecraft.client.gui.GuiScreen

// TODO(BREAKING): Delete this (and replace providedLibs reference with GuiUtil)
object GuiHandler {
    fun openGui(gui: GuiScreen) = GuiUtil.open(gui)
}
