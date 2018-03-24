package com.chattriggers.ctjs.minecraft.wrappers;

import lombok.Getter;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraftforge.client.GuiIngameForge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A helper which reads the scoreboard.
 *
 * @author palechip
 */
public class Scoreboard {
    private static boolean needsUpdate = true;
    private static List<Score> scoreboardNames;
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
    public static List<Score> getLines() {
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
     * @return the score object at the index
     */
    public static Score getLineByIndex(int index) {
        return getLines().get(index);
    }

    /**
     * Gets a list of lines that have a certain score,
     * i.e. the numbers shown on the right
     *
     * @param score the score to look for
     * @return a list of actual score objects
     */
    public static List<Score> getLinesByScore(int score) {
        return getLines()
                .stream()
                .filter(theScore -> theScore.getPoints() == score)
                .collect(Collectors.toList());
    }

    /**
     * Sets a line in the scoreboard to the specified name and score.
     *
     * @param score the score value for this item
     * @param line the string to display on said line
     * @param override whether or not to remove old lines with the same score
     */
    public static void setLine(int score, String line, boolean override) {
        try {
            net.minecraft.scoreboard.Scoreboard scoreboard = World.getWorld().getScoreboard();

            ScoreObjective sidebarObjective = scoreboard.getObjectiveInDisplaySlot(1);

            Collection<net.minecraft.scoreboard.Score> scores = scoreboard.getSortedScores(sidebarObjective);

            if (override) {
                for (net.minecraft.scoreboard.Score theScore : scores) {
                    if (theScore.getScorePoints() == score) {
                        scoreboard.removeObjectiveFromEntity(theScore.getPlayerName(), sidebarObjective);
                    }
                }
            }

            net.minecraft.scoreboard.Score theScore = scoreboard.getValueFromObjective(line, sidebarObjective);

            theScore.setScorePoints(score);
        } catch (Exception ignored) { }
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
        if (!scoreboardNames.isEmpty()) {
            scoreboardNames.clear();
        }
        scoreboardTitle = "";

        try {
            net.minecraft.scoreboard.Scoreboard scoreboard = World.getWorld().getScoreboard();

            ScoreObjective sidebarObjective = scoreboard.getObjectiveInDisplaySlot(1);

            scoreboardTitle = sidebarObjective.getDisplayName();

            Collection<net.minecraft.scoreboard.Score> scores = scoreboard.getSortedScores(sidebarObjective);

            scoreboardNames = scores
                    .stream()
                    .map(Score::new)
                    .collect(Collectors.toList());
        } catch (Exception ignored) { }
    }

    public static class Score {
        @Getter
        private net.minecraft.scoreboard.Score score;

        private Score(net.minecraft.scoreboard.Score score) {
            this.score = score;
        }

        /**
         * Gets the score point value for this score,
         * i.e. the number on the right of the board
         *
         * @return the actual point value
         */
        public int getPoints() {
            return score.getScorePoints();
        }

        /**
         * Gets the display string of this score
         *
         * @return the display name
         */
        public String getName() {
            ScorePlayerTeam team = World.getWorld().getScoreboard().getPlayersTeam(score.getPlayerName());

            return ScorePlayerTeam.formatPlayerName(team, score.getPlayerName());
        }
    }
}