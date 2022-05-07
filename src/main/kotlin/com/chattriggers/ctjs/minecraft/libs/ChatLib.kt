package com.chattriggers.ctjs.minecraft.libs

import com.chattriggers.ctjs.launch.mixins.transformers.ChatGuiMixin
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.listeners.ClientListener
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.printToConsole
import com.chattriggers.ctjs.utils.kotlin.setChatLineId
import com.chattriggers.ctjs.utils.kotlin.setRecursive
import com.chattriggers.ctjs.utils.kotlin.times
import gg.essential.universal.UPacket
import gg.essential.universal.wrappers.message.UMessage
import gg.essential.universal.wrappers.message.UTextComponent
import net.minecraftforge.client.event.ClientChatReceivedEvent
import org.mozilla.javascript.regexp.NativeRegExp
import java.util.regex.Pattern
import kotlin.math.roundToInt

//#if MC<=11202
import net.minecraftforge.client.ClientCommandHandler
typealias MCChatLine = net.minecraft.client.gui.ChatLine
//#else
//$$ import com.chattriggers.ctjs.CTJS
//$$ import com.chattriggers.ctjs.utils.kotlin.MCITextComponent
//$$ import net.minecraft.ChatFormatting
//$$ import net.minecraft.client.GuiMessage
//$$ import net.minecraft.network.chat.Style
//$$ import net.minecraft.network.chat.TextColor
//$$ import net.minecraft.util.FormattedCharSequence
//$$ import net.minecraft.util.FormattedCharSink
//#endif

object ChatLib {
    /**
     * Prints text in the chat.
     * The text can be a String, a [UMessage] or a [UTextComponent]
     *
     * @param text the text to be printed
     */
    @JvmStatic
    fun chat(text: Any) {
        when (text) {
            is String -> UMessage(text).chat()
            is UMessage -> text.chat()
            is UTextComponent -> text.chat()
            else -> UMessage(text.toString()).chat()
        }
    }

    /**
     * Shows text in the action bar.
     * The text can be a String, a [UMessage] or a [UTextComponent]
     *
     * @param text the text to show
     */
    @JvmStatic
    fun actionBar(text: Any) {
        when (text) {
            is String -> UMessage(text).actionBar()
            is UMessage -> text.actionBar()
            is UTextComponent -> text.actionBar()
            else -> UMessage(text.toString()).actionBar()
        }
    }

    /**
     * Simulates a chat message to be caught by other triggers for testing.
     * The text can be a String, a [UMessage] or a [UTextComponent]
     *
     * @param text The message to simulate
     */
    @JvmStatic
    fun simulateChat(text: Any) {
        when (text) {
            is String -> UMessage(text).setRecursive(true).chat()
            is UMessage -> text.setRecursive(true).chat()
            is UTextComponent -> UMessage(text).setRecursive(true).chat()
            else -> UMessage(text.toString()).setRecursive(true).chat()
        }
    }

    /**
     * Says chat message.
     * This message is actually sent to the server.
     *
     * @param text the message to be sent
     */
    @JvmStatic
    fun say(text: String) = UPacket.sendChatMessage(UTextComponent(text).apply { formatted = false })

    /**
     * Runs a command.
     *
     * @param text the command to run, without the leading slash (Ex. "help")
     * @param clientSide should the command be ran as a client side command
     */
    // TODO(VERIFY)
    @JvmOverloads
    @JvmStatic
    fun command(text: String, clientSide: Boolean = false) {
        if (clientSide) {
            //#if MC<=11202
            ClientCommandHandler.instance.executeCommand(Player.getPlayer(), "/$text")
            //#else
            // CTJS.commandDispatcher?.execute(text, Player.getPlayer()!!.createCommandSourceStack())
            //#endif
        } else say("/$text")
    }

