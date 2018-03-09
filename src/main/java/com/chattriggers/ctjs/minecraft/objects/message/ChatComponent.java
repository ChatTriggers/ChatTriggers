package com.chattriggers.ctjs.minecraft.objects.message;

import com.chattriggers.ctjs.minecraft.libs.ChatLib;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

@Accessors(chain = true)
public class ChatComponent {
    @Getter
    private IChatComponent chatComponentText;

    /**
     * -- GETTER --
     * Gets the String text of the ChatComponent.
     *
     * @return the text
     */
    @Getter
    private String text;

    /**
     * -- GETTER --
     * Gets if the component is formatted (true by default).
     *
     * @return if the component is formatted
     */
    @Getter
    private boolean formatted;

    /**
     * -- GETTER --
     * Gets the String clickAction of the ChatComponent.
     *
     * @return the clickAction
     */
    @Getter
    private String clickAction = null;
    /**
     * -- GETTER --
     * Gets the String clickValue of the ChatComponent.
     *
     * @return the clickValue
     */
    @Getter
    private String clickValue = null;

    /**
     * -- GETTER --
     * Gets the String hoverAction of the ChatComponent. show_text by default.
     *
     * @return the hoverAction
     */
    @Getter
    private String hoverAction = "show_text";
    /**
     * -- GETTER --
     * Gets the String hoverValue of the ChatComponent.
     *
     * @return the hoverValue
     */
    @Getter
    private String hoverValue = null;

    /**
     * Creates a new ChatComponent to be used in {@link Message}
     * @param text the component text
     */
    public ChatComponent(String text) {
        this.text = text;
        this.formatted = true;

        reInstance();
    }

    public ChatComponent(IChatComponent chatComponent) {
        this.chatComponentText = chatComponent;
    }

    /**
     * Sets the component's text.
     *
     * @param text the component text
     * @return the component for method chaining
     */
    public ChatComponent setText(String text) {
        this.text = text;
        reInstance();

        return this;
    }

    /**
     * Sets if the component is formatted (true by default).
     *
     * @param formatted if the component is formatted
     * @return the component for method chaining
     */
    public ChatComponent setFormatted(boolean formatted) {
        this.formatted = formatted;
        reInstance();

        return this;
    }

    /**
     * Sets the component's click action.
     *
     * @param action the component click action
     * @return the component for method chaining
     */
    public ChatComponent setClickAction(String action) {
        this.clickAction = action;
        reInstanceClick();

        return this;
    }

    /**
     * Sets the component's click value.
     *
     * @param value the component click value
     * @return the component for method chaining
     */
    public ChatComponent setClickValue(String value) {
        this.clickValue = value;
        reInstanceClick();

        return this;
    }

    /**
     * Sets the component's hover action (show_text by default).
     *
     * @param action the component hover action
     * @return the component for method chaining
     */
    public ChatComponent setHoverAction(String action) {
        this.hoverAction = action;
        reInstanceHover();

        return this;
    }

    /**
     * Sets the component's hover value.
     *
     * @param value the component hover value
     * @return the component for method chaining
     */
    public ChatComponent setHoverValue(String value) {
        this.hoverValue = value;
        reInstanceHover();

        return this;
    }

    /**
     * Outputs the component into the client's chat.
     */
    public void chat() {
        new Message(this).chat();
    }

    // helper method to re-instance the component
    private void reInstance() {
        String text = this.text;
        if (this.formatted) text = ChatLib.addColor(text);
        this.chatComponentText = new ChatComponentText(text);

        reInstanceClick();
        reInstanceHover();
    }

    // helper method to re-instance the clickable part of the component
    private void reInstanceClick() {
        if (this.clickAction == null || this.clickValue == null) return;

        String clickValue = this.clickValue;
        if (this.formatted) clickValue = ChatLib.addColor(clickValue);

        chatComponentText.getChatStyle().setChatClickEvent(new ClickEvent(
                ClickEvent.Action.getValueByCanonicalName(this.clickAction), clickValue
        ));
    }

    // helper method to re-instance the hover-able part of the component
    private void reInstanceHover() {
        if (this.hoverValue == null) return;

        String hoverValue = this.hoverValue;
        if (this.formatted) hoverValue = ChatLib.addColor(hoverValue);

        chatComponentText.getChatStyle().setChatHoverEvent(new HoverEvent(
                HoverEvent.Action.getValueByCanonicalName(this.hoverAction), new ChatComponentText(hoverValue)
        ));
    }
}
