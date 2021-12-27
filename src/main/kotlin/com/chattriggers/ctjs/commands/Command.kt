package com.chattriggers.ctjs.commands

import com.chattriggers.ctjs.triggers.OnTrigger

import com.chattriggers.ctjs.CTJS
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

class Command(
    trigger: OnTrigger,
    private val name: String,
    private val usage: String,
    private val tabCompletionOptions: MutableList<String>
) {
    var triggers = mutableListOf(trigger)

    private fun trigger(args: Array<String>) {
        triggers.forEach { it.trigger(args) }
    }

    fun register() {
        activeCommands[name] = this

        fun execute(context: CommandContext<ServerCommandSource>): Int {
            val args = context.nodes.map {
                context.input.substring(it.range.start, it.range.end)
            }.toTypedArray()
            triggers.forEach { it.trigger(args) }
            return 1
        }

        var command = CommandManager.literal(name)

        for (option in tabCompletionOptions) {
            command = command.then(CommandManager.argument(
                option,
                StringArgumentType.greedyString()
            )).executes(::execute)
        }

        CTJS.commandDispatcher.register(command.executes(::execute))
    }

    fun unregister() {
        activeCommands.remove(name)

        CTJS.commandDispatcher.root.children.removeIf { it.name == name }
    }

    companion object {
        internal val activeCommands = mutableMapOf<String, Command>()
    }
}
