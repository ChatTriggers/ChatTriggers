package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.commands.Command
import com.chattriggers.ctjs.commands.CommandHandler
import net.minecraftforge.client.ClientCommandHandler

class OnCommandTrigger(method: Any) : OnTrigger(method, TriggerType.COMMAND) {
    private var commandName: String? = null
    private var command: Command? = null

    override fun trigger(vararg args: Any) {
        if (args::javaClass == Array<String>::javaClass) throw IllegalArgumentException("Arguments must be string array")

        callMethod(*args)

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
            if (command.commandName == this.commandName) {
                command.getTriggers().add(this)
                return
            }
        }

        this.command = Command(this, this.commandName!!, "/${this.commandName}")
        ClientCommandHandler.instance.registerCommand(this.command!!)
        CommandHandler.getCommandList().add(this.command ?: return)
    }
}