    /**
     * Clear chat messages with the specified message ID, or all chat messages if no ID is specified
     *
     * @param chatLineIDs the id(s) to be cleared
     */
    @JvmStatic
    fun clearChat(vararg chatLineIDs: Int) {
        @Suppress("CAST_NEVER_SUCCEEDS")
        val gui = Client.getChatGUI() as? ChatGuiMixin ?: return

        if (chatLineIDs.isEmpty())
            gui.clearMessages()
        for (chatLineID in chatLineIDs)
            gui.deleteMessage(chatLineID)
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
        //#if MC<=11202
        return Client.getChatGUI()?.chatWidth ?: 0
        //#else
        //$$ return Client.getChatGUI()?.width ?: 0
        //#endif
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
    fun editChat(regexp: NativeRegExp, vararg replacements: UMessage) {
        val global = regexp["global"] as Boolean
        val ignoreCase = regexp["ignoreCase"] as Boolean
        val multiline = regexp["multiline"] as Boolean

        val flags = (if (ignoreCase) Pattern.CASE_INSENSITIVE else 0) or if (multiline) Pattern.MULTILINE else 0
        val pattern = Pattern.compile(regexp["source"] as String, flags)

        editChat(
            {
                val matcher = pattern.matcher(it.chatMessage.unformattedText)
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
    fun editChat(toReplace: String, vararg replacements: UMessage) {
        editChat(
            {
                removeFormatting(it.chatMessage.unformattedText) == toReplace
            },
            *replacements
        )
    }

    /**
     * Edits an already sent chat message by the [UMessage]
     *
     * @param toReplace the message to be replaced
     * @param replacements the new message(s) to be put in place of the old one
     */
    @JvmStatic
    fun editChat(toReplace: UMessage, vararg replacements: UMessage) {
        editChat(
            {
                toReplace.chatMessage.formattedText == it.chatMessage.formattedText.substring(4)
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
    fun editChat(chatLineId: Int, vararg replacements: UMessage) {
        editChat(
            { message ->
                message.chatLineId == chatLineId
            },
            *replacements
        )
    }

    private fun editChat(toReplace: (UMessage) -> Boolean, vararg replacements: UMessage) {
        @Suppress("CAST_NEVER_SUCCEEDS")
        val chatGui = Client.getChatGUI()!! as ChatGuiMixin
        val drawnChatLines = chatGui.drawnChatLines
        val chatLines = chatGui.chatLines

        editChatLineList(chatLines, toReplace, *replacements)
        editChatLineList(drawnChatLines, toReplace, *replacements)
    }

    // TODO(VERIFY)
    private fun <T> editChatLineList(
        iterator: ChatLineListIterator<T>,
        toReplace: (UMessage) -> Boolean,
        vararg replacements: UMessage
    ) {
        while (iterator.hasNext()) {
            val chatLine = iterator.next()

            val result = toReplace(
                UMessage(chatLine.component).setChatLineId(chatLine.id)
            )

            if (!result) {
                continue
            }

            iterator.remove()

            replacements.map {
                val lineId = if (it.chatLineId == -1) 0 else it.chatLineId

                ChatLine(lineId, chatLine.addedTime, it.chatMessage)
            }.forEach(iterator::add)
        }
    }

    /**
     * Deletes an already sent chat message matching [regexp].
     *
     * @param regexp the regex object to match to the message
     */
    @JvmStatic
    fun deleteChat(regexp: NativeRegExp) {
        val global = regexp["global"] as Boolean
        val ignoreCase = regexp["ignoreCase"] as Boolean
        val multiline = regexp["multiline"] as Boolean

        val flags = (if (ignoreCase) Pattern.CASE_INSENSITIVE else 0) or if (multiline) Pattern.MULTILINE else 0
        val pattern = Pattern.compile(regexp["source"] as String, flags)

        deleteChat {
            val matcher = pattern.matcher(it.chatMessage.unformattedText)
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
            removeFormatting(it.chatMessage.unformattedText) == toDelete
        }
    }

    /**
     * Deletes an already sent chat message by the [UMessage]
     *
     * @param toDelete the message to be deleted
     */
    @JvmStatic
    fun deleteChat(toDelete: UMessage) {
        deleteChat {
            toDelete.chatMessage.formattedText == it.chatMessage.formattedText.substring(4)
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
            it.chatLineId == chatLineId
        }
    }

    private fun deleteChat(toDelete: (UMessage) -> Boolean) {
        @Suppress("CAST_NEVER_SUCCEEDS")
        val chatGui = Client.getChatGUI()!! as ChatGuiMixin
        val drawnChatLines = chatGui.drawnChatLines
        val chatLines = chatGui.chatLines

        deleteChatLineList(chatLines, toDelete)
        deleteChatLineList(drawnChatLines, toDelete)
    }

    // TODO(VERIFY)
    private fun <T> deleteChatLineList(
        iterator: ChatLineListIterator<T>,
        toDelete: (UMessage) -> Boolean,
    ) {
        while (iterator.hasNext()) {
            val chatLine = iterator.next()

            if (toDelete(UMessage(chatLine.component).setChatLineId(chatLine.id)))
                iterator.remove()
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
        //#if MC<=11202
        val sentMessages = Client.getChatGUI()!!.sentMessages
        //#else
        //$$ val sentMessages = Client.getChatGUI()!!.recentChat
        //#endif

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

    data class ChatLine(val id: Int, val addedTime: Int, val component: UTextComponent)

    interface ChatLineMapper<MCType> {
        fun toCT(chatLine: MCType): ChatLine

        fun toMC(chatLine: ChatLine): MCType
    }

    abstract class BaseChatLineListIterator<T>(
        underlyingList: MutableList<T>
    ) : MutableListIterator<ChatLine>, ChatLineMapper<T> {
        private val listIterator = underlyingList.listIterator()

        override fun hasNext() = listIterator.hasNext()

        override fun hasPrevious() = listIterator.hasPrevious()

        override fun next() = toCT(listIterator.next())

        override fun nextIndex() = listIterator.nextIndex()

        override fun previous() = toCT(listIterator.previous())

        override fun previousIndex() = listIterator.previousIndex()

        override fun add(element: ChatLine) {
            listIterator.add(toMC(element))
        }

        override fun remove() = listIterator.remove()

        override fun set(element: ChatLine) {
            listIterator.set(toMC(element))
        }
    }

    //#if MC<=11202
    class ChatLineListIterator<T>(underlyingList: MutableList<MCChatLine>) : BaseChatLineListIterator<MCChatLine>(underlyingList) {
        override fun toCT(chatLine: MCChatLine) = ChatLine(chatLine.chatLineID, chatLine.updatedCounter, UTextComponent(chatLine.chatComponent))

        override fun toMC(chatLine: ChatLine) = MCChatLine(chatLine.addedTime, chatLine.component.component, chatLine.id)
    }
    //#else
    //$$ class ChatLineListIterator<T>(
    //$$     underlyingList: MutableList<GuiMessage<T>>,
    //$$     private val isDrawnList: Boolean,
    //$$ ) : BaseChatLineListIterator<GuiMessage<T>>(underlyingList) {
    //$$     override fun toCT(chatLine: GuiMessage<T>): ChatLine {
    //$$         val component = if (isDrawnList) {
    //$$             val seq = chatLine.message as FormattedCharSequence
    //$$             val builder = TextBuilder(true)
    //$$             seq.accept(builder)
    //$$             UTextComponent(builder.getString())
    //$$         } else UTextComponent(chatLine.message as MCITextComponent)
    //$$
    //$$         return ChatLine(chatLine.id, chatLine.addedTime, component)
    //$$     }
    //$$
    //$$     override fun toMC(chatLine: ChatLine): GuiMessage<T> = GuiMessage(
    //$$         chatLine.addedTime,
    //$$         let {
    //$$             if (isDrawnList) {
    //$$                 chatLine.component.visualOrderText
    //$$             } else chatLine.component.component
    //$$         } as T,
    //$$         chatLine.id
    //$$     )
    //$$ }
    //$$
    // Taken from UTextComponent
    //$$ private class TextBuilder(private val isFormatted: Boolean) : FormattedCharSink {
    //$$     private val builder = StringBuilder()
    //$$     private var cachedStyle: Style? = null
    //$$
    //$$     override fun accept(index: Int, style: Style, codePoint: Int): Boolean  {
    //$$         if (isFormatted && style != cachedStyle) {
    //$$             cachedStyle = style
    //$$             builder.append(formatString(style))
    //$$         }
    //$$
    //$$         builder.append(codePoint.toChar())
    //$$         return true
    //$$     }
    //$$
    //$$     fun getString() = builder.toString()
    //$$
    //$$     private fun formatString(style: Style): String {
    //$$         val builder = StringBuilder("§r")
    //$$
    //$$         when {
    //$$             style.isBold -> builder.append("§l")
    //$$             style.isItalic -> builder.append("§o")
    //$$             style.isUnderlined -> builder.append("§n")
    //$$             style.isStrikethrough -> builder.append("§m")
    //$$             style.isObfuscated -> builder.append("§k")
    //$$         }
    //$$
    //$$         style.color?.let(colorToFormatChar::get)?.let {
    //$$             builder.append(it)
    //$$         }
    //$$         return builder.toString()
    //$$     }
    //$$
    //$$     companion object {
    //$$         private val colorToFormatChar = ChatFormatting.values().mapNotNull { format ->
    //$$             TextColor.fromLegacyFormat(format)?.let { it to format }
    //$$         }.toMap()
    //$$     }
    //$$ }
    //#endif
}
