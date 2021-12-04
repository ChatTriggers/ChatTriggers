package com.chattriggers.ctjs.utils.kotlin

import com.fasterxml.jackson.core.Version
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.graalvm.polyglot.Value

fun MCITextComponent.getStyling(): MCTextStyle =
    //#if MC<=10809
    this.chatStyle

//#else
//$$ this.style
//#endif
fun MCTextStyle.getClick(): MCTextClickEvent? =
    //#if MC<=10809
    chatClickEvent
//#else
//$$ clickEvent
//#endif

fun MCTextStyle.getHover(): MCTextHoverEvent? =
    //#if MC<=10809
    chatHoverEvent
//#else
//$$ hoverEvent
//#endif

fun MCTessellator.getRenderer(): MCWorldRenderer =
    //#if MC<=10809
    worldRenderer
//#else
//$$ buffer
//#endif

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

inline fun <reified T> Value.getMemberAs(key: String): T? {
    return getMember(key)?.let {
        when (T::class.java) {
            Boolean::class.java -> if (!it.isBoolean) {
                throw IllegalArgumentException("Expected $this to have a boolean property named $key")
            } else it.asBoolean()
            Int::class.java -> if (!it.isNumber) {
                throw IllegalArgumentException("Expected $this to have a numeric property named $key")
            } else it.asInt()
            Long::class.java -> if (!it.isNumber) {
                throw IllegalArgumentException("Expected $this to have a numeric property named $key")
            } else it.asLong()
            Float::class.java -> if (!it.isNumber) {
                throw IllegalArgumentException("Expected $this to have a numeric property named $key")
            } else it.asFloat()
            Double::class.java -> if (!it.isNumber) {
                throw IllegalArgumentException("Expected $this to have a numeric property named $key")
            } else it.asDouble()
            String::class.java -> if (!it.isString) {
                throw IllegalArgumentException("Expected $this to have a string property named $key")
            } else it.asString()
        }
        it as T
    }
}
