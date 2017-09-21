package com.chattriggers.jsct.libs;

import net.minecraft.client.Minecraft;

public class MinecraftVars {
    public static String getPlayerName() {
        return Minecraft.getMinecraft().getSession().getUsername();
    }
}
