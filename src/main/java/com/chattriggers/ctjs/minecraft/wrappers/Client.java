package com.chattriggers.ctjs.minecraft.wrappers;

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
import com.chattriggers.ctjs.minecraft.mixins.MixinGuiChat;
import com.chattriggers.ctjs.minecraft.objects.KeyBind;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.util.Arrays;

public class Client {
    /**
     * Gets the Minecraft object.
     *
     * @return the Minecraft object
     */
    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    /**
     * Gets the connection object.
     *
     * @return the connection object
     */
    public static NetHandlerPlayClient getConnection() {
        //#if MC<=10809
        return getMinecraft().getNetHandler();
        //#else
        //$$ return getMinecraft().getConnection();
        //#endif
    }

    /**
     * Gets the chat gui object.
     *
     * @return the chat gui object
     */
    public static GuiNewChat getChatGUI() {
        return getMinecraft().ingameGUI.getChatGUI();
    }

    /**
     * Returns true if the player has the chat open.
     *
     * @return true if the player has the chat open, false otherwise
     */
    public static boolean isInChat() {
        return getMinecraft().currentScreen instanceof GuiChat;
    }

    /**
     * Returns true if the player has the tab list open.
     *
     * @return true if the player has the tab list open, false otherwise
     */
    public static boolean isInTab() {
        return getMinecraft().gameSettings.keyBindPlayerList.isKeyDown();
    }

    /**
     * Gets whether or not the Minecraft window is active
     * and in the foreground of the user's screen.
     *
     * @return true if the game is active, false otherwise
     */
    public static boolean isTabbedIn() {
        return Display.isActive();
    }

    /**
     * Get the {@link KeyBind} from an already existing
     * Minecraft KeyBinding, otherwise, returns null.
     *
     * @param keyCode the keycode to search for, see Keyboard below. Ex. Keyboard.KEY_A
     * @return the {@link KeyBind} from a Minecraft KeyBinding, or null if one doesn't exist
     * @see <a href="http://legacy.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html">Keyboard</a>
     */
    public static KeyBind getKeyBindFromKey(int keyCode) {
        for (KeyBinding keyBinding : getMinecraft().gameSettings.keyBindings) {
            if (keyBinding.getKeyCode() == keyCode) {
                return new KeyBind(keyBinding);
            }
        }

        return null;
    }

    /**
     * Get the {@link KeyBind} from an already existing
     * Minecraft KeyBinding, else, return a new one.
     *
     * @param keyCode the keycode to search for, see Keyboard below. Ex. Keyboard.KEY_A
     * @return the {@link KeyBind} from a Minecraft KeyBinding, or null if one doesn't exist
     * @see <a href="http://legacy.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html">Keyboard</a>
     */
    public static KeyBind getKeyBindFromKey(int keyCode, String description) {
        return Arrays.stream(getMinecraft().gameSettings.keyBindings)
                .filter(keyBinding -> keyBinding.getKeyCode() == keyCode)
                .findAny()
                .map(KeyBind::new)
                .orElse(new KeyBind(description, keyCode));
    }

    /**
     * Get the {@link KeyBind} from an already existing
     * Minecraft KeyBinding, else, null.
     *
     * @param description the key binding's original description
     * @return the key bind, or null if one doesn't exist
     */
    public static KeyBind getKeyBindFromDescription(String description) {
        return Arrays.stream(getMinecraft().gameSettings.keyBindings)
                .filter(keyBinding -> keyBinding.getKeyDescription().equals(description))
                .findAny()
                .map(KeyBind::new)
                .orElse(null);
    }

    /**
     * Gets the game's FPS count.
     *
     * @return The game's FPS count.
     */
    public static int getFPS() {
        return Minecraft.getDebugFPS();
    }

    /**
     * Gets the player's minecraft version.
     *
     * @return The player's minecraft version.
     */
    public static String getVersion() {
        return getMinecraft().getVersion();
    }

    /**
     * Gets the player's max memory.
     *
     * @return The player's max memory.
     */
    public static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    /**
     * Gets the player's total memory.
     *
     * @return The player's total memory.
     */
    public static long getTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    /**
     * Gets the player's free memory.
     *
     * @return The player's free memory.
     */
    public static long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    /**
     * Gets the player's memory usage.
     *
     * @return The player's memory usage.
     */
    public static int getMemoryUsage() {
        return Math.round((getTotalMemory() - getFreeMemory()) * 100 / getMaxMemory());
    }

    /**
     * Gets the system time.
     *
     * @return the system time
     */
    public static Long getSystemTime() {
        return Minecraft.getSystemTime();
    }

    /**
     * Gets the mouse x location.
     *
     * @return the mouse x location
     */
    public static float getMouseX() {
        float mx = (float) Mouse.getX();
        float rw = (float) Renderer.screen.getWidth();
        float dw = (float) getMinecraft().displayWidth;
        return mx * rw / dw;
    }

    /**
     * Gets the mouse y location.
     *
     * @return the mouse y location
     */
    public static float getMouseY() {
        float my = (float) Mouse.getY();
        float rh = (float) Renderer.screen.getHeight();
        float dh = (float) getMinecraft().displayHeight;
        return rh - my * rh / dh - 1L;
    }

    public static boolean isInGui() {
        return gui.get() != null;
    }

    /**
     * Gets the chat message currently typed into the chat gui.
     *
     * @return A blank string if the gui isn't open, otherwise, the message
     */
    public static String getCurrentChatMessage() {
        if (!isInChat()) {
            return "";
        }

        MixinGuiChat chatGui = (MixinGuiChat) getMinecraft().currentScreen;
        return chatGui.getInputField().getText();
    }

    /**
     * Sets the current chat message, if the chat gui is not open, one will be opened.
     *
     * @param message the message to put in the chat text box.
     */
    public static void setCurrentChatMessage(String message) {
        if (!isInChat()) {
            Client.getMinecraft().displayGuiScreen(new GuiChat(message));
            return;
        }

        MixinGuiChat chatGui = (MixinGuiChat) getMinecraft().currentScreen;
        chatGui.getInputField().setText(message);
    }

    public static class gui {
        /**
         * Gets the Minecraft gui class that is currently open
         *
         * @return the Minecraft gui
         */
        public static GuiScreen get() {
            return getMinecraft().currentScreen;
        }

        /**
         * Gets the Java class name of the currently open gui, for example, "GuiChest"
         *
         * @return the class name of the current gui
         */
        public static String getClassName() {
            return get() == null ? "null" : get().getClass().getSimpleName();
        }

        /**
         * Closes the currently open gui
         */
        public static void close() {
            getMinecraft().displayGuiScreen(null);
        }
    }

    public static class camera {
        /**
         * Gets the camera's x position
         * @return the camera's x position
         */
        public static double getX() {
            return Client.getMinecraft().getRenderManager().viewerPosX;
        }

        /**
         * Gets the camera's y position
         * @return the camera's y position
         */
        public static double getY() {
            return Client.getMinecraft().getRenderManager().viewerPosY;
        }

        /**
         * Gets the camera's z position
         * @return the camera's z position
         */
        public static double getZ() {
            return Client.getMinecraft().getRenderManager().viewerPosZ;
        }
    }
}
