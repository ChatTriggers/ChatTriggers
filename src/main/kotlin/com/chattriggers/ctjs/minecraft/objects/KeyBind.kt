package com.chattriggers.ctjs.minecraft.objects

import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.fml.client.registry.ClientRegistry
import org.apache.commons.lang3.ArrayUtils
import java.util.*

@External
class KeyBind {
    private var keyBinding: KeyBinding
    private var isCustom: Boolean = false

    /**
     * Creates a new key bind, editable in the user's controls.
     *
     * @param category the keybind category the keybind will be in
     * @param description what the key bind does
     * @param keyCode     the keycode which the key bind will respond to, see Keyboard below. Ex. Keyboard.KEY_A
     * @see [Keyboard](http://legacy.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html)
     */
    @JvmOverloads
    constructor(description: String, keyCode: Int, category: String = "ChatTriggers") {
        for (key in Client.getMinecraft().gameSettings.keyBindings) {
            if (key.keyCategory == category && key.keyDescription == description) {
                Client.getMinecraft().gameSettings.keyBindings = ArrayUtils.removeElement(
                    Client.getMinecraft().gameSettings.keyBindings, key
                )
                break
            }
        }

        keyBinding = KeyBinding(description, keyCode, category)
        ClientRegistry.registerKeyBinding(keyBinding)

        isCustom = true
    }

    constructor(keyBinding: KeyBinding) {
        this.keyBinding = keyBinding
        isCustom = false
    }

    /**
     * Returns true if the key is pressed (used for continuous querying).
     *
     * @return whether or not the key is pressed
     */
    fun isKeyDown(): Boolean = keyBinding.isKeyDown

    /**
     * Returns true on the initial key press. For continuous querying use [isKeyDown].
     *
     * @return whether or not the key has just been pressed
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
}
