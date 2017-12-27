package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.commands.Command;
import com.chattriggers.ctjs.utils.console.Console;
import net.minecraftforge.client.ClientCommandHandler;

import javax.script.ScriptException;

public class OnCommandTrigger extends OnTrigger {
    private String commandName = null;
    private String commandUsage = null;

    public OnCommandTrigger(String methodName) {
        super(methodName, TriggerType.COMMAND);
    }

    @Override
    public void trigger(Object... args) {
        if (!(args instanceof String[])) throw new IllegalArgumentException("Arguments must be string array");

        try {
            CTJS.getInstance().getModuleManager().invokeFunction(methodName, args);
        } catch (ScriptException | NoSuchMethodException e) {
            Console.getConsole().printStackTrace(e, this);
        }
    }

    /**
     * Sets the command name.<br>
     * Example:<br>
     * OnCommandTrigger.setCommandName("test")<br>
     * would result in the command being /test
     * @param commandName The command name
     * @return the trigger for additional modification
     */
    public OnCommandTrigger setCommandName(String commandName) {
        this.commandName = commandName;
        if (commandUsage == null)
            commandUsage = "/" + commandName;

        reInstance();

        return this;
    }

    /**
     * Sets the command usage.
     * @param commandUsage The command usage
     * @return the trigger for additional modification
     */
    public OnCommandTrigger setCommandUsage(String commandUsage) {
        this.commandUsage = commandUsage;

        if (this.commandName != null)
            reInstance();

        return this;
    }

    private void reInstance() {
        for (Command command : CTJS.getInstance().getCommandHandler().getCommandList()) {
            if (command.getCommandName().equals(this.commandName)) {
                command.getTriggers().add(this);
                return;
            }
        }

        Command command = new Command(this, this.commandName, this.commandUsage);
        ClientCommandHandler.instance.registerCommand(command);
        CTJS.getInstance().getCommandHandler().getCommandList().add(command);
    }
}
