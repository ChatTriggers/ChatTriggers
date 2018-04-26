package com.chattriggers.ctjs.commands;

import lombok.Getter;

import java.util.ArrayList;

public class CommandHandler {
    @Getter
    private static CommandHandler instance;

    @Getter
    private ArrayList<Command> commandList = new ArrayList<>();

    public CommandHandler() {
        instance = this;
    }
}
