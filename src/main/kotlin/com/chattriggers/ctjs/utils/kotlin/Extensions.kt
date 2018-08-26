package com.chattriggers.ctjs.utils.kotlin

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer

fun ITextComponent.getStyling() = this.chatStyle!!
fun TextStyle.getClick(): TextClickEvent? =
        //#if MC<=10809
        chatClickEvent
        //#else
        //$$ clickEvent
        //#endif

fun TextStyle.getHover(): TextHoverEvent? =
        //#if MC<=10809
        chatHoverEvent
        //#else
        //$$ hoverEvent
        //#endif

operator fun String.times(times: Number): String {
    val stringBuilder = StringBuilder()

    for (i in 0..times.toInt()) {
        stringBuilder.append(this)
    }

    return stringBuilder.toString()
}