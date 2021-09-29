package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.objects.KeyBind
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.utils.kotlin.External
import gg.essential.universal.UKeyboard
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.GuiPlayerTabOverlay
import net.minecraft.client.gui.GuiMainMenu
import net.minecraft.client.gui.GuiMultiplayer
import net.minecraft.client.gui.GuiNewChat
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.client.network.NetHandlerPlayClient
import net.minecraft.network.INetHandler
import net.minecraft.network.Packet
import net.minecraft.realms.RealmsBridge
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.Display
import kotlin.math.roundToInt

@External
object Client {
    @JvmStatic
    val settings = Settings

    /**
     * Gets Minecraft's Minecraft object
     *
     * @return The Minecraft object
     */
    @JvmStatic
    fun getMinecraft(): Minecraft = Minecraft.getMinecraft()

    /**
     * Gets Minecraft's NetHandlerPlayClient object
     *
     * @return The NetHandlerPlayClient object
     */
    @JvmStatic
    fun getConnection(): NetHandlerPlayClient? =
        //#if MC<=10809
        getMinecraft().netHandler
        //#else
        //$$ getMinecraft().connection
        //#endif

    /**
     * Quits the client back to the main menu.
     * This acts just like clicking the "Disconnect" or "Save and quit to title" button.
     */
    @JvmStatic
    fun disconnect() {
        World.getWorld()?.sendQuittingDisconnectingPacket()
        getMinecraft().loadWorld(null as WorldClient?)

        when {
            getMinecraft().isIntegratedServerRunning -> getMinecraft().displayGuiScreen(GuiMainMenu())
            getMinecraft().isConnectedToRealms -> RealmsBridge().switchToRealms(GuiMainMenu())
            else -> getMinecraft().displayGuiScreen(GuiMultiplayer(GuiMainMenu()))
        }
    }

    /**
     * Gets the Minecraft GuiNewChat object for the chat gui
     *
     * @return The GuiNewChat object for the chat gui
     */
    @JvmStatic
    fun getChatGUI(): GuiNewChat? = getMinecraft().ingameGUI?.chatGUI

    @JvmStatic
    fun isInChat(): Boolean = getMinecraft().currentScreen is GuiChat

    @JvmStatic
    fun getTabGui(): GuiPlayerTabOverlay? = getMinecraft().ingameGUI?.tabList

    @JvmStatic
    fun isInTab(): Boolean = getMinecraft().gameSettings.keyBindPlayerList.isKeyDown

    /**
     * Gets whether or not the Minecraft window is active
     * and in the foreground of the user's screen.
     *
     * @return true if the game is active, false otherwise
     */
    @JvmStatic
    fun isTabbedIn(): Boolean = Display.isActive()

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
        return getMinecraft().gameSettings.keyBindings
            .firstOrNull {
                //#if MC==11602
                //$$ it.key.keyCode == keyCode
                //#else
                it.keyCode == keyCode
                //#endif
            }
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
    @JvmOverloads
    @JvmStatic
    fun getKeyBindFromKey(keyCode: Int, description: String, category: String  = "ChatTriggers"): KeyBind {
        return getMinecraft().gameSettings.keyBindings
                .firstOrNull {
                    //#if MC==11602
                    //$$ it.key.keyCode == keyCode
                    //#else
                    it.keyCode == keyCode
                    //#endif
                }
                ?.let { KeyBind(it) }
                ?: KeyBind(description, keyCode, category)
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

    @JvmStatic
    fun getFPS(): Int = Minecraft.getDebugFPS()

    @JvmStatic
    fun getVersion(): String = getMinecraft().version

    @JvmStatic
    fun getMaxMemory(): Long = Runtime.getRuntime().maxMemory()

    @JvmStatic
    fun getTotalMemory(): Long = Runtime.getRuntime().totalMemory()

    @JvmStatic
    fun getFreeMemory(): Long = Runtime.getRuntime().freeMemory()

    @JvmStatic
    fun getMemoryUsage(): Int = ((getTotalMemory() - getFreeMemory()) * 100 / getMaxMemory().toFloat()).roundToInt()

    @JvmStatic
    fun getSystemTime(): Long = Minecraft.getSystemTime()

    @JvmStatic
    fun getDisplayWidth(): Float {
        //#if MC==11602
        //$$ return getMinecraft().mainWindow.width.toFloat()
        //#else
        return getMinecraft().displayWidth.toFloat()
        //#endif
    }

    @JvmStatic
    fun getDisplayHeight(): Float {
        //#if MC==11602
        //$$ return getMinecraft().mainWindow.height.toFloat()
        //#else
        return getMinecraft().displayHeight.toFloat()
        //#endif
    }

    @JvmStatic
    fun getMouseX(): Float {
        //#if MC==11602
        //$$ val mx = getMinecraft().mouseHelper.mouseX.toFloat()
        //#else
        val mx = Mouse.getX().toFloat()
        //#endif
        val rw = Renderer.screen.getWidth().toFloat()
        return mx * rw / getDisplayWidth()
    }

    @JvmStatic
    fun getMouseY(): Float {
        //#if MC==11602
        //$$ val my = getMinecraft().mouseHelper.mouseY.toFloat()
        //#else
        val my = Mouse.getY().toFloat()
        //#endif
        val rh = Renderer.screen.getHeight().toFloat()
        return rh - my * rh / getDisplayHeight() - 1f
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
            val chatGui = getMinecraft().currentScreen as GuiChat
            chatGui.inputField.text
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
            val chatGui = getMinecraft().currentScreen as GuiChat
            chatGui.inputField.text = message
        } else Client.getMinecraft().displayGuiScreen(GuiChat(message))
    }

    @JvmStatic
    fun <T : INetHandler> sendPacket(packet: Packet<T>) {
        //#if MC==11602
        //$$ getMinecraft().connection?.networkManager?.sendPacket(packet)
        //#else
        getMinecraft().netHandler.networkManager.sendPacket(packet)
        //#endif
    }

    /**
     * Display a title.
     *
     * @param title    title text
     * @param subtitle subtitle text
     * @param fadeIn   time to fade in
     * @param time     time to stay on screen
     * @param fadeOut  time to fade out
     */
    @JvmStatic
    fun showTitle(title: String, subtitle: String, fadeIn: Int, time: Int, fadeOut: Int) {
        val gui = getMinecraft().ingameGUI
        //#if MC==11602
        //$$ gui.func_238452_a_(TextComponent(title).component, null, fadeIn, time, fadeOut)
        //$$ gui.func_238452_a_(null, TextComponent(title).component, fadeIn, time, fadeOut)
        //$$ gui.func_238452_a_(null, null, fadeIn, time, fadeOut)
        //#else
        gui.displayTitle(ChatLib.addColor(title), null, fadeIn, time, fadeOut)
        gui.displayTitle(null, ChatLib.addColor(subtitle), fadeIn, time, fadeOut)
        gui.displayTitle(null, null, fadeIn, time, fadeOut)
        //#endif
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
        fun get(): GuiScreen? = getMinecraft().currentScreen

        /**
         * Closes the currently open gui
         */
        @JvmStatic
        fun close() {
            Player.getPlayer()?.closeScreen()
        }
    }

    object camera {
        @JvmStatic
        fun getX(): Double = Client.getMinecraft().renderManager.viewerPosX

        @JvmStatic
        fun getY(): Double = Client.getMinecraft().renderManager.viewerPosY

        @JvmStatic
        fun getZ(): Double = Client.getMinecraft().renderManager.viewerPosZ
    }
}
