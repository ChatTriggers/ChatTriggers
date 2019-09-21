package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.commands.Command
import com.chattriggers.ctjs.commands.CommandHandler
import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.engine.module.Module
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.command.ICommand
import net.minecraft.command.ICommandManager
import net.minecraft.command.ICommandSender
import net.minecraft.network.play.client.C14PacketTabComplete
import net.minecraftforge.client.ClientCommandHandler
import scala.Console

@External
class OnCommandTrigger(method: Any, loader: ILoader) : OnTrigger(method, TriggerType.COMMAND, loader) {
    private var commandName: String? = null
    private var tabCompletions: MutableList<String> = mutableListOf()
    private var command: Command? = null

    override fun trigger(vararg args: Any?) {
        if (args::javaClass == Array<String>::javaClass) throw IllegalArgumentException("Arguments must be string array")

        callMethod(*args)
    }

    /**
     * Sets the tab completion options for the command.
     * This method must be used before setting the command name, otherwise, the tab completions will not be set.
     *
     * @param args all the tab completion options.
     */
    fun setTabCompletions(vararg args: String) = apply {
        this.tabCompletions = args.toMutableList()
    }

    /**
     * Sets the command name.<br></br>
     * Example:<br></br>
     * OnCommandTrigger.setCommandName("test")<br></br>
     * would result in the command being /test
     *
     * @param commandName The command name
     * @return the trigger for additional modification
     */
    fun setCommandName(commandName: String) = apply {
        this.commandName = commandName
        reInstance()
    }

    /**
     * Alias for [.setCommandName]
     *
     * @param commandName The command name
     * @return the trigger for additional modification
     */
    fun setName(commandName: String) = setCommandName(commandName)

    // helper method to re instance the command
    private fun reInstance() {
        for (command in CommandHandler.getCommandList()) {
            //#if MC<=10809
            if (command.commandName == this.commandName) {
            //#else
            //$$ if (command.name == this.commandName) {
            //#endif
                command.getTriggers().add(this)
                return
            }
        }

        this.command = Command(this, this.commandName!!, "/${this.commandName}", this.tabCompletions)
        ClientCommandHandler.instance.registerCommand(this.command!!)
        CommandHandler.getCommandList().add(this.command ?: return)
    }
}
