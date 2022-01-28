package com.chattriggers.ctjs.minecraft.objects

import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.client.resources.I18n
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.fml.client.registry.ClientRegistry
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
        val possibleDuplicate = Client.getMinecraft().gameSettings.keyBindings.find {
            I18n.format(it.keyDescription) == I18n.format(description) &&
            I18n.format(it.keyCategory) == I18n.format(category)
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
            ClientRegistry.registerKeyBinding(keyBinding)
            keyBinds.add(keyBinding)
            ctKeyBinds.add(this)
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
    fun isKeyDown(): Boolean = keyBinding.isKeyDown

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
    fun getKeyCode(): Int = keyBinding.keyCode

    /**
     * Sets the state of the key.
     *
     * @param pressed True to press, False to release
     */
    fun setState(pressed: Boolean) = KeyBinding.setKeyBindState(keyBinding.keyCode, pressed)

    override fun equals(other: Any?): Boolean {
        return if (other is KeyBind) {
            other.keyBinding == keyBinding &&
            other.keyBinding.keyCode == keyBinding.keyCode &&
            other.keyBinding.keyCategory == keyBinding.keyCategory &&
            other.keyBinding.keyDescription == keyBinding.keyDescription
        } else false
    }

    override fun toString(): String {
        return "KeyBind{description=${keyBinding.keyDescription}, " +
            "keyCode=${keyBinding.keyCode}, " +
            "category=${keyBinding.keyCategory}}"
    }

    override fun hashCode(): Int {
        return keyBinding.hashCode()
    }

    companion object {
        private val keyBinds = mutableListOf<KeyBinding>()
        val ctKeyBinds = mutableListOf<KeyBind>()
    }
}
