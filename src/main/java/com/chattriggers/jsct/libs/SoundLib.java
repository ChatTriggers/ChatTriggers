package com.chattriggers.jsct.libs;

import net.minecraft.client.Minecraft;

public class SoundLib {
    public static void playSound(String name, float volume, float pitch) {
        Minecraft.getMinecraft().thePlayer.playSound(name, volume, pitch);
    }
}
