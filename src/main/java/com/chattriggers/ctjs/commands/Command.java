package com.chattriggers.ctjs.commands;

import com.chattriggers.ctjs.minecraft.libs.ChatLib;
import com.chattriggers.ctjs.triggers.OnTrigger;
import com.chattriggers.ctjs.utils.console.Console;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class Command extends CommandBase {
    private String name;
    private String usage;
    @Setter
    private List<String> tabComplete;
    @Getter
    private ArrayList<OnTrigger> triggers = new ArrayList<>();

    public Command(OnTrigger trigger, String name, String usage) {
        this.triggers.add(trigger);
        this.name = name;
        this.usage = usage;
    }

    public void addTabComplete(String option) {
        this.tabComplete.add(option);
    }

    public String getName() {
        return getCommandName();
    }
    public String getCommandName() {
        return name;
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return tabComplete;
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
            ChatLib.chat("&cSomething went wrong while running that command");
            ChatLib.chat("&cCheck the ct console for more information");
            Console.getInstance().printStackTrace(exception);
        }
    }
}
