package com.chattriggers.ctjs.commands

import com.chattriggers.ctjs.Reference
import com.chattriggers.ctjs.engine.module.ModuleManager
import com.chattriggers.ctjs.engine.module.ModulesGui
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.listeners.ClientListener
import com.chattriggers.ctjs.printTraceToConsole
import com.chattriggers.ctjs.utils.Config
import com.chattriggers.ctjs.utils.kotlin.setChatLineId
import com.chattriggers.ctjs.utils.kotlin.toVersion
import gg.essential.api.commands.*
import gg.essential.api.utils.GuiUtil
import gg.essential.universal.wrappers.message.UMessage
import gg.essential.universal.wrappers.message.UTextComponent
import net.minecraft.event.ClickEvent
import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.File
import java.io.IOException
import gg.essential.api.commands.Command as EssentialCommand

object CTCommand : EssentialCommand("ct") {
    override val commandAliases = setOf(Alias("chattriggers"))

    private const val idFixed = 90123 // ID for dumped chat
    private var idFixedOffset = -1 // ID offset (increments)

    private fun dumpChat(lines: Int) = dumpList(ClientListener.chatHistory, lines)
    private fun dumpActionBar(lines: Int) = dumpList(ClientListener.actionBarHistory, lines)
    private fun dumpList(messages: List<String>, lines: Int) {
        clearOldDump()

        val toDump = lines.coerceAtMost(messages.size)
        UMessage("&6&m${ChatLib.getChatBreak()}").setChatLineId(idFixed).chat()
        var msg: String
        for (i in 0 until toDump) {
            msg = ChatLib.replaceFormatting(messages[messages.size - toDump + i])
            UMessage(
                UTextComponent(msg).apply {
                    setClick(ClickEvent.Action.RUN_COMMAND, "/ct copy $msg")
                    hoverValue = ChatLib.addColor("&eClick here to copy this message.")
                    formatted = false
                }
            ).apply {
                isFormatted = false
                chatLineId = idFixed + lines + 1
                chat()
            }
        }
        UMessage("&6&m${ChatLib.getChatBreak()}").setChatLineId(idFixed + lines + 1).chat()

        idFixedOffset = idFixed + lines + 1
    }

    private fun clearOldDump() {
        if (idFixedOffset == -1) return
        while (idFixedOffset >= idFixed)
            ChatLib.clearChat(idFixedOffset--)
        idFixedOffset = -1
    }

    @DefaultHandler
    fun handle() = ChatLib.say("/ct help")

    @SubCommand("load", aliases = ["reload"], description = "Reloads all ChatTriggers modules.")
    fun load() = Reference.loadCT()

    @SubCommand("unload", description = "Unloads all of ChatTriggers.")
    fun unload() = Reference.unloadCT()

    @SubCommand("files", aliases = ["file"], description = "Opens the ChatTriggers folder.")
    fun files() {
        try {
            Desktop.getDesktop().open(File("./config/ChatTriggers"))
        } catch (exception: IOException) {
            exception.printTraceToConsole()
            ChatLib.chat("&cCould not open file location")
        }
    }

    @SubCommand("import", description = "Imports a module.")
    fun import(@DisplayName("module name") moduleName: String) {
        if (ModuleManager.cachedModules.any { it.name.equals(moduleName, ignoreCase = true) }) {
            ChatLib.chat("&cModule $moduleName is already installed!")
        } else {
            ChatLib.chat("&cImporting ${moduleName}...")
            Reference.conditionalThread {
                val (module, dependencies) = ModuleManager.importModule(moduleName)
                if (module == null) {
                    ChatLib.chat("&cUnable to import module $moduleName")
                    return@conditionalThread
                }

                val allModules = listOf(module) + dependencies
                val modVersion = Reference.MODVERSION.toVersion()
                allModules.forEach {
                    val version = it.targetModVersion ?: return@forEach
                    if (version.majorVersion < modVersion.majorVersion)
                        ModuleManager.tryReportOldVersion(it)
                }

                ChatLib.chat("&aSuccessfully imported ${module.metadata.name ?: module.name}")
                if (Config.moduleImportHelp && module.metadata.helpMessage != null) {
                    ChatLib.chat(module.metadata.helpMessage.toString().take(383))
                }
            }
        }
    }

    @SubCommand("delete", description = "Deletes a module.")
    fun delete(@DisplayName("module name") moduleName: String) = when {
        ModuleManager.deleteModule(moduleName) -> ChatLib.chat("&aDeleted")
        else -> ChatLib.chat("&cFailed to delete $moduleName")
    }

    @SubCommand("modules", description = "Opens the modules GUI.")
    fun modules() = GuiUtil.open(ModulesGui)

    @SubCommand("console", description = "Opens the ChatTriggers console.")
    fun console(@DisplayName("console language") language: String?) {
        if (language == null) {
            ModuleManager.generalConsole.showConsole()
        } else ModuleManager.getConsole(language).showConsole()
    }

    @SubCommand("config", aliases = ["settings", "setting"], description = "Opens the ChatTriggers settings.")
    fun config() = GuiUtil.open(Config.gui())

    @SubCommand("simulate", aliases = ["sim"], description = "Simulates a received chat message.")
    fun simulate(@Greedy message: String) = ChatLib.simulateChat(message)

    @SubCommand("dump", description = "Dumps previous chat messages into chat.")
    fun dump(@Options(["chat", "actionbar"]) type: String?, lines: Int?) {
        when (type?.lowercase()) {
            "chat" -> dumpChat(lines ?: 100)
            "actionbar" -> dumpActionBar(lines ?: 100)
            null -> dumpChat(100)
        }
    }

    @SubCommand("copy", description = "Copies a message to the clipboard. Used in /ct dump.")
    fun copy(@Greedy message: String?) {
        clearOldDump()
        Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(message), null)
    }
}
