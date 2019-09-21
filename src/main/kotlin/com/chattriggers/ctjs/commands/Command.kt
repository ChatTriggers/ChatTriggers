package com.chattriggers.ctjs.commands

import com.chattriggers.ctjs.triggers.OnTrigger
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos

class Command(
    trigger: OnTrigger,
    private val name: String,
    private val usage: String,
    private val tabCompletionOptions: MutableList<String>
) : CommandBase() {
    private var triggers = mutableListOf<OnTrigger>()

    init {
        this.triggers.add(trigger)
    }

    fun getTriggers() = this.triggers

    //#if MC<=10809
    override fun getCommandName() = this.name
    //#else
    //$$ override fun getName() = this.name
    //#endif

    override fun getRequiredPermissionLevel() = 0

    //#if MC<=10809
    override fun getCommandUsage(sender: ICommandSender) = this.usage
    //#else
    //$$ override fun getUsage(sender: ICommandSender) = this.usage
    //#endif

    override fun addTabCompletionOptions(
        sender: ICommandSender?,
        args: Array<out String>?,
        pos: BlockPos?
    ): MutableList<String> {
        return this.tabCompletionOptions
    }

    @Throws(CommandException::class)
    //#if MC<=10809
    override fun processCommand(sender: ICommandSender, args: Array<String>) = trigger(args)
    //#else
    //$$ override fun execute(server: net.minecraft.server.MinecraftServer?, sender: ICommandSender, args: Array<String>) = trigger(args)
    //#endif

    private fun trigger(args: Array<String>) {
        for (trigger in this.triggers)
            trigger.trigger(args)
    }
}
