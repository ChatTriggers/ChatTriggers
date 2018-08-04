package com.chattriggers.ctjs.minecraft.libs;

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
import com.chattriggers.ctjs.minecraft.listeners.ChatListener;
import com.chattriggers.ctjs.minecraft.objects.message.Message;
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import com.chattriggers.ctjs.minecraft.wrappers.Player;
import com.chattriggers.ctjs.utils.console.Console;
import lombok.experimental.UtilityClass;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.ChatComponentText;
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
     * Prints text in the chat.
     *
     * @param text the text to be printed
     */
    public static void chat(String text) {
        new Message(text).chat();
    }

    /**
     * Print a {@link Message} in chat.
     *
     * @param message the {@link Message} to be printed
     */
    public static void chat(Message message) {
        message.chat();
    }

    /**
     * Prints a {@link TextComponent} in chat.
     * 
     * @param component the {@link TextComponent} to be printed
     */
    public static void chat(TextComponent component) {
        component.chat();
    }

    /**
     * Shows text in the action bar.
     *
     * @param text the text to show
     */
    public static void actionBar(String text) {
        new Message(text).actionBar();
    }

    /**
     * Shows a {@link Message} in the action bar.
     *
     * @param message the {@link Message} to show
     */
    public static void actionBar(Message message) {
        message.actionBar();
    }

    /**
     * Shows a {@link TextComponent} in the action bar.
     *
     * @param component the {@link TextComponent} to show
     */
    public static void actionBar(TextComponent component) {
        component.actionBar();
    }

    /**
     * Simulates a chat message to be caught by other triggers for testing.
     *
     * @param message The message to simulate
     */
    public static void simulateChat(String message) {
        new Message(message).setRecursive(true).chat();
    }

    /**
     * Say chat message.
     *
     * @param message the message to be sent
     */
    public static void say(String message) {
        if (!isPlayer("[SAY]: " + message)) return;

        Player.getPlayer().sendChatMessage(message);
    }

    /**
     * Run a command.
     *
     * @param command the command to run, without the leading slash (Ex. "help")
     */
    public static void command(String command) {
        if (!isPlayer("[COMMAND]: /" + command)) return;

        Player.getPlayer().sendChatMessage("/" + command);
    }

    /**
     * Clear chat
     */
    public static void clearChat() {
        Client.getChatGUI().clearChatMessages();
    }

    /**
     * Clear chat messages with the specified ID
     *
     * @param chatLineIDs the id(s) to be cleared
     */
    public static void clearChat(int... chatLineIDs) {
        for (int chatLineID : chatLineIDs) {
            Client.getChatGUI().deleteChatLine(chatLineID);
        }
    }

    /**
     * Get a message that will be perfectly one line of chat,
     * the separator repeated as many times as necessary.
     *
     * @param separator the message to split chat with
     * @return the message that would split chat
     */
    public static String getChatBreak(String separator) {
        StringBuilder stringBuilder = new StringBuilder();
        FontRenderer fRenderer = Renderer.getFontRenderer();

        while (fRenderer.getStringWidth(stringBuilder.toString()) < Client.getChatGUI().getChatWidth()) {
            stringBuilder.append(separator);
        }

        return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
    }

    /**
     * Gets the width of Minecraft's chat
     *
     * @return the width of chat
     */
    public static int getChatWidth() {
        return Client.getChatGUI().getChatWidth();
    }

    /**
     * Remove all formatting
     *
     * @param toRemove the string to un-format
     * @return the unformatted string
     */
    public static String removeFormatting(String toRemove) {
        return toRemove.replaceAll("\\u00a7[0-9a-fklmnor]", "")
                .replaceAll("&[0-9a-fklmnor]", "");
    }

    /**
     * Replaces Minecraft formatted text with normal formatted text
     *
     * @param toUnformat the formatted string
     * @return the unformatted string
     */
    public static String replaceFormatting(String toUnformat) {
        return toUnformat.replaceAll("\\u00a7(?![^0-9a-fklmnor]|$)", "&");
    }

    /**
     * Get a message that will be perfectly centered in chat.
     *
     * @param input the text to be centered
     * @return the centered message
     */
    public static String getCenteredText(String input) {
        boolean left = true;
        StringBuilder stringBuilder = new StringBuilder(removeFormatting(input));
        FontRenderer fRenderer = Client.getMinecraft().fontRendererObj;

        if (fRenderer.getStringWidth(stringBuilder.toString()) > Client.getChatGUI().getChatWidth()) {
            return stringBuilder.toString();
        }

        while (fRenderer.getStringWidth(stringBuilder.toString()) < Client.getChatGUI().getChatWidth()) {
            if (left) {
                stringBuilder.insert(0, " ");
                left = false;
            } else {
                stringBuilder.append(" ");
                left = true;
            }
        }

        return stringBuilder.deleteCharAt(left ? 0 : stringBuilder.length() - 1).toString().replace(removeFormatting(input), input);
    }

    /**
     * Edits an already sent chat message
     *
     * @param chatMessage the unformatted text of the message to be replaced
     * @param toReplace   the new message to be put in replace of the old one
     */
    public static void editChat(String chatMessage, String toReplace) {
        editChat(chatMessage, toReplace, false);
    }

    /**
     * Edits an already sent chat message
     *
     * @param chatMessage the unformatted text of the message to be replaced
     * @param toReplace   the new message to be put in replace of the old one
     */
    public static void editChat(String chatMessage, String toReplace, boolean once) {
        List<ChatLine> drawnChatLines = ReflectionHelper.getPrivateValue(GuiNewChat.class, Client.getChatGUI(),
                "drawnChatLines", "field_146252_h");
        List<ChatLine> chatLines = ReflectionHelper.getPrivateValue(GuiNewChat.class, Client.getChatGUI(),
                "chatLines", "field_146252_h");

        ChatComponentText cct = new ChatComponentText(addColor(toReplace));

        for (ChatLine chatLine : drawnChatLines) {
            if (removeFormatting(chatLine.getChatComponent().getUnformattedText()).equals(chatMessage)) {
                ReflectionHelper.setPrivateValue(ChatLine.class, chatLine, cct, "lineString", "field_74541_b");

                if (once) break;
            }
        }

        for (ChatLine chatLine : chatLines) {
            if (removeFormatting(chatLine.getChatComponent().getUnformattedText()).equals(chatMessage)) {
                ReflectionHelper.setPrivateValue(ChatLine.class, chatLine, cct, "lineString", "field_74541_b");

                if (once) break;
            }
        }
    }

    /**
     * Edits an already sent chat message
     *
     * @param chatLineId    the ID of the chat message to replace
     * @param toReplace     the content to replace the message with
     */
    public static void editChat(int chatLineId, String toReplace) {
        editChat(chatLineId, toReplace, false);
    }

    /**
     * Edits an already sent chat message
     *
     * @param chatLineId    the ID of the chat message to replace
     * @param toReplace     the content to replace the message with
     */
    public static void editChat(int chatLineId, String toReplace, boolean once) {
        List<ChatLine> drawnChatLines = ReflectionHelper.getPrivateValue(GuiNewChat.class, Client.getChatGUI(),
                "drawnChatLines", "field_146252_h");
        List<ChatLine> chatLines = ReflectionHelper.getPrivateValue(GuiNewChat.class, Client.getChatGUI(),
                "chatLines", "field_146252_h");

        ChatComponentText cct = new ChatComponentText(addColor(toReplace));

        for (ChatLine chatLine : drawnChatLines) {
            if (chatLine.getChatLineID() == chatLineId) {
                ReflectionHelper.setPrivateValue(ChatLine.class, chatLine, cct, "lineString", "field_74541_b");

                if (once) break;
            }
        }

        for (ChatLine chatLine : chatLines) {
            if (chatLine.getChatLineID() == chatLineId) {
                ReflectionHelper.setPrivateValue(ChatLine.class, chatLine, cct, "lineString", "field_74541_b");

                if (once) break;
            }
        }
    }

    /**
     * Edits an already sent chat message
     *
     * @param chatLineId    the ID of the chat message to replace
     * @param toReplace     the {@link Message} object to replace the message with
     */
    public static void editChat(int chatLineId, Message toReplace) {
        editChat(chatLineId, toReplace, false);
    }

    /**
     * Edits an already sent chat message
     *
     * @param chatLineId    the ID of the chat message to replace
     * @param toReplace     the {@link Message} object to replace the message with
     */
    public static void editChat(int chatLineId, Message toReplace, boolean once) {
        List<ChatLine> drawnChatLines = ReflectionHelper.getPrivateValue(GuiNewChat.class, Client.getChatGUI(),
                "drawnChatLines", "field_146252_h");
        List<ChatLine> chatLines = ReflectionHelper.getPrivateValue(GuiNewChat.class, Client.getChatGUI(),
                "chatLines", "field_146252_h");

        toReplace.parseMessages();
        ChatComponentText cct = (ChatComponentText) toReplace.getChatMessage();

        for (ChatLine chatLine : drawnChatLines) {
            if (chatLine.getChatLineID() == chatLineId) {
                ReflectionHelper.setPrivateValue(ChatLine.class, chatLine, cct, "lineString", "field_74541_b");

                if (once) break;
            }
        }

        for (ChatLine chatLine : chatLines) {
            if (chatLine.getChatLineID() == chatLineId) {
                ReflectionHelper.setPrivateValue(ChatLine.class, chatLine, cct, "lineString", "field_74541_b");

                if (once) break;
            }
        }
    }

    /**
     * Gets the previous 1000 lines of chat
     *
     * @return A list of the last 1000 chat lines
     */
    public static ArrayList<String> getChatLines() {
        ArrayList<String> out = new ArrayList<>();
        ArrayList<String> in = ChatListener.getInstance().getChatHistory();
        for (int i = in.size() - 1; i >= 0; i--)
            out.add(in.get(i));
        return out;
    }

    /**
     * Get the text of a chat event.
     *
     * @param event     The chat event passed in by a chat trigger
     * @param formatted If true, returns formatted text. Otherwise, returns
     *                  unformatted text
     * @return The text of the event
     */
    public static String getChatMessage(ClientChatReceivedEvent event, boolean formatted) {
        if (formatted) {
            return EventLib.getMessage(event).getFormattedText().replace('\u00A7', '&');
        } else {
            return EventLib.getMessage(event).getUnformattedText();
        }
    }

    /**
     * Get the unformatted text of a chat event.
     *
     * @param event The chat event passed in by a chat trigger
     * @return The unformatted text
     */
    public static String getChatMessage(ClientChatReceivedEvent event) {
        return getChatMessage(event, false);
    }

    /**
     * Replaces the easier to type '&amp;' color codes with proper color codes in a string.
     *
     * @param message The string to add color codes to
     * @return the formatted message
     */
    public static String addColor(String message) {
        if (message == null)
            message = "null";

        return message.replaceAll("(?:(?<!\\\\))&(?![^0-9a-fklmnor]|$)", "\u00a7");
    }

    // helper method to make sure player exists before putting something in chat
    public Boolean isPlayer(String out) {
        if (Player.getPlayer() == null) {
            Console.getInstance().out.println(out);
            return false;
        }

        return true;
    }
}
