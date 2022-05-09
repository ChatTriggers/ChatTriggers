package com.chattriggers.ctjs.utils.kotlin

import com.fasterxml.jackson.core.Version
import gg.essential.universal.wrappers.message.UMessage
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.resources.I18n
import org.mozilla.javascript.NativeObject

//#if MC<=10809
fun MCITextComponent.getStyling() = chatStyle
fun MCTextStyle.getClick() = chatClickEvent
fun MCTextStyle.getHover() = chatHoverEvent
fun Tessellator.getRenderer() = worldRenderer
fun String.i18Format(vararg objects: Any) = I18n.format(this, *objects)
//#else
//$$ fun MCITextComponent.getStyling() = style
//$$ fun MCTextStyle.getClick() = clickEvent
//$$ fun MCTextStyle.getHover() = hoverEvent
//$$ fun Tesselator.getRenderer() = builder
//$$ fun String.i18Format(vararg objects: Any) = I18n.get(this, *objects)
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

// To make the intent clear
@Suppress("UNCHECKED_CAST")
fun <T> Any?.asMixin(): T = this as T
