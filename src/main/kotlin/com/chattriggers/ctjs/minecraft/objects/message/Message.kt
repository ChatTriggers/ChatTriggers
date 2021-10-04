package com.chattriggers.ctjs.minecraft.objects.message

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.network.play.server.S02PacketChat
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent
import net.minecraftforge.client.event.ClientChatReceivedEvent

//#if MC==11602
//$$ import net.minecraft.util.text.ChatType
//#endif

@External
class Message {
    private lateinit var chatMessage: IChatComponent

    private var messageParts = mutableListOf<TextComponent>()
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
    constructor(component: IChatComponent) {
        if (component.siblings.isEmpty()) {
            messageParts.add(TextComponent(component))
        } else {
            messageParts.addAll(component.siblings.map { TextComponent(it) })
        }
    }

    /**
     * Creates a new Message object in parts of TextComponents or Strings.
     * @param messageParts the list of TextComponents or Strings
     */
    constructor(messageParts: ArrayList<Any>) {
        messageParts.addAll(messageParts.map {
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
    fun getChatMessage(): IChatComponent {
        parseMessage()
        return chatMessage
    }

    /**
     * @return the formatted text of the parsed message
     */
    fun getFormattedText() = TextComponent(getChatMessage()).getFormattedText()

    /**
     * @return the unformatted text of the parsed message
     */
    fun getUnformattedText(): String = TextComponent(getChatMessage()).getUnformattedText()

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
     * Sets whether or not the message can trip other triggers.
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
    // TODO(1.16.2)
    //#if MC==10809
    fun edit(vararg replacements: Message) {
        ChatLib.editChat(this, *replacements)
    }
    //#endif

    /**
     * Outputs the Message into the client's chat.
     */
    fun chat() {
        parseMessage()
        if (!ChatLib.isPlayer("[CHAT]: " + getFormattedText())) return

        if (this.chatLineId != -1) {
            // TODO(1.16.2)
            //#if MC==10809
            Client.getChatGUI()?.printChatMessageWithOptionalDeletion(chatMessage, chatLineId)
            //#endif
            return
        }

        //#if MC<=10809
        if (this.recursive) {
            Client.getConnection()?.handleChat(S02PacketChat(chatMessage, 0))
        } else {
            Player.getPlayer()?.addChatMessage(chatMessage)
        }
        //#else
        //$$ if (this.recursive) {
        //$$    Client.getConnection()?.handleChat(SChatPacket(chatMessage, ChatType.CHAT, Client.getMinecraft().session.profile.id))
        //$$ } else {
        //$$    Player.getPlayer()?.sendMessage(chatMessage, Player.getUUIDObj())
        //$$ }
        //#endif
    }

    /**
     * Outputs the Message into the client's action bar.
     */
    fun actionBar() {
        parseMessage()
        if (!ChatLib.isPlayer("[ACTION BAR]: " + getFormattedText())) return

        Client.getConnection()?.handleChat(
            S02PacketChat(
                chatMessage,
                //#if MC<=10809
                2
                //#else
                //$$ ChatType.GAME_INFO,
                //$$ Client.getMinecraft().session.profile.id,
                //#endif
            )
        )
    }

    override fun toString() =
        "Message{" +
                "formatted=$formatted, " +
                "recursive=$recursive, " +
                "chatLineId=$chatLineId, " +
                "messageParts=$messageParts" +
                "}"

    private fun parseMessage() {
        chatMessage = ChatComponentText("")

        messageParts.map {
            //#if MC==11602
            //$$ // TODO: In 1.8.9 appendSibling does some styling stuff. Is
            //$$ // this equivalent?
            //$$ chatMessage.siblings.add(it.component)
            //#else
            chatMessage.appendSibling(it.component)
            //#endif
        }
    }
}
