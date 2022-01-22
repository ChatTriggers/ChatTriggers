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
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import gg.essential.api.utils.GuiUtil
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.ServerCommandSource
import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.File
import java.io.IOException

object CTCommand {
    private const val idFixed = 90123 // ID for dumped chat
    private var idFixedOffset = -1 // ID offset (increments)

    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(literal("ct")
            .then(literal("load").executes { Reference.loadCT(); 1 })
            .then(literal("reload").executes { Reference.loadCT(); 1 })
            .then(literal("unload").executes { Reference.unloadCT(); 1 })
            .then(literal("file").executes { openFileLocation(); 1 })
            .then(literal("files").executes { openFileLocation(); 1 })
            .then(literal("import")
                .then(argument("moduleName", StringArgumentType.word())
                    .executes {
                        import(StringArgumentType.getString(it, "moduleName"))
                        1
                    }
            ))
            .then(literal("delete")
                .then(argument("moduleName", StringArgumentType.word())
                    .executes {
                        val moduleName = StringArgumentType.getString(it, "moduleName")
                        if (ModuleManager.deleteModule(moduleName)) {
                            ChatLib.chat("&aDelete module $moduleName")
                            1
                        } else {
                            ChatLib.chat("&cFailed to delete module $moduleName")
                            0
                        }
                    }
            ))
            .then(literal("modules").executes { GuiUtil.open(ModulesGui); 1 })
            .then(literal("console")
                .executes { ModuleManager.generalConsole.showConsole(); 1 }
                .then(argument("consoleType", StringArgumentType.word())
                    .executes {
                        ModuleManager.getConsole(StringArgumentType.getString(it, "consoleType")).showConsole()
                        1
                    }
            ))
            .then(literal("config").executes { GuiUtil.open(Config.gui()!!); 1 })
            .then(literal("settings").executes { GuiUtil.open(Config.gui()!!); 1 })
            .then(literal("setting").executes { GuiUtil.open(Config.gui()!!); 1 })
            .then(literal("sim")
                .then(argument("text", StringArgumentType.greedyString())
                    .executes { ChatLib.simulateChat(StringArgumentType.getString(it, "text")); 1 }
                )
            )
            .then(literal("simulate")
                .then(argument("text", StringArgumentType.greedyString())
                    .executes { ChatLib.simulateChat(StringArgumentType.getString(it, "text")); 1 }
                )
            )
            .then(literal("dump")
                .executes { dump(isActionBar = false); 1 }
                .then(literal("chat")
                    .executes { dump(isActionBar = false); 1 }
                    .then(argument("amount", IntegerArgumentType.integer(1, 1000))
                        .executes {
                            dump(isActionBar = false, IntegerArgumentType.getInteger(it, "amount"))
                            1
                        }
                    )
                )
                .then(literal("actionbar")
                    .executes { dump(isActionBar = true); 1 }
                    .then(argument("amount", IntegerArgumentType.integer(1, 1000))
                        .executes {
                            dump(isActionBar = true, IntegerArgumentType.getInteger(it, "amount"))
                            1
                        }
                    )
                )
            )
            .then(literal("copy")
                .then(argument("text", StringArgumentType.greedyString())
                    .executes { copyTextToClipboard(StringArgumentType.getString(it, "text")); 1 }
                )
            )
            .then(literal("generatebindings").executes { Reference.generateBindings(); 1 })
        )
    }

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

    // private fun getUsage() = """
    //     &b&m${ChatLib.getChatBreak()}
    //     &c/ct load &7- &oReloads all of the ChatTriggers modules.
    //     &c/ct import [module] &7- &oImports a module.
    //     &c/ct delete [module] &7- &oDeletes a module.
    //     &c/ct files &7- &oOpens the ChatTriggers folder.
    //     &c/ct modules &7- &oOpens the modules GUI.
    //     &c/ct console [language] &7- &oOpens the ChatTriggers console.
    //     &c/ct simulate [message] &7- &oSimulates a received chat message.
    //     &c/ct dump &7- &oDumps previous chat messages into chat.
    //     &c/ct settings &7- &oOpens the ChatTriggers settings.
    //     &c/ct &7- &oDisplays this help dialog.
    //     &b&m${ChatLib.getChatBreak()}
    // """.trimIndent()

    private fun openFileLocation() {
        try {
            Desktop.getDesktop().open(File("./config/ChatTriggers"))
        } catch (exception: IOException) {
            exception.printTraceToConsole()
            ChatLib.chat("&cCould not open file location")
        }
    }

    private fun dump(isActionBar: Boolean, amount: Int = 100) {
        if (isActionBar) {
            dumpActionBar(amount)
        } else dumpChat(amount)
    }

    private fun dumpChat(lines: Int) = dumpList(ClientListener.chatHistory, lines)
    private fun dumpActionBar(lines: Int) = dumpList(ClientListener.actionBarHistory, lines)
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

    private fun copyTextToClipboard(text: String) {
        clearOldDump()
        Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(text), null)
    }
}
