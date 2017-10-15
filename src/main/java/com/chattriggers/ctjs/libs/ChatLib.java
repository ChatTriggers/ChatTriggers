package com.chattriggers.ctjs.libs;

import com.chattriggers.ctjs.utils.Message;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
@SideOnly(Side.CLIENT)
public class ChatLib {

    /**
     * Adds as many chat message to chat as passed.
     * @param recursive whether or not triggers should be triggered by this message
     * @param message the message to be printed
     */
    public static void chat(String message, boolean recursive) {
        if (!isPlayer("[CHAT]: " + message)) return;

        ChatComponentText cct = new ChatComponentText(addColor(message));

        if (recursive) {
            Minecraft.getMinecraft().getNetHandler().handleChat(new S02PacketChat(cct, (byte) 0));
        } else {
            Minecraft.getMinecraft().thePlayer.addChatMessage(cct);
        }
    }

    /**
     * Adds a message to chat (IS RECURSIVE! See {@link #chat(String, boolean)}
     * to specify it not being recursive.
     * @param message the message to be printed
     */
    public static void chat(String message) {
        chat(message, true);
    }

    /**
     * Print a {@link Message} in chat.
     * @param recursive whether or not triggers should be triggered by this message
     * @param message the message to be printed
     */
    public static void chat(Message message, boolean recursive) {
        if (message.getChatLineId() != -1) {
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(message.getChatMessage(), message.getChatLineId());
            return;
        }

        if (recursive) {
            Minecraft.getMinecraft().getNetHandler().handleChat(new S02PacketChat(message.getChatMessage(), (byte) 0));
        } else {
            Minecraft.getMinecraft().thePlayer.addChatMessage(message.getChatMessage());
        }
    }

    /**
     * Print a {@link Message} in chat (IS RECURSIVE! See {@link #chat(Message, boolean)}
     * to specify it not being recursive.
     * @param message the message to be printed
     */
    public static void chat(Message message) {
        chat(message, true);
    }

    /**
     * Add a chat message to chat, but with a special ID which allows
     * them to be clear with {@link #clearChat(int...)}.
     * This ID can't be used more than once.
     * @param chatLineID the chat line to save the message under (pass to clearChat)
     * @param message the message to be printed
     */
    public static void chat(String message, int chatLineID) {
        if (!isPlayer("[CHAT]: " + message)) return;

        ChatComponentText cct = new ChatComponentText(addColor(message));
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(cct, chatLineID);
    }

    /**
     * Say chat message.
     * @param message the message to be sent
     */
    public static void say(String message) {
        //TODO: Add checking for creator
        if (!isPlayer("[SAY]: " + message)) return;

        Minecraft.getMinecraft().thePlayer.sendChatMessage(message);
    }

    /**
     * Run a command.
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
        Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages();
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
     * Get a message that will be perfectly one line of chat,
     * the sepearator repeated as many times as necessary.
     * @param seperator the message to split chat with
     * @return the message that would split chat
     */
    public static String getChatBreak(String seperator) {
        StringBuilder stringBuilder = new StringBuilder();
        FontRenderer fRenderer = Minecraft.getMinecraft().fontRendererObj;

        while (fRenderer.getStringWidth(stringBuilder.toString()) < Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatWidth()) {
            stringBuilder.append(seperator);
        }

        return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
    }

    /**
     * Remove all formatting
     * @param toRemove the string to un-format
     * @return the unformatted string
     */
    public static String removeFormatting(String toRemove) {
        return toRemove.replaceAll("\\u00a7[0-9a-fklmnor]", "")
                .replaceAll("&[0-9a-fklmnor]", "");
    }

    /**
     * Replaces minecraft formatted text with normal formatted text
     * @param toUnformat the formatted string
     * @return the unformatted string
     */
    public static String replaceFormatting(String toUnformat) {
        return toUnformat.replaceAll("\\u00a7(?![^0-9a-fklmnor]|$)", "&");
    }

