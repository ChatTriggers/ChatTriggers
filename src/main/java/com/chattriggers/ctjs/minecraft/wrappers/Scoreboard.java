package com.chattriggers.ctjs.minecraft.wrappers;

import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraftforge.client.GuiIngameForge;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A helper which reads the scoreboard.
 *
 * @author palechip
 */
public class Scoreboard {
    private static boolean needsUpdate = true;
    private static ArrayList<String> scoreboardNames;
    private static String scoreboardTitle;

    static {
        scoreboardNames = new ArrayList<>();
        scoreboardTitle = "";
    }

    // prevent instantiation
    private Scoreboard() {
    }

    /**
     * Used to make sure that we maximally update the scoreboard names once a tick.
     */
    public static void resetCache() {
        needsUpdate = true;
    }

    /**
     * Alias for {@link Scoreboard#getTitle()}.
     *
     * @return the scoreboard title
     */
    public static String getScoreboardTitle() {
        return getTitle();
    }

    /**
     * Gets the top-most string which is displayed on the scoreboard. (doesn't have a score on the side).<br>
     * Be aware that this can contain color codes.
     *
     * @return the scoreboard title
     */
    public static String getTitle() {
        if (needsUpdate) {
            updateNames();
            needsUpdate = false;
        }
        return scoreboardTitle;
    }

    /**
     * Get all currently visible strings on the scoreboard. (excluding title)<br>
     * Be aware that this can contain color codes.
     *
     * @return the list of lines
     */
    public static ArrayList<String> getLines() {
        // the array will only be updated upon request
        if (needsUpdate) {
            updateNames();
            needsUpdate = false;
        }
        return scoreboardNames;
    }

    /**
     * Gets the line at the specified index (0 based)
     * Equivalent to Scoreboard.getLines().get(index)
     *
     * @param index the line index
     * @return the string at the index
     */
    public static String getLine(int index) {
        return getLines().get(index);
    }

    /**
     * Sets whether the scoreboard should be rendered
     *
     * @param shouldRender whether the scoreboard should be rendered
     */
    public static void setShouldRender(boolean shouldRender) {
        GuiIngameForge.renderObjective = shouldRender;
    }

    /**
     * Gets if the scoreboard should be rendered
     *
     * @return whether the scoreboard whether the scoreboard should be rendered
     */
    public static boolean getShouldRender() {
        return GuiIngameForge.renderObjective;
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
            net.minecraft.scoreboard.Scoreboard scoreboard = World.getWorld().getScoreboard();
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