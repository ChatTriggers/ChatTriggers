package com.chattriggers.ctjs.objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.apache.commons.lang3.ArrayUtils;

public class KeyBind {
    private KeyBinding keyBinding;

    /**
     * Create a new key bind, editable in the user's controls.
     * @param description what the key bind does
     * @param keyCode the keycode which the key bind will respond to, see Keyboard below. Ex. Keyboard.KEY_A
     * @see <a href="http://legacy.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html">Keyboard</a>
     */
    public KeyBind(String description, int keyCode) {
        KeyBinding foundKey = null;

        for (KeyBinding key : Minecraft.getMinecraft().gameSettings.keyBindings) {
            if (key.getKeyCategory().equals("ChatTriggers") && key.getKeyDescription().equals(description)) {
                this.keyBinding = key;
                break;
            }
        }

        if (this.keyBinding == null) {
            this.keyBinding = new KeyBinding(description, keyCode, "ChatTriggers");
            ClientRegistry.registerKeyBinding(this.keyBinding);
        }
    }

    /**
     * Returns true if the key is pressed (used for continuous querying).
     * @return whether or not the key is pressed
     */
    public boolean isKeyDown() {
        return keyBinding.isKeyDown();
    }

    /**
     * Returns true on the initial key press. For continuous querying use {@link #isKeyDown()}.
     * @return whether or not the key has just been pressed
     */
    public boolean isPressed() {
        return keyBinding.isPressed();
    }
}
