package com.chattriggers.ctjs.minecraft.objects.message;

import com.chattriggers.ctjs.minecraft.libs.ChatLib;
import lombok.Getter;
import lombok.experimental.Accessors;

//#if MC<=10809
//$$ import net.minecraft.event.ClickEvent;
//$$ import net.minecraft.event.HoverEvent;
//$$ import net.minecraft.util.ChatComponentText;
//$$ import net.minecraft.util.ChatStyle;
//$$ import net.minecraft.util.IChatComponent;
//#else
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
//#endif

@Accessors(chain = true)
public class TextComponent {
    @Getter
    //#if MC<=10809
    //$$ private IChatComponent chatComponentText;
    //#else
    private ITextComponent chatComponentText;
    //#endif

    /**
     * -- GETTER --
     * Gets the String text of the TextComponent.
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
     * Gets the String clickAction of the TextComponent.
     *
     * @return the clickAction
     */
    @Getter
    private String clickAction = null;
    /**
     * -- GETTER --
     * Gets the String clickValue of the TextComponent.
     *
     * @return the clickValue
     */
    @Getter
    private String clickValue = null;

    /**
     * -- GETTER --
     * Gets the String hoverAction of the TextComponent. show_text by default.
     *
     * @return the hoverAction
     */
    @Getter
    private String hoverAction = "show_text";
    /**
     * -- GETTER --
     * Gets the String hoverValue of the TextComponent.
     *
     * @return the hoverValue
     */
    @Getter
    private String hoverValue = null;

    /**
     * Creates a new TextComponent to be used in {@link Message}
     * @param text the component text
     */
    public TextComponent(String text) {
        this.text = text;
        this.formatted = true;

        reInstance();
    }

    /**
     * Creates a new TextComponent from a IChatComponent.<br>
     * Used primarily in the {@link Message} constructor.
     *
     * @param chatComponent the IChatComponent
     */
    //#if MC<=10809
    //$$ public TextComponent(IChatComponent chatComponent) {
    //#else
    public TextComponent(ITextComponent chatComponent) {
    //#endif
        this.chatComponentText = chatComponent;
        this.text = chatComponentText.getFormattedText();
        this.formatted = true;

        //#if MC<=10809
        //$$ ChatStyle chatStyle = chatComponent.getChatStyle();
        //#else
        Style chatStyle = chatComponent.getStyle();
        //#endif

        //#if MC<=10809
        //$$ if (chatStyle.getChatClickEvent() != null) {
        //$$     ClickEvent clickEvent = chatStyle.getChatClickEvent();
        //#else
        if (chatStyle.getClickEvent() != null) {
            ClickEvent clickEvent = chatStyle.getClickEvent();
        //#endif

            this.clickAction = clickEvent.getAction().getCanonicalName();
            this.clickValue = clickEvent.getValue();
        }


        //#if MC<=10809
        //$$ if (chatStyle.getChatHoverEvent() != null) {
        //$$     HoverEvent hoverEvent = chatStyle.getChatHoverEvent();
            //#else
            if (chatStyle.getHoverEvent() != null) {
                HoverEvent hoverEvent = chatStyle.getHoverEvent();
            //#endif
            this.hoverAction = hoverEvent.getAction().getCanonicalName();
            this.hoverValue = hoverEvent.getValue().getFormattedText();
        }
    }

    /**
     * Sets the component's text.
     *
     * @param text the component text
     * @return the component for method chaining
     */
    public TextComponent setText(String text) {
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
    public TextComponent setFormatted(boolean formatted) {
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
    public TextComponent setClickAction(String action) {
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
    public TextComponent setClickValue(String value) {
        this.clickValue = value;
        reInstanceClick();

        return this;
    }

    /**
     * Sets the component's click action and value.
     *
     * @param action the component click action
     * @param value the component click value
     * @return the component for method chaining
     */
    public TextComponent setClick(String action, String value) {
        this.clickAction = action;
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
    public TextComponent setHoverAction(String action) {
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
    public TextComponent setHoverValue(String value) {
        this.hoverValue = value;
        reInstanceHover();

        return this;
    }

    /**
     * Sets the component's hover action and value.
     *
     * @param action the component hover action
     * @param value the component hover value
     * @return the component for method chaining
     */
    public TextComponent setHover(String action, String value) {
        this.hoverAction = action;
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

    /**
     * Outputs the component into the client's action bar.
     */
    public void actionBar() {
        new Message(this).actionBar();
    }

    // helper method to re-instance the component
    private void reInstance() {
        String text = this.text;
        if (this.formatted) text = ChatLib.addColor(text);
        //#if MC<=10809
        //$$ this.chatComponentText = new ChatComponentText(text);
        //#else
        this.chatComponentText = new TextComponentString(text);
        //#endif

        reInstanceClick();
        reInstanceHover();
    }

    // helper method to re-instance the clickable part of the component
    private void reInstanceClick() {
        if (this.clickAction == null || this.clickValue == null) return;

        String clickValue = this.clickValue;
        if (this.formatted) clickValue = ChatLib.addColor(clickValue);

        //#if MC<=10809
        //$$ chatComponentText.getChatStyle().setChatClickEvent(new ClickEvent(
        //#else
        chatComponentText.getStyle().setClickEvent(new ClickEvent(
        //#endif
                ClickEvent.Action.getValueByCanonicalName(this.clickAction), clickValue
        ));
    }

    // helper method to re-instance the hover-able part of the component
    private void reInstanceHover() {
        if (this.hoverValue == null) return;

        String hoverValue = this.hoverValue;
        if (this.formatted) hoverValue = ChatLib.addColor(hoverValue);

        //#if MC<=10809
        //$$ chatComponentText.getChatStyle().setChatHoverEvent(new HoverEvent(
        //$$        HoverEvent.Action.getValueByCanonicalName(this.hoverAction), new ChatComponentText(hoverValue)
        //#else
        chatComponentText.getStyle().setHoverEvent(new HoverEvent(
                HoverEvent.Action.getValueByCanonicalName(this.hoverAction), new TextComponentString(hoverValue)
        //#endif
        ));
    }

    @Override
    public String toString() {
        String textComponentString = "TextComponent{text=" + text
                + ", formatted=" + formatted;

        if (this.hoverAction != null && this.hoverValue != null) {
            textComponentString += ", hover{action=" + this.hoverAction + ", value=" + this.hoverValue + "}";
        }

        if (this.clickAction != null && this.clickValue != null) {
            textComponentString += ", click{action=" + this.clickAction + ", value=" + this.clickValue + "}";
        }

        textComponentString += "}";

        return textComponentString;
    }
}
