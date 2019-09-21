package com.chattriggers.ctjs.commands

import com.chattriggers.ctjs.Reference
import com.chattriggers.ctjs.engine.ModuleManager
import com.chattriggers.ctjs.engine.module.ModulesGui
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.listeners.ChatListener
import com.chattriggers.ctjs.minecraft.objects.gui.GuiHandler
import com.chattriggers.ctjs.minecraft.objects.message.Message
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.print
import com.chattriggers.ctjs.utils.config.GuiConfig
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.File
import java.io.IOException
import java.util.*

object CTCommand : CommandBase() {
    private const val idFixed = 90123 // ID for dumped chat
    private var idFixedOffset = -1 // ID offset (increments)

    //#if MC<=10809
    override fun getCommandUsage(sender: ICommandSender?) = getUsage()
    //#else
    //$$ override fun getUsage(sender: ICommandSender) = getUsage()
    //#endif

    //#if MC<=10809
    override fun getCommandName() = "chattriggers"
    //#else
    //$$ override fun getName() = "chattriggers"
    //#endif

    //#if MC<=10809
    override fun getCommandAliases() = mutableListOf("ct")
    //#else
    //$$ override fun getAliases() = mutableListOf("ct")
    //#endif

    override fun getRequiredPermissionLevel() = 0

    @Throws(CommandException::class)
    //#if MC<=10809
    override fun processCommand(sender: ICommandSender?, args: Array<String>) = run(args)
    //#else
    //$$ override fun execute(server: net.minecraft.server.MinecraftServer?, sender: ICommandSender, args: Array<String>) = run(args)
    //#endif

    private fun run(args: Array<String>) {
        if (args.isEmpty()) {
            ChatLib.chat(getUsage())
            return
        }

        when (args[0].toLowerCase()) {
            "load" -> Reference.loadCT()
            "reload" -> Reference.reloadCT()
            "unload" -> Reference.unloadCT()
            "files", "file" -> openFileLocation()
            "import" ->
                if (args.size == 1) ChatLib.chat("&c/ct import [module name]")
                else ModuleManager.importModule(args[1])
            "delete" ->
                if (args.size == 1) ChatLib.chat("&c/ct delete [module name]")
                else ChatLib.chat((if (ModuleManager.deleteModule(args[1])) "&aDeleted " else "&cFailed to delete ") + args[1])
            "modules" -> GuiHandler.openGui(ModulesGui)
            "console" ->
                if (args.size == 1) ModuleManager.generalConsole.showConsole()
                else ModuleManager.getConsole(args[1]).showConsole()
            "config", "settings", "setting" ->
                GuiHandler.openGui(GuiConfig())
            "sim", "simulate" ->
                ChatLib.simulateChat(Arrays.copyOfRange(args, 1, args.size).joinToString(" "))
            "dump" -> dump(args)
            "copy" -> copyArgsToClipboard(args)
            else -> ChatLib.chat(getUsage())
        }
    }

    private fun getUsage() =
        "&b&m${ChatLib.getChatBreak()}\n" +
                "&c/ct <load/reload> &7- &oReloads all of the ct modules.\n" +
                "&c/ct import [module] &7- &oImports a module.\n" +
                "&c/ct files &7- &oOpens the ChatTriggers folder.\n" +
                "&c/ct modules &7- &oOpens the modules gui\n" +
                "&c/ct console &7- &oOpens the ct console.\n" +
                "&c/ct simulate [message]&7- &oSimulates a received chat message.\n" +
                "&c/ct dump &7- &oDumps previous chat messages into chat.\n" +
                "&c/ct settings &7- &oChange ChatTrigger's settings.\n" +
                "&c/ct &7- &oDisplays this help dialog.\n" +
                "&b&m${ChatLib.getChatBreak()}"

    private fun openFileLocation() {
        try {
            Desktop.getDesktop().open(File("./config/ChatTriggers"))
        } catch (exception: IOException) {
            exception.print()
            ChatLib.chat("&cCould not open file location")
        }
    }

    private fun dump(args: Array<String>) {
        if (args.size == 1) {
            dumpChat()
            return
        }
        when (args[1].toLowerCase()) {
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

    private fun dumpChat(lines: Int = 100) = dumpList(ChatListener.chatHistory, lines)
    private fun dumpActionBar(lines: Int = 100) = dumpList(ChatListener.actionBarHistory, lines)
    private fun dumpList(messages: List<String>, lines: Int) {
        clearOldDump()

        var toDump = lines
        if (toDump > messages.size) toDump = messages.size
        Message("&6&m${ChatLib.getChatBreak()}").setChatLineId(this.idFixed).chat()
        var msg: String
        for (i in 0 until toDump) {
            msg = ChatLib.replaceFormatting(messages[messages.size - toDump + i])
            Message(
                TextComponent(msg)
                    .setClick("run_command", "/ct copy $msg")
                    .setHoverValue(ChatLib.addColor("&eClick here to copy this message."))
                    .setFormatted(false)
            ).setFormatted(false).setChatLineId(this.idFixed + i + 1).chat()
        }
        Message("&6&m${ChatLib.getChatBreak()}").setChatLineId(this.idFixed + lines + 1).chat()

        this.idFixedOffset = this.idFixed + lines + 1
    }

    private fun clearOldDump() {
        if (this.idFixedOffset == -1) return
        while (this.idFixedOffset >= this.idFixed)
            ChatLib.clearChat(this.idFixedOffset--)
        this.idFixedOffset = -1
    }

    private fun copyArgsToClipboard(args: Array<String>) {
        clearOldDump()
        val toCopy = Arrays.copyOfRange(args, 1, args.size).joinToString(" ")
        Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(toCopy), null)
    }
}