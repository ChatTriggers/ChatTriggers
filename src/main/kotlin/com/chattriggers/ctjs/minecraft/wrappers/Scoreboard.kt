package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.launch.mixins.asMixin
import com.chattriggers.ctjs.launch.mixins.transformers.ScoreboardAccessor
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCScore
import net.minecraft.scoreboard.Team
import net.minecraft.text.LiteralText
import net.minecraft.text.Text

@External
object Scoreboard {
    private var needsUpdate = true
    private var scoreboardNames = mutableListOf<Score>()
    private var scoreboardTitle = TextComponent("")
    var shouldRender = true

    /**
     * Alias for [Scoreboard.getTitle].
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

        return scoreboardTitle.getFormattedText()
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
        try {
            val scoreboard = World.getWorld()?.scoreboard ?: return
            val sidebarObjective = scoreboard.getObjectiveForSlot(1)
            val scores = scoreboard.getAllPlayerScores(sidebarObjective)

            if (override) {
                val playerObjectives = scoreboard.asMixin<ScoreboardAccessor>().playerObjectives

                scores.filter {
                    it.score == score
                }.forEach {
                    playerObjectives.remove(it.playerName)
                }
            }

            val theScore = scoreboard.getPlayerScore(line, sidebarObjective)
            theScore.score = score
        } catch (ignored: Exception) {
        }
    }

    private fun updateNames() {
        scoreboardNames.clear()
        scoreboardTitle = TextComponent("")

        try {
            val scoreboard = World.getWorld()?.scoreboard ?: return
            val sidebarObjective = scoreboard.getObjectiveForSlot(1) ?: return
            // val sidebarObjective = scoreboard.getObjectiveInDisplaySlot(1) ?: return
            scoreboardTitle = sidebarObjective.displayName?.let(::TextComponent) ?: return

            val scores = scoreboard.getAllPlayerScores(sidebarObjective)

            scoreboardNames = scores.map(::Score).toMutableList()
        } catch (ignored: Exception) {
        }
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
        fun getPoints(): Int = score.score

        /**
         * Gets the display string of this score
         *
         * @return the display name
         */
        // TODO(BREAKING): Return TextComponent instead of String
        fun getName() = TextComponent(Team.decorateName(
            World.getWorld()!!.scoreboard.getPlayerTeam(score.playerName),
            LiteralText(score.playerName),
        ))

        override fun toString(): String = getName().getFormattedText()
    }
}
