package com.chattriggers.ctjs.engine.langs.js

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.engine.IRegister
import com.chattriggers.ctjs.minecraft.objects.display.Display
import com.chattriggers.ctjs.minecraft.objects.display.DisplayLine
import com.chattriggers.ctjs.minecraft.objects.gui.Gui
import com.chattriggers.ctjs.minecraft.objects.keybind.KeyBind
import com.chattriggers.ctjs.minecraft.wrappers.Client
import net.minecraft.client.settings.KeyBinding
import org.mozilla.javascript.NativeObject

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
    constructor(text: String, config: NativeObject) : super(text, config)

    override fun getLoader(): ILoader = JSLoader
}

class JSDisplay : Display {
    constructor() : super()
    constructor(config: NativeObject?) : super(config)

    override fun createDisplayLine(text: String): DisplayLine {
        return JSDisplayLine(text)
    }
}

class JSKeyBind : KeyBind {
    @JvmOverloads
    constructor(category: String, key: Int, description: String = "ChatTriggers") : super(category, key, description)
    constructor(keyBinding: KeyBinding) : super(keyBinding)

    override fun getLoader(): ILoader = JSLoader
}

object JSClient : Client() {
    override fun getKeyBindFromKey(keyCode: Int): KeyBind? {
        return getMinecraft().gameSettings.keyBindings
            .firstOrNull { it.keyCode == keyCode }
            ?.let(::JSKeyBind)
    }

    override fun getKeyBindFromKey(keyCode: Int, description: String, category: String): KeyBind {
        return getMinecraft().gameSettings.keyBindings
            .firstOrNull { it.keyCode == keyCode }
            ?.let(::JSKeyBind)
            ?: JSKeyBind(description, keyCode, category)
    }

    override fun getKeyBindFromKey(keyCode: Int, description: String): KeyBind {
        return getKeyBindFromKey(keyCode, description, "ChatTriggers")
    }

    override fun getKeyBindFromDescription(description: String): KeyBind? {
        return getMinecraft().gameSettings.keyBindings
            .firstOrNull { it.keyDescription == description }
            ?.let(::JSKeyBind)
    }

    val currentGui = Client.Companion.currentGui
    val camera = Client.Companion.camera
}
