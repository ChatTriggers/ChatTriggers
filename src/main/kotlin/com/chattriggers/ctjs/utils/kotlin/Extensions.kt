package com.chattriggers.ctjs.utils.kotlin

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