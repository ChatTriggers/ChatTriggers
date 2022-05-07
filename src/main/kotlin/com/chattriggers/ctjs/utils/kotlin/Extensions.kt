package com.chattriggers.ctjs.utils.kotlin

import com.fasterxml.jackson.core.Version
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import gg.essential.universal.wrappers.message.UMessage
import org.mozilla.javascript.NativeObject

//#if MC<=10809
fun MCITextComponent.getStyling(): MCTextStyle = chatStyle
fun MCTextStyle.getClick(): MCTextClickEvent? = chatClickEvent
fun MCTextStyle.getHover(): MCTextHoverEvent? = chatHoverEvent
fun MCTessellator.getRenderer(): MCWorldRenderer = worldRenderer
//#else
//$$ fun MCITextComponent.getStyling(): MCTextStyle = style
//$$ fun MCTextStyle.getClick(): MCTextClickEvent? = clickEvent
//$$ fun MCTextStyle.getHover(): MCTextHoverEvent? = hoverEvent
//$$ fun MCTessellator.getRenderer(): MCWorldRenderer = buffer
//#endif

fun UMessage.setChatLineId(id: Int) = apply {
    chatLineId = id
}
fun UMessage.setRecursive(recursive: Boolean) = apply {
    isRecursive = recursive
}

operator fun String.times(times: Number): String {
    val stringBuilder = StringBuilder()

    for (i in 0 until times.toInt()) {
        stringBuilder.append(this)
    }

    return stringBuilder.toString()
}

fun String.toVersion(): Version {
    val split = this.split(".").map(String::toInt)
    return Version(split.getOrNull(0) ?: 0, split.getOrNull(1) ?: 0, split.getOrNull(2) ?: 0, null, null, null)
}

fun NativeObject?.getOption(key: String, default: Any): String {
    return (this?.get(key) ?: default).toString()
}

fun NativeObject?.getOptionNullable(key: String, default: Any?): String? {
    return (this?.get(key) ?: default)?.toString()
}
