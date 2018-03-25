package com.chattriggers.ctjs.commands;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.minecraft.libs.ChatLib;
import com.chattriggers.ctjs.minecraft.objects.message.Message;
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent;
import com.chattriggers.ctjs.modules.gui.ModulesGui;
import com.chattriggers.ctjs.triggers.TriggerType;
import com.chattriggers.ctjs.utils.console.Console;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CTCommand extends CommandBase {
    private final int idFixed = 90123; // ID for dumped chat
    private Integer idFixedOffset = null; // ID offset (increments)

    private Boolean isLoaded = true;

    public String getName() {
        return getCommandName();
    }
    public String getCommandName() {
        return "chattriggers";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        List<String> tabOptions = new ArrayList<>();

        tabOptions.add("help");
        tabOptions.add("load");
        tabOptions.add("settings");
        tabOptions.add("import");
        tabOptions.add("modules");
        tabOptions.add("files");
        tabOptions.add("console");
        tabOptions.add("simulate");
        tabOptions.add("dump");
        tabOptions.add("copy");

        return tabOptions;
    }

    public String getUsage(ICommandSender sender) {
        return getCommandUsage(sender);
    }
    public String getCommandUsage(ICommandSender sender) {
        return "&b&m" + ChatLib.getChatBreak("-") + "\n" +
                "&c/ct <load/reload> &7- &oReloads all of the ct modules.\n" +
                "&c/ct import [module] &7- &oImports a module.\n" +
                "&c/ct files &7- &oOpens the ChatTriggers folder.\n" +
                "&c/ct modules &7- &oOpens the modules gui\n" +
                "&c/ct console &7- &oOpens the ct console.\n" +
                "&c/ct simulate [message]&7- &oSimulates a received chat message.\n" +
                "&c/ct dump &7- &oDumps previous chat messages into chat.\n" +
                "&c/ct settings &7- &oChange ChatTrigger's settings.\n" +
                "&c/ct &7- &oDisplays this help dialog.\n" +
                "&b&m" + ChatLib.getChatBreak("-");
    }

    public List<String> getAliases() {
        return getCommandAliases();
    }
    public List<String> getCommandAliases() {
        return Collections.singletonList("ct");
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        processCommand(sender, args);
    }
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case("reload"):
                case("load"):
                    if (!this.isLoaded) return;
                    this.isLoaded = false;

                    TriggerType.GAME_UNLOAD.triggerAll();
                    TriggerType.WORLD_UNLOAD.triggerAll();
                    ChatLib.chat("&cReloading ct.js scripts...");
                    new Thread(() -> {
                        for (TriggerType type : TriggerType.values())
                            type.clearTriggers();
                        CTJS.getInstance().getCommandHandler().getCommandList().clear();
                        CTJS.getInstance().getModuleManager().unload();

                        if (CTJS.getInstance().getConfig().getClearConsoleOnLoad().value)
                            CTJS.getInstance().getConsole().clearConsole();

                        CTJS.getInstance().setupConfig();

                        CTJS.getInstance().getModuleManager().load();
                        ChatLib.chat("&aDone reloading scripts!");
                        TriggerType.WORLD_LOAD.triggerAll();
                        this.isLoaded = true;
                    }).start();
                    break;
                case("files"):
                case("file"):
                    openFileLocation();
                    break;
                case("import"):
                    if (args.length == 1) {
                        ChatLib.chat("&c/ct import [module name]");
                    } else {
                        ChatLib.chat("&6Importing " + args[1]);
                        CTJS.getInstance().getModuleManager().importModule(args[1]);
                    }
                    break;
                case("console"):
                    Console.getConsole().showConsole(true);
                    break;
                case("modules"):
                case("module"):
                case("imports"):
                    CTJS.getInstance().getGuiHandler().openGui(
                            new ModulesGui(CTJS.getInstance().getModuleManager().getModules())
                    );
                    break;
                case("config"):
                case("settings"):
                case("setting"):
                    CTJS.getInstance().getGuiHandler().openGui(
                            CTJS.getInstance().getGuiConfig()
                    );
                    break;
                case("sim"):
                case("simulate"):
                    ChatLib.simulateChat(String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
                    break;
                case("dump"):
                    try {
                        if (args.length > 1 && args[1] != null)
                            dumpChat(Integer.parseInt(args[1]));
                        else
                            dumpChat(100);
                    } catch (NumberFormatException e) {
                        ChatLib.chat("&cThe second command argument must be an integer!");
                    }
                    break;
                case("copy"):
                    copyArgsToClipboard(args);
                    break;
                default:
                    ChatLib.chat(getCommandUsage(sender));
                    break;
            }
        } else {
            ChatLib.chat(getCommandUsage(sender));
        }
    }

    private void dumpChat(int lines) {
        clearOldDump();
        ArrayList<String> messages = CTJS.getInstance().getChatListener().getChatHistory();

        int amount = lines;
        if (amount > messages.size()) amount = messages.size();
        new Message("&6&m" + ChatLib.getChatBreak("-")).setChatLineId(this.idFixed).chat();
        String msg;
        for (int i = 0; i < amount; i++) {
            msg = ChatLib.replaceFormatting(messages.get(messages.size() - amount + i));
            new Message(
                new TextComponent(msg)
                    .setClick("run_command", "/ct copy " + msg)
                    .setHoverValue("&eClick here to copy this message.")
                    .setFormatted(false)
            ).setFormatted(false).setChatLineId(this.idFixed + i + 1).chat();
        }
        new Message("&6&m" + ChatLib.getChatBreak("-")).setChatLineId(this.idFixed + amount + 1).chat();

        idFixedOffset = idFixed + amount + 1;
    }

    private void copyArgsToClipboard(String[] args) {
        clearOldDump();
        String toCopy = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(toCopy), null);
    }

    private void clearOldDump() {
        if (idFixedOffset == null) return;

        while (idFixedOffset >= idFixed) {
            ChatLib.clearChat(idFixedOffset--);
        }

        idFixedOffset = null;
    }

    // Open the folder containing all of ChatTrigger's files
    private void openFileLocation() {
        try {
            Desktop.getDesktop().open(new File("./config/ChatTriggers"));
        } catch (IOException exception) {
            Console.getConsole().printStackTrace(exception);
            ChatLib.chat("&cCould not open file location");
        }
    }
}
