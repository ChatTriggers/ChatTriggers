package com.chattriggers.ctjs.engine.langs.js

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.engine.IRegister
import com.chattriggers.ctjs.minecraft.listeners.ClientListener
import com.chattriggers.ctjs.minecraft.objects.display.Display
import com.chattriggers.ctjs.minecraft.objects.display.DisplayLine
import com.chattriggers.ctjs.minecraft.objects.gui.Gui
import com.chattriggers.ctjs.minecraft.objects.keybind.KeyBind
import com.chattriggers.ctjs.minecraft.objects.keybind.KeyBindHandler
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.utils.WrappedThread
import com.chattriggers.ctjs.triggers.Trigger
import net.minecraft.client.settings.KeyBinding
import org.graalvm.polyglot.Value
import kotlin.reflect.full.memberFunctions

/*
 * This file holds the "glue" for this language.
 *
 * Certain classes have triggers inside them that need to know what loader to use,
 * and that's where these implementations come in.
 */

object JSRegister : IRegister<Value> {
    override fun register(triggerType: String, method: Value): Trigger {
        val name = triggerType.lowercase()

        var func = IRegister.methodMap[name]

        if (func == null) {
            func = this::class.memberFunctions.firstOrNull {
                it.name.lowercase() == "register$name"
            } ?: throw NoSuchMethodException("No trigger type named '$triggerType'")
            IRegister.methodMap[name] = func
        }

        return func.call(this, method) as Trigger
    }
    override fun getImplementationLoader(): ILoader = JSLoader
}

class JSGui : Gui() {
    override fun getLoader(): ILoader = JSLoader
}

class JSDisplayLine : DisplayLine {
    constructor(text: String) : super(text)
    constructor(text: String, config: Value) : super(text, config)

    override fun getLoader(): ILoader = JSLoader
}

class JSDisplay : Display {
    constructor() : super()
    constructor(config: Value) : super(config)

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
        return KeyBindHandler.getKeyBinds()
            .find { it.getKeyCode() == keyCode }
            ?: getMinecraft().gameSettings.keyBindings
                .find { it.keyCode == keyCode }
                ?.let(::JSKeyBind)
    }

    override fun getKeyBindFromKey(keyCode: Int, description: String, category: String): KeyBind {
        return getKeyBindFromKey(keyCode) ?: JSKeyBind(description, keyCode, category)
    }

    override fun getKeyBindFromKey(keyCode: Int, description: String): KeyBind {
        return getKeyBindFromKey(keyCode, description, "ChatTriggers")
    }

    override fun getKeyBindFromDescription(description: String): KeyBind? {
        return KeyBindHandler.getKeyBinds()
            .find { it.getDescription() == description }
            ?: getMinecraft().gameSettings.keyBindings
                .find { it.keyDescription == description }
                ?.let(::JSKeyBind)
    }

    val currentGui = Client.Companion.currentGui
    val camera = Client.Companion.camera
}

class JSWrappedThread(task: Runnable) : WrappedThread(task) {
    override fun getLoader() = JSLoader
}
