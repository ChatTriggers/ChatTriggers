package com.chattriggers.ctjs.minecraft.wrappers;

import com.chattriggers.ctjs.minecraft.libs.MinecraftVars;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A helper which reads the scoreboard.
 *
 * @author palechip
 */
public class ScoreboardReader {
    private static boolean needsUpdate = true;
    private static ArrayList<String> scoreboardNames;
    private static String scoreboardTitle;

    static {
        scoreboardNames = new ArrayList<>();
        scoreboardTitle = "";
    }

    // prevent instantiation
    private ScoreboardReader() {
    }

    /**
     * Used to make sure that we maximally update the scoreboard names once a tick.
     */
    public static void resetCache() {
        needsUpdate = true;
    }

    /**
     * Get the top-most string which is displayed on the scoreboard. (doesn't have a score on the side)
     * Be aware that this can contain color codes.
     */
    public static String getScoreboardTitle() {
        if (needsUpdate) {
            updateNames();
            needsUpdate = false;
        }
        return scoreboardTitle;
    }

    /**
     * Get all currently visible strings on the scoreboard. (excluding title)
     * Be aware that this can contain color codes.
     */
    public static ArrayList<String> getScoreboardNames() {
        // the array will only be updated upon request
        if (needsUpdate) {
            updateNames();
            needsUpdate = false;
        }
        return scoreboardNames;
    }

    private static void updateNames() {
        // All the magic happens here...

        // Clear the array
        if (!scoreboardNames.isEmpty()) {
            scoreboardNames.clear();
        }
        scoreboardTitle = "";

        try {
            // Get the scoreboard.
            Scoreboard scoreboard = MinecraftVars.getWorld().getScoreboard();
            // Get the right objective. I think the 1 stands for the sidebar objective but I've just copied it from the rendering code.
            ScoreObjective sidebarObjective = scoreboard.getObjectiveInDisplaySlot(1);
            // only update if there actually is something to update
            scoreboardTitle = sidebarObjective.getDisplayName();
            // Get a collection of all scores
            Collection<Score> scores = scoreboard.getSortedScores(sidebarObjective);
            // Process all scores
            for (Score score : scores) {
                // Get the team for the fake player
                ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
                // Add the nm to the array. formatPlayerName() is used to add prefix and suffix which are used to circumvent the 16 char limit for the nm.
                scoreboardNames.add(ScorePlayerTeam.formatPlayerName(team, score.getPlayerName()));

            }
        } catch (Exception e) {
            // it is possible that there is a null pointer exception thrown when there is no scoreboard
            // just ignore this
        }
    }
}