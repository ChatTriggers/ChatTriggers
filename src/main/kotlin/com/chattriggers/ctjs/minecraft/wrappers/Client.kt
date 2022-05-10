package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.listeners.ClientListener
import com.chattriggers.ctjs.minecraft.objects.keybind.KeyBind
import com.chattriggers.ctjs.minecraft.wrappers.inventory.Slot
import gg.essential.api.utils.GuiUtil
import gg.essential.universal.UMinecraft
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.*
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.multiplayer.ServerData
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.client.network.NetHandlerPlayClient
import net.minecraft.network.INetHandler
import net.minecraft.network.Packet
import net.minecraft.realms.RealmsBridge
import net.minecraftforge.fml.client.FMLClientHandler
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.Display
import kotlin.math.roundToInt

abstract class Client {
    /**
     * Get the [KeyBind] from an already existing Minecraft KeyBinding, otherwise, returns null.
     *
     * @param keyCode the keycode to search for, see Keyboard below. Ex. Keyboard.KEY_A
     * @return the [KeyBind] from a Minecraft KeyBinding, or null if one doesn't exist
     * @see [org.lwjgl.input.Keyboard](http://legacy.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html)
     */
    abstract fun getKeyBindFromKey(keyCode: Int): KeyBind?

    /**
     * Get the [KeyBind] from an already existing Minecraft KeyBinding, else, return a new one.
     *
     * @param keyCode the keycode which the keybind will respond to, see Keyboard below. Ex. Keyboard.KEY_A
     * @param description the description of the keybind
     * @param category the keybind category the keybind will be in
     * @return the [KeyBind] from a Minecraft KeyBinding, or a new one if one doesn't exist
     * @see [org.lwjgl.input.Keyboard](http://legacy.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html)
     */
    abstract fun getKeyBindFromKey(keyCode: Int, description: String, category: String): KeyBind

    /**
     * Get the [KeyBind] from an already existing Minecraft KeyBinding, else, return a new one.
     * This will create the [KeyBind] with the default category "ChatTriggers".
     *
     * @param keyCode the keycode to search for, see Keyboard below. Ex. Keyboard.KEY_A
     * @param description the description of the keybind
     * @return the [KeyBind] from a Minecraft KeyBinding, or a new one if one doesn't exist
     * @see [org.lwjgl.input.Keyboard](http://legacy.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html)
     */
    abstract fun getKeyBindFromKey(keyCode: Int, description: String): KeyBind

    /**
     * Get the [KeyBind] from an already existing
     * Minecraft KeyBinding, otherwise, returns null.
     *
     * @param description the description of the keybind
     * @return the [KeyBind], or null if one doesn't exist
     */
    abstract fun getKeyBindFromDescription(description: String): KeyBind?

    companion object {
        @JvmStatic
        val settings = Settings()

        /**
         * Gets Minecraft's Minecraft object
         *
         * @return The Minecraft object
         */
        @JvmStatic
        fun getMinecraft() = UMinecraft.getMinecraft()

        /**
         * Gets Minecraft's NetHandlerPlayClient object
         *
         * @return The NetHandlerPlayClient object
         */
        @JvmStatic
        fun getConnection() = UMinecraft.getNetHandler()

        /**
         * Schedule's a task to run on Minecraft's main thread in [delay] ticks.
         * Defaults to the next tick.
         * @param delay The delay in ticks
         * @param callback The task to run on the main thread
         */
        @JvmOverloads
        @JvmStatic
        fun scheduleTask(delay: Int = 0, callback: () -> Unit) {
            ClientListener.addTask(delay, callback)
        }

        /**
         * Quits the client back to the main menu.
         * This acts just like clicking the "Disconnect" or "Save and quit to title" button.
         */
        @JvmStatic
        fun disconnect() {
            scheduleTask {
                World.getWorld()?.sendQuittingDisconnectingPacket()
                getMinecraft().loadWorld(null as WorldClient?)

                when {
                    getMinecraft().isIntegratedServerRunning -> getMinecraft().displayGuiScreen(GuiMainMenu())
                    getMinecraft().isConnectedToRealms -> RealmsBridge().switchToRealms(GuiMainMenu())
                    else -> getMinecraft().displayGuiScreen(GuiMultiplayer(GuiMainMenu()))
                }
            }
        }

        /**
         * Connects to the server with the given ip.
         * @param ip The ip to connect to
         */
        @JvmStatic
        fun connect(ip: String) {
            scheduleTask {
                FMLClientHandler.instance().connectToServer(
                    GuiMultiplayer(GuiMainMenu()),
                    ServerData("Server", ip, false)
                )
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
        fun isInChat(): Boolean = GuiUtil.getOpenedScreen() is GuiChat

        @JvmStatic
        fun getTabGui(): GuiPlayerTabOverlay? = getMinecraft().ingameGUI?.tabList

        @JvmStatic
        fun isInTab(): Boolean = getMinecraft().gameSettings.keyBindPlayerList.isKeyDown

        /**
         * Gets whether the Minecraft window is active
         * and in the foreground of the user's screen.
         *
         * @return true if the game is active, false otherwise
         */
        @JvmStatic
        fun isTabbedIn(): Boolean = Display.isActive()

        @JvmStatic
        fun isControlDown(): Boolean = GuiScreen.isCtrlKeyDown()

        @JvmStatic
        fun isShiftDown(): Boolean = GuiScreen.isShiftKeyDown()

        @JvmStatic
        fun isAltDown(): Boolean = GuiScreen.isAltKeyDown()

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
        fun getMouseX(): Float {
            val mx = Mouse.getX().toFloat()
            val rw = Renderer.screen.getWidth().toFloat()
            val dw = getMinecraft().displayWidth.toFloat()
            return mx * rw / dw
        }

        @JvmStatic
        fun getMouseY(): Float {
            val my = Mouse.getY().toFloat()
            val rh = Renderer.screen.getHeight().toFloat()
            val dh = getMinecraft().displayHeight.toFloat()
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
            } else getMinecraft().displayGuiScreen(GuiChat(message))
        }

        @JvmStatic
        fun <T : INetHandler> sendPacket(packet: Packet<T>) {
            //#if MC<=10809
            getConnection()?.networkManager?.sendPacket(packet)
            //#else
            //$$getConnection()?.networkManager?.sendPacket(packet)
            //#endif
        }

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
            val gui = getMinecraft().ingameGUI
            gui.displayTitle(ChatLib.addColor(title), null, fadeIn, time, fadeOut)
            gui.displayTitle(null, ChatLib.addColor(subtitle), fadeIn, time, fadeOut)
            gui.displayTitle(null, null, fadeIn, time, fadeOut)
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
            fun get(): GuiScreen? = GuiUtil.getOpenedScreen()

            /**
             * Gets the slot under the mouse in the current gui, if one exists.
             *
             * @return the [Slot] under the mouse
             */
            @JvmStatic
            fun getSlotUnderMouse(): Slot? {
                val screen: GuiScreen? = get()
                return if ((screen is GuiContainer) && (screen.slotUnderMouse != null)) {
                    Slot(screen.slotUnderMouse)
                } else null
            }

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
            fun getX(): Double = getMinecraft().renderManager.viewerPosX

            @JvmStatic
            fun getY(): Double = getMinecraft().renderManager.viewerPosY

            @JvmStatic
            fun getZ(): Double = getMinecraft().renderManager.viewerPosZ
        }
    }
}
