package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.launch.mixins.asMixin
import com.chattriggers.ctjs.launch.mixins.transformers.ChatScreenAccessor
import com.chattriggers.ctjs.launch.mixins.transformers.GameOptionsAccessor
import com.chattriggers.ctjs.launch.mixins.transformers.MinecraftClientMixin
import com.chattriggers.ctjs.launch.mixins.transformers.PlayerListHudMixin
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.objects.KeyBind
import com.chattriggers.ctjs.utils.kotlin.External
import gg.essential.universal.UKeyboard
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.*
import net.minecraft.client.gui.hud.ChatHud
import net.minecraft.client.gui.hud.PlayerListHud
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.Packet
import net.minecraft.util.Util
import kotlin.math.roundToInt

@External
object Client {
    @JvmStatic
    val settings = Settings()

    /**
     * Gets Minecraft's Minecraft object
     *
     * @return The Minecraft object
     */
    @JvmStatic
    fun getMinecraft(): MinecraftClient = MinecraftClient.getInstance()

    /**
     * Gets Minecraft's NetHandlerPlayClient object
     *
     * @return The NetHandlerPlayClient object
     */
    @JvmStatic
    fun getConnection(): ClientPlayNetworkHandler? = getMinecraft().networkHandler

    /**
     * Quits the client back to the main menu.
     * This acts just like clicking the "Disconnect" or "Save and quit to title" button.
     */
    @JvmStatic
    fun disconnect() = getMinecraft().disconnect()

    /**
     * Gets the Minecraft ChatHud object for the chat gui
     *
     * @return The ChatHud object for the chat gui
     */
    @JvmStatic
    fun getChatGUI(): ChatHud? = getMinecraft().inGameHud?.chatHud

    @JvmStatic
    fun isInChat(): Boolean = getMinecraft().currentScreen is ChatScreen

    @JvmStatic
    fun getTabGui(): PlayerListHud? = getMinecraft().inGameHud?.playerListHud

    @JvmStatic
    fun isInTab(): Boolean = getTabGui()?.asMixin<PlayerListHudMixin>()?.isVisible ?: false

    /**
     * Gets whether the Minecraft window is active
     * and in the foreground of the user's screen.
     *
     * @return true if the game is active, false otherwise
     */
    @JvmStatic
    fun isTabbedIn(): Boolean = TODO()

    @JvmStatic
    fun isControlDown(): Boolean = UKeyboard.isCtrlKeyDown()

    @JvmStatic
    fun isShiftDown(): Boolean = UKeyboard.isShiftKeyDown()

    @JvmStatic
    fun isAltDown(): Boolean = UKeyboard.isAltKeyDown()

    /**
     * Get the [KeyBind] from an already existing
     * Minecraft KeyBinding, otherwise, returns null.
     *
     * @param keyCode the keycode to search for, see Keyboard below. Ex. Keyboard.KEY_A
     * @return the [KeyBind] from a Minecraft KeyBinding, or null if one doesn't exist
     * @see [Keyboard](http://legacy.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html)
     */
    @JvmStatic
    fun getKeyBindFromKey(keyCode: Int): KeyBind? {
        val options = getMinecraft().options.asMixin<GameOptionsAccessor>()
        val keybinds = KeyBindingRegistryImpl.process(options.keysAll)
        return keybinds.firstOrNull {
            KeyBindingHelper.getBoundKeyOf(it).code == keyCode
        }?.let(::KeyBind)
    }

    /**
     * Get the [KeyBind] from an already existing
     * Minecraft KeyBinding, else, return a new one.
     *
     * @param keyCode the keycode to search for, see Keyboard below. Ex. Keyboard.KEY_A
     * @return the [KeyBind] from a Minecraft KeyBinding, or null if one doesn't exist
     * @see [Keyboard](http://legacy.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html)
     */
    @JvmOverloads
    @JvmStatic
    fun getKeyBindFromKey(keyCode: Int, description: String, category: String = "ChatTriggers"): KeyBind {
        val options = getMinecraft().options.asMixin<GameOptionsAccessor>()
        val keybinds = KeyBindingRegistryImpl.process(options.keysAll)
        return keybinds.firstOrNull {
            KeyBindingHelper.getBoundKeyOf(it).code == keyCode
        }?.let(::KeyBind) ?: KeyBind(description, keyCode, category)
    }

