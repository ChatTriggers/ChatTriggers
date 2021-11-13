package com.chattriggers.ctjs.engine.langs.js

import com.chattriggers.ctjs.engine.IRegister
import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.minecraft.objects.display.Display
import com.chattriggers.ctjs.minecraft.objects.display.DisplayHandler
import com.chattriggers.ctjs.minecraft.objects.display.DisplayLine
import com.chattriggers.ctjs.minecraft.objects.gui.Gui
import com.chattriggers.ctjs.utils.kotlin.getOrNull
import com.reevajs.reeva.core.Agent
import com.reevajs.reeva.jvmcompat.JVMValueMapper
import com.reevajs.reeva.runtime.JSValue
import com.reevajs.reeva.runtime.objects.JSObject
import com.reevajs.reeva.runtime.toBoolean
import com.reevajs.reeva.runtime.toJSString
import com.reevajs.reeva.runtime.toNumber

/*
This file holds the "glue" for this language.

Certain classes have triggers inside of them that need to know what loader to use,
and that's where these implementations come in.
 */

object JSRegister : IRegister {
    override fun getImplementationLoader(): ILoader = JSLoader
}

class JSGui : Gui() {
    override fun getLoader(): ILoader = JSLoader
}

class JSDisplayLine : DisplayLine {
    constructor(text: String) : super(text)

    constructor(text: String, config: JSObject) : super(text) {
        setTextColor(config.getOrNull("textColor"))
        setBackgroundColor(config.getOrNull("textColor"))
        setAlign(config.getOrNull("align"))
        setBackground(config.getOrNull("background"))
    }

    override fun getLoader(): ILoader = JSLoader
}

class JSDisplay : Display {
    constructor() : super()

    constructor(config: JSObject) : super() {
        fun <T> getOption(prop: String, default: T, ifHas: (JSValue) -> T): T {
            return if (config.hasProperty(prop)) {
                ifHas(config.get(prop))
            } else default
        }

        val shouldRender = config.getOrNull<Boolean>("shouldRender") ?: true
        val renderX = config.getOrNull<Float>("renderX") ?: 0f
        val renderY = config.getOrNull<Float>("renderY") ?: 0f
        val backgroundColor = config.getOrNull<Long>("backgroundColor") ?: 0x50000000
        val textColor = config.getOrNull<Long>("textColor") ?: 0xffffffff
        val minWidth = config.getOrNull<Float>("minWidth") ?: 0f

        val background = getOption("background", DisplayHandler.Background.NONE) {
            DisplayHandler.Background.valueOf(it.toJSString(Agent.activeRealm).string)
        }
        val align = getOption("align", DisplayHandler.Align.LEFT) {
            DisplayHandler.Align.valueOf(it.toJSString(Agent.activeRealm).string)
        }
        val order = getOption("order", DisplayHandler.Order.DOWN) {
            DisplayHandler.Order.valueOf(it.toJSString(Agent.activeRealm).string)
        }

        setShouldRender(shouldRender)
        setRenderX(renderX)
        setRenderY(renderY)
        setBackgroundColor(backgroundColor)
        setTextColor(textColor)
        setBackground(background)
        setAlign(align)
        setOrder(order)
        setMinWidth(minWidth)
    }

    override fun createDisplayLine(text: String): DisplayLine {
        return JSDisplayLine(text)
    }
}
