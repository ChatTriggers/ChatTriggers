package com.chattriggers.ctjs.minecraft.libs

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.listeners.ClientListener
import com.chattriggers.ctjs.minecraft.objects.message.Message
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.printToConsole
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.times
import net.minecraft.client.gui.ChatLine
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.client.event.ClientChatReceivedEvent
import org.mozilla.javascript.NativeObject
import java.util.regex.Pattern
import kotlin.math.roundToInt

@External
object ChatLib {
    /**
     * Prints text in the chat.
     * The text can be a String, a [Message] or a [TextComponent]
     *
     * @param text the text to be printed
     */
    @JvmStatic
    fun chat(text: Any) {
        when (text) {
            is String -> Message(text).chat()
            is Message -> text.chat()
            is TextComponent -> text.chat()
            else -> Message(text.toString()).chat()
        }
    }

    @JvmStatic
    fun test(e: Any) {
        println(e)
    }

    /**
     * Shows text in the action bar.
     * The text can be a String, a [Message] or a [TextComponent]
     *
     * @param text the text to show
     */
    @JvmStatic
    fun actionBar(text: Any) {
        when (text) {
            is String -> Message(text).actionBar()
            is Message -> text.actionBar()
            is TextComponent -> text.actionBar()
        }
    }

    /**
     * Simulates a chat message to be caught by other triggers for testing.
     * The text can be a String, a [Message] or a [TextComponent]
     *
     * @param text The message to simulate
     */
    @JvmStatic
    fun simulateChat(text: Any) {
        when (text) {
            is String -> Message(text).setRecursive(true).chat()
            is Message -> text.setRecursive(true).chat()
            is TextComponent -> Message(text).setRecursive(true).chat()
        }
    }

    /**
     * Says chat message.
     * This message is actually sent to the server.
     *
     * @param text the message to be sent
     */
    @JvmStatic
    fun say(text: String) = Player.getPlayer()?.sendChatMessage(text)

    /**
     * Runs a command.
     *
     * @param text the command to run, without the leading slash (Ex. "help")
     * @param clientSide should the command be ran as a client side command
     */
    @JvmOverloads
    @JvmStatic
    fun command(text: String, clientSide: Boolean = false) {
        if (clientSide) ClientCommandHandler.instance.executeCommand(Player.getPlayer(), "/$text")
        else say("/$text")
    }

    /**
     * Clear chat messages with the specified message ID, or all chat messages if no ID is specified
     *
     * @param chatLineIDs the id(s) to be cleared
     */
    @JvmStatic
    fun clearChat(vararg chatLineIDs: Int) {
        if (chatLineIDs.isEmpty()) {
            //#if MC<=10809
            Client.getChatGUI()?.clearChatMessages()
            //#else
            //$$ Client.getChatGUI()?.clearChatMessages(false)
            //#endif
            return
        }
        for (chatLineID in chatLineIDs)
            Client.getChatGUI()?.deleteChatLine(chatLineID)
    }

    /**
     * Get a message that will be perfectly one line of chat,
     * the separator repeated as many times as necessary.
     * The separator defaults to "-"
     *
     * @param separator the message to split chat with
     * @return the message that would split chat
     */
    @JvmOverloads
    @JvmStatic
    fun getChatBreak(separator: String = "-"): String {
        val len = Renderer.getStringWidth(separator)
        val times = getChatWidth() / len

        return separator * times
    }

    /**
     * Gets the width of Minecraft's chat
     *
     * @return the width of chat
     */
    @JvmStatic
    fun getChatWidth(): Int {
        return Client.getChatGUI()?.chatWidth ?: 0
    }

    /**
     * Remove all formatting
     *
     * @param text the string to un-format
     * @return the unformatted string
     */
    @JvmStatic
    fun removeFormatting(text: String): String {
        return text.replace("[\u00a7&][0-9a-fk-or]".toRegex(), "")
    }

    /**
     * Replaces Minecraft formatted text with normal formatted text
     *
     * @param text the formatted string
     * @return the unformatted string
     */
    @JvmStatic
    fun replaceFormatting(text: String): String {
        return text.replace("\u00a7(?![^0-9a-fk-or]|$)".toRegex(), "&")
    }

