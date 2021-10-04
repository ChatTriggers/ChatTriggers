package com.chattriggers.ctjs.minecraft.objects.message

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.utils.kotlin.*
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.IChatComponent

//#if MC==11602
//$$ import net.minecraft.util.text.Style
//$$ import com.chattriggers.ctjs.CTJS
//$$ import net.minecraft.util.text.ITextProperties
//$$ import net.minecraft.util.text.StringTextComponent
//$$ import net.minecraft.util.text.IFormattableTextComponent
//$$ import com.google.gson.JsonElement
//$$ import java.util.Optional
//#else
import net.minecraft.util.ChatComponentText
//#endif

@External
class TextComponent {
    lateinit var component: IChatComponent
        private set

    private var text: String
    private var formatted = true

    private var clickAction: ClickEvent.Action? = null
    private var clickValue: String? = null
    //#if MC==11602
    //$$ private var hoverAction: HoverEvent.Action<*>? = HoverEvent.Action.SHOW_TEXT
    //#else
    private var hoverAction: HoverEvent.Action? = HoverEvent.Action.SHOW_TEXT
    //#endif
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
     * @param textComponent the ITextComponent to convert
     */
    constructor(textComponent: IChatComponent) {
        component = textComponent
        text = getFormattedText()

        //#if MC==11602
        //$$ val clickEvent = textComponent.style.clickEvent
        //#else
        val clickEvent = textComponent.chatStyle.chatClickEvent
        //#endif

        if (clickEvent != null) {
            clickAction = clickEvent.action
            clickValue = clickEvent.value
        }

        //#if MC==11602
        //$$ val hoverEvent = textComponent.style.hoverEvent
        //#else
        val hoverEvent = textComponent.chatStyle.chatHoverEvent
        //#endif

        if (hoverEvent != null) {
            hoverAction = hoverEvent.action

            //#if MC==11602
            //$$ hoverValue = hoverEvent.action.serialize(hoverEvent.action).toString()
            //#else
            hoverValue = hoverEvent.value.formattedText
            //#endif
        }
    }

    /**
     * @return the text in the component
     */
    fun getText(): String = text

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
    fun isFormatted(): Boolean = formatted

    /**
     * Sets if the component is to be formatted
     * @param formatted true if formatted
     */
    fun setFormatted(formatted: Boolean) = apply {
        this.formatted = formatted
        reInstance()
    }

    /**
     * @return the formatted text of the parsed message
     */
    fun getFormattedText(): String {
        //#if MC==11602
        //$$ val builder = StyledStringAcceptor()
        //$$ component.getComponentWithStyle(builder, Style.EMPTY)
        //$$ return builder.toString()
        //#else
        return component.formattedText
        //#endif
    }

    /**
     * @return the unformatted text of the parsed message
     */
    fun getUnformattedText(): String {
        //#if MC==11602
        //$$ return component.unformattedComponentText
        //#else
        return component.unformattedText
        //#endif
    }

    /**
     * Sets the click action and value of the component.
     * See [TextComponent.setClickAction] for possible click actions.
     * @param action the click action
     * @param value the click value
     */
    fun setClick(action: String, value: String) = apply {
        clickAction = ClickEvent.Action.getValueByCanonicalName(action)
        clickValue = value
        reInstanceClick()
    }

