package com.chattriggers.ctjs.minecraft.objects.message;

import com.chattriggers.ctjs.minecraft.libs.ChatLib;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import com.chattriggers.ctjs.minecraft.wrappers.Player;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

//#if MC<=10809
//$$ import net.minecraft.network.play.server.S02PacketChat;
//$$ import net.minecraft.util.ChatComponentText;
//$$ import net.minecraft.util.ChatStyle;
//$$ import net.minecraft.util.IChatComponent;
//#else
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
//#endif

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Used to create a Message object to be sent to the Client's chat.<br>
 * For use with {@link com.chattriggers.ctjs.minecraft.libs.ChatLib#chat(Message)} or {@link Message#chat()}
 */
@Accessors(chain = true)
public class Message {
    //#if MC<=10809
    //$$ private IChatComponent chatMessage;
    //#else
    private ITextComponent chatMessage;
    //#endif

    /**
     * -- GETTER --
     * Gets the message parts as a list.
     *
     * @return the message parts
     */
    @Getter
    private ArrayList<Object> messageParts;

    /**
     * -- GETTER --
     * Gets the chat line ID.
     *
     * @return the chat line ID
     *
     * -- SETTER --
     * Sets the chat line ID. Useful for deleting messages by ID.
     *
     * @param chatLineId the ID of the message
     * @return the Message for method chaining
     */
    @Getter
    @Setter
    private int chatLineId;

    /**
     * -- GETTER --
     * Gets if the message is recursive (can be overwritten).
     *
     * @return true if the message is recursive
     *
     * -- SETTER --
     * Sets if the message is recursive (can be overwritten).
     *
     * @param recursive if the message is recursive
     * @return the Message for method chaining
     */
    @Getter
    @Setter
    private boolean recursive;

    /**
     * -- GETTER --
     * Gets if the message is formatted (true by default).
     *
     * @return true if the message is formatted
     *
     * -- SETTER --
     * Sets if the message is formatted.
     *
     * @param formatted if the message is formatted
     * @return the Message for method chaining
     */
    @Getter
    @Setter
    private boolean formatted;

    /**
     * Creates a new Message object from a chat event.
     *
     * @param event the chat event
     */
    public Message(ClientChatReceivedEvent event) {
        //#if MC<=10809
        //$$ this(event.message);
        //#else
        this(event.getMessage());
        //#endif
    }

    /**
     * Creates a new Message object from an IChatComponent.
     * 
     * @param component the IChatComponent
     */
    //#if MC<=10809
    //$$ public Message(IChatComponent component) {
    //#else
    public Message(ITextComponent component) {
    //#endif
        this.chatLineId = -1;
        this.recursive = false;
        this.formatted = true;

        this.messageParts = new ArrayList<>();

        // TODO remove for next release
        if (component.getSiblings().isEmpty()) {
            this.messageParts.add(new TextComponent(component));
        } else {
            //#if MC<=10809
            //$$ for (IChatComponent sibling : component.getSiblings()) {
            //$$     this.messageParts.add(new TextComponent(sibling));
            //$$ }
            //#else
            for (ITextComponent sibling : component.getSiblings()) {
                this.messageParts.add(new TextComponent(sibling));
            }
            //#endif
        }

        /* TODO fix for next release
        if (component instanceof ChatComponentText) {
            ChatComponentText cct = (ChatComponentText) component;
            this.messageParts.add(cct.getChatComponentText_TextValue());
            for (IChatComponent sibling : cct.getSiblings()) {
                this.messageParts.add(new TextComponent(sibling));
            }
        } else {
            if (component.getSiblings().isEmpty()) {
                this.messageParts.add(new TextComponent(component));
            } else {
                for (IChatComponent sibling : component.getSiblings()) {
                    this.messageParts.add(new TextComponent(sibling));
                }
            }
        }
        */
    }

    /**
     * Creates a new Message object in parts of TextComponents or Strings.
     *
     * @param components the TextComponents or String
     */
    public Message(Object... components) {
        this.chatLineId = -1;
        this.recursive = false;
        this.formatted = true;

        this.messageParts = new ArrayList<>();
        this.messageParts.addAll(Arrays.asList(components));
    }

    /**
     * Sets the TextComponent or String in the Message at index.
     *
     * @param index    the index of the TextComponent or String to change
     * @param component the new TextComponent or String to replace with
     * @return the Message for method chaining
     */
    public Message setTextComponent(int index, Object component) {
        this.messageParts.set(index, component);

        return this;
    }

    /**
     * Adds a TextComponent or String to the end of the Message.
     * 
     * @param component the new TextComponent or String to add
     * @return the Message for method chaining
     */
    public Message addTextComponent(Object component) {
        this.messageParts.add(component);
        
        return this;
    }

    /**
     * Adds a TextComponent or String at index of the Message.
     *
     * @param index the index to insert the new TextComponent or String
     * @param component the new TextComponent or String to insert
     * @return the Message for method chaining
     */
    public Message addTextComponent(int index, Object component) {
        this.messageParts.add(index, component);
        
        return this;
    }

    /**
     * Gets an exact copy of the Message.
     *
     * @return the copy of the Message
     */
    public Message copy() {
        if (this.chatLineId != -1) {
            return new Message(this.chatLineId, this.messageParts);
        }

        return new Message(this.messageParts);
    }

    /**
     * Alias for {@link Message#clone()}
     *
     * @return the copy of the Message
     */
    public Message clone() {
        return copy();
    }

    /**
     * Outputs the Message into the client's chat.
     */
    public void chat() {
        parseMessages();

        if (!ChatLib.isPlayer("[CHAT]: " + this.chatMessage.getFormattedText())) return;

        if (this.chatLineId != -1) {
            Client.getChatGUI().printChatMessageWithOptionalDeletion(this.chatMessage, this.chatLineId);
            return;
        }

        //#if MC<=10809
        //$$ if (this.recursive) {
        //$$     Client.getConnection().handleChat(new S02PacketChat(this.chatMessage, (byte) 0));
        //$$ } else {
        //$$     Player.getPlayer().addChatMessage(this.chatMessage);
        //$$ }
        //#else
        if (this.recursive) {
            Client.getConnection().handleChat(new SPacketChat(this.chatMessage, ChatType.CHAT));
        } else {
            Player.getPlayer().sendMessage(this.chatMessage);
        }
        //#endif
    }

    /**
     * Outputs the Message into the client's action bar.
     */
    public void actionBar() {
        parseMessages();

        if (!ChatLib.isPlayer("[ACTION BAR]: " + this.chatMessage.getFormattedText())) return;

        //#if MC<=10809
        //$$ Client.getConnection().handleChat(new S02PacketChat(this.chatMessage, (byte) 2));
        //#else
        Client.getConnection().handleChat(new SPacketChat(this.chatMessage, ChatType.GAME_INFO));
        //#endif
    }

    /**
     * Edits this message (once it is already sent)
     *
     * @param replacements the new message(s) to be put in place of the old one
     */
    public void edit(Message... replacements) {
        ChatLib.editChat(this, replacements);
    }

    //#if MC<=10809
    //$$ public IChatComponent getChatMessage() {
    //#else
    public ITextComponent getChatMessage() {
    //#endif
        if (this.chatMessage == null)
            parseMessages();

        return this.chatMessage;
    }

    // helper method to parse chat component parts
    private void parseMessages() {
        //#if MC<=10809
        //$$ this.chatMessage = new ChatComponentText("");
        //#else
        this.chatMessage = new TextComponentString("");
        //#endif

        for (Object message : this.messageParts) {
            if (message instanceof String) {
                String toAdd = ((String) message);

                //#if MC<=10809
                //$$ ChatComponentText cct = new ChatComponentText(this.formatted ? ChatLib.addColor(toAdd) : toAdd);
                //$$ cct.setChatStyle(new ChatStyle().setParentStyle(null));
                //#else
                TextComponentString cct = new TextComponentString(this.formatted ? ChatLib.addColor(toAdd) : toAdd);
                cct.setStyle(new Style().setParentStyle(null));
                //#endif

                this.chatMessage.appendSibling(cct);
            } else if (message instanceof TextComponent) {
                this.chatMessage.appendSibling(((TextComponent) message).getChatComponentText());
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder messageString = new StringBuilder("Message{");

        if (this.chatLineId != -1) {
            messageString.append("chatLineId=").append(this.chatLineId).append(", ");
        }

        messageString.append("formatted=").append(this.formatted).append(", ");

        messageString.append("recursive=").append(this.formatted).append(", ");

        messageString.append("messageParts=[");
        Iterator<Object> parts = this.messageParts.iterator();
        while (parts.hasNext()) {
            Object part = parts.next();

            if (part instanceof String) {
                messageString.append("String=").append(part);
            } else if (part instanceof TextComponent) {
                messageString.append("TextComponent=").append(part);
            }


            if (parts.hasNext()) {
                messageString.append(", ");
            }
        }
        messageString.append("]");

        messageString.append("}");

        return messageString.toString();
    }
}
