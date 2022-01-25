package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.commands.Command
import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.utils.kotlin.External

@External
class OnCommandTrigger(method: Any, loader: ILoader) : OnTrigger(method, TriggerType.Command, loader) {
    private lateinit var commandName: String
    private val tabCompletions = mutableListOf<String>()
    private val aliases = mutableListOf<String>()
    private var command: Command? = null

    override fun trigger(args: Array<out Any?>) {
        callMethod(args)
    }

    /**
     * Sets the tab completion options for the command.
     * This method must be used before setting the command name, otherwise, the tab completions will not be set.
     *
     * @param args all the tab completion options.
     */
    fun setTabCompletions(vararg args: String) = apply {
        tabCompletions.addAll(args)
    }

    /**
     * Sets the aliases for the command.
     *
     * @param args all the aliases.
     */
    fun setAliases(vararg args: String) = apply {
        if (!::commandName.isInitialized) {
            throw IllegalStateException("Command name must be set before aliases!")
        }

        aliases.addAll(args)
        reInstance()
    }

    /**
     * Sets the command name.
     * Example:
     * OnCommandTrigger.setCommandName("test")
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
     * Alias for [setCommandName]
     *
     * @param commandName The command name
     * @return the trigger for additional modification
     */
    fun setName(commandName: String) = setCommandName(commandName)

    private fun reInstance() {
        command?.unregister()
        command = Command(this, commandName, "/$commandName", tabCompletions, aliases)
        command!!.register()
    }
}
