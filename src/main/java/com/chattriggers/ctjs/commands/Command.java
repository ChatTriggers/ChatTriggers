package com.chattriggers.ctjs.commands;

import com.chattriggers.ctjs.triggers.Trigger;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class Command extends CommandBase {
    private String name;
    private String usage;
    private Trigger trigger;

    public Command(Trigger trigger, String name, String usage) {
        this.trigger = trigger;
        this.name = name;
        this.usage = usage;
    }

    @Override
    public String getCommandName() {
        return name;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return usage;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        trigger.trigger((Object[]) args);
    }
}
