package com.chattriggers.jsct.libs;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;

@UtilityClass
public class WorldLib {
    public static void playSound(String name, float volume, float pitch) {
        Minecraft.getMinecraft().thePlayer.playSound(name, volume, pitch);
    }
}