    /**
     * Get the [KeyBind] from an already existing
     * Minecraft KeyBinding, else, null.
     *
     * @param description the key binding's original description
     * @return the key bind, or null if one doesn't exist
     */
    @JvmStatic
    fun getKeyBindFromDescription(description: String): KeyBind? {
        val options = getMinecraft().options.asMixin<GameOptionsAccessor>()
        val keybinds = KeyBindingRegistryImpl.process(options.keysAll)
        return keybinds.firstOrNull {
            KeyBindingHelper.getBoundKeyOf(it).translationKey == description
        }?.let(::KeyBind)
    }

    @JvmStatic
    fun getFPS(): Int = getMinecraft().asMixin<MinecraftClientMixin>().currentFps

    @JvmStatic
    fun getVersion(): String = getMinecraft().gameVersion

    @JvmStatic
    fun getMaxMemory(): Long = Runtime.getRuntime().maxMemory()

    @JvmStatic
    fun getTotalMemory(): Long = Runtime.getRuntime().totalMemory()

    @JvmStatic
    fun getFreeMemory(): Long = Runtime.getRuntime().freeMemory()

    @JvmStatic
    fun getMemoryUsage(): Int = ((getTotalMemory() - getFreeMemory()) * 100 / getMaxMemory().toFloat()).roundToInt()

    @JvmStatic
    fun getSystemTime(): Long = Util.getMeasuringTimeNano()

    @JvmStatic
    fun getMouseX(): Float {
        val mx = getMinecraft().mouse.x.toFloat()
        val rw = Renderer.screen.getWidth().toFloat()
        val dw = getMinecraft().window.width.toFloat()
        return mx * rw / dw
    }

    @JvmStatic
    fun getMouseY(): Float {
        val my = getMinecraft().mouse.y.toFloat()
        val rh = Renderer.screen.getHeight().toFloat()
        val dh = getMinecraft().window.height.toFloat()
        return rh - my * rh / dh - 1f
    }

    @JvmStatic
    fun isInGui(): Boolean = currentGui.get() != null

    /**
     * Gets the chat message currently typed into the chat gui.
     *
     * @return A blank string if the gui isn't open, otherwise, the message
     */
    @JvmStatic
    fun getCurrentChatMessage(): String {
        return if (isInChat()) {
            val chatGui = getMinecraft().currentScreen as ChatScreen
            chatGui.asMixin<ChatScreenAccessor>().chatField.text
        } else ""
    }

    /**
     * Sets the current chat message, if the chat gui is not open, one will be opened.
     *
     * @param message the message to put in the chat text box.
     */
    @JvmStatic
    fun setCurrentChatMessage(message: String) {
        if (isInChat()) {
            val chatGui = getMinecraft().currentScreen as ChatScreen
            chatGui.asMixin<ChatScreenAccessor>().chatField.text = message
        }
    }

    @JvmStatic
    fun sendPacket(packet: Packet<*>) = getMinecraft().networkHandler!!.sendPacket(packet)

    /**
     * Display a title.
     *
     * @param title title text
     * @param subtitle subtitle text
     * @param fadeIn time to fade in
     * @param time time to stay on screen
     * @param fadeOut time to fade out
     */
    @JvmStatic
    fun showTitle(title: String, subtitle: String, fadeIn: Int, time: Int, fadeOut: Int) {
        TODO()
    }

    object currentGui {
        /**
         * Gets the Java class name of the currently open gui, for example, "GuiChest"
         *
         * @return the class name of the current gui
         */
        @JvmStatic
        fun getClassName(): String = get()?.javaClass?.simpleName ?: "null"

        /**
         * Gets the Minecraft gui class that is currently open
         *
         * @return the Minecraft gui
         */
        @JvmStatic
        fun get(): Screen? = getMinecraft().currentScreen

        /**
         * Closes the currently open gui
         */
        @JvmStatic
        fun close() {
            getMinecraft().setScreen(null)
        }
    }

    object camera {
        @JvmStatic
        fun getX(): Double = getMinecraft().gameRenderer.camera.pos.x

        @JvmStatic
        fun getY(): Double = getMinecraft().gameRenderer.camera.pos.y

        @JvmStatic
        fun getZ(): Double = getMinecraft().gameRenderer.camera.pos.z
    }
}
