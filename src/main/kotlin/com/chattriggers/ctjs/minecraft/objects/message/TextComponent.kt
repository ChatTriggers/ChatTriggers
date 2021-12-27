package com.chattriggers.ctjs.minecraft.objects.message

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.utils.kotlin.*
import net.minecraft.text.*
import java.util.Optional

@External
class TextComponent {
    lateinit var component: BaseText
        private set

    private var text: String
    private var formatted = true

    private var clickAction: ClickEvent.Action? = null
    private var clickValue: String? = null
    private var hoverAction: HoverEvent.Action<*>? = HoverEvent.Action.SHOW_TEXT
    // TODO(BREAKING: String? -> Any?
    private var hoverValue: Any? = null

    /**
     * Creates a TextComponent from a string.
     * @param text the text string in the component.
     */
    constructor(text: String) {
        this.text = text
        reInstance()
    }

    /**
     * Creates a TextComponent from an OrderedText.
     *
     * @param textComponent the OrderedText to convert
     */
    constructor(textComponent: OrderedText) : this(runGlobal {
        val builder = StyledCharacterVisitor()
        textComponent.accept(builder)
        LiteralText(builder.toString())
    })

    /**
     * Creates a TextComponent from an existing ITextComponent.
     *
     * @param textComponent the ITextComponent to convert
     */
    constructor(textComponent: Text) {
        component = if (textComponent !is BaseText) {
            val builder = StyledStringVisitor()
            textComponent.visit(builder, Style.EMPTY)
            LiteralText(builder.toString())
        } else textComponent
        text = getFormattedText()

        val clickEvent = textComponent.style.clickEvent

        if (clickEvent != null) {
            clickAction = clickEvent.action
            clickValue = clickEvent.value
        }

        val hoverEvent = textComponent.style.hoverEvent
        if (hoverEvent != null) {
            hoverAction = hoverEvent.action
            hoverValue = hoverEvent.getValue(hoverEvent.action)
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
    fun getFormattedText() = text

    /**
     * @return the unformatted text of the parsed message
     */
    fun getUnformattedText() = component.asString()

    /**
     * Sets the click action and value of the component.
     * See [TextComponent.setClickAction] for possible click actions.
     * @param action the click action
     * @param value the click value
     */
    fun setClick(action: String, value: String) = apply {
        clickAction = ClickEvent.Action.byName(action)
        clickValue = value
        reInstanceClick()
    }

    /**
     * @return the current click action
     */
    fun getClickAction(): String? = clickAction?.getName()

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
        clickAction = ClickEvent.Action.byName(action)
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
    fun setHover(action: String, value: Any?) = apply {
        if (value !is Text && value !is HoverEvent.ItemStackContent && value !is HoverEvent.EntityContent && value !is String)
            throw IllegalArgumentException("TextComponent hover value must be one of: String, Text, ItemStackContext, or EntityContent")

        hoverAction = HoverEvent.Action.byName(action)
        hoverValue = if (value is String) {
            LiteralText(ChatLib.addColor(value))
        } else value
        reInstanceHover()
    }

    /**
     * @return the current hover action
     */
    fun getHoverAction(): String? = hoverAction?.name

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
        hoverAction = HoverEvent.Action.byName(action)
        reInstanceHover()
    }

    /**
     * @return the current hover value
     */
    fun getHoverValue(): Any? = this.hoverValue

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
        component = LiteralText(text.formatIf(formatted))

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

        // TODO: Is this safe?
        component.style = component.style.withClickEvent(event)
    }

    private fun reInstanceHover() {
        if (hoverAction == null || hoverValue == null)
            return

        component.style = component.style.withHoverEvent(makeHoverEvent(hoverAction!!, hoverValue))
    }

    // This needs to be a separate method because we can't get the T (it's always
    // CapturedType(*)) at the call site
    @Suppress("UNCHECKED_CAST")
    fun <T> makeHoverEvent(action: HoverEvent.Action<T>, value: Any?): HoverEvent {
        return HoverEvent(action, value as T)
    }

    private fun String.formatIf(predicate: Boolean) = if (predicate) ChatLib.addColor(this) else this

    private open class FormattedBuilder {
        protected val builder = StringBuilder()
        protected var cachedStyle: Style? = null

        override fun toString() = builder.toString()

        protected fun formatString(style: Style): String {
            val builder = StringBuilder("§r")

            if (style.isBold)
                builder.append("§l")
            if (style.isItalic)
                builder.append("§o")
            if (style.isUnderlined)
                builder.append("§n")
            if (style.isStrikethrough)
                builder.append("§m")
            if (style.isObfuscated)
                builder.append("§k")
            if (style.color != null)
                builder.append(style.color.toString())

            return builder.toString()
        }
    }

    private class FormattedStringBuilder : FormattedBuilder() {
        fun accept(style: Style, string: String) {
            if (style != cachedStyle) {
                cachedStyle = style
                builder.append(formatString(style))
            }

            builder.append(string)
        }
    }

    private class FormattedCharBuilder : FormattedBuilder() {
        fun accept(style: Style, codePoint: Int) {
            if (style != cachedStyle) {
                cachedStyle = style
                builder.append(formatString(style))
            }

            builder.appendCodePoint(codePoint)
        }
    }

    class StyledStringVisitor : StringVisitable.StyledVisitor<Any> {
        private val builder = FormattedStringBuilder()

        override fun accept(style: Style, string: String): Optional<Any> {
            builder.accept(style, string)
            return Optional.empty()
        }

        override fun toString() = builder.toString()
    }

    class StyledCharacterVisitor : CharacterVisitor {
        private val builder = FormattedCharBuilder()

        override fun accept(index: Int, style: Style, codePoint: Int): Boolean {
            builder.accept(style, codePoint)
            return true
        }

        override fun toString() = builder.toString()
    }
}
