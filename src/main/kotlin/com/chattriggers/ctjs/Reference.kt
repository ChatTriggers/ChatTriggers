package com.chattriggers.ctjs

import com.chattriggers.ctjs.commands.Command
import com.chattriggers.ctjs.commands.CommandHandler
import com.chattriggers.ctjs.engine.module.ModuleManager
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.objects.display.DisplayHandler
import com.chattriggers.ctjs.minecraft.objects.gui.GuiHandler
import com.chattriggers.ctjs.minecraft.objects.message.Message
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.config.Config
import com.chattriggers.ctjs.utils.kotlin.times
import net.minecraftforge.client.ClientCommandHandler
import java.lang.Math.round
import kotlin.concurrent.thread
import kotlin.math.roundToInt

object Reference {
    const val MODID = "ct.js"
    const val MODNAME = "ChatTriggers"
    const val MODVERSION = "1.1.0"

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

        printLoadCompletionStatus(0f)

        conditionalThread {
            ModuleManager.setup()
            ModuleManager.entryPass(completionListener = ::printLoadCompletionStatus)

            ChatLib.chat("&aDone reloading scripts!")

            TriggerType.GAME_LOAD.triggerAll()
            if (World.isLoaded())
                TriggerType.WORLD_LOAD.triggerAll()

            this.isLoaded = true
        }
    }

    private fun printLoadCompletionStatus(percentComplete: Float) {
        val completionInteger = (percentComplete * 100).roundToInt()
        val prefix = "$completionInteger% ["
        val postfix = "]"

        val charWidth = Renderer.getStringWidth("=")
        val availableWidth = ChatLib.getChatWidth() - Renderer.getStringWidth(prefix + postfix)
        val correctLength = availableWidth / charWidth
        val completedLength = (percentComplete * correctLength).roundToInt()
        val fullWidth = "=" * completedLength
        val spaceWidth = Renderer.getStringWidth(" ")
        val spaceLeft = (availableWidth - completedLength * charWidth) / spaceWidth
        val padding = " " * spaceLeft

        val correctLine = "&c$prefix$fullWidth$padding$postfix"

        Message(correctLine).setChatLineId(28445).chat()
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