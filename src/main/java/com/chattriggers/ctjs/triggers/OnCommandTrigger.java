package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.commands.Command;
import com.chattriggers.ctjs.commands.CommandHandler;
import net.minecraftforge.client.ClientCommandHandler;

import java.util.ArrayList;
import java.util.Arrays;

public class OnCommandTrigger extends OnTrigger {
    private String commandName = null;
    private Command command = null;

    public OnCommandTrigger(Object method) {
        super(method, TriggerType.COMMAND);
    }

    @Override
    public void trigger(Object... args) {
        if (!(args instanceof String[])) throw new IllegalArgumentException("Arguments must be string array");

        callMethod(args);

    }

    /**
     * Sets the command name.<br>
     * Example:<br>
     * OnCommandTrigger.setCommandName("test")<br>
     * would result in the command being /test
     *
     * @param commandName The command name
     * @return the trigger for additional modification
     */
    public OnCommandTrigger setCommandName(String commandName) {
        this.commandName = commandName;

        reInstance();

        return this;
    }

    /**
     * Alias for {@link #setCommandName(String)}
     *
     * @param commandName The command name
     * @return the trigger for additional modification
     */
    public OnCommandTrigger setName(String commandName) {
        return setCommandName(commandName);
    }

    // helper method to re instance the command
    private void reInstance() {
        for (Command command : CommandHandler.getCommandList()) {
            if (command.getCommandName().equals(this.commandName)) {
                command.getTriggers().add(this);
                return;
            }
        }

        this.command = new Command(this, this.commandName, "/" + this.commandName);
        ClientCommandHandler.instance.registerCommand(this.command);
        CommandHandler.getCommandList().add(this.command);
    }
}
