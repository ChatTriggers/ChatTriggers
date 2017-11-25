package com.chattriggers.ctjs.objects;

import com.chattriggers.ctjs.libs.MinecraftVars;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

public class TabList {
    /**
     * Gets names set in scoreboard objectives (useful for newer games)
     * @return The formatted names
     */
    public static List<String> getNamesByObjectives() {
        List<String> tabNames = new ArrayList<>();
        try {
            Scoreboard scoreboard = FMLClientHandler.instance().getClient().theWorld.getScoreboard();
            ScoreObjective sidebarObjective = scoreboard.getObjectiveInDisplaySlot(0);
            Collection<Score> scores = scoreboard.getSortedScores(sidebarObjective);
            for (Score score : scores) {
                ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
                String name = ScorePlayerTeam.formatPlayerName(team, score.getPlayerName());
                tabNames.add(name);
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
        return tabNames;
    }

    /**
     * Gets all names in tabs without formatting
     * @return the unformatted names
     */
    public static List<String> getNames() {
        List<String> names = new ArrayList<>();

        Ordering<NetworkPlayerInfo> tab = Ordering.from(new PlayerComparator());
        NetHandlerPlayClient nethandlerplayclient = Minecraft.getMinecraft().thePlayer.sendQueue;
        List<NetworkPlayerInfo> list = tab.sortedCopy(nethandlerplayclient.getPlayerInfoMap());

        for (NetworkPlayerInfo player : list) {
            names.add(player.getGameProfile().getName());
        }

        return names;
    }

    @SideOnly(Side.CLIENT)
    static class PlayerComparator implements Comparator<NetworkPlayerInfo> {
        private PlayerComparator(){}

        public int compare(NetworkPlayerInfo p_compare_1_, NetworkPlayerInfo p_compare_2_) {
            ScorePlayerTeam scoreplayerteam = p_compare_1_.getPlayerTeam();
            ScorePlayerTeam scoreplayerteam1 = p_compare_2_.getPlayerTeam();
            return ComparisonChain.start().compareTrueFirst(p_compare_1_.getGameType() != WorldSettings.GameType.SPECTATOR, p_compare_2_.getGameType() != WorldSettings.GameType.SPECTATOR).compare(scoreplayerteam != null ? scoreplayerteam.getRegisteredName() : "", scoreplayerteam1 != null ? scoreplayerteam1.getRegisteredName() : "").compare(p_compare_1_.getGameProfile().getName(), p_compare_2_.getGameProfile().getName()).result();
        }
    }

    //TODO decide if needed
    /**
     * Gets a list of the players in tabs list.
     * @return A string array containing the names of the players in the tabs list.
     *          If the player is in a single player world, returns an array containing
     *          the player's name.
     */
    public static ArrayList<String> getTabList() {
        if (Minecraft.getMinecraft().isSingleplayer()) return new ArrayList<>(Collections.singletonList(MinecraftVars.getPlayerName()));
        if (Minecraft.getMinecraft().getNetHandler() == null || Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap() == null) return null;

        ArrayList<String> playerNames = new ArrayList<>();

        for (NetworkPlayerInfo playerInfo : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
            playerNames.add(playerInfo.getGameProfile().getName());
        }

        return playerNames;
    }
}