    /**
     * Get a message that will be perfectly centered in chat.
     *
     * @param text the text to be centered
     * @return the centered message
     */
    @JvmStatic
    fun getCenteredText(text: String): String {
        val textWidth = Renderer.getStringWidth(addColor(text))
        val chatWidth = getChatWidth()

        if (textWidth >= chatWidth)
            return text

        val spaceWidth = (chatWidth - textWidth) / 2f
        val spaceBuilder = StringBuilder().apply {
            repeat((spaceWidth / Renderer.getStringWidth(" ")).roundToInt()) {
                append(' ')
            }
        }

        return spaceBuilder.append(text).toString()
    }

    /**
     * Edits an already sent chat message matched by [regexp].
     *
     * @param regexp the regex object to match to the message
     * @param replacements the new message(s) to be put in replace of the old one
     */
    @JvmStatic
    fun editChat(regexp: NativeObject, vararg replacements: Message) {
        val global = regexp["global"] as Boolean
        val ignoreCase = regexp["ignoreCase"] as Boolean
        val multiline = regexp["multiline"] as Boolean

        val flags = (if (ignoreCase) Pattern.CASE_INSENSITIVE else 0) or if (multiline) Pattern.MULTILINE else 0
        val pattern = Pattern.compile(regexp["source"] as String, flags)

        editChat(
            {
                val matcher = pattern.matcher(it.getChatMessage().unformattedText)
                if (global) matcher.find() else matcher.matches()
            },
            *replacements
        )
    }

    /**
     * Edits an already sent chat message by the text of the chat
     *
     * @param toReplace the unformatted text of the message to be replaced
     * @param replacements the new message(s) to be put in place of the old one
     */
    @JvmStatic
    fun editChat(toReplace: String, vararg replacements: Message) {
        editChat(
            {
                removeFormatting(it.getChatMessage().unformattedText) == toReplace
            },
            *replacements
        )
    }

    /**
     * Edits an already sent chat message by the [Message]
     *
     * @param toReplace the message to be replaced
     * @param replacements the new message(s) to be put in place of the old one
     */
    @JvmStatic
    fun editChat(toReplace: Message, vararg replacements: Message) {
        editChat(
            {
                toReplace.getChatMessage().formattedText == it.getChatMessage().formattedText.replaceFirst(
                    "\u00a7r".toRegex(),
                    ""
                )
            },
            *replacements
        )
    }

    /**
     * Edits an already sent chat message by its chat line id
     *
     * @param chatLineId the chat line id of the message to be replaced
     * @param replacements the new message(s) to be put in place of the old one
     */
    @JvmStatic
    fun editChat(chatLineId: Int, vararg replacements: Message) {
        editChat(
            { message ->
                message.getChatLineId() == chatLineId
            },
            *replacements
        )
    }

    private fun editChat(toReplace: (Message) -> Boolean, vararg replacements: Message) {
        val drawnChatLines = Client.getChatGUI()!!.drawnChatLines
        val chatLines = Client.getChatGUI()!!.chatLines

        editChatLineList(chatLines, toReplace, *replacements)
        editChatLineList(drawnChatLines, toReplace, *replacements)
    }

    private fun editChatLineList(
        lineList: MutableList<ChatLine>,
        toReplace: (Message) -> Boolean,
        vararg replacements: Message
    ) {
        val chatLineIterator = lineList.listIterator()

        while (chatLineIterator.hasNext()) {
            val chatLine = chatLineIterator.next()

            val result = toReplace(
                Message(chatLine.chatComponent).setChatLineId(chatLine.chatLineID)
            )

            if (!result) {
                continue
            }

            chatLineIterator.remove()

            replacements.map {
                val lineId = if (it.getChatLineId() == -1) 0 else it.getChatLineId()

                ChatLine(chatLine.updatedCounter, it.getChatMessage(), lineId)
            }.forEach(chatLineIterator::add)
        }
    }

