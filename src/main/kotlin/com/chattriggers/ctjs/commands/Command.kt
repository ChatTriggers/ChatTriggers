package com.chattriggers.ctjs.commands

import com.chattriggers.ctjs.printToConsole
import com.chattriggers.ctjs.triggers.Trigger
import com.chattriggers.ctjs.utils.console.LogType
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos

//#if MC==10809
import net.minecraftforge.client.ClientCommandHandler
//#endif

class Command(
    private val trigger: Trigger,
    private val name: String,
    private val usage: String,
    private val tabCompletionOptions: MutableList<String>,
    private var aliases: MutableList<String>,
    private val overrideExisting: Boolean = false,
    private val callback: ((Array<out String>) -> MutableList<String>)? = null,
) : CommandBase() {

    fun getTriggers() = listOf(trigger)

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
        pos: BlockPos?,
    ): MutableList<String> {
        return callback?.invoke(args ?: arrayOf())?.toMutableList() ?: tabCompletionOptions
    }

    @Throws(CommandException::class)
    //#if MC<=10809
    override fun processCommand(sender: ICommandSender, args: Array<String>) = trigger.trigger(args)
    //#else
    //$$ override fun execute(server: net.minecraft.server.MinecraftServer?, sender: ICommandSender, args: Array<String>) = trigger(args)
    //#endif

    fun register() {
        if (name in ClientCommandHandler.instance.commands.keys && !overrideExisting) {
            ("Command with name $name already exists! " +
                    "This will not override the other command with the same name. " +
                    "To override conflicting commands, " +
                    "set the 2nd argument in setName() to true.").printToConsole(trigger.loader.console, LogType.WARN)

            return
        }

        ClientCommandHandler.instance.registerCommand(this)
        activeCommands[name] = this
    }

    fun unregister() {
        ClientCommandHandler.instance.commandSet.remove(this)
        ClientCommandHandler.instance.commands.remove(name)
        activeCommands.remove(name)
    }

    companion object {
        internal val activeCommands = mutableMapOf<String, Command>()
    }
}
