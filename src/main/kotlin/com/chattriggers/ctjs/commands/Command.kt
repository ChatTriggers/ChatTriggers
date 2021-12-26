package com.chattriggers.ctjs.commands

import com.chattriggers.ctjs.triggers.OnTrigger

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import com.mojang.brigadier.tree.CommandNode
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

class Command(
    trigger: OnTrigger,
    private val name: String,
    private val usage: String,
    private val tabCompletionOptions: MutableList<String>
) {
    var triggers = mutableListOf(trigger)
        private set

    internal lateinit var commandNode: CommandNode<ServerCommandSource>

    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        fun execute(context: CommandContext<*>): Int {
            val args = context.nodes.map {
                context.input.substring(it.range.start, it.range.end)
            }.toTypedArray()
            triggers.forEach { it.trigger(args) }
            return 1
        }

        var command: LiteralArgumentBuilder<ServerCommandSource> = CommandManager.literal(name)

        for (option in tabCompletionOptions) {
            command = command.then(CommandManager.argument(
                option,
                StringArgumentType.greedyString()
            )).executes(::execute)
        }

        commandNode = dispatcher.register(command.executes(::execute))
    }

    private fun trigger(args: Array<String>) {
        triggers.forEach { it.trigger(args) }
    }
}
