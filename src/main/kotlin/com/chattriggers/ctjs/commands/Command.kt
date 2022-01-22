package com.chattriggers.ctjs.commands

import com.chattriggers.ctjs.triggers.OnTrigger

import com.chattriggers.ctjs.CTJS
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.CommandNode
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

class Command(
    trigger: OnTrigger,
    private val name: String,
    private val usage: String,
    private val tabCompletionOptions: MutableList<String>
) {
    private var commandNode: CommandNode<ServerCommandSource>? = null
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

        CTJS.registerCommand(command.executes(::execute).build().also {
            this.commandNode = it
        })
    }

    fun unregister() {
        commandNode?.also {
            activeCommands.remove(name)
            CTJS.unregisterCommand(it)
        }
    }

    companion object {
        internal val activeCommands = mutableMapOf<String, Command>()
    }
}
