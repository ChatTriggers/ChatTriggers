package com.chattriggers.jsct.libs;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Iterator;
import java.util.List;

@UtilityClass
@SideOnly(Side.CLIENT)
public class ChatLib {

    /**
     * Adds as many chat message to chat as passed
     * @param messages the message(s) to be printed
     */
    public static void chat(String... messages) {
        for (String message : messages) {
            if (!isPlayer("[CHAT]: " + message)) return;

            ChatComponentText cct = new ChatComponentText(addColor(message));
            Minecraft.getMinecraft().getNetHandler().handleChat(new S02PacketChat(cct, (byte) 0));
        }
    }

    /**
     * Add a chat message to chat, but with a special ID which allows
     * them to be clear with {@link #clearChat(int...)}.
     * This ID can be used more than once.
     * @param message the message to be printed
     * @param chatLineID the chat line to save the message under (pass to clearChat)
     */
    public static void chat(int chatLineID, String message) {
        if (!isPlayer("[CHAT]: " + message)) return;

        ChatComponentText cct = new ChatComponentText(addColor(message));
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(cct, chatLineID);
    }

    /**
     * Say chat message
     * @param message the message to be sent
     */
    public static void say(String message) {
        //TODO: Add checking for creator
        if (!isPlayer("[SAY]: " + message)) return;

        Minecraft.getMinecraft().thePlayer.sendChatMessage(message);
    }

    /**
     * Run a command
     * @param command the command to run, without the leading slash (Ex. "help")
     */
    public static void command(String command) {
        //TODO: Add checking for creator
        if (!isAllowedCommand(command)) return;
        if (!isPlayer("[COMMAND]: /" + command)) return;

        Minecraft.getMinecraft().thePlayer.sendChatMessage("/" + command);
    }

    /**
     * Clear chat
     */
    public static void clearChat() {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().deleteChatLine(0);
    }

    /**
     * Clear chat messages with the specified ID
     * @param chatLineIDs the id(s) to be cleared
     */
    public static void clearChat(int... chatLineIDs) {
        for (int chatLineID : chatLineIDs) {
            Minecraft.getMinecraft().ingameGUI.getChatGUI().deleteChatLine(chatLineID);
        }
    }

    /**
     * Edits an already sent chat message
     * @param chatMessage the unformatted text of the message to be replaced
     * @param toReplace the new message to be put in replace of the old one
     */
    public static void editChat(String chatMessage, String toReplace) {
        List<ChatLine> drawnChatLines = ReflectionHelper.getPrivateValue(GuiNewChat.class, Minecraft.getMinecraft().ingameGUI.getChatGUI(),
                "drawnChatLines", "field_146253_i");
        List<ChatLine> chatLines = ReflectionHelper.getPrivateValue(GuiNewChat.class, Minecraft.getMinecraft().ingameGUI.getChatGUI(),
                "chatLines", "field_146252_h");

        ChatComponentText cct = new ChatComponentText(addColor(toReplace));

        for (int i = 0; i < drawnChatLines.size(); i++) {
            ChatLine chatLine = drawnChatLines.get(i);

            if (chatLine.getChatComponent().getUnformattedText().equals(chatMessage)) {
                ReflectionHelper.setPrivateValue(ChatLine.class, chatLine, cct, "lineString", "field_74541_b");
            }
        }

        for (int i = 0; i < chatLines.size(); i++) {
            ChatLine chatLine = chatLines.get(i);

            if (chatLine.getChatComponent().getUnformattedText().equals(chatMessage)) {
                ReflectionHelper.setPrivateValue(ChatLine.class, chatLine, cct, "lineString", "field_74541_b");
            }
        }
    }

    private static boolean isAllowedCommand(String command) {
        if (Minecraft.getMinecraft().isSingleplayer())
            return true;

        switch (command.toLowerCase()) {
            case("who"):
            case("whereami"):
            case("wtfmap"):
                return true;
            default:
                return false;
        }
    }

    private Boolean isPlayer(String out) {
        if (Minecraft.getMinecraft().thePlayer == null) {
            System.out.println(out);
            return false;
        }
        return true;
    }

    public static String addColor(String message) {
        if (message == null) {
            message = "null";
        }

        return message.replace("&", "\u00a7");
    }
}
