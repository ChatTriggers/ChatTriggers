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

    private var text: String
    private var formatted = true

    private var clickAction: String? = null
    private var clickValue: String? = null
    private var hoverAction: String? = "show_text"
    private var hoverValue: String? = null

    constructor(text: String) {
        this.text = text
        reInstance()
    }

    //#if MC<=10809
    constructor(chatComponent: IChatComponent) {
    //#else
    //$$ constructor(chatComponent: ITextComponent) {
    //#endif
        this.chatComponentText = chatComponent
        this.text = this.chatComponentText.formattedText

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
        this.clickAction = clickEvent?.action?.canonicalName
        this.clickValue = clickEvent?.value

        val hoverEvent = chatStyle.
                //#if MC<=10809
                chatHoverEvent
                //#else
                //$$ hoverEvent
                //#endif
        this.hoverAction = hoverEvent?.action?.canonicalName
        this.hoverValue = hoverEvent?.value?.formattedText
    }

    fun getText() = this.text
    fun setText(text: String): TextComponent {
        this.text = text
        reInstance()
        return this
    }

    fun isFormatted() = this.formatted
    fun setFormatted(formatted: Boolean): TextComponent {
        this.formatted = formatted
        reInstance()
        return this
    }

    fun setClick(action: String, value: String): TextComponent {
        this.clickAction = action
        this.clickValue = value
        reInstanceClick()
        return this
    }

    fun getClickAction() = this.clickAction
    fun setClickAction(action: String): TextComponent {
        this.clickAction = action
        reInstanceClick()
        return this
    }

    fun getClickValue() = this.clickValue
    fun setClickValue(value: String): TextComponent {
        this.clickValue = value
        reInstanceClick()
        return this
    }

    fun setHover(action: String, value: String): TextComponent {
        this.hoverAction = action
        this.hoverValue = value
        return this
    }

    fun getHoverAction() = this.hoverAction
    fun setHoverAction(action: String): TextComponent {
        this.hoverAction = action
        reInstanceHover()
        return this
    }

    fun getHoverValue() = this.hoverValue
    fun setHoverValue(value: String): TextComponent {
        this.hoverValue = value
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
                        if (this.formatted) ChatLib.addColor(this.text)
                        else this.text
                )

        reInstanceClick()
        reInstanceHover()
    }

    private fun reInstanceClick() {
        if (this.clickAction == null || this.clickValue == null) return

        this.chatComponentText.
                //#if MC<=10809
                chatStyle.chatClickEvent =
                //#else
                //$$ style.clickEvent =
                //#endif
                ClickEvent(
                        ClickEvent.Action.getValueByCanonicalName(this.clickAction),
                        if (this.formatted) ChatLib.addColor(this.clickValue)
                        else this.clickValue
                )
    }

    private fun reInstanceHover() {
        if (this.hoverAction == null || this.hoverValue == null) return

        this.chatComponentText.
                //#if MC<=10809
                chatStyle.chatHoverEvent =
                //#else
                //$$ style.hoverEvent =
                //#endif
                HoverEvent(
                        HoverEvent.Action.getValueByCanonicalName(this.hoverAction),
                        //#if MC<=10809
                        ChatComponentText(
                        //#else
                        //$$ TextComponentString(
                        //#endif
                                if (this.formatted) ChatLib.addColor(this.hoverValue)
                                else this.hoverValue
                        )
                )
    }
}