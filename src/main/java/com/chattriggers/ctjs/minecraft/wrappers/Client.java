package com.chattriggers.ctjs.minecraft.wrappers;

import com.chattriggers.ctjs.minecraft.libs.RenderLib;
import com.chattriggers.ctjs.minecraft.objects.KeyBind;
import com.chattriggers.ctjs.utils.console.Console;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.lang.reflect.Field;

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
        return getMinecraft().getNetHandler();
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
     * Gets whether or not the minecraft window is active
     * and in the foreground of the user's screen.
     *
     * @return true if the game is active, false otherwise
     */
    public static boolean isTabbedIn() {
        return Display.isActive();
    }

    /**
     * Get the {@link KeyBind} from an already existing
     * Minecraft KeyBinding.
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
        for (KeyBinding keyBinding : getMinecraft().gameSettings.keyBindings) {
            if (keyBinding.getKeyCode() == keyCode) {
                return new KeyBind(keyBinding);
            }
        }

        return new KeyBind(description, keyCode);
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
        float rw = (float) RenderLib.getRenderWidth();
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
        float rh = (float) RenderLib.getRenderHeight();
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

        GuiChat chatGui = ((GuiChat) getMinecraft().currentScreen);

        try {
            GuiTextField textField = ReflectionHelper.getPrivateValue(GuiChat.class, chatGui, "inputField", "field_146415_a");

            return textField.getText();
        } catch (Exception e) {
            Console.getConsole().printStackTrace(e);
        }

        return "";
    }

    public static void setCurrentChatMessage(String message) {
        if (!isInChat()) {
            Client.getMinecraft().displayGuiScreen(new GuiChat(message));
            return;
        }

        GuiChat chatGui = ((GuiChat) getMinecraft().currentScreen);

        try {
            Field inputField = chatGui.getClass().getDeclaredField("");
            inputField.setAccessible(true);

            ReflectionHelper.setPrivateValue(GuiChat.class, chatGui, message, "inputField", "field_146415_a");
        } catch (Exception e) {
            Console.getConsole().printStackTrace(e);
        }
    }

    public static class gui {
        public static GuiScreen get() {
            return getMinecraft().currentScreen;
        }

        public static String getType() {
            return get() == null ? "null" : get().getClass().getSimpleName();
        }

        public static void close() {
            getMinecraft().displayGuiScreen(null);
        }
    }
}
