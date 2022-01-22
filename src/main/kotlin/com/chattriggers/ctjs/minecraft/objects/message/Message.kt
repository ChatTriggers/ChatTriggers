package com.chattriggers.ctjs.minecraft.objects.message

import com.chattriggers.ctjs.launch.mixins.asMixin
import com.chattriggers.ctjs.launch.mixins.transformers.ChatHudAccessor
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText

@External
class Message {
    private lateinit var textComponent: TextComponent

    private var messageParts = mutableListOf<TextComponent>()
    private var chatLineId = -1
    private var recursive = false
    private var formatted = true

    // TODO("fabric")
    // /**
    //  * Creates a new Message object from a chat event.
    //  * @param event the chat event
    //  */
    // constructor(event: ClientChatReceivedEvent) : this(event.message)

    /**
     * Creates a new Message object from a Text object.
     * @param component the IChatComponent
     */
    constructor(component: Text) {
        messageParts.add(TextComponent(component))
    }

    /**
     * Creates a new Message object in parts of TextComponents or Strings.
     * @param messageParts the list of TextComponents or Strings
     */
    constructor(messageParts: Collection<Any>) {
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
    constructor(vararg components: Any) : this(components.toList())

    /**
     * @return the parsed message as a Text object
     */
    fun getChatMessage(): Text {
        parseMessage()
        return textComponent.component
    }

    /**
     * @return the parsed message as a TextComponent object
     */
    // TODO(FEATURE)
    fun getTextComponent(): TextComponent {
        parseMessage()
        return textComponent
    }

    /**
     * @return the formatted text of the parsed message
     */
    fun getFormattedText(): String {
        parseMessage()
        return textComponent.getFormattedText()
    }

    /**
     * @return the unformatted text of the parsed message
     */
    fun getUnformattedText(): String {
        parseMessage()
        return textComponent.getUnformattedText()
    }

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
     * @return true if the message can activate other triggers.
     */
    fun isRecursive() = recursive

    /**
     * Sets whether or not the message can trip other triggers.
     * @param recursive true if message can trip other triggers.
     */
    fun setRecursive(recursive: Boolean) = apply { this.recursive = recursive }

    /**
     * @return true if the message is formatted
     */
    fun isFormatted() = formatted

    /**
     * Sets if the message is to be formatted
     * @param formatted true if formatted
     */
    fun setFormatted(formatted: Boolean) = apply { this.formatted = formatted }

    /**
     * Sets the TextComponent or String in the Message at index.
     * @param index    the index of the TextComponent or String to change
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
        return Message(messageParts).setChatLineId(chatLineId).also {
            it.recursive = recursive
            it.formatted = formatted
        }
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
        if (!ChatLib.isPlayer("[CHAT]: " + getFormattedText())) return

        if (chatLineId != -1) {
            Client.getChatGUI()?.asMixin<ChatHudAccessor>()?.invokeAddMessage(textComponent.component, chatLineId)
            return
        }

        // TODO("fabric")
        // if (recursive) {
        //     Client.getConnection()?.sendPacket(ChatMessageC2SPacket(getFormattedText()))
        // } else {
        //     Player.getPlayer()?.sendMessage(text, false)
        // }

        Player.getPlayer()?.sendMessage(textComponent.component, false)
    }

    /**
     * Outputs the Message into the client's action bar.
     */
    fun actionBar() {
        parseMessage()
        if (ChatLib.isPlayer("[ACTION BAR]: " + getFormattedText()))
            Player.getPlayer()?.sendMessage(textComponent.component, true)
    }

    override fun toString() =
        "Message{" +
            "formatted=$formatted, " +
            "recursive=$recursive, " +
            "chatLineId=$chatLineId, " +
            "messageParts=$messageParts" +
            "}"

    private fun parseMessage() {
        val text = LiteralText("")

        messageParts.forEach {
            text.siblings.add(it.component)
        }

        textComponent = TextComponent(text)
    }
}