    /**
     * Deletes an already sent chat message matching [regexp].
     *
     * @param regexp the regex object to match to the message
     */
    @JvmStatic
    fun deleteChat(regexp: NativeObject) {
        val global = regexp["global"] as Boolean
        val ignoreCase = regexp["ignoreCase"] as Boolean
        val multiline = regexp["multiline"] as Boolean

        val flags = (if (ignoreCase) Pattern.CASE_INSENSITIVE else 0) or if (multiline) Pattern.MULTILINE else 0
        val pattern = Pattern.compile(regexp["source"] as String, flags)

        deleteChat {
            val matcher = pattern.matcher(it.getChatMessage().unformattedText)
            if (global) matcher.find() else matcher.matches()
        }
    }

    /**
     * Deletes an already sent chat message by the text of the chat
     *
     * @param toDelete the unformatted text of the message to be deleted
     */
    @JvmStatic
    fun deleteChat(toDelete: String) {
        deleteChat {
            removeFormatting(it.getChatMessage().unformattedText) == toDelete
        }
    }

    /**
     * Deletes an already sent chat message by the [Message]
     *
     * @param toDelete the message to be deleted
     */
    @JvmStatic
    fun deleteChat(toDelete: Message) {
        deleteChat {
            toDelete.getChatMessage().formattedText == it.getChatMessage().formattedText.replaceFirst(
                "\u00a7r".toRegex(),
                ""
            )
        }
    }

    /**
     * Deletes an already sent chat message by its chat line id
     *
     * @param chatLineId the chat line id of the message to be deleted
     */
    @JvmStatic
    fun deleteChat(chatLineId: Int) {
        deleteChat {
            it.getChatLineId() == chatLineId
        }
    }

    private fun deleteChat(toDelete: (Message) -> Boolean) {
        val drawnChatLines = Client.getChatGUI()!!.drawnChatLines
        val chatLines = Client.getChatGUI()!!.chatLines

        deleteChatLineList(chatLines, toDelete)
        deleteChatLineList(drawnChatLines, toDelete)
    }

    private fun deleteChatLineList(
        lineList: MutableList<ChatLine>,
        toDelete: (Message) -> Boolean,
    ) {
        val chatLineIterator = lineList.listIterator()

        while (chatLineIterator.hasNext()) {
            val chatLine = chatLineIterator.next()

            if (toDelete(Message(chatLine.chatComponent).setChatLineId(chatLine.chatLineID)))
                chatLineIterator.remove()
        }
    }

    /**
     * Gets the previous 1000 lines of chat
     *
     * @return A list of the last 1000 chat lines
     */
    @JvmStatic
    fun getChatLines(): List<String> {
        val hist = ClientListener.chatHistory.toMutableList()
        hist.reverse()
        return hist
    }

    /**
     * Adds a message to the player's chat history. This allows the message to
     * show up for the player when pressing the up/down keys while in the chat gui
     *
     * @param index the index to insert the message
     * @param message the message to add to chat history
     */
    @JvmOverloads
    @JvmStatic
    fun addToSentMessageHistory(index: Int = -1, message: String) {
        val sentMessages = Client.getChatGUI()!!.sentMessages

        if (index == -1) sentMessages.add(message)
        else sentMessages.add(index, message)
    }

    /**
     * Get the text of a chat event.
     * Defaults to the unformatted version.
     *
     * @param event The chat event passed in by a chat trigger
     * @param formatted If true, returns formatted text. Otherwise, returns
     * unformatted text
     * @return The text of the event
     */
    @JvmOverloads
    @JvmStatic
    fun getChatMessage(event: ClientChatReceivedEvent, formatted: Boolean = false): String {
        return if (formatted) {
            replaceFormatting(EventLib.getMessage(event).formattedText)
        } else {
            EventLib.getMessage(event).unformattedText
        }
    }

    /**
     * Replaces the easier to type '&' color codes with proper color codes in a string.
     *
     * @param message The string to add color codes to
     * @return the formatted message
     */
    @JvmStatic
    fun addColor(message: String?): String {
        return message.toString().replace("(?<!\\\\)&(?![^0-9a-fk-or]|$)".toRegex(), "\u00a7")
    }

    // helper method to make sure player exists before putting something in chat
    fun isPlayer(out: String): Boolean {
        if (Player.getPlayer() == null) {
            out.printToConsole()
            return false
        }

        return true
    }
}
