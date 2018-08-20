package com.chattriggers.ctjs.minecraft.objects.message

import com.chattriggers.ctjs.minecraft.libs.ChatLib

//#if MC<=10809
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent
//#else
//$$ import net.minecraft.util.text.event.ClickEvent
//$$ import net.minecraft.util.text.event.HoverEvent
//$$ import net.minecraft.util.text.TextComponentString
//$$ import net.minecraft.util.text.ITextComponent
//#endif

class TextComponent {

    lateinit var chatComponentText:
            //#if MC<=10809
            IChatComponent
            //#else
            //$$ ITextComponent
            //#endif

    private var _text: String
    private var _formatted = true

    private var _clickAction: String? = null
    private var _clickValue: String? = null
    private var _hoverAction: String? = "show_text"
    private var _hoverValue: String? = null

    constructor(text: String) {
        this._text = text
        reInstance()
    }

    //#if MC<=10809
    constructor(chatComponent: IChatComponent) {
    //#else
    //$$ constructor(chatComponent: ITextComponent) {
    //#endif
        this.chatComponentText = chatComponent
        this._text = this.chatComponentText.formattedText

        val chatStyle =
                //#if MC<=10809
                chatComponent.chatStyle
                //#else
                //$$ chatComponent.style
                //#endif

        val clickEvent = chatStyle.
                //#if MC<=10809
                chatClickEvent
                //#else
                //$$ clickEvent
                //#endif
        this._clickAction = clickEvent?.action?.canonicalName
        this._clickValue = clickEvent?.value

        val hoverEvent = chatStyle.
                //#if MC<=10809
                chatHoverEvent
                //#else
                //$$ hoverEvent
                //#endif
        this._hoverAction = hoverEvent?.action?.canonicalName
        this._hoverValue = hoverEvent?.value?.formattedText
    }

    fun getText() = this._text
    fun setText(text: String): TextComponent {
        this._text = text
        reInstance()
        return this
    }

    fun isFormatted() = this._formatted
    fun setFormatted(formatted: Boolean): TextComponent {
        this._formatted = formatted
        reInstance()
        return this
    }

    fun setClick(action: String, value: String): TextComponent {
        this._clickAction = action
        this._clickValue = value
        reInstanceClick()
        return this
    }

    fun getClickAction() = this._clickAction
    fun setClickAction(action: String): TextComponent {
        this._clickAction = action
        reInstanceClick()
        return this
    }

    fun getClickValue() = this._clickValue
    fun setClickValue(value: String): TextComponent {
        this._clickValue = value
        reInstanceClick()
        return this
    }

    fun setHover(action: String, value: String): TextComponent {
        this._hoverAction = action
        this._hoverValue = value
        return this
    }

    fun getHoverAction() = this._hoverAction
    fun setHoverAction(action: String): TextComponent {
        this._hoverAction = action
        reInstanceHover()
        return this
    }

    fun getHoverValue() = this._hoverValue
    fun setHoverValue(value: String): TextComponent {
        this._hoverValue = value
        reInstanceHover()
        return this
    }

    fun chat() = Message(this).chat()
    fun actionBar() = Message(this).actionBar()

    private fun reInstance() {
        this.chatComponentText =
                //#if MC<=10809
                ChatComponentText(
                //#else
                //$$ TextComponentString(
                //#endif
                        if (this._formatted) ChatLib.addColor(this._text)
                        else this._text
                )

        reInstanceClick()
        reInstanceHover()
    }

    private fun reInstanceClick() {
        if (this._clickAction == null || this._clickValue == null) return

        this.chatComponentText.
                //#if MC<=10809
                chatStyle.chatClickEvent =
                //#else
                //$$ style.clickEvent =
                //#endif
                ClickEvent(
                        ClickEvent.Action.getValueByCanonicalName(this._clickAction),
                        if (this._formatted) ChatLib.addColor(this._clickValue)
                        else this._clickValue
                )
    }

    private fun reInstanceHover() {
        if (this._hoverAction == null || this._hoverValue == null) return

        this.chatComponentText.
                //#if MC<=10809
                chatStyle.chatHoverEvent =
                //#else
                //$$ style.hoverEvent =
                //#endif
                HoverEvent(
                        HoverEvent.Action.getValueByCanonicalName(this._hoverAction),
                        //#if MC<=10809
                        ChatComponentText(
                        //#else
                        //$$ TextComponentString(
                        //#endif
                                if (this._formatted) ChatLib.addColor(this._hoverValue)
                                else this._hoverValue
                        )
                )
    }
}