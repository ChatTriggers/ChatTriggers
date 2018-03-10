package com.chattriggers.ctjs.minecraft.objects.message;

import com.chattriggers.ctjs.minecraft.libs.ChatLib;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import com.chattriggers.ctjs.minecraft.wrappers.Player;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Used to create a Message object to be sent to the Client's chat.<br>
 * For use with {@link com.chattriggers.ctjs.minecraft.libs.ChatLib#chat(Message)} or {@link Message#chat()}
 */
@Accessors(chain = true)
public class Message {
    @Getter
    private IChatComponent chatMessage;
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
     * @return the message for method chaining
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
     * @return the message for method chaining
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
     * @return the message for method chaining
     */
    @Getter
    @Setter
    private boolean formatted;

    public Message(Object... messages) {
        this.chatLineId = -1;
        this.recursive = false;
        this.formatted = true;

        this.messageParts = new ArrayList<>();
        this.messageParts.addAll(Arrays.asList(messages));
        System.out.println(this.messageParts);
    }

    /**
     * Sets a part of the message (defined by the splits made in the constructor)
     *
     * @param part    the index of the part to change
     * @param message the new message to replace with
     * @return the message for method chaining
     */
    public Message setMessagePart(int part, Object message) {
        this.messageParts.set(part, message);

        return this;
    }

    /**
     * Alias for {@link Message#clone()}
     *
     * @return the copy of the message
     */
    public Message copy() {
        if (this.chatLineId != -1) {
            return new Message(this.chatLineId, this.messageParts);
        }

        return new Message(this.messageParts);
    }

    /**
     * Gets an exact copy of the message.
     *
     * @return the copy of the message
     */
    public Message clone() {
        return copy();
    }

    /**
     * Outputs the message into the client's chat.
     */
    public void chat() {
        parseMessages();

        if (this.chatLineId != -1) {
            Client.getChatGUI().printChatMessageWithOptionalDeletion(this.chatMessage, this.chatLineId);
            return;
        }

        if (this.recursive) {
            Client.getConnection().handleChat(new S02PacketChat(this.chatMessage, (byte) 0));
        } else {
            Player.getPlayer().addChatMessage(this.chatMessage);
        }
    }

    // helper method to parse chat component parts
    private void parseMessages() {
        this.chatMessage = new ChatComponentText("");

        for (Object message : this.messageParts) {
            if (message instanceof String) {
                String toAdd = ((String) message);

                ChatComponentText cct = new ChatComponentText(this.formatted ? ChatLib.addColor(toAdd) : toAdd);
                cct.setChatStyle(new ChatStyle().setParentStyle(null));

                this.chatMessage.appendSibling(cct);
            } else if (message instanceof IChatComponent) {
                this.chatMessage.appendSibling((IChatComponent) message);
            } else if (message instanceof ChatComponent) {
                this.chatMessage.appendSibling(((ChatComponent) message).getChatComponentText());
            }
        }
    }
}
