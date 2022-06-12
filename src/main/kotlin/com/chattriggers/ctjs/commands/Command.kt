package com.chattriggers.ctjs.commands

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.triggers.Trigger
import com.chattriggers.ctjs.utils.kotlin.asMixin
//#if MC==10809
import com.chattriggers.ctjs.launch.mixins.transformers.CommandHandlerAccessor
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
import net.minecraftforge.client.ClientCommandHandler
//#else
//$$ import com.mojang.brigadier.CommandDispatcher
//$$ import com.mojang.brigadier.arguments.StringArgumentType
//$$ import com.mojang.brigadier.context.CommandContext
//$$ import net.minecraft.commands.CommandSourceStack
//$$ import net.minecraft.commands.Commands
//$$ import com.mojang.brigadier.tree.CommandNode
//#endif

class Command(
    trigger: Trigger,
    private val name: String,
    private val usage: String,
    private val tabCompletionOptions: MutableList<String>,
    private var aliases: MutableList<String>,
    private val overrideExisting: Boolean = false,
    private val callback: ((Array<out String>) -> MutableList<String>)? = null,
//#if MC<=11202
) : CommandBase() {
//#else
//$$ ) {
//#endif
    val triggers = mutableListOf<Trigger>()

    init {
        triggers.add(trigger)
    }

    //#if MC<=11202
    override fun getCommandName() = name

    override fun getCommandAliases() = aliases

    override fun getRequiredPermissionLevel() = 0

    override fun getCommandUsage(sender: ICommandSender) = usage

    override fun addTabCompletionOptions(
        sender: ICommandSender?,
        args: Array<out String>?,
        pos: BlockPos?
    ): MutableList<String> {
        return callback?.invoke(args ?: arrayOf())?.toMutableList() ?: tabCompletionOptions
    }

    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, args: Array<String>) = trigger(args)
    //#else
    //$$ private lateinit var commandNode: CommandNode<CommandSourceStack>
    //$$
    //$$ fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
    //$$     fun execute(context: CommandContext<CommandSourceStack>): Int {
    //$$         val args = context.nodes.map {
    //$$             context.input.substring(it.range.start, it.range.end)
    //$$         }.toTypedArray()
    //$$         triggers.forEach { it.trigger(args) }
    //$$         return 1
    //$$     }
    //$$
    //$$     var command = Commands.literal(name)
    //$$
    //$$     for (option in tabCompletionOptions) {
    //$$         command = command.then(Commands.argument(
    //$$             option,
    //$$             StringArgumentType.greedyString()
    //$$         )).executes(::execute)
    //$$     }
    //$$
    //$$     commandNode = dispatcher.register(command.executes(::execute))
    //$$ }
    //#endif

    private fun trigger(args: Array<String>) {
        triggers.forEach { it.trigger(args) }
    }

    fun register() {
        //#if MC<=11202
        if (name in commandHandler().commandMap.keys && !overrideExisting) {
            throw IllegalArgumentException("Command with name $name already exists!")
        }

        ClientCommandHandler.instance.registerCommand(this)
        activeCommands[name] = this
        //#else
        //$$ CTJS.registerCommand(commandNode)
        //#endif
    }

    fun unregister() {
        //#if MC<=11202
        commandHandler().commandSet.remove(this)
        commandHandler().commandMap.remove(name)
        activeCommands.remove(name)
        //#else
        //$$ CTJS.unregisterCommand(commandNode)
        //#endif
    }

    //#if MC<=11202
    private fun commandHandler() = ClientCommandHandler.instance.asMixin<CommandHandlerAccessor>()
    //#endif

    companion object {
        internal val activeCommands = mutableMapOf<String, Command>()
    }
}
