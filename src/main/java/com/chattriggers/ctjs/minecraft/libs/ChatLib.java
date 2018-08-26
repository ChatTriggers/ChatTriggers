//package com.chattriggers.ctjs.minecraft.libs;
//
//import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
//import com.chattriggers.ctjs.minecraft.listeners.ChatListener;
//import com.chattriggers.ctjs.minecraft.mixins.MixinGuiNewChat;
//import com.chattriggers.ctjs.minecraft.objects.message.Message;
//import com.chattriggers.ctjs.minecraft.objects.message.TextComponent;
//import com.chattriggers.ctjs.minecraft.wrappers.Client;
//import com.chattriggers.ctjs.minecraft.wrappers.Player;
//import com.chattriggers.ctjs.utils.console.Console;
//import jdk.nashorn.api.scripting.ScriptObjectMirror;
//import lombok.experimental.UtilityClass;
//import net.minecraft.client.gui.ChatLine;
//import net.minecraft.client.gui.FontRenderer;
//import net.minecraftforge.client.event.ClientChatReceivedEvent;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.ListIterator;
//import java.util.function.Function;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//@UtilityClass
//@SideOnly(Side.CLIENT)
//public class ChatLib {
//    /**
//     * Prints text in the chat.
//     *
//     * @param text the text to be printed
//     */
//    public static void chat(String text) {
//        new Message(text).chat();
//    }
//
//    /**
//     * Print a {@link Message} in chat.
//     *
//     * @param message the {@link Message} to be printed
//     */
//    public static void chat(Message message) {
//        message.chat();
//    }
//
//    /**
//     * Prints a {@link TextComponent} in chat.
//     *
//     * @param component the {@link TextComponent} to be printed
//     */
//    public static void chat(TextComponent component) {
//        component.chat();
//    }
//
//    /**
//     * Shows text in the action bar.
//     *
//     * @param text the text to show
//     */
//    public static void actionBar(String text) {
//        new Message(text).actionBar();
//    }
//
//    /**
//     * Shows a {@link Message} in the action bar.
//     *
//     * @param message the {@link Message} to show
//     */
//    public static void actionBar(Message message) {
//        message.actionBar();
//    }
//
//    /**
//     * Shows a {@link TextComponent} in the action bar.
//     *
//     * @param component the {@link TextComponent} to show
//     */
//    public static void actionBar(TextComponent component) {
//        component.actionBar();
//    }
//
//    /**
//     * Simulates a chat message to be caught by other triggers for testing.
//     *
//     * @param message The message to simulate
//     */
//    public static void simulateChat(String message) {
//        new Message(message).setRecursive(true).chat();
//    }
//
//    /**
//     * Say chat message.
//     *
//     * @param message the message to be sent
//     */
//    public static void say(String message) {
//        if (!isPlayer("[SAY]: " + message)) return;
//
//        Player.getPlayer().sendChatMessage(message);
//    }
//
//    /**
//     * Run a command.
//     *
//     * @param command the command to run, without the leading slash (Ex. "help")
//     */
//    public static void command(String command) {
//        if (!isPlayer("[COMMAND]: /" + command)) return;
//
//        Player.getPlayer().sendChatMessage("/" + command);
//    }
//
//    /**
//     * Clear chat
//     */
//    public static void clearChat() {
//        //#if MC<=10809
//        Client.getChatGUI().clearChatMessages();
//        //#else
//        //$$ Client.getChatGUI().clearChatMessages(false);
//        //#endif
//    }
//
//    /**
//     * Clear chat messages with the specified ID
//     *
//     * @param chatLineIDs the id(s) to be cleared
//     */
//    public static void clearChat(int... chatLineIDs) {
//        for (int chatLineID : chatLineIDs) {
//            Client.getChatGUI().deleteChatLine(chatLineID);
//        }
//    }
//
//    /**
//     * Get a message that will be perfectly one line of chat,
//     * the separator repeated as many times as necessary.
//     *
//     * @param separator the message to split chat with
//     * @return the message that would split chat
//     */
//    public static String getChatBreak(String separator) {
//        StringBuilder stringBuilder = new StringBuilder();
//        FontRenderer fRenderer = Renderer.getFontRenderer();
//
//        while (fRenderer.getStringWidth(stringBuilder.toString()) < Client.getChatGUI().getChatWidth()) {
//            stringBuilder.append(separator);
//        }
//
//        return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
//    }
//
//    /**
//     * Gets the width of Minecraft's chat
//     *
//     * @return the width of chat
//     */
//    public static int getChatWidth() {
//        return Client.getChatGUI().getChatWidth();
//    }
//
//    /**
//     * Remove all formatting
//     *
//     * @param toRemove the string to un-format
//     * @return the unformatted string
//     */
//    public static String removeFormatting(String toRemove) {
//        return toRemove.replaceAll("[\\u00a7&][0-9a-fklmnor]", "");
//    }
//
//    /**
//     * Replaces Minecraft formatted text with normal formatted text
//     *
//     * @param toUnformat the formatted string
//     * @return the unformatted string
//     */
//    public static String replaceFormatting(String toUnformat) {
//        return toUnformat.replaceAll("\\u00a7(?![^0-9a-fklmnor]|$)", "&");
//    }
//
//    /**
//     * Get a message that will be perfectly centered in chat.
//     *
//     * @param input the text to be centered
//     * @return the centered message
//     */
//    public static String getCenteredText(String input) {
//        boolean left = true;
//        StringBuilder stringBuilder = new StringBuilder(removeFormatting(input));
//        FontRenderer fRenderer = Renderer.getFontRenderer();
//
//        if (fRenderer.getStringWidth(stringBuilder.toString()) > Client.getChatGUI().getChatWidth()) {
//            return stringBuilder.toString();
//        }
//
//        while (fRenderer.getStringWidth(stringBuilder.toString()) < Client.getChatGUI().getChatWidth()) {
//            if (left) {
//                stringBuilder.insert(0, " ");
//                left = false;
//            } else {
//                stringBuilder.append(" ");
//                left = true;
//            }
//        }
//
//        return stringBuilder.deleteCharAt(left ? 0 : stringBuilder.length() - 1).toString().replace(removeFormatting(input), input);
//    }
//
//    /**
//     * Edits an already sent chat message by regex.
//     * If the JavaScript RegExp object passed in matches a message, it will be replaced.
//     * The regex object will be created by the {@code new RegExp()} constructor,
//     * or the {@code //} regex literal. All flags will be respected.
//     *
//     * @param regexp the regex object to match to the message
//     * @param replacements the new message(s) to be put in replace of the old one
//     */
//    public static void editChat(Object regexp, Message... replacements) {
//        if (!(regexp instanceof ScriptObjectMirror)) {
//            throw new IllegalArgumentException("Regex object not correct");
//        }
//
//        ScriptObjectMirror regex = ((ScriptObjectMirror) regexp);
//
//        boolean global = (boolean) regex.get("global");
//        boolean ignoreCase = (boolean) regex.get("ignoreCase");
//        boolean multiline = (boolean) regex.get("multiline");
//
//        int flags = (ignoreCase ? Pattern.CASE_INSENSITIVE : 0)
//                    | (multiline ? Pattern.MULTILINE : 0);
//        Pattern pattern = Pattern.compile((String) regex.get("source"), flags);
//
//        editChat(
//                message -> {
//                    Matcher matcher = pattern.matcher(message.getChatMessage().getUnformattedText());
//                    return global ? matcher.find() : matcher.matches();
//                },
//                replacements
//        );
//    }
//
//    /**
//     * Edits an already sent chat message by the text of the chat
//     *
//     * @param toReplace the unformatted text of the message to be replaced
//     * @param replacements the new message(s) to be put in place of the old one
//     */
//    public static void editChat(String toReplace, Message... replacements) {
//        editChat(
//                message -> removeFormatting(message.getChatMessage().getUnformattedText()).equals(toReplace),
//                replacements
//        );
//    }
//
//    /**
//     * Edits an already sent chat message by the {@link Message}
//     *
//     * @param toReplace the message to be replaced
//     * @param replacements the new message(s) to be put in place of the old one
//     */
//    public static void editChat(Message toReplace, Message... replacements) {
//        editChat(
//                message -> {
//                    System.out.println("tr: " + toReplace.getChatMessage().getFormattedText());
//                    System.out.println("m: " + message.getChatMessage().getFormattedText().replaceFirst("\\u00a7r", ""));
//                    return toReplace.getChatMessage().getFormattedText().equals(message.getChatMessage().getFormattedText().replaceFirst("\\u00a7r", ""));
//                },
//                replacements
//        );
//    }
//
//    /**
//     * Edits an already sent chat message by its chat line id
//     *
//     * @param chatLineId the chat line id of the message to be replaced
//     * @param replacements the new message(s) to be put in place of the old one
//     */
//    public static void editChat(int chatLineId, Message... replacements) {
//        editChat(
//                message -> message.getChatLineId() == chatLineId,
//                replacements
//        );
//    }
//
//    /**
//     * Edits an already sent chat message.
//     * Whether each specific message is edited or not is up to the first parameter, the "comparator" function.
//     * This function will be passed a {@link Message} object and has to return a boolean for whether or not
//     * that specific message should be edited. (true for yes, false for no). There are overrides of this function
//     * that already implement different versions of this method and those should be used in place of this one
//     * if there is already a suitable replacement. Otherwise, create one and use this method.
//     *
//     * @param toReplace the "comparator" function
//     * @param replacements the replacement messages
//     */
//    public static void editChat(Function<Message, Boolean> toReplace, Message... replacements) {
//        List<ChatLine> drawnChatLines = ((MixinGuiNewChat) Client.getChatGUI()).getDrawnChatLines();
//        List<ChatLine> chatLines = ((MixinGuiNewChat) Client.getChatGUI()).getChatLines();
//
//        editChatLineList(chatLines, toReplace, replacements);
//        editChatLineList(drawnChatLines, toReplace, replacements);
//    }
//
//    private static void editChatLineList(List<ChatLine> lineList, Function<Message, Boolean> toReplace, Message... replacements) {
//        ListIterator<ChatLine> chatLineIterator = lineList.listIterator();
//
//        while (chatLineIterator.hasNext()) {
//            ChatLine chatLine = chatLineIterator.next();
//
//            boolean result = toReplace.apply(
//                    new Message(chatLine.getChatComponent()).setChatLineId(chatLine.getChatLineID())
//            );
//
//            if (!result) {
//                continue;
//            }
//
//            chatLineIterator.remove();
//
//            for (Message message : replacements) {
//                int lineId = message.getChatLineId() == -1 ? 0 : message.getChatLineId();
//
//                ChatLine newChatLine = new ChatLine(chatLine.getUpdatedCounter(), message.getChatMessage(), lineId);
//                chatLineIterator.add(newChatLine);
//            }
//        }
//    }
//
//    /**
//     * Gets the previous 1000 lines of chat
//     *
//     * @return A list of the last 1000 chat lines
//     */
//    public static ArrayList<String> getChatLines() {
//        ArrayList<String> out = new ArrayList<>();
//        List<String> in = ChatListener.INSTANCE.getChatHistory();
//        for (int i = in.size() - 1; i >= 0; i--)
//            out.add(in.get(i));
//        return out;
//    }
//
//    /**
//     * Get the text of a chat event.
//     *
//     * @param event     The chat event passed in by a chat trigger
//     * @param formatted If true, returns formatted text. Otherwise, returns
//     *                  unformatted text
//     * @return The text of the event
//     */
//    public static String getChatMessage(ClientChatReceivedEvent event, boolean formatted) {
//        if (formatted) {
//            return EventLib.getMessage(event).getFormattedText().replace('\u00A7', '&');
//        } else {
//            return EventLib.getMessage(event).getUnformattedText();
//        }
//    }
//
//    /**
//     * Get the unformatted text of a chat event.
//     *
//     * @param event The chat event passed in by a chat trigger
//     * @return The unformatted text
//     */
//    public static String getChatMessage(ClientChatReceivedEvent event) {
//        return getChatMessage(event, false);
//    }
//
//    /**
//     * Replaces the easier to type '&amp;' color codes with proper color codes in a string.
//     *
//     * @param message The string to add color codes to
//     * @return the formatted message
//     */
//    public static String addColor(String message) {
//        if (message == null)
//            message = "null";
//
//        return message.replaceAll("(?:(?<!\\\\))&(?![^0-9a-fklmnor]|$)", "\u00a7");
//    }
//
//    // helper method to make sure player exists before putting something in chat
//    public static boolean isPlayer(String out) {
//        if (Player.getPlayer() == null) {
//            Console.getInstance().out.println(out);
//            return false;
//        }
//
//        return true;
//    }
//}
