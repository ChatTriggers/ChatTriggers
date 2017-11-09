package com.chattriggers.ctjs.commands;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.imports.gui.ModulesGui;
import com.chattriggers.ctjs.libs.ChatLib;
import com.chattriggers.ctjs.triggers.TriggerType;
import com.chattriggers.ctjs.utils.Message;
import com.chattriggers.ctjs.utils.console.Console;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

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
               "&c/ct <load/reload> &b- &eReloads all of the ct imports.\n" +
               "&c/ct files &b- &eOpens the ChatTriggers folder.\n" +
               "&c/ct console &b- &eOpens the ct console.\n" +
               "&c/ct simulate &b- &eSimulates a received chat message.\n" +
               "&c/ct dump &b- &eDumps previous chat messages into chat, showing color codes.\n" +
               "&c/ct &b- &eDisplays this help dialog\n" +
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
                    TriggerType.WORLD_UNLOAD.triggerAll();
                    CTJS.getInstance().getModuleManager().unload();
                    ChatLib.chat(EnumChatFormatting.RED + "Reloading ct.js scripts...");
                    new Thread(() -> {
                        CTJS.getInstance().getConfig().loadConfig();
                        CTJS.getInstance().getModuleManager().load();
                        ChatLib.chat(EnumChatFormatting.GREEN + "Done reloading scripts!");
                        TriggerType.WORLD_LOAD.triggerAll();
                    }).start();
                    break;
                case("files"):
                    openFileLocation();
                    break;
                case("console"):
                    Console.getConsole().showConsole(true);
                    Console.clear();
                    break;
                case("gui"):
                    new Thread(() -> {
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException exception) {
                            Console.getConsole().printStackTrace(exception);
                        }

                        Minecraft.getMinecraft().displayGuiScreen(
                                new ModulesGui(CTJS.getInstance().getModuleManager().getModules())
                        );
                    }).start();
                    break;
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

        for (String arg : args) toSend.append(arg).append(" ");

        ClientChatReceivedEvent event = new ClientChatReceivedEvent((byte) 0, new ChatComponentText(toSend.toString().trim()));
        CTJS.getInstance().getChatListener().onReceiveChat(event);

        if (!event.isCanceled()) ChatLib.chat(event.message.getFormattedText());
    }

    private final int idFixed = 90123;
    private Integer idFixedOffset = null;
    private void dumpChat(int lines) {
        System.out.println(CTJS.getInstance().getChatListener().getChatHistory());
        ArrayList<String> messages = CTJS.getInstance().getChatListener().getChatHistory();

        if (lines > messages.size()) lines = messages.size();
        ChatLib.chat("&6&m" + ChatLib.getChatBreak("-"));
        for (int i = 0; i < lines; i++) {
            ChatLib.chat(
                    new Message(
                            ChatLib.clickable(
                                    messages.get(i),
                                    "run_command",
                                    "/ct copy " + messages.get(i),
                                    "&eClick to copy text"
                            )
                    ).setChatLineId(idFixed + i)
            );
        }
        ChatLib.chat("&6&m" + ChatLib.getChatBreak("-"));

        idFixedOffset = idFixed + lines;
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
            ChatLib.clearChat(idFixedOffset);
            idFixedOffset--;
        }

        idFixedOffset = null;
    }

    // Open the folder containing all of ChatTrigger's files
    private void openFileLocation() {
        try {
            Desktop.getDesktop().open(new File("./mods/ChatTriggers"));
        } catch (IOException exception) {
            Console.getConsole().printStackTrace(exception);
            ChatLib.chat(EnumChatFormatting.RED + "Could not open file location");
        }
    }
}
