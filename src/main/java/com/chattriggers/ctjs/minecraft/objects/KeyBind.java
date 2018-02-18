package com.chattriggers.ctjs.minecraft.objects;

import com.chattriggers.ctjs.minecraft.wrappers.objects.Client;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;

public class KeyBind {
    private static ArrayList<KeyBind> keyBinds = new ArrayList<>();

    private KeyBinding keyBinding;
    private boolean isCustom;

    /**
     * Create a new key bind, editable in the user's controls.
     *
     * @param description what the key bind does
     * @param keyCode     the keycode which the key bind will respond to, see Keyboard below. Ex. Keyboard.KEY_A
     * @see <a href="http://legacy.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html">Keyboard</a>
     */
    public KeyBind(String description, int keyCode) {
        for (KeyBinding key : Client.getMinecraft().gameSettings.keyBindings) {
            if (key.getKeyCategory().equals("ChatTriggers") && key.getKeyDescription().equals(description)) {
                Client.getMinecraft().gameSettings.keyBindings =
                        ArrayUtils.removeElement(Client.getMinecraft().gameSettings.keyBindings, key);
                break;
            }
        }

        this.keyBinding = new KeyBinding(description, keyCode, "ChatTriggers");
        ClientRegistry.registerKeyBinding(this.keyBinding);

        keyBinds.add(this);
        isCustom = true;
    }

    public KeyBind(KeyBinding keyBinding) {
        this.keyBinding = keyBinding;
        isCustom = false;
    }

    /**
     * Returns true if the key is pressed (used for continuous querying).
     *
     * @return whether or not the key is pressed
     */
    public boolean isKeyDown() {
        return keyBinding.isKeyDown();
    }

    /**
     * Returns true on the initial key press. For continuous querying use {@link #isKeyDown()}.
     *
     * @return whether or not the key has just been pressed
     */
    public boolean isPressed() {
        return keyBinding.isPressed();
    }

    public static void clearKeyBinds() {
        for (KeyBind keyBind : keyBinds) {
            if (!keyBind.isCustom) continue;

            Client.getMinecraft().gameSettings.keyBindings =
                    ArrayUtils.removeElement(Client.getMinecraft().gameSettings.keyBindings, keyBind.keyBinding);
        }
    }
}
