package com.chattriggers.ctjs.libs;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@UtilityClass
@SideOnly(Side.CLIENT)
public class WorldLib {
    /**
     * Play a sound at the player location.
     * @param name the name of the sound
     * @param volume the volume of the sound
     * @param pitch the pitch of the sound
     */
    public static void playSound(String name, float volume, float pitch) {
        Minecraft.getMinecraft().thePlayer.playSound(name, volume, pitch);
    }

    /**
     * Display a title.
     * @param title title text
     * @param subtitle subtitle text
     * @param fadeIn time to fade in
     * @param time time to stay on screen
     * @param fadeOut time to fade out
     */
    public static void showTitle(String title, String subtitle, int fadeIn, int time, int fadeOut) {
        Minecraft.getMinecraft().ingameGUI.displayTitle(ChatLib.addColor(title), null, fadeIn, time, fadeOut);
        Minecraft.getMinecraft().ingameGUI.displayTitle(null, ChatLib.addColor(subtitle), 0, 0, 0);
        Minecraft.getMinecraft().ingameGUI.displayTitle(null, null, fadeIn, time, fadeOut);
    }
}
