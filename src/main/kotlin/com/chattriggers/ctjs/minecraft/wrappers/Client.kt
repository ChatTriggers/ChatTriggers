package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.mixins.gui.GuiChatAccessor
import com.chattriggers.ctjs.mixins.MinecraftAccessor
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.listeners.ClientListener
import com.chattriggers.ctjs.minecraft.objects.keybind.KeyBind
import com.chattriggers.ctjs.minecraft.wrappers.inventory.Slot
import com.chattriggers.ctjs.utils.kotlin.asMixin
import gg.essential.api.utils.GuiUtil
import gg.essential.universal.UKeyboard
import gg.essential.universal.UMouse
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.GuiMainMenu
import net.minecraft.client.gui.GuiMultiplayer
import net.minecraft.client.gui.GuiNewChat
import net.minecraft.client.gui.GuiPlayerTabOverlay
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.multiplayer.ServerData
import net.minecraft.client.network.NetHandlerPlayClient
import net.minecraft.network.INetHandler
import net.minecraft.network.Packet
import kotlin.math.roundToInt

//#if MC<=11202
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.realms.RealmsBridge
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraft.client.multiplayer.WorldClient
//#else
//$$ import com.mojang.realmsclient.RealmsMainScreen
//$$ import gg.essential.universal.wrappers.message.UTextComponent
//$$ import net.minecraft.client.gui.screens.ConnectScreen
//$$ import net.minecraft.client.gui.screens.GenericDirtMessageScreen
//$$ import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
//$$ import net.minecraft.client.multiplayer.resolver.ServerAddress
//$$ import net.minecraft.network.chat.TranslatableComponent
//#endif

