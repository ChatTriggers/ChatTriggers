package com.chattriggers.ctjs.commands;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.modules.gui.ModulesGui;
import com.chattriggers.ctjs.libs.ChatLib;
import com.chattriggers.ctjs.triggers.TriggerType;
import com.chattriggers.ctjs.objects.Message;
import com.chattriggers.ctjs.utils.console.Console;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CTCommand extends CommandBase {

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
        return "&b&m" + ChatLib.getChatBreak("-") + "\n" +
                "&c/ct <load/reload> &7- &oReloads all of the ct modules.\n" +
                "&c/ct import [module] &7- &oImports a module.\n" +
                "&c/ct files &7- &oOpens the ChatTriggers folder.\n" +
                "&c/ct console &7- &oOpens the ct console.\n" +
                "&c/ct simulate [message]&7- &oSimulates a received chat message.\n" +
                "&c/ct dump &7- &oDumps previous chat messages into chat.\n" +
                "&c/ct &7- &oDisplays this help dialog\n" +
                "&b&m" + ChatLib.getChatBreak("-");
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
                    TriggerType.GAME_UNLOAD.triggerAll();
                    CTJS.getInstance().getModuleManager().unload();
                    ChatLib.chat("&cReloading ct.js scripts...");
                    new Thread(() -> {
                        CTJS.getInstance().getConfig().loadConfig();
                        CTJS.getInstance().getModuleManager().load();
                        ChatLib.chat("&aDone reloading scripts!");
                        TriggerType.WORLD_LOAD.triggerAll();
                    }).start();
                    break;
                case("files"):
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
                case("gui"):
                    CTJS.getInstance().getGuiHandler().openGui(
                            new ModulesGui(CTJS.getInstance().getModuleManager().getModules())
                    );
                    break;
                case("sim"):
                case("simulate"):
                    simulateChat(args);
                    break;
                case("dump"):
                    try {
                        if (args.length > 1 && args[1] != null) dumpChat(Integer.parseInt(args[1]));
                        else dumpChat(100);
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


    private void simulateChat(String[] args) {
        StringBuilder toSend = new StringBuilder();

        for (String arg : args) {
            if (!arg.equals(args[0])) toSend.append(arg).append(" ");
        }

        ClientChatReceivedEvent event = new ClientChatReceivedEvent((byte) 0, new ChatComponentText(toSend.toString().trim()));
        CTJS.getInstance().getChatListener().onReceiveChat(event);

        if (!event.isCanceled()) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(ChatLib.addColor(toSend.toString().trim())));
        }
    }

    private final int idFixed = 90123;
    private Integer idFixedOffset = null;
    private void dumpChat(int lines) {
        clearOldDump();
        ArrayList<String> messages = CTJS.getInstance().getChatListener().getChatHistory();

        if (lines > messages.size()) lines = messages.size();
        ChatLib.chat("&6&m" + ChatLib.getChatBreak("-"), idFixed);
        String msg;
        for (int i = 0; i < lines; i++) {
            msg = ChatLib.replaceFormatting(messages.get(messages.size() - lines + i));
            ChatComponentText cct = new ChatComponentText(msg);

            cct.setChatStyle(new ChatStyle()
                    .setChatClickEvent(
                            new ClickEvent(ClickEvent.Action.getValueByCanonicalName("run_command"), "/ct copy " + msg))
                    .setChatHoverEvent(
                            new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("§eClick here to copy this message."))));

            ChatLib.chat(new Message(cct).setChatLineId(idFixed + i + 1));
        }
        ChatLib.chat("&6&m" + ChatLib.getChatBreak("-"), idFixed + lines + 1);

        idFixedOffset = idFixed + lines + 1;
    }

    private void copyArgsToClipboard(String[] args) {
        clearOldDump();
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }

        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(sb.toString()), null);
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
            Desktop.getDesktop().open(new File("./mods/ChatTriggers"));
        } catch (IOException exception) {
            Console.getConsole().printStackTrace(exception);
            ChatLib.chat("&cCould not open file location");
        }
    }
}
