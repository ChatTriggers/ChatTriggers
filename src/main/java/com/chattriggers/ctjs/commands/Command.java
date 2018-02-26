package com.chattriggers.ctjs.commands;

import com.chattriggers.ctjs.minecraft.wrappers.Chat;
import com.chattriggers.ctjs.triggers.OnTrigger;
import com.chattriggers.ctjs.utils.console.Console;
import lombok.Getter;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;

public class Command extends CommandBase {
    private String name;
    private String usage;
    @Getter
    private ArrayList<OnTrigger> triggers = new ArrayList<>();

    public Command(OnTrigger trigger, String name, String usage) {
        this.triggers.add(trigger);
        this.name = name;
        this.usage = usage;
    }

    public String getName() {
        return getCommandName();
    }
    public String getCommandName() {
        return name;
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public String getUsage(ICommandSender sender) {
        return getCommandUsage(sender);
    }
    public String getCommandUsage(ICommandSender sender) {
        return usage;
    }

    public void execute(MinecraftServer server,ICommandSender sender, String[] args) throws CommandException {
        processCommand(sender, args);
    }
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        try {
            for (OnTrigger trigger : triggers)
                trigger.trigger((Object[]) args);
        } catch (Exception exception) {
            Chat.chat("&cSomething went wrong while running that command");
            Chat.chat("&cCheck the ct console for more information");
            Console.getConsole().printStackTrace(exception);
        }
    }
}
