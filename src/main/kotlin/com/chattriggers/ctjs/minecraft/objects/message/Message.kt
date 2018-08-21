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
    private lateinit var chatMessage: IChatComponent
    //#else
    //$$ lateinit var chatMessage: ITextComponent
    //#endif

    private var messageParts = mutableListOf<TextComponent>()
    private var chatLineId = -1
    private var recursive = false
    private var formatted = true

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
            this.messageParts.add(TextComponent(component))
        } else {
            this.messageParts.addAll(component.siblings.map { TextComponent(it) })
        }
    }

    /**
     * Creates a new Message object in parts of TextComponents or Strings.
     *
     * @param messageParts the list of TextComponents or Strings
     */
    constructor(messageParts: ArrayList<Any>) {
        this.messageParts.addAll(messageParts.map{
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
        return this.chatMessage
    }

    /**
     * Gets the message TextComponent parts as a list.
     *
     * @return the message parts
     */
    fun getMessageParts() = this.messageParts

    fun getChatLineId() = this.chatLineId
    fun setChatLineId(id: Int): Message {
        this.chatLineId = id
        return this
    }

    fun isRecursive() = this.recursive
    fun setRecursive(recursive: Boolean): Message {
        this.recursive = recursive
        return this
    }

    fun isFormatted() = this.formatted
    fun setFormatted(formatted: Boolean): Message {
        this.formatted = formatted
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
            is String -> this.messageParts[index] = TextComponent(component)
            is TextComponent -> this.messageParts[index] = component
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
            is String -> this.messageParts.add(TextComponent(component))
            is TextComponent -> this.messageParts.add(component)
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
            is String -> this.messageParts.add(index, TextComponent(component))
            is TextComponent -> this.messageParts.add(index, component)
        }

        return this
    }

    fun clone(): Message = copy()
    fun copy(): Message {
        val copy = Message(this.messageParts)
                .setChatLineId(this.chatLineId)
        copy.recursive = this.recursive
        copy.formatted = this.formatted
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
        if (!ChatLib.isPlayer("[CHAT]: " + this.chatMessage.formattedText)) return

        if (this.chatLineId != 1) {
            Client.getChatGUI().printChatMessageWithOptionalDeletion(this.chatMessage, this.chatLineId)
            return
        }

        if (this.recursive) {
            Client.getConnection().handleChat(S02PacketChat(this.chatMessage, 0))
        } else {
            Player.getPlayer()?.addChatMessage(this.chatMessage)
        }
    }

    /**
     * Outputs the Message into the client's action bar.
     */
    fun actionBar() {
        parseMessage()
        if (!ChatLib.isPlayer("[ACTION BAR]: " + this.chatMessage.formattedText)) return

        Client.getConnection().handleChat(S02PacketChat(this.chatMessage, 2))
    }

    private fun parseMessage() {
        //#if MC<=10809
        this.chatMessage = ChatComponentText("")
        //#else
        //$$ this.chatMessage = TextComponentString("")
        //#endif

        this.messageParts.map {
            this.chatMessage.appendSibling(it.chatComponentText)
        }
    }
}