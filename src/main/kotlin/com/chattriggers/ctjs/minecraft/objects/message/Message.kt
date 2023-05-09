package com.chattriggers.ctjs.minecraft.objects.message

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.inventory.Item
import com.chattriggers.ctjs.utils.kotlin.MCBaseTextComponent
import com.chattriggers.ctjs.utils.kotlin.MCChatPacket
import com.chattriggers.ctjs.utils.kotlin.MCITextComponent
import net.minecraft.util.ChatComponentTranslation
import net.minecraftforge.client.event.ClientChatReceivedEvent

//#if MC>=11202
//$$ import net.minecraft.util.text.ChatType
//#endif

class Message {
    private lateinit var chatMessage: MCITextComponent

    private val messageParts = mutableListOf<TextComponent>()
    private var chatLineId = -1
    private var recursive = false
    private var formatted = true

    /**
     * Creates a new Message object from a chat event.
     * @param event the chat event
     */
    constructor(event: ClientChatReceivedEvent) : this(event.message)

    /**
     * Creates a new Message object from an IChatComponent.
     * @param component the IChatComponent
     */
    constructor(component: MCITextComponent) {
        if (component.siblings.isEmpty()) {
            if (component !is ChatComponentTranslation) {
                messageParts.add(TextComponent(component))
                return
            }

            component.forEach {
                if (it.siblings.isEmpty()) {
                    messageParts.add(TextComponent(it))
                }
            }
        } else {
            val formattedText = component.formattedText

            val firstComponent = MCBaseTextComponent(
                formattedText.substring(0, formattedText.indexOf(component.siblings[0].formattedText))
            ).apply { chatStyle = component.chatStyle }

            messageParts.add(TextComponent(firstComponent))
            messageParts.addAll(component.siblings.map(::TextComponent))
        }
    }

    /**
     * Creates a new Message object in parts of TextComponents or Strings.
     * @param messageParts the list of TextComponents or Strings
     */
    constructor(messageParts: ArrayList<Any>) {
        this.messageParts.addAll(messageParts.map {
            when (it) {
                is String -> TextComponent(it)
                is TextComponent -> it
                is Item -> it.getTextComponent()
                else -> return
            }
        })
    }

    /**
     * Creates a new Message object in parts of TextComponents or Strings.
     * @param components the TextComponents or Strings
     */
    constructor(vararg components: Any) : this(ArrayList(components.asList()))

    /**
     * @return the parsed message as an ITextComponent
     */
    fun getChatMessage(): MCITextComponent {
        parseMessage()
        return chatMessage
    }

    /**
     * @return the formatted text of the parsed message
     */
    fun getFormattedText(): String = getChatMessage().formattedText

    /**
     * @return the unformatted text of the parsed message
     */
    fun getUnformattedText(): String = getChatMessage().unformattedText

    /**
     * @return the message [TextComponent] parts as a list.
     */
    fun getMessageParts(): List<TextComponent> = messageParts

    /**
     * @return the chat line ID of the message
     */
    fun getChatLineId(): Int = chatLineId

    /**
     * Sets the chat line ID of the message. Useful for updating an already sent chat message.
     */
    fun setChatLineId(id: Int) = apply { chatLineId = id }

    /**
     * @return true if the message can trip other triggers.
     */
    fun isRecursive(): Boolean = recursive

    /**
     * Sets whether the message can trip other triggers.
     * @param recursive true if message can trip other triggers.
     */
    fun setRecursive(recursive: Boolean) = apply { this.recursive = recursive }

    /**
     * @return true if the message is formatted
     */
    fun isFormatted(): Boolean = formatted

    /**
     * Sets if the message is to be formatted
     * @param formatted true if formatted
     */
    fun setFormatted(formatted: Boolean) = apply { this.formatted = formatted }

    /**
     * Sets the TextComponent or String in the Message at index.
     * @param index the index of the TextComponent or String to change
     * @param component the new TextComponent or String to replace with
     * @return the Message for method chaining
     */
    fun setTextComponent(index: Int, component: Any) = apply {
        when (component) {
            is String -> messageParts[index] = TextComponent(component)
            is TextComponent -> messageParts[index] = component
        }
    }

    /**
     * Adds a TextComponent or String to the end of the Message.
     * @param component the new TextComponent or String to add
     * @return the Message for method chaining
     */
    fun addTextComponent(component: Any) = apply {
        when (component) {
            is String -> messageParts.add(TextComponent(component))
            is TextComponent -> messageParts.add(component)
        }
    }

    /**
     * Adds a TextComponent or String at index of the Message.
     * @param index the index to insert the new TextComponent or String
     * @param component the new TextComponent or String to insert
     * @return the Message for method chaining
     */
    fun addTextComponent(index: Int, component: Any) = apply {
        when (component) {
            is String -> messageParts.add(index, TextComponent(component))
            is TextComponent -> messageParts.add(index, component)
        }
    }

    fun clone(): Message = copy()

    fun copy(): Message {
        val copy = Message(messageParts)
            .setChatLineId(chatLineId)
        copy.recursive = recursive
        copy.formatted = formatted
        return copy
    }

    /**
     * Edits this message (once it is already sent)
     * @param replacements the new message(s) to be put in place of the old one
     */
    fun edit(vararg replacements: Message) {
        ChatLib.editChat(this, *replacements)
    }

    /**
     * Outputs the Message into the client's chat.
     */
    fun chat() {
        parseMessage()
        if (!ChatLib.isPlayer("[CHAT]: " + chatMessage.formattedText)) return

        if (chatLineId != -1) {
            Client.getChatGUI()?.printChatMessageWithOptionalDeletion(chatMessage, chatLineId)
            return
        }

        //#if MC<=10809
        if (recursive) {
            Client.scheduleTask {
                Client.getConnection()?.handleChat(MCChatPacket(chatMessage, 0))
            }
        } else {
            Player.getPlayer()?.addChatMessage(chatMessage)
        }
        //#else
        //$$ if (recursive) {
        //$$    Client.getConnection()?.handleChat(ChatPacket(chatMessage, ChatType.CHAT))
        //$$ } else {
        //$$    Player.getPlayer()?.sendMessage(chatMessage)
        //$$ }
        //#endif
    }

    /**
     * Outputs the Message into the client's action bar.
     */
    fun actionBar() {
        parseMessage()
        if (!ChatLib.isPlayer("[ACTION BAR]: " + chatMessage.formattedText)) return

        Client.scheduleTask {
            Client.getConnection()?.handleChat(
                MCChatPacket(
                    chatMessage,
                    //#if MC<=10809
                    2
                    //#else
                    //$$ ChatType.GAME_INFO
                    //#endif
                )
            )
        }
    }

    override fun toString() =
        "Message{" +
                "formatted=$formatted, " +
                "recursive=$recursive, " +
                "chatLineId=$chatLineId, " +
                "messageParts=$messageParts" +
                "}"

    private fun parseMessage() {
        chatMessage = MCBaseTextComponent("")

        messageParts.forEach {
            chatMessage.appendSibling(it.chatComponentText)
        }
    }
}
