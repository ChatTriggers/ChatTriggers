package com.chattriggers.ctjs.commands

import com.chattriggers.ctjs.triggers.Trigger
import gg.essential.api.EssentialAPI
import gg.essential.api.commands.DefaultHandler
import gg.essential.api.commands.Greedy
import gg.essential.api.commands.Command as EssentialCommand

// TODO(BREAKING): Removed callback parameter since nested subcommands are very difficult to implement,
// you have to use a subcommand and then @Options
class Command(
    val trigger: Trigger,
    name: String,
    tabCompletionOptions: MutableList<String>,
    aliases: MutableList<String>,
) : EssentialCommand(name, autoHelpSubcommand = false) {
    override val commandAliases = aliases.map(::Alias).toSet()

    val triggers = mutableListOf<Trigger>()
    private val customSubCommands = mutableMapOf<String, Handler>()

    init {
        activeCommands.add(this)
        triggers.add(trigger)

        tabCompletionOptions.forEach { command ->
            val handler = Handler(javaClass.methods.first { it.name == "dummy" })
            customSubCommands[command] = handler
            subCommands[command] = handler
        }
    }

    fun dummy() {
        // noop, used to generate tab completions
    }

    @DefaultHandler
    fun handle(@Greedy args: String?) {
        triggers.forEach { it.trigger((args ?: "").split(" ").toTypedArray()) }
    }

    fun unregister() {
        activeCommands.remove(this)
        trigger.unregister()
        EssentialAPI.getCommandRegistry().unregisterCommand(this)
    }

    companion object {
        internal val activeCommands = mutableListOf<Command>()
    }
}
