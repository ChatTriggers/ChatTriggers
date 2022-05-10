package com.chattriggers.ctjs.minecraft.objects.keybind

import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.triggers.RegularTrigger
import com.chattriggers.ctjs.triggers.TriggerType
import net.minecraft.client.resources.I18n
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.fml.client.registry.ClientRegistry
import org.apache.commons.lang3.ArrayUtils

@Suppress("LeakingThis")
class KeyBind {
    private val keyBinding: KeyBinding
    private var onKeyPress: RegularTrigger? = null
    private var onKeyRelease: RegularTrigger? = null
    private var onKeyDown: RegularTrigger? = null

    private var down: Boolean = false

    init {
        KeyBindHandler.registerKeyBind(this)
    }

    /**
     * Creates a new keybind, editable in the user's controls.
     *
     * @param description what the keybind does
     * @param keyCode the keycode which the keybind will respond to, see Keyboard below. Ex. Keyboard.KEY_A
     * @param category the keybind category the keybind will be in
     * @see [org.lwjgl.input.Keyboard](http://legacy.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html)
     */
    @JvmOverloads
    constructor(description: String, keyCode: Int, category: String = "ChatTriggers") {
        val possibleDuplicate = Client.getMinecraft().gameSettings.keyBindings.find {
            I18n.format(it.keyDescription) == I18n.format(description) &&
                    I18n.format(it.keyCategory) == I18n.format(category)
        }

        if (possibleDuplicate != null) {
            if (possibleDuplicate in customKeyBindings) {
                keyBinding = possibleDuplicate
            } else throw IllegalArgumentException(
                "KeyBind already exists! To get a KeyBind from an existing Minecraft KeyBinding, " +
                        "use the other KeyBind constructor or Client.getKeyBindFromKey."
            )
        } else {
            if (category !in KeyBinding.getKeybinds()) {
                uniqueCategories[category] = 0
            }
            uniqueCategories[category] = uniqueCategories[category]!! + 1
            keyBinding = KeyBinding(description, keyCode, category)
            ClientRegistry.registerKeyBinding(keyBinding)
            customKeyBindings.add(keyBinding)
        }
    }

    constructor(keyBinding: KeyBinding) {
        this.keyBinding = keyBinding
    }

    fun registerKeyPress(method: Any) = run {
        onKeyPress = RegularTrigger(method, TriggerType.Other)
        onKeyPress
    }

    fun registerKeyRelease(method: Any) = run {
        onKeyRelease = RegularTrigger(method, TriggerType.Other)
        onKeyRelease
    }

    fun registerKeyDown(method: Any) = run {
        onKeyDown = RegularTrigger(method, TriggerType.Other)
        onKeyDown
    }

    internal fun onTick() {
        if (isPressed()) {
            onKeyPress?.trigger(arrayOf())
            down = true
        }

        if (isKeyDown()) {
            onKeyDown?.trigger(arrayOf())
            down = true
        }

        if (down && !isKeyDown()) {
            onKeyRelease?.trigger(arrayOf())
            down = false
        }
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
     * Gets the description of the key.
     *
     * @return the description
     */
    fun getDescription(): String = keyBinding.keyDescription

    /**
     * Gets the key code of the key.
     *
     * @return the integer key code
     */
    fun getKeyCode(): Int = keyBinding.keyCode

    /**
     * Gets the category of the key.
     *
     * @return the category
     */
    fun getCategory(): String = keyBinding.keyCategory

    /**
     * Sets the state of the key.
     *
     * @param pressed True to press, False to release
     */
    fun setState(pressed: Boolean) = KeyBinding.setKeyBindState(keyBinding.keyCode, pressed)

    override fun toString() = "KeyBind{" +
            "description=${getDescription()}, " +
            "keyCode=${getKeyCode()}, " +
            "category=${getCategory()}" +
            "}"

    companion object {
        private val customKeyBindings = mutableListOf<KeyBinding>()
        private val uniqueCategories = mutableMapOf<String, Int>()

        private fun removeKeyBinding(keyBinding: KeyBinding) {
            Client.getMinecraft().gameSettings.keyBindings = ArrayUtils.removeElement(
                Client.getMinecraft().gameSettings.keyBindings,
                keyBinding
            )
            val category = keyBinding.keyCategory

            if (category in uniqueCategories) {
                uniqueCategories[category] = uniqueCategories[category]!! - 1

                if (uniqueCategories[category] == 0) {
                    uniqueCategories.remove(category)
                    KeyBinding.getKeybinds().remove(category)
                }
            }
        }

        @JvmStatic
        fun removeKeyBind(keyBind: KeyBind) {
            val keyBinding = keyBind.keyBinding
            if (keyBinding !in customKeyBindings) return

            removeKeyBinding(keyBinding)
            customKeyBindings.remove(keyBinding)
            KeyBindHandler.unregisterKeyBind(keyBind)
        }

        @JvmStatic
        fun clearKeyBinds() {
            val copy = KeyBindHandler.getKeyBinds().toList()
            copy.forEach(::removeKeyBind)
            customKeyBindings.clear()
            KeyBindHandler.clearKeyBinds()
        }
    }
}
