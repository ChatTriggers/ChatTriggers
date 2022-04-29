package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.utils.kotlin.MCScore
import com.chattriggers.ctjs.utils.kotlin.MCScoreboard
import net.minecraft.scoreboard.ScoreObjective
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraftforge.client.GuiIngameForge

object Scoreboard {
    private var needsUpdate = true
    private var scoreboardNames = mutableListOf<Score>()
    private var scoreboardTitle = ""

    @JvmStatic
    fun getScoreboard(): MCScoreboard? {
        return World.getWorld()?.scoreboard
    }

    @JvmStatic
    fun getSidebar(): ScoreObjective? {
        return getScoreboard()?.getObjectiveInDisplaySlot(1)
    }

    /**
     * Alias for [getTitle].
     *
     * @return the scoreboard title
     */
    @JvmStatic
    fun getScoreboardTitle(): String = getTitle()

    /**
     * Gets the top-most string which is displayed on the scoreboard. (doesn't have a score on the side).
     * Be aware that this can contain color codes.
     *
     * @return the scoreboard title
     */
    @JvmStatic
    fun getTitle(): String {
        if (needsUpdate) {
            updateNames()
            needsUpdate = false
        }

        return scoreboardTitle
    }

    /**
     * Sets the scoreboard title.
     *
     * @param title the new title
     * @return the scoreboard title
     */
    @JvmStatic
    fun setTitle(title: String) {
        getSidebar()?.displayName = title
    }

    /**
     * Get all currently visible strings on the scoreboard. (excluding title)
     * Be aware that this can contain color codes.
     *
     * @return the list of lines
     */
    @JvmStatic
    @JvmOverloads
    fun getLines(descending: Boolean = true): List<Score> {
        // the array will only be updated upon request
        if (needsUpdate) {
            updateNames()
            needsUpdate = false
        }

        return if (descending) scoreboardNames else scoreboardNames.asReversed()
    }

    /**
     * Gets the line at the specified index (0 based)
     * Equivalent to Scoreboard.getLines().get(index)
     *
     * @param index the line index
     * @return the score object at the index
     */
    @JvmStatic
    fun getLineByIndex(index: Int): Score = getLines()[index]

    /**
     * Gets a list of lines that have a certain score,
     * i.e. the numbers shown on the right
     *
     * @param score the score to look for
     * @return a list of actual score objects
     */
    @JvmStatic
    fun getLinesByScore(score: Int): List<Score> = getLines().filter {
        it.getPoints() == score
    }

    /**
     * Sets a line in the scoreboard to the specified name and score.
     *
     * @param score the score value for this item
     * @param line the string to display on said line
     * @param override whether to remove old lines with the same score
     */
    @JvmStatic
    fun setLine(score: Int, line: String, override: Boolean) {
        val scoreboard = getScoreboard() ?: return
        val sidebarObjective = getSidebar() ?: return

        val scores: Collection<MCScore> = scoreboard.getSortedScores(sidebarObjective)

        if (override) {
            scores.filter {
                it.scorePoints == score
            }.forEach {
                scoreboard.removeObjectiveFromEntity(it.playerName, sidebarObjective)
            }
        }

        //#if MC<=10809
        val theScore = scoreboard.getValueFromObjective(line, sidebarObjective)!!
        //#else
        //$$ val theScore = scoreboard.getOrCreateScore(line, sidebarObjective)
        //#endif

        theScore.scorePoints = score
    }

    @JvmStatic
    fun setShouldRender(shouldRender: Boolean) {
        GuiIngameForge.renderObjective = shouldRender
    }

    @JvmStatic
    fun getShouldRender() = GuiIngameForge.renderObjective

    private fun updateNames() {
        scoreboardNames.clear()
        scoreboardTitle = ""

        val scoreboard = getScoreboard() ?: return
        val sidebarObjective = getSidebar() ?: return
        scoreboardTitle = sidebarObjective.displayName

        val scores: Collection<MCScore> = scoreboard.getSortedScores(sidebarObjective)

        scoreboardNames = scores.map(::Score).toMutableList()
    }

    @JvmStatic
    fun resetCache() {
        needsUpdate = true
    }

    class Score(val score: MCScore) {
        /**
         * Gets the score point value for this score,
         * i.e. the number on the right of the board
         *
         * @return the actual point value
         */
        fun getPoints(): Int = score.scorePoints

        /**
         * Gets the display string of this score
         *
         * @return the display name
         */
        fun getName(): String = ScorePlayerTeam.formatPlayerName(
            getScoreboard()!!.getPlayersTeam(score.playerName),
            score.playerName
        )

        override fun toString(): String = getName()
    }
}
