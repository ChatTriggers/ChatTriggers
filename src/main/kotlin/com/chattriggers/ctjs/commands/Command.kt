package com.chattriggers.ctjs.commands

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.triggers.OnTrigger
import com.chattriggers.ctjs.utils.console.Console
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender

class Command(trigger: OnTrigger, private val name: String, private val usage: String) : CommandBase() {
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

    @Throws (CommandException::class)
    //#if MC<=10809
    override fun processCommand(sender: ICommandSender, args: Array<String>) = trigger(args)
    //#else
    //$$ override fun execute(server: net.minecraft.server.MinecraftServer, sender: ICommandSender, args: Array<String>) = trigger(args)
    //#endif

    private fun trigger(args: Array<String>) {
        try {
            for (trigger in this.triggers)
                trigger.trigger(*args)
        } catch (exception: Exception) {
            ChatLib.chat("&cSomething went wrong while running that command")
            ChatLib.chat("&cCheck the ct console for more information")
            Console.getInstance().printStackTrace(exception)
        }
    }
}