package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.mixins.MixinGuiChat
import com.chattriggers.ctjs.minecraft.objects.KeyBind
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.network.NetHandlerPlayClient
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.Display

object Client {
    /**
     * Gets the Minecraft object.
     *
     * @return the Minecraft object
     */
    @JvmStatic
    fun getMinecraft() = Minecraft.getMinecraft()!!

    /**
     * Gets the connection object.
     *
     * @return the connection object
     */
    @JvmStatic
    fun getConnection(): NetHandlerPlayClient =
        //#if MC<=10809
        getMinecraft().netHandler
        //#else
        //$$ getMinecraft().connection;
        //#endif

    /**
     * Gets the chat gui object.
     *
     * @return the chat gui object
     */
    @JvmStatic
    fun getChatGUI() = getMinecraft().ingameGUI.chatGUI!!

    /**
     * Returns true if the player has the chat open.
     *
     * @return true if the player has the chat open, false otherwise
     */
    @JvmStatic
    fun isInChat() = getMinecraft().currentScreen is GuiChat

    /**
     * Returns true if the player has the tab list open.
     *
     * @return true if the player has the tab list open, false otherwise
     */
    @JvmStatic
    fun isInTab() =  getMinecraft().gameSettings.keyBindPlayerList.isKeyDown

    /**
     * Gets whether or not the Minecraft window is active
     * and in the foreground of the user's screen.
     *
     * @return true if the game is active, false otherwise
     */
    @JvmStatic
    fun isTabbedIn() = Display.isActive()

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
        return getMinecraft().gameSettings.keyBindings
                .firstOrNull { it.keyCode == keyCode }
                ?.let { KeyBind(it) }
    }

    /**
     * Get the [KeyBind] from an already existing
     * Minecraft KeyBinding, else, return a new one.
     *
     * @param keyCode the keycode to search for, see Keyboard below. Ex. Keyboard.KEY_A
     * @return the [KeyBind] from a Minecraft KeyBinding, or null if one doesn't exist
     * @see [Keyboard](http://legacy.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html)
     */
    @JvmStatic
    fun getKeyBindFromKey(keyCode: Int, description: String): KeyBind {
        return getMinecraft().gameSettings.keyBindings
                .firstOrNull { it.keyCode == keyCode }
                ?.let { KeyBind(it) }
                ?: KeyBind(description, keyCode)
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
        return getMinecraft().gameSettings.keyBindings
                .firstOrNull { it.keyDescription == description }
                ?.let { KeyBind(it) }
    }

    /**
     * Gets the game's FPS count.
     *
     * @return The game's FPS count.
     */
    @JvmStatic
    fun getFPS() = Minecraft.getDebugFPS()

    /**
     * Gets the client minecraft version.
     *
     * @return The client minecraft version.
     */
    @JvmStatic
    fun getVersion() = getMinecraft().version

    /**
     * Gets the client's max memory.
     *
     * @return The client's max memory.
     */
    @JvmStatic
    fun getMaxMemory() = Runtime.getRuntime().maxMemory()

    /**
     * Gets the client's total memory.
     *
     * @return The client's total memory.
     */
    @JvmStatic
    fun getTotalMemory() = Runtime.getRuntime().totalMemory()

    /**
     * Gets the client's free memory.
     *
     * @return The client's free memory.
     */
    @JvmStatic
    fun getFreeMemory() = Runtime.getRuntime().freeMemory()

    /**
     * Gets the player's memory usage.
     *
     * @return The player's memory usage.
     */
    @JvmStatic
    fun getMemoryUsage() = Math.round(
            (getTotalMemory() - getFreeMemory()) * 100 / getMaxMemory().toFloat()
    )

    /**
     * Gets the system time.
     *
     * @return the system time
     */
    @JvmStatic
    fun getSystemTime() = Minecraft.getSystemTime()

    /**
     * Gets the mouse x location.
     *
     * @return the mouse x location
     */
    @JvmStatic
    fun getMouseX(): Float {
        val mx = Mouse.getX().toFloat()
        val rw = Renderer.screen.getWidth().toFloat()
        val dw = getMinecraft().displayWidth.toFloat()
        return mx * rw / dw
    }

    /**
     * Gets the mouse y location.
     *
     * @return the mouse y location
     */
    @JvmStatic
    fun getMouseY(): Float {
        val my = Mouse.getY().toFloat()
        val rh = Renderer.screen.getHeight().toFloat()
        val dh = getMinecraft().displayHeight.toFloat()
        return rh - my * rh / dh - 1f
    }

    @JvmStatic
    fun isInGui() = gui.get() != null

    /**
     * Gets the chat message currently typed into the chat gui.
     *
     * @return A blank string if the gui isn't open, otherwise, the message
     */
    @JvmStatic
    fun getCurrentChatMessage(): String {
        if (!isInChat()) {
            return ""
        }

        val chatGui = getMinecraft().currentScreen as MixinGuiChat
        return chatGui.inputField.text
    }

    /**
     * Sets the current chat message, if the chat gui is not open, one will be opened.
     *
     * @param message the message to put in the chat text box.
     */
    @JvmStatic
    fun setCurrentChatMessage(message: String) {
        if (!isInChat()) {
            Client.getMinecraft().displayGuiScreen(GuiChat(message))
            return
        }

        val chatGui = getMinecraft().currentScreen as MixinGuiChat
        chatGui.inputField.text = message
    }

    object gui {

        /**
         * Gets the Java class name of the currently open gui, for example, "GuiChest"
         *
         * @return the class name of the current gui
         */
        @JvmStatic
        fun getClassName() = get()?.javaClass?.simpleName ?: "null"

        /**
         * Gets the Minecraft gui class that is currently open
         *
         * @return the Minecraft gui
         */
        @JvmStatic
        fun get(): GuiScreen? = getMinecraft().currentScreen

        /**
         * Closes the currently open gui
         */
        @JvmStatic
        fun close() {
            getMinecraft().displayGuiScreen(null)
        }
    }

    object camera {
        /**
         * Gets the camera's x position
         * @return the camera's x position
         */
        @JvmStatic
        val x: Double
            get() = Client.getMinecraft().renderManager.viewerPosX

        /**
         * Gets the camera's y position
         * @return the camera's y position
         */
        @JvmStatic
        val y: Double
            get() = Client.getMinecraft().renderManager.viewerPosY

        /**
         * Gets the camera's z position
         * @return the camera's z position
         */
        @JvmStatic
        val z: Double
            get() = Client.getMinecraft().renderManager.viewerPosZ
    }
}