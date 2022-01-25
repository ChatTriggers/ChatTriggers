package com.chattriggers.ctjs.commands

import com.chattriggers.ctjs.triggers.OnTrigger
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos

//#if MC==10809
import net.minecraftforge.client.ClientCommandHandler
//#endif

class Command(
    trigger: OnTrigger,
    private val name: String,
    private val usage: String,
    private val tabCompletionOptions: MutableList<String>,
    private var aliases: MutableList<String>,
) : CommandBase() {
    private var triggers = mutableListOf<OnTrigger>()

    init {
        triggers.add(trigger)
    }

    fun getTriggers() = triggers

    //#if MC<=10809
    override fun getCommandName() = name
    //#else
    //$$ override fun getName() = name
    //#endif

    //#if MC<=10809
    override fun getCommandAliases() = aliases
    //#else
    //$$ override fun getAliases() = aliases
    //#endif

    override fun getRequiredPermissionLevel() = 0

    //#if MC<=10809
    override fun getCommandUsage(sender: ICommandSender) = usage
    //#else
    //$$ override fun getUsage(sender: ICommandSender) = usage
    //#endif

    override fun addTabCompletionOptions(
        sender: ICommandSender?,
        args: Array<out String>?,
        pos: BlockPos?
    ): MutableList<String> {
        return tabCompletionOptions
    }

    @Throws(CommandException::class)
    //#if MC<=10809
    override fun processCommand(sender: ICommandSender, args: Array<String>) = trigger(args)
    //#else
    //$$ override fun execute(server: net.minecraft.server.MinecraftServer?, sender: ICommandSender, args: Array<String>) = trigger(args)
    //#endif

    private fun trigger(args: Array<String>) {
        triggers.forEach { it.trigger(args) }
    }

    fun register() {
        if (name in ClientCommandHandler.instance.commandMap.keys) {
            throw IllegalArgumentException("Command with name $name already exists!")
        }

        ClientCommandHandler.instance.registerCommand(this)
        activeCommands[name] = this
    }

    fun unregister() {
        ClientCommandHandler.instance.commandSet.remove(this)
        ClientCommandHandler.instance.commandMap.remove(name)
        activeCommands.remove(name)
    }

    companion object {
        internal val activeCommands = mutableMapOf<String, Command>()
    }
}
