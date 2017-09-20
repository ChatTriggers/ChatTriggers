package com.chattriggers.jsct.commands;

import com.chattriggers.jsct.JSCT;
import com.chattriggers.jsct.libs.ChatLib;
import com.chattriggers.jsct.loader.ScriptLoader;
import com.chattriggers.jsct.objects.DisplayHandler;
import com.chattriggers.jsct.triggers.TriggerRegister;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class CTCommand extends CommandBase {
    private ScriptEngine scriptEngine;
    private ScriptLoader scriptLoader;

    public CTCommand() {
        this.scriptEngine = JSCT.getInstance().getScriptEngine();
        this.scriptLoader = JSCT.getInstance().getScriptLoader();
    }

    @Override
    public String getCommandName() {
        return "chattriggers";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/ct <reload/files>";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("ct");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case("reload"):
                case("load"):
                    reload();
                    ChatLib.chat(EnumChatFormatting.RED + "Reloaded js files");
                    break;
                case("files"):
                    openFileLocation();
                    break;
                default:
                    ChatLib.chat(EnumChatFormatting.RED + getCommandUsage(sender));
                    break;
            }
        } else {
            ChatLib.chat(EnumChatFormatting.RED + getCommandUsage(sender));
        }
    }

    /**
     * Open the folder containing all of ChatTrigger's files
     */
    private void openFileLocation() {
        try {
            Desktop.getDesktop().open(new File("./mods/ChatTriggers"));
        } catch (IOException exception) {
            exception.printStackTrace();
            ChatLib.chat(EnumChatFormatting.RED + "Could not open file location");
        }
    }

    /**
     * Reload mod's supporting js files and reinitialise script engine
     */
    public void reload() {
        TriggerRegister.TriggerTypes.clearAllTriggers();

        MinecraftForge.EVENT_BUS.unregister(scriptLoader);

        scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
        JSCT.getInstance().setScriptEngine(scriptEngine);
        JSCT.getInstance().getDisplayHandler().clearDisplays();

        scriptLoader = new ScriptLoader();
        JSCT.getInstance().setScriptLoader(scriptLoader);
        JSCT.getInstance().setInvocableEngine(((Invocable) scriptEngine));

        MinecraftForge.EVENT_BUS.register(scriptLoader);
    }
}
