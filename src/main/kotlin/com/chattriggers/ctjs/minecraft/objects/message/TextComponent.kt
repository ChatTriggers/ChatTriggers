package com.chattriggers.ctjs.minecraft.objects.message

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.utils.kotlin.*

@External
class TextComponent {

    lateinit var chatComponentText: MCITextComponent

    private var text: String
    private var formatted = true

    private var clickAction: String? = null
    private var clickValue: String? = null
    private var hoverAction: String? = "show_text"
    private var hoverValue: String? = null

    /**
     * Creates a TextComponent from a string.
     * @param text the text string in the component.
     */
    constructor(text: String) {
        this.text = text
        reInstance()
    }

    /**
     * Creates a TextComponent from an existing ITextComponent.
     * @param chatComponent the ITextComponent to convert
     */
    constructor(chatComponent: MCITextComponent) {
        this.chatComponentText = chatComponent
        this.text = this.chatComponentText.formattedText

        val chatStyle = chatComponent.getStyling()

        val clickEvent = chatStyle.getClick()
        this.clickAction = clickEvent?.action?.canonicalName
        this.clickValue = clickEvent?.value

        val hoverEvent = chatStyle.getHover()
        this.hoverAction = hoverEvent?.action?.canonicalName
        this.hoverValue = hoverEvent?.value?.formattedText
    }

    /**
     * @return the text in the component
     */
    fun getText(): String = this.text

    /**
     * Sets the components text string.
     * @param text the text string in the component.
     */
    fun setText(text: String) = apply {
        this.text = text
        reInstance()
    }

    /**
     * @return true if the component is formatted
     */
    fun isFormatted(): Boolean = this.formatted

    /**
     * Sets if the component is to be formatted
     * @param formatted true if formatted
     */
    fun setFormatted(formatted: Boolean) = apply {
        this.formatted = formatted
        reInstance()
    }

    /**
     * Sets the click action and value of the component.
     * See [TextComponent.setClickAction] for possible click actions.
     * @param action the click action
     * @param value the click value
     */
    fun setClick(action: String, value: String) = apply {
        this.clickAction = action
        this.clickValue = value
        reInstanceClick()
    }

    /**
     * @return the current click action
     */
    fun getClickAction(): String? = this.clickAction

    /**
     * Sets the action to be performed when the component is clicked on.
     * Possible actions include:
     * - open_url
     * - open_file
     * - run_command
     * - suggest_command
     * - change_page
     * @param action the click action
     */
    fun setClickAction(action: String) = apply {
        this.clickAction = action
        reInstanceClick()
    }

    /**
     * @return the current click value
     */
    fun getClickValue(): String? = this.clickValue

    /**
     * Sets the value to be used by the click action.
     * See [TextComponent.setClickAction] for possible click actions.
     * @param value the click value
     */
    fun setClickValue(value: String) = apply {
        this.clickValue = value
        reInstanceClick()
    }

    /**
     * Sets the hover action and value of the component.
     * See [TextComponent.setHoverValue] for possible hover actions.
     * @param action the hover action
     * @param value the hover value
     */
    fun setHover(action: String, value: String) = apply {
        this.hoverAction = action
        this.hoverValue = value
        reInstanceHover()
    }

    /**
     * @return the current hover action
     */
    fun getHoverAction(): String? = this.hoverAction

    /**
     * Sets the action to be performed when the component is hovered over.
     * Hover action is set to 'show_text' by default.
     * Possible actions include:
     * - show_text
     * - show_achievement
     * - show_item
     * - show_entity
     * @param action the hover action
     */
    fun setHoverAction(action: String) = apply {
        this.hoverAction = action
        reInstanceHover()
    }

    /**
     * @return the current hover value
     */
    fun getHoverValue(): String? = this.hoverValue

    /**
     * Sets the value to be used by the hover action.
     * See [TextComponent.setHoverValue] for possible hover actions.
     * @param value the hover value
     */
    fun setHoverValue(value: String) = apply {
        this.hoverValue = value
        reInstanceHover()
    }

    /**
     * Shows the component in chat as a new [Message]
     */
    fun chat() = Message(this).chat()

    /**
     * Shows the component on the actionbar as a new [Message]
     */
    fun actionBar() = Message(this).actionBar()

    override fun toString() =
        "TextComponent{" +
                "text:$text, " +
                "formatted:$formatted, " +
                "hoverAction:$hoverAction, " +
                "hoverValue:$hoverValue, " +
                "clickAction:$clickAction, " +
                "clickValue:$clickValue, " +
                "}"

    private fun reInstance() {
        this.chatComponentText = MCBaseTextComponent(
            if (this.formatted) ChatLib.addColor(this.text)
            else this.text
        )

        reInstanceClick()
        reInstanceHover()
    }

    private fun reInstanceClick() {
        if (this.clickAction == null || this.clickValue == null) return

        this.chatComponentText.getStyling()
            //#if MC<=10809
            .chatClickEvent =
                //#else
                //$$ .clickEvent =
                //#endif
            MCTextClickEvent(
                MCClickEventAction.getValueByCanonicalName(this.clickAction),
                if (this.formatted) ChatLib.addColor(this.clickValue)
                else this.clickValue
            )
    }

    private fun reInstanceHover() {
        if (this.hoverAction == null || this.hoverValue == null) return

        this.chatComponentText.getStyling()
            //#if MC<=10809
            .chatHoverEvent =
                //#else
                //$$ .hoverEvent =
                //#endif
            MCTextHoverEvent(
                MCHoverEventAction.getValueByCanonicalName(this.hoverAction),
                MCBaseTextComponent(
                    if (this.formatted) ChatLib.addColor(this.hoverValue)
                    else this.hoverValue
                )
            )
    }
}