    /**
     * Get a message that will be perfectly centered in chat.
     * @param input the text to be centered
     * @return the centered message
     */
    public static String getCenteredText(String input) {
        boolean left = true;
        StringBuilder stringBuilder = new StringBuilder(removeFormatting(input));
        FontRenderer fRenderer = Minecraft.getMinecraft().fontRendererObj;

        if (fRenderer.getStringWidth(stringBuilder.toString()) > Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatWidth()) {
            return stringBuilder.toString();
        }

        while (fRenderer.getStringWidth(stringBuilder.toString()) < Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatWidth()) {
            if (left) {
                stringBuilder.insert(0, " ");
                left = false;
            } else {
                stringBuilder.append(" ");
                left = true;
            }
        }

        return stringBuilder.deleteCharAt(left ? 0 : stringBuilder.length() - 1).toString().replaceAll(removeFormatting(input), input);
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

        for (ChatLine chatLine : drawnChatLines) {
            if (removeFormatting(chatLine.getChatComponent().getUnformattedText()).equals(chatMessage)) {
                ReflectionHelper.setPrivateValue(ChatLine.class, chatLine, cct, "lineString", "field_74541_b");
            }
        }

        for (ChatLine chatLine : chatLines) {
            if (removeFormatting(chatLine.getChatComponent().getUnformattedText()).equals(chatMessage)) {
                ReflectionHelper.setPrivateValue(ChatLine.class, chatLine, cct, "lineString", "field_74541_b");
            }
        }
    }

    public static ArrayList<String> getChatLines() {
        List<ChatLine> chatLines =  ReflectionHelper.getPrivateValue(GuiNewChat.class, Minecraft.getMinecraft().ingameGUI.getChatGUI(),
                "drawnChatLines", "field_146253_i");
        ArrayList<String> messages = new ArrayList<>();

        for (ChatLine chatLine : chatLines) {
            messages.add(removeFormatting(chatLine.getChatComponent().getUnformattedText()));
        }

        return messages;
    }

    /**
     * Create a clickable message in chat, to be used with {@link Message}.
     * Also shows text on hover.
     * @param text the text to show in the message
     * @param action the action to perform
     * @param value the value to perform the action with
     * @param hoverText the text to show when hovered over
     * @return the chat component created
     */
    public static IChatComponent clickable(String text, String action, String value, String hoverText) {
        ChatComponentText cct = new ChatComponentText(addColor(text));

        cct.setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(
                ClickEvent.Action.getValueByCanonicalName(action), value
        )));

        if (hoverText != null) {
            cct.getChatStyle().setChatHoverEvent(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT, new ChatComponentText(addColor(hoverText))
            ));
        }

        return cct;
    }

    /**
     * Create a clickable message in chat, to be used with {@link Message}.
     * @param text the text to show in the message
     * @param action the action to perform
     * @param value the value to perform the action with
     * @return the chat component created
     */
    public static IChatComponent clickable(String text, String action, String value) {
        return clickable(text, action, value, null);
    }

    /**
     * Create a hoverable message in chat, to be used with {@link Message}
     * @param text the text to show in the message
     * @param hover the text to show when hovered over
     * @return the chat component created
     */
    public static IChatComponent hover(String text, String hover) {
        ChatComponentText cct = new ChatComponentText(addColor(text));

        cct.setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT, new ChatComponentText(addColor(hover))
        )));

        return cct;
    }

    /**
     * Get the text of a chat event.
     * @param event     The chat event passed in by a chat trigger
     * @param formatted If true, returns formatted text. Otherwise, returns
     *                   unformatted text
     * @return          The text of the event
     */
    public static String getChatMessage(ClientChatReceivedEvent event, boolean formatted) {
        if (formatted) {
            return event.message.getFormattedText().replace('\u00A7', '&');
        } else {
            return event.message.getUnformattedText();
        }
    }

    /**
     * Get the unformatted text of a chat event.
     * @param event The chat event passed in by a chat trigger
     * @return      The unformatted text
     */
    public static String getChatMessage(ClientChatReceivedEvent event) {
        return getChatMessage(event, false);
    }

    private static boolean isAllowedCommand(String command) {
        return true;

        // TODO: check for creator
        /*if (Minecraft.getMinecraft().isSingleplayer())
            return true;

        switch (command.toLowerCase()) {
            case("who"):
            case("whereami"):
            case("wtfmap"):
                return true;
            default:
                return false;
        }*/
    }

    private Boolean isPlayer(String out) {
        if (Minecraft.getMinecraft().thePlayer == null) {
            return false;
        }

        return true;
    }

    public static String addColor(String message) {
        if (message == null) {
            message = "null";
        }

        return message.replaceAll("&(?![^0-9a-fklmnor]|$)", "\u00a7");
    }
}
