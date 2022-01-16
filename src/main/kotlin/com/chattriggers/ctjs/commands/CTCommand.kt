package com.chattriggers.ctjs.commands

import com.chattriggers.ctjs.Reference
import com.chattriggers.ctjs.engine.module.ModuleManager
import com.chattriggers.ctjs.engine.module.ModulesGui
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.listeners.ClientListener
import com.chattriggers.ctjs.minecraft.objects.message.Message
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.printTraceToConsole
import com.chattriggers.ctjs.utils.Config
import com.chattriggers.ctjs.utils.kotlin.toVersion
import com.mojang.brigadier.CommandDispatcher
import gg.essential.api.utils.GuiUtil
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.File
import java.io.IOException

object CTCommand {
    private const val idFixed = 90123 // ID for dumped chat
    private var idFixedOffset = -1 // ID offset (increments)

    private val commands = listOf(
        subcommand("load", "reload") { Reference.loadCT() },
        subcommand("unload") { Reference.unloadCT() },
        subcommand("files", "file") { openFileLocation() },
        subcommand("import") {
            if (it.isEmpty()) {
                ChatLib.chat("&c/ct import [module name]")
            } else import(it[0])
        },
        subcommand("delete") {
            when {
                it.isEmpty() -> ChatLib.chat("&c/ct delete [module name]")
                ModuleManager.deleteModule(it[0]) -> ChatLib.chat("&aDeleted")
                else -> ChatLib.chat("&cFailed to delete ${it[0]}")
            }
        },
        subcommand("modules") { GuiUtil.open(ModulesGui) },
        subcommand("console") {
            if (it.isEmpty()) {
                ModuleManager.generalConsole.showConsole()
            } else ModuleManager.getConsole(it[0]).showConsole()
        },
        subcommand("config", "settings", "setting") { GuiUtil.open(Config.gui()!!) },
        subcommand("sim", "simulate") { ChatLib.simulateChat(it.joinToString(" ")) },
        subcommand("dump") { dump(it) },
        subcommand("copy") { copyArgsToClipboard(it) },
        subcommand("generatebindings") { Reference.generateBindings() }
    )

    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        var command = CommandManager.literal("ct")

        for ((aliases, action) in commands) {
            for (alias in aliases) {
                command = command.then(CommandManager.literal(alias).executes { ctx ->
                    action(ctx.nodes.map {
                        ctx.input.substring(it.range.start, it.range.end)
                    })
                    1
                })
            }
        }

        dispatcher.register(command)
    }

    private fun subcommand(vararg aliases: String, executor: (List<String>) -> Unit) = Pair(aliases.toList(), executor)

    private fun import(moduleName: String) {
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
                    val version = it.metadata.version?.toVersion() ?: return@forEach
                    if (version.major < modVersion.major)
                        ModuleManager.tryReportOldVersion(it)
                }

                ChatLib.chat("&aSuccessfully imported ${module.metadata.name ?: module.name}")
                if (Config.moduleImportHelp && module.metadata.helpMessage != null) {
                    ChatLib.chat(module.metadata.helpMessage.toString().take(383))
                }
            }
        }
    }

    private fun getUsage() = """
        &b&m${ChatLib.getChatBreak()}
        &c/ct load &7- &oReloads all of the ChatTriggers modules.
        &c/ct import [module] &7- &oImports a module.
        &c/ct delete [module] &7- &oDeletes a module.
        &c/ct files &7- &oOpens the ChatTriggers folder.
        &c/ct modules &7- &oOpens the modules GUI.
        &c/ct console [language] &7- &oOpens the ChatTriggers console.
        &c/ct simulate [message] &7- &oSimulates a received chat message.
        &c/ct dump &7- &oDumps previous chat messages into chat.
        &c/ct settings &7- &oOpens the ChatTriggers settings.
        &c/ct &7- &oDisplays this help dialog.
        &b&m${ChatLib.getChatBreak()}
    """.trimIndent()

    private fun openFileLocation() {
        try {
            Desktop.getDesktop().open(File("./config/ChatTriggers"))
        } catch (exception: IOException) {
            exception.printTraceToConsole()
            ChatLib.chat("&cCould not open file location")
        }
    }

    private fun dump(args: List<String>) {
        if (args.size == 1) {
            dumpChat()
            return
        }
        when (args[1].lowercase()) {
            "chat" -> {
                if (args.size == 2) dumpChat()
                else dumpChat(args[2].toInt())
            }
            "actionbar" -> {
                if (args.size == 2) dumpActionBar()
                else dumpActionBar(args[2].toInt())
            }
        }
    }

    private fun dumpChat(lines: Int = 100) = dumpList(ClientListener.chatHistory, lines)
    private fun dumpActionBar(lines: Int = 100) = dumpList(ClientListener.actionBarHistory, lines)
    private fun dumpList(messages: List<String>, lines: Int) {
        clearOldDump()

        var toDump = lines
        if (toDump > messages.size)
            toDump = messages.size
        Message("&6&m${ChatLib.getChatBreak()}").setChatLineId(idFixed).chat()
        var msg: String
        for (i in 0 until toDump) {
            msg = ChatLib.replaceFormatting(messages[messages.size - toDump + i])
            Message(
                TextComponent(msg)
                    .setClick("run_command", "/ct copy $msg")
                    .setHoverValue(ChatLib.addColor("&eClick here to copy this message."))
                    .setFormatted(false)
            ).setFormatted(false).setChatLineId(idFixed + i + 1).chat()
        }
        Message("&6&m${ChatLib.getChatBreak()}").setChatLineId(idFixed + lines + 1).chat()

        idFixedOffset = idFixed + lines + 1
    }

    private fun clearOldDump() {
        if (idFixedOffset == -1)
            return
        while (idFixedOffset >= idFixed)
            ChatLib.clearChat(idFixedOffset--)
        idFixedOffset = -1
    }

    private fun copyArgsToClipboard(args: List<String>) {
        clearOldDump()
        val toCopy = args.drop(1).joinToString(" ")
        Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(toCopy), null)
    }
}
