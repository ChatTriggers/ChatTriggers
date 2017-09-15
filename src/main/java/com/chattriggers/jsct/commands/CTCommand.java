package com.chattriggers.jsct.commands;

import com.chattriggers.jsct.JSCT;
import com.chattriggers.jsct.libs.ChatLib;
import com.chattriggers.jsct.loader.ScriptLoader;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;

import javax.script.ScriptEngineManager;
import java.util.Collections;
import java.util.List;

public class CTCommand extends CommandBase {
    private NashornScriptEngine scriptEngine = JSCT.getInstance().getScriptEngine();
    private ScriptLoader scriptLoader = JSCT.getInstance().getScriptLoader();

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
        return "/ct <reload>";
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
                    reload();
                    ChatLib.chat(EnumChatFormatting.RED + "Reloaded js files");
                    break;
                default:

            }
        } else {
            ChatLib.chat(getCommandUsage(sender));
        }
    }

    /**
     * Reload mod's supporting js files and reinitialise script engine
     */
    public void reload() {
        MinecraftForge.EVENT_BUS.unregister(scriptLoader);

        scriptEngine = (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");
        scriptLoader = new ScriptLoader();

        MinecraftForge.EVENT_BUS.register(scriptLoader);
    }
}