    /**
     * @return the current click action
     */
    fun getClickAction(): String? = clickAction?.canonicalName

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
        clickAction = ClickEvent.Action.getValueByCanonicalName(action)
        reInstanceClick()
    }

    /**
     * @return the current click value
     */
    fun getClickValue(): String? = clickValue

    /**
     * Sets the value to be used by the click action.
     * See [TextComponent.setClickAction] for possible click actions.
     * @param value the click value
     */
    fun setClickValue(value: String) = apply {
        clickValue = value
        reInstanceClick()
    }

    /**
     * Sets the hover action and value of the component.
     * See [TextComponent.setHoverValue] for possible hover actions.
     * @param action the hover action
     * @param value the hover value
     */
    fun setHover(action: String, value: String) = apply {
        hoverAction = HoverEvent.Action.getValueByCanonicalName(action)
        hoverValue = value
        reInstanceHover()
    }

    /**
     * @return the current hover action
     */
    fun getHoverAction(): String? = hoverAction?.canonicalName

    /**
     * Sets the action to be performed when the component is hovered over.
     * Hover action is set to 'show_text' by default.
     * Possible actions include:
     * - show_text
     * - show_item
     * - show_entity
     *
     * Note: show_achievement is not recommended as it is only supported on 1.8.9
     * @param action the hover action
     */
    fun setHoverAction(action: String) = apply {
        hoverAction = HoverEvent.Action.getValueByCanonicalName(action)
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
        hoverValue = value
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
        component = ChatComponentText(text.formatIf(formatted))

        reInstanceClick()
        reInstanceHover()
    }

    private fun reInstanceClick() {
        if (clickAction == null || clickValue == null)
            return

        val event = ClickEvent(
            clickAction,
            if (formatted) ChatLib.addColor(clickValue) else clickValue
        )

        //#if MC==11602
        //$$ // TODO: Is this safe?
        //$$ (component as IFormattableTextComponent).style = component.style.setClickEvent(event)
        //#else
        component.chatStyle.chatClickEvent = event
        //#endif
    }

    private fun reInstanceHover() {
        if (hoverAction == null || hoverValue == null)
            return

        //#if MC==11602
        //$$ val event = hoverAction!!.deserialize(CTJS.gson.fromJson(hoverValue!!, JsonElement::class.java))
        //$$ setHoverEventHelper(event!!)
        //#else
        setHoverEventHelper(HoverEvent(
            hoverAction,
            TextComponent(hoverValue!!).component
        ))
        //#endif
    }

    private fun setHoverEventHelper(event: HoverEvent) {
        //#if MC==11602
        //$$ // TODO: Is this safe?
        //$$ (component as IFormattableTextComponent).style = component.style.setHoverEvent(event)
        //#else
        component.chatStyle.chatHoverEvent = event
        //#endif
    }

    private fun String.formatIf(predicate: Boolean) = if (predicate) ChatLib.addColor(this) else this

    //#if MC==11602
    //$$ class StyledStringAcceptor : ITextProperties.IStyledTextAcceptor<Any> {
    //$$     private val builder = FormattedTextBuilder()
    //$$
    //$$     override fun accept(style: Style, string: String): Optional<Any> {
    //$$         builder.accept(style, string)
    //$$         return Optional.empty()
    //$$     }
    //$$
    //$$     override fun toString() = builder.toString()
    //$$ }
    //$$
    //$$ // Common base class for shared functionality between forge and fabric
    //$$ class FormattedTextBuilder {
    //$$     private val builder = StringBuilder()
    //$$     private var cachedStyle: Style? = null
    //$$
    //$$     fun accept(style: Style, string: String) {
    //$$         if (style != cachedStyle) {
    //$$             cachedStyle = style
    //$$             builder.append(formatString(style))
    //$$         }
    //$$
    //$$         builder.append(string)
    //$$     }
    //$$
    //$$     override fun toString() = builder.toString()
    //$$
    //$$     private fun formatString(style: Style): String {
    //$$         val builder = StringBuilder("§r")
    //$$
    //$$         when {
    //$$             style.bold -> builder.append("§l")
    //$$             style.italic -> builder.append("§o")
    //$$             style.underlined -> builder.append("§n")
    //$$             style.strikethrough -> builder.append("§m")
    //$$             style.obfuscated -> builder.append("§k")
    //$$         }
    //$$
    //$$         if (style.color != null)
    //$$             builder.append(style.color.toString())
    //$$         return builder.toString()
    //$$     }
    //$$ }
    //#endif
}
