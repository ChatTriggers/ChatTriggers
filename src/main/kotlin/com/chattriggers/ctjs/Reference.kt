package com.chattriggers.ctjs

import com.chattriggers.ctjs.commands.Command
import com.chattriggers.ctjs.commands.CommandHandler
import com.chattriggers.ctjs.engine.module.ModuleManager
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.objects.display.DisplayHandler
import com.chattriggers.ctjs.minecraft.objects.gui.GuiHandler
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.config.Config
import net.minecraftforge.client.ClientCommandHandler
import kotlin.concurrent.thread

object Reference {
    const val MODID = "ct.js"
    const val MODNAME = "ChatTriggers"
    const val MODVERSION = "1.0.0"

    var isLoaded = true

    @Deprecated("Does not provide any additional functionality", ReplaceWith("loadCT"))
    fun reloadCT() = loadCT()

    fun unloadCT(asCommand: Boolean = true) {
        TriggerType.WORLD_UNLOAD.triggerAll()
        TriggerType.GAME_UNLOAD.triggerAll()

        DisplayHandler.clearDisplays()
        GuiHandler.clearGuis()
        CommandHandler.getCommandList().clear()
        ModuleManager.teardown()

        if (asCommand) {
            ChatLib.chat("&7Unloaded all of ChatTriggers")
            this.isLoaded = false
        }
    }

    fun loadCT() {
        if (!this.isLoaded) return
        this.isLoaded = false

        unloadCT(false)

        ChatLib.chat("&cReloading ct.js scripts...")
        ClientCommandHandler.instance.commandSet.removeIf { it is Command }
        ClientCommandHandler.instance.commandMap.entries.removeIf { it.value is Command }

        CTJS.loadConfig()

        conditionalThread {
            ModuleManager.setup()
            ModuleManager.entryPass()

            ChatLib.chat("&aDone reloading scripts!")

            TriggerType.GAME_LOAD.triggerAll()
            if (World.isLoaded())
                TriggerType.WORLD_LOAD.triggerAll()

            this.isLoaded = true
        }
    }

    fun conditionalThread(block: () -> Unit) {
        if (Config.threadedLoading) {
            thread { block() }
        } else {
            block()
        }
    }
}

fun Exception.print() {
    try {
        ModuleManager.generalConsole.printStackTrace(this)
    } catch (exception: Exception) {
        this.printStackTrace()
    }
}

fun String.print() {
    try {
        ModuleManager.generalConsole.out.println(this)
    } catch (exception: Exception) {
        println(this)
    }
}