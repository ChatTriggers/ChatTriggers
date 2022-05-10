package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.commands.Command

class CommandTrigger(method: Any) : Trigger(method, TriggerType.Command) {
    private lateinit var commandName: String
    private var overrideExisting: Boolean = false
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
     * setCommandName("test")
     * would result in the command being /test
     *
     * @param commandName The command name
     * @param overrideExisting Whether existing commands with the same name should be overridden
     * @return the trigger for additional modification
     */
    @JvmOverloads
    fun setCommandName(commandName: String, overrideExisting: Boolean = false) = apply {
        this.commandName = commandName
        this.overrideExisting = overrideExisting
        reInstance()
    }

    /**
     * Alias for [setCommandName]
     *
     * @param commandName The command name
     * @param overrideExisting Whether existing commands with the same name should be overridden
     * @return the trigger for additional modification
     */
    @JvmOverloads
    fun setName(commandName: String, overrideExisting: Boolean = false) = setCommandName(commandName, overrideExisting)

    private fun reInstance() {
        command?.unregister()
        command = Command(this, commandName, "/$commandName", tabCompletions, aliases, overrideExisting)
        command!!.register()
    }
}
