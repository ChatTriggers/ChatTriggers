package com.chattriggers.ctjs.commands;

import lombok.Getter;

import java.util.ArrayList;

public class CommandHandler {
    @Getter
    private ArrayList<Command> commandList = new ArrayList<>();
}
