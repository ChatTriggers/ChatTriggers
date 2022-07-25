package com.chattriggers.ctjs.minecraft.wrappers.chat

import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent

class ChatStyle(mcStyle: net.minecraft.util.ChatStyle) {
    var mcStyle = mcStyle
        private set

    //#if MC<=11202
    fun isBold() = mcStyle.bold

    fun setBold(bold: Boolean) = apply {
        mcStyle.bold = bold
    }

    fun isItalic(): Boolean = mcStyle.italic

    fun setItalic(italic: Boolean) = apply {
        mcStyle.italic = italic
    }

    fun isStrikethrough(): Boolean = mcStyle.strikethrough

    fun setStrikethrough(strikethrough: Boolean) = apply {
        mcStyle.strikethrough = strikethrough
    }

    fun isUnderlined(): Boolean = mcStyle.underlined

    fun setUnderlined(underlined: Boolean) = apply {
        mcStyle.underlined = underlined
    }

    fun isObfuscated(): Boolean = mcStyle.obfuscated

    fun setObfuscated(obfuscated: Boolean) = apply {
        mcStyle.obfuscated = obfuscated
    }

    fun isEmpty(): Boolean = mcStyle.isEmpty

    fun getClickEvent(): ClickEvent? = mcStyle.chatClickEvent

    fun getHoverEvent(): HoverEvent? = mcStyle.chatHoverEvent
    //#elseif MC>=11701
    //$$ fun isBold() = mcStyle.isBold
    //$$
    //$$ fun setBold(bold: Boolean) = apply {
    //$$     mcStyle = mcStyle.withBold(bold)
    //$$ }
    //$$
    //$$ fun isItalic(): Boolean = mcStyle.isItalic
    //$$
    //$$ fun setItalic(italic: Boolean) = apply {
    //$$     mcStyle = mcStyle.withItalic(italic)
    //$$ }
    //$$
    //$$ fun isStrikethrough(): Boolean = mcStyle.isStrikethrough
    //$$
    //$$ fun setStrikethrough(strikethrough: Boolean) = apply {
    //$$     mcStyle = mcStyle.withStrikethrough(strikethrough)
    //$$ }
    //$$
    //$$ fun isUnderlined(): Boolean = mcStyle.isUnderlined
    //$$
    //$$ fun setUnderlined(underlined: Boolean) = apply {
    //$$     mcStyle = mcStyle.withUnderline(underlined)
    //$$ }
    //$$
    //$$ fun isObfuscated(): Boolean = mcStyle.isObfuscated
    //$$
    //$$ fun setObfuscated(obfuscated: Boolean) = apply {
    //$$     mcStyle = mcStyle.obfuscated(obfuscated)
    //$$ }
    //$$
    //$$ fun isEmpty(): Boolean = mcStyle.isEmpty
    //$$
    //$$ fun getClickEvent(): ClickEvent? = mcStyle.clickEvent
    //$$
    //$$ fun getHoverEvent(): HoverEvent? = mcStyle.hoverEvent
    //#endif
}
