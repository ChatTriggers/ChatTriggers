package com.chattriggers.ctjs

import com.chattriggers.ctjs.commands.Command
import com.chattriggers.ctjs.engine.module.ModuleManager
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.listeners.MouseListener
import com.chattriggers.ctjs.minecraft.objects.display.DisplayHandler
import com.chattriggers.ctjs.minecraft.objects.keybind.KeyBind
import com.chattriggers.ctjs.minecraft.objects.message.Message
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.Config
import com.chattriggers.ctjs.utils.console.Console
import com.chattriggers.ctjs.utils.console.LogType
import com.chattriggers.ctjs.utils.kotlin.times
import kotlin.concurrent.thread
import kotlin.math.roundToInt

object Reference {
    const val MODID = "chattriggers"
    const val MODNAME = "ChatTriggers"
    const val MODVERSION = "2.1.5"

    var isLoaded = true

    @Deprecated("Does not provide any additional functionality", ReplaceWith("loadCT"))
    @JvmStatic
    fun reloadCT() = loadCT()

    @JvmStatic
    fun unloadCT(asCommand: Boolean = true) {
        TriggerType.WorldUnload.triggerAll()
        TriggerType.GameUnload.triggerAll()

        isLoaded = false

        DisplayHandler.clearDisplays()
        ModuleManager.teardown()
        MouseListener.clearListeners()
        KeyBind.clearKeyBinds()

        Command.activeCommands.values.toList().forEach(Command::unregister)

        Client.scheduleTask {
            CTJS.images.forEach { it.getTexture().deleteGlTexture() }
            CTJS.images.clear()
        }

        if (asCommand) {
            ChatLib.chat("&7Unloaded all of ChatTriggers")
        }
    }

    @JvmStatic
    fun loadCT() {
        Client.getMinecraft().gameSettings.saveOptions()
        unloadCT(false)

        ChatLib.chat("&cReloading ChatTriggers scripts...")

        printLoadCompletionStatus(0f)

        conditionalThread {
            ModuleManager.setup()
            ModuleManager.entryPass(completionListener = ::printLoadCompletionStatus)
            MouseListener.registerTriggerListeners()

            Client.getMinecraft().gameSettings.loadOptions()
            ChatLib.chat("&aDone reloading scripts!")
            isLoaded = true

            TriggerType.GameLoad.triggerAll()
            if (World.isLoaded())
                TriggerType.WorldLoad.triggerAll()
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

    @JvmStatic
    fun conditionalThread(block: () -> Unit) {
        if (Config.threadedLoading) {
            thread {
                try {
                    block()
                } catch (e: Throwable) {
                    e.printTraceToConsole()
                }
            }
        } else {
            block()
        }
    }
}

fun Any.printToConsole(console: Console = ModuleManager.generalConsole, logType: LogType = LogType.INFO) {
    console.println(this, logType)
}

fun Throwable.printTraceToConsole(console: Console = ModuleManager.generalConsole) = console.printStackTrace(this)
