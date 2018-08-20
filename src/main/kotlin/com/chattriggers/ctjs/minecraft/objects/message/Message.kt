package com.chattriggers.ctjs.minecraft.objects.message

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import net.minecraftforge.client.event.ClientChatReceivedEvent

//#if MC<=10809
import net.minecraft.network.play.server.S02PacketChat
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent
//#else
//$$ import net.minecraft.network.play.server.SPacketChat
//$$ import net.minecraft.util.text.ITextComponent
//$$ import net.minecraft.util.text.TextComponentString
//#endif

class Message {
    //#if MC<=10809
    lateinit var _chatMessage: IChatComponent
    //#else
    //$$ lateinit var chatMessage: ITextComponent
    //#endif

    private var _messageParts = mutableListOf<TextComponent>()
    private var _chatLineId = -1
    private var _recursive = false
    private var _formatted = true

    /**
     * Creates a new Message object from a chat event.
     *
     * @param event the chat event
     */
    constructor(event: ClientChatReceivedEvent):
            //#if MC<=10809
            this(event.message)
            //#else
            //$$ this(event.getMessage())
            //#endif

    /**
     * Creates a new Message object from an IChatComponent.
     *
     * @param component the IChatComponent
     */
    //#if MC<=10809
    constructor(component: IChatComponent) {
    //#else
    //$$ constructor(component: ITextComponent) {
    //#endif
        if (component.siblings.isEmpty()) {
            this._messageParts.add(TextComponent(component))
        } else {
            this._messageParts.addAll(component.siblings.map { TextComponent(it) })
        }
    }

    /**
     * Creates a new Message object in parts of TextComponents or Strings.
     *
     * @param messageParts the list of TextComponents or Strings
     */
    constructor(messageParts: ArrayList<Any>) {
        this._messageParts.addAll(messageParts.map{
            when (it) {
                is String -> TextComponent(it)
                is TextComponent -> it
                else -> return
            }
        })
    }

    /**
     * Creates a new Message object in parts of TextComponents or Strings.
     *
     * @param components the TextComponents or Strings
     */
    constructor(vararg components: Any): this(ArrayList(components.asList()))



    fun getChatMessage():
            //#if MC<=10809
            IChatComponent {
            //#else
            //$$ ITextComponent {
            //#endif
        parseMessage()
        return this._chatMessage
    }

    /**
     * Gets the message TextComponent parts as a list.
     *
     * @return the message parts
     */
    fun getMessageParts() = this._messageParts

    fun getChatLineId() = this._chatLineId
    fun setChatLineId(id: Int): Message {
        this._chatLineId = id
        return this
    }

    fun isRecursive() = this._recursive
    fun setRecursive(recursive: Boolean): Message {
        this._recursive = recursive
        return this
    }

    fun isFormatted() = this._formatted
    fun setFormatted(formatted: Boolean): Message {
        this._formatted = formatted
        return this
    }

    /**
     * Sets the TextComponent or String in the Message at index.
     *
     * @param index    the index of the TextComponent or String to change
     * @param component the new TextComponent or String to replace with
     * @return the Message for method chaining
     */
    fun setTextComponent(index: Int, component: Any): Message {
        when (component) {
            is String -> this._messageParts[index] = TextComponent(component)
            is TextComponent -> this._messageParts[index] = component
        }

        return this
    }

    /**
     * Adds a TextComponent or String to the end of the Message.
     *
     * @param component the new TextComponent or String to add
     * @return the Message for method chaining
     */
    fun addTextComponent(component: Any): Message {
        when (component) {
            is String -> this._messageParts.add(TextComponent(component))
            is TextComponent -> this._messageParts.add(component)
        }

        return this
    }

    /**
     * Adds a TextComponent or String at index of the Message.
     *
     * @param index the index to insert the new TextComponent or String
     * @param component the new TextComponent or String to insert
     * @return the Message for method chaining
     */
    fun addTextComponent(index: Int, component: Any): Message {
        when (component) {
            is String -> this._messageParts.add(index, TextComponent(component))
            is TextComponent -> this._messageParts.add(index, component)
        }

        return this
    }

    fun clone(): Message = copy()
    fun copy(): Message {
        val copy = Message(this._messageParts)
                .setChatLineId(this._chatLineId)
        copy._recursive = this._recursive
        copy._formatted = this._formatted
        return copy
    }

    /**
     * Edits this message (once it is already sent)
     *
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
        if (!ChatLib.isPlayer("[CHAT]: " + this._chatMessage.formattedText)) return

        if (this._chatLineId != 1) {
            Client.getChatGUI().printChatMessageWithOptionalDeletion(this._chatMessage, this._chatLineId)
            return
        }

        if (this._recursive) {
            Client.getConnection().handleChat(S02PacketChat(this._chatMessage, 0))
        } else {
            Player.getPlayer()?.addChatMessage(this._chatMessage)
        }
    }

    /**
     * Outputs the Message into the client's action bar.
     */
    fun actionBar() {
        parseMessage()
        if (!ChatLib.isPlayer("[ACTION BAR]: " + this._chatMessage.formattedText)) return

        Client.getConnection().handleChat(S02PacketChat(this._chatMessage, 2))
    }

    private fun parseMessage() {
        //#if MC<=10809
        this._chatMessage = ChatComponentText("")
        //#else
        //$$ this._chatMessage = TextComponentString("")
        //#endif

        this._messageParts.map {
            this._chatMessage.appendSibling(it.chatComponentText)
        }
    }
}