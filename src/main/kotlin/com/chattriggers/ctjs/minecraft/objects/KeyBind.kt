package com.chattriggers.ctjs.minecraft.objects

import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.utils.kotlin.External
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.resource.language.I18n
import java.util.*

@External
class KeyBind {
    private val keyBinding: KeyBinding

    /**
     * Creates a new key bind, editable in the user's controls.
     *
     * @param category the keybind category the keybind will be in
     * @param description what the key bind does
     * @param keyCode the keycode which the key bind will respond to, see Keyboard below. Ex. Keyboard.KEY_A
     * @see [Keyboard](http://legacy.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html)
     */
    @JvmOverloads
    constructor(description: String, keyCode: Int, category: String = "ChatTriggers") {
        val possibleDuplicate = Client.getMinecraft().options.keysAll.find {
            I18n.translate(it.translationKey) == I18n.translate(description) &&
                I18n.translate(it.category) == I18n.translate(category)
        }

        if (possibleDuplicate != null) {
            if (possibleDuplicate in keyBinds)
                keyBinding = possibleDuplicate
            else throw IllegalArgumentException(
                "KeyBind already exists! To get a KeyBind from an existing Minecraft KeyBinding, " +
                    "use the other KeyBind constructor or Client.getKeyBindFromKey."
            )
        } else {
            keyBinding = KeyBinding(description, keyCode, category)
            KeyBindingHelper.registerKeyBinding(keyBinding)
            keyBinds.add(keyBinding)
        }
    }

    constructor(keyBinding: KeyBinding) {
        this.keyBinding = keyBinding
    }

    /**
     * Returns true if the key is pressed (used for continuous querying).
     *
     * @return whether the key is pressed
     */
    fun isKeyDown(): Boolean = keyBinding.wasPressed()

    /**
     * Returns true on the initial key press. For continuous querying use [isKeyDown].
     *
     * @return whether the key has just been pressed
     */
    fun isPressed(): Boolean = keyBinding.isPressed

    /**
     * Gets the key code of the key.
     *
     * @return the integer key code
     */
    fun getKeyCode(): Int = keyBinding.defaultKey.code

    /**
     * Sets the state of the key.
     *
     * @param pressed True to press, False to release
     */
    fun setState(pressed: Boolean) = apply {
        keyBinding.isPressed = true
    }

    companion object {
        private val keyBinds = mutableListOf<KeyBinding>()
    }
}
