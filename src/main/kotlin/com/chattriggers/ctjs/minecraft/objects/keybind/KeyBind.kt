package com.chattriggers.ctjs.minecraft.objects.keybind

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.triggers.OnRegularTrigger
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.NotAbstract
import net.minecraft.client.resources.I18n
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.fml.client.registry.ClientRegistry
import org.lwjgl.input.Keyboard

@Suppress("LeakingThis")
@NotAbstract
@External
abstract class KeyBind {
    private val keyBinding: KeyBinding
    private var onKeyPress: OnRegularTrigger? = null
    private var onKeyRelease: OnRegularTrigger? = null
    private var onKeyDown: OnRegularTrigger? = null

    fun registerKeyPress(method: Any) = run {
        onKeyPress = OnRegularTrigger(method, TriggerType.Other, getLoader())
        onKeyPress
    }

    fun registerKeyRelease(method: Any) = run {
        onKeyRelease = OnRegularTrigger(method, TriggerType.Other, getLoader())
        onKeyRelease
    }

    fun registerKeyDown(method: Any) = run {
        onKeyDown = OnRegularTrigger(method, TriggerType.Other, getLoader())
        onKeyDown
    }

    internal fun onTick() {
        if (isKeyDown()) {
            onKeyDown?.trigger(arrayOf())
        }
    }

    internal fun onKeyInput() {
        if (Keyboard.getEventKey() != getKeyCode())
            return

        if (Keyboard.getEventKeyState()) {
            onKeyPress?.trigger(arrayOf())
        } else {
            onKeyRelease?.trigger(arrayOf())
        }
    }

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
        }
        KeyBindHandler.registerKeyBind(this)
    }

    constructor(keyBinding: KeyBinding) {
        this.keyBinding = keyBinding
        KeyBindHandler.registerKeyBind(this)
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

    internal abstract fun getLoader(): ILoader

    companion object {
        private val keyBinds = mutableListOf<KeyBinding>()
    }
}