//#if FABRIC
//$$ import com.chattriggers.ctjs.mixins.gui.HandledScreenAccessor
//#endif

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
            //#if MC<=11202
            return (screen as? GuiContainer)?.slotUnderMouse?.let(::Slot)
            //#else
            //#if FORGE
            //$$ return (screen as? AbstractContainerScreen<*>)?.slotUnderMouse?.let(::Slot)
            //#else
            //$$ return (screen as? HandledScreen<*>)?.asMixin<HandledScreenAccessor>()?.focusedSlot?.let(::Slot)
            //#endif
            //#endif
        }

        /**
         * Closes the currently open gui
         */
        @JvmStatic
        fun close() {
            //#if MC<=11202
            Player.getPlayer()?.closeScreen()
            //#else
            //$$ Player.getPlayer()?.closeContainer()
            //#endif
        }
    }

    object camera {
        //#if MC<=11202
        @JvmStatic
        fun getX(): Double = Renderer.getRenderManager().viewerPosX

        @JvmStatic
        fun getY(): Double = Renderer.getRenderManager().viewerPosY

        @JvmStatic
        fun getZ(): Double = Renderer.getRenderManager().viewerPosZ
        //#else
        //$$ @JvmStatic
        //$$ fun getX(): Double = Renderer.getRenderManager().mainCamera.position.x
        //$$
        //$$ @JvmStatic
        //$$ fun getY(): Double = Renderer.getRenderManager().mainCamera.position.y
        //$$
        //$$ @JvmStatic
        //$$ fun getZ(): Double = Renderer.getRenderManager().mainCamera.position.z
        //#endif
    }

    companion object {
        @JvmStatic
        val settings = Settings()

        /**
         * Gets Minecraft's Minecraft object
         *
         * @return The Minecraft object
         */
        @JvmStatic
        fun getMinecraft(): Minecraft {
            //#if MC<=11202
            return Minecraft.getMinecraft()
            //#elseif MC>=11701
            //$$ return Minecraft.getInstance()
            //#endif
        }

        /**
         * Gets Minecraft's NetHandlerPlayClient object
         *
         * @return The NetHandlerPlayClient object
         */
        @JvmStatic
        fun getConnection(): NetHandlerPlayClient? {
            //#if MC<=11202
            return getMinecraft().netHandler
            //#elseif MC>=11701
            //$$ return getMinecraft().connection
            //#endif
        }

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
                //#if MC<=11202
                World.getWorld()?.sendQuittingDisconnectingPacket()
                getMinecraft().loadWorld(null as WorldClient?)

                when {
                    getMinecraft().isIntegratedServerRunning -> getMinecraft().displayGuiScreen(GuiMainMenu())
                    getMinecraft().isConnectedToRealms -> RealmsBridge().switchToRealms(GuiMainMenu())
                    else -> getMinecraft().displayGuiScreen(GuiMultiplayer(GuiMainMenu()))
                }
                //#else
                //$$ World.getWorld()?.disconnect()
                //$$ getMinecraft().clearLevel()
                //$$ // See PauseScreen::createPauseMenu
                //$$ if (getMinecraft().isLocalServer) {
                //$$     getMinecraft().clearLevel(GenericDirtMessageScreen(TranslatableComponent("menu.savingLevel")))
                //$$ } else {
                //$$     getMinecraft().clearLevel()
                //$$ }
                //$$
                //$$ if (getMinecraft().isLocalServer) {
                //$$     getMinecraft().setScreen(TitleScreen())
                //$$ } else if (getMinecraft().isConnectedToRealms) {
                //$$     getMinecraft().setScreen(RealmsMainScreen(TitleScreen()))
                //$$ } else {
                //$$     getMinecraft().setScreen(JoinMultiplayerScreen(TitleScreen()))
                //$$ }
                //#endif

            }
        }

        /**
         * Connects to the server with the given ip.
         * @param ip The ip to connect to
         */
        @JvmStatic
        fun connect(ip: String) {
            scheduleTask {
                //#if MC<=11202
                FMLClientHandler.instance().connectToServer(
                    GuiMultiplayer(GuiMainMenu()),
                    ServerData("Server", ip, false)
                )
                //#else
                //$$ ConnectScreen.startConnecting(
                //$$     JoinMultiplayerScreen(TitleScreen()),
                //$$     getMinecraft(),
                //$$     ServerAddress.parseString(ip),
                //$$     ServerData("Server", ip, false),
                //$$ )
                //#endif
            }
        }

        // TODO(Breaking) Renamed from GUI to Gui
        /**
         * Gets the Minecraft GuiNewChat object for the chat gui
         *
         * @return The GuiNewChat object for the chat gui
         */
        @JvmStatic
        fun getChatGui(): GuiNewChat? {
            //#if MC<=11202
            return getMinecraft().ingameGUI?.chatGUI
            //#else
            //$$ return getMinecraft().gui?.chat
            //#endif
        }

        @JvmStatic
        fun isInChat() = currentGui.get() is GuiChat

        @JvmStatic
        fun getTabGui(): GuiPlayerTabOverlay? {
            //#if MC<=11202
            return getMinecraft().ingameGUI?.tabList
            //#else
            //$$ return getMinecraft().gui?.tabList
            //#endif
        }

        @JvmStatic
        fun isInTab(): Boolean {
            //#if MC<=11202
            return getMinecraft().gameSettings.keyBindPlayerList.isKeyDown
            //#else
            //$$ return getMinecraft().options.keyPlayerList.isDown
            //#endif
        }

        /**
         * Gets whether the Minecraft window is active
         * and in the foreground of the user's screen.
         *
         * @return true if the game is active, false otherwise
         */
        @JvmStatic
        fun isTabbedIn(): Boolean {
            //#if MC<=11202
            return getMinecraft().inGameHasFocus
            //#elseif MC>=11701
            //$$ return getMinecraft().isWindowActive
            //#endif
        }

        @JvmStatic
        fun isControlDown(): Boolean = UKeyboard.isCtrlKeyDown()

        @JvmStatic
        fun isShiftDown(): Boolean = UKeyboard.isShiftKeyDown()

        @JvmStatic
        fun isAltDown(): Boolean = UKeyboard.isAltKeyDown()

        // TODO(BREAKING): Renamed from getFPS
        @JvmStatic
        fun getFps() = MinecraftAccessor.getFps()

        @JvmStatic
        fun getVersion(): String {
            //#if MC<=11202
            return getMinecraft().version
            //#else
            //$$ return getMinecraft().launchedVersion
            //#endif
        }

        @JvmStatic
        fun getMaxMemory(): Long = Runtime.getRuntime().maxMemory()

        @JvmStatic
        fun getTotalMemory(): Long = Runtime.getRuntime().totalMemory()

        @JvmStatic
        fun getFreeMemory(): Long = Runtime.getRuntime().freeMemory()

        @JvmStatic
        fun getMemoryUsage(): Int = ((getTotalMemory() - getFreeMemory()) * 100 / getMaxMemory().toFloat()).roundToInt()

        @JvmStatic
        fun getSystemTime(): Long = System.currentTimeMillis()

        @JvmStatic
        fun getMouseX(): Double = UMouse.Scaled.x

        @JvmStatic
        fun getMouseY(): Double = UMouse.Scaled.y

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
                val inputField = currentGui.get().asMixin<GuiChatAccessor>().inputField

                //#if MC<=11202
                inputField.text
                //#else
                //$$ inputField.value
                //#endif
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
                val inputField = currentGui.get().asMixin<GuiChatAccessor>().inputField

                //#if MC<=11202
                inputField.text = message
                //#else
                //$$ inputField.value = message
                //#endif
            } else GuiUtil.open(GuiChat(message))
        }

        @JvmStatic
        fun <T : INetHandler> sendPacket(packet: Packet<T>) {
            //#if MC<=11202
            getConnection()?.networkManager?.sendPacket(packet)
            //#else
            //$$ getConnection()?.connection?.send(packet)
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
            //#if MC<=11202
            val gui = getMinecraft().ingameGUI
            gui.displayTitle(ChatLib.addColor(title), null, fadeIn, time, fadeOut)
            gui.displayTitle(null, ChatLib.addColor(subtitle), fadeIn, time, fadeOut)
            gui.displayTitle(null, null, fadeIn, time, fadeOut)
            //#else
            //$$ val gui = getMinecraft().gui
            //$$ gui.setTimes(fadeIn, time, fadeOut)
            //$$ gui.setTitle(UTextComponent(ChatLib.addColor(title)))
            //$$ gui.setSubtitle(UTextComponent(ChatLib.addColor(subtitle)))
            //#endif
        }
    }
}
