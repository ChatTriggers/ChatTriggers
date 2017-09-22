package com.chattriggers.jsct.libs;

import net.minecraft.client.Minecraft;

public class MinecraftVars {
    public static String getPlayerName() {
        return Minecraft.getMinecraft().getSession().getUsername();
    }

    public static Double getPlayerPosX() {
        if (Minecraft.getMinecraft().thePlayer != null)
            return Minecraft.getMinecraft().thePlayer.getPositionVector().xCoord;
        return 0d;
    }
    public static Double getPlayerPosY() {
        if (Minecraft.getMinecraft().thePlayer != null)
            return Minecraft.getMinecraft().thePlayer.getPositionVector().yCoord;
        return 0d;
    }
    public static Double getPlayerPosZ() {
        if (Minecraft.getMinecraft().thePlayer != null)
            return Minecraft.getMinecraft().thePlayer.getPositionVector().zCoord;
        return 0d;
    }

    public static int getPlayerFPS() {
        return Minecraft.getDebugFPS();
    }
}
