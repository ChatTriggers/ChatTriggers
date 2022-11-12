package com.chattriggers.ctjs.minecraft.objects.message

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.utils.kotlin.*
import net.minecraft.event.ClickEvent
import net.minecraft.util.ChatStyle
import net.minecraft.util.EnumChatFormatting
import java.net.URI

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
        chatComponentText = chatComponent
        text = chatComponentText.formattedText

        val chatStyle = chatComponent.getStyling()

        val clickEvent = chatStyle.getClick()
        clickAction = clickEvent?.action?.canonicalName
        clickValue = clickEvent?.value

        val hoverEvent = chatStyle.getHover()
        hoverAction = hoverEvent?.action?.canonicalName
        hoverValue = hoverEvent?.value?.formattedText
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
     * Sets the click action and value of the component.
     * See [TextComponent.setClickAction] for possible click actions.
     * @param action the click action
     * @param value the click value
     */
    fun setClick(action: String, value: String) = apply {
        clickAction = action
        clickValue = value
        reInstanceClick()
    }

    /**
     * @return the current click action
     */
    fun getClickAction(): String? = clickAction

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
        clickAction = action
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
     * See [TextComponent.setHoverAction] for possible hover actions.
     * @param action the hover action
     * @param value the hover value
     */
    fun setHover(action: String, value: String) = apply {
        hoverAction = action
        hoverValue = value
        reInstanceHover()
    }

    /**
     * @return the current hover action
     */
    fun getHoverAction(): String? = hoverAction

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
        hoverAction = action
        reInstanceHover()
    }

    /**
     * @return the current hover value
     */
    fun getHoverValue(): String? = hoverValue

    /**
     * Sets the value to be used by the hover action.
     * See [TextComponent.setHoverAction] for possible hover actions.
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
                "clickValue:$clickValue" +
                "}"

    private fun reInstance() {
        val string = if (formatted) ChatLib.addColor(text) else text

        chatComponentText = stringToComponent(string)

        reInstanceClick()
        reInstanceHover()
    }

    private fun reInstanceClick() {
        if (clickAction == null || clickValue == null) return

        chatComponentText.getStyling()
            //#if MC<=10809
            .chatClickEvent =
                //#else
                //$$ .clickEvent =
                //#endif
            MCTextClickEvent(
                MCClickEventAction.getValueByCanonicalName(clickAction),
                if (formatted) ChatLib.addColor(clickValue)
                else clickValue
            )
    }

    private fun reInstanceHover() {
        if (hoverAction == null || hoverValue == null) return

        chatComponentText.getStyling()
            //#if MC<=10809
            .chatHoverEvent =
                //#else
                //$$ .hoverEvent =
                //#endif
            MCTextHoverEvent(
                MCHoverEventAction.getValueByCanonicalName(hoverAction),
                MCBaseTextComponent(
                    if (formatted) ChatLib.addColor(hoverValue)
                    else hoverValue
                )
            )
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun stringToComponent(string: String): MCBaseTextComponent {
        val buffer = StringBuilder()
        val comp = MCBaseTextComponent("")
        var style = ChatStyle()
        var i = 0
        while (i < string.length) {
            if (i < string.length - 1 && formatRegex.matches(string.substring(i..i + 1))) {
                // add the previous component as a sibling of the base component
                val prevText = MCBaseTextComponent(buffer.toString()).apply { chatStyle = style.createDeepCopy() }
                buffer.clear()
                comp.appendSibling(prevText)

                // Update the style local var
                i++
                when {
                    string[i] in '0'..'f' -> style.color = EnumChatFormatting.values()[string[i].digitToInt(16)]
                    string[i] == 'k' -> style.obfuscated = true
                    string[i] == 'l' -> style.bold = true
                    string[i] == 'm' -> style.strikethrough = true
                    string[i] == 'n' -> style.underlined = true
                    string[i] == 'o' -> style.italic = true
                    string[i] == 'r' -> style = ChatStyle()
                }
            } else if (URL_REGEX.matchesAt(string, i)) {
                // add the previous component as a sibling of the base component
                val prevText = MCBaseTextComponent(buffer.toString()).apply { chatStyle = style.createDeepCopy() }
                buffer.clear()
                comp.appendSibling(prevText)

                val link = URL_REGEX.matchAt(string, i)!!.value
                i += link.length - 1

                // add the link with previous styles
                val linkComponent = MCBaseTextComponent(link).apply {
                    chatStyle = style.createDeepCopy()

                    chatStyle.chatClickEvent = ClickEvent(
                        ClickEvent.Action.OPEN_URL, if (URI(link).scheme == null) {
                            "http://$link"
                        } else {
                            link
                        }
                    )
                }

                comp.appendSibling(linkComponent)
            } else {
                // store this char for later use
                buffer.append(string[i])
            }
            i++
        }

        if (buffer.isNotEmpty()) {
            // add the leftover parts of the string
            comp.appendSibling(MCBaseTextComponent(buffer.toString()).apply { chatStyle = style.createDeepCopy() })
        }
        return comp
    }

    companion object {
        private val URL_REGEX =
        // a modified version of ForgeHooks.URL_PATTERN disallowing connecting to IPs
        //           schema             namespace            port   path         ends
            //   |-------------|  |------------------|    |-------| |--|   |---------------|
            "((?:[a-z\\d]{2,}://)?[-\\w.]+\\.[a-z]{2,}?(?::\\d{1,5})?.*?(?=[!\"\u00A7 \n]|$))".toRegex()

        private val formatRegex = "[\u00A7&][\\da-fk-or]".toRegex()
    }
}
