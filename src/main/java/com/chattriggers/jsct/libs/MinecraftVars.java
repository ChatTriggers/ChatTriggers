package com.chattriggers.jsct.libs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.util.ArrayList;
import java.util.Collections;

public class MinecraftVars {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static String getPlayerName() {
        return mc.getSession().getUsername();
    }

    public static String getPlayerUUID() {
        return mc.getSession().getPlayerID();
    }

    public static Float getPlayerHP() {
        return mc.thePlayer == null ? null : mc.thePlayer.getHealth();
    }

    public static Integer getPlayerHunger() {
        return mc.thePlayer == null ? null : mc.thePlayer.getFoodStats().getFoodLevel();
    }

    public static Float getPlayerSaturation() {
        return mc.thePlayer == null ? null : mc.thePlayer.getFoodStats().getSaturationLevel();
    }

    public static Integer getXPLevel() {
        return mc.thePlayer == null ? null : mc.thePlayer.experienceLevel;
    }

    public static Float getXPProgress() {
        return mc.thePlayer == null ? null : mc .thePlayer.experience;
    }

    public static boolean isInChat() {
        return mc.currentScreen instanceof GuiChat;
    }

    public static boolean isInTab() {
        return mc.gameSettings.keyBindPlayerList.isKeyDown();
    }

    public static String getServerName() {
        if (mc.isSingleplayer()) return "SinglePlayer";

        return mc.getCurrentServerData() == null ? null : mc.getCurrentServerData().serverName;
    }

    public static Long getPing() {
        if (mc.isSingleplayer()) return 5L;

        return mc.getCurrentServerData() == null ? null : mc.getCurrentServerData().pingToServer;
    }

    public static ArrayList<String> getTabList() {
        if (mc.isSingleplayer()) return new ArrayList<>(Collections.singletonList(getPlayerName()));
        if (mc.getNetHandler() == null || mc.getNetHandler().getPlayerInfoMap() == null) return null;

        ArrayList<String> playerNames = new ArrayList<>();

        for (NetworkPlayerInfo playerInfo : mc.getNetHandler().getPlayerInfoMap()) {
            playerNames.add(playerInfo.getGameProfile().getName());
        }

        return playerNames;
    }

    public static Double getPlayerPosX() {
        return mc.thePlayer == null ? null : mc.thePlayer.posX;
    }
    public static Double getPlayerPosY() {
        return mc.thePlayer == null ? null : mc.thePlayer.posY;
    }
    public static Double getPlayerPosZ() {
        return mc.thePlayer == null ? null : mc.thePlayer.posZ;
    }

    public static int getPlayerFPS() {
        return Minecraft.getDebugFPS();
    }
}
