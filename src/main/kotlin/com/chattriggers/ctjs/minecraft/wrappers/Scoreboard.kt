package com.chattriggers.ctjs.minecraft.wrappers

import gg.essential.universal.wrappers.message.UTextComponent
import net.minecraft.scoreboard.ScoreObjective
import net.minecraft.scoreboard.ScorePlayerTeam

//#if FORGE
import net.minecraftforge.client.GuiIngameForge
//#if MC>=11701
//$$ import net.minecraftforge.client.gui.OverlayRegistry
//#endif
//#endif

object Scoreboard {
    private var needsUpdate = true
    private var scoreboardNames = mutableListOf<Score>()
    private var scoreboardTitle = UTextComponent("")
    //#if FABRIC
    //$$ @JvmStatic
    //$$ internal var shouldRender = true
    //#endif

    @JvmStatic
    fun getScoreboard(): net.minecraft.scoreboard.Scoreboard? {
        return World.getWorld()?.scoreboard
    }

    @JvmStatic
    fun getSidebar(): ScoreObjective? {
        //#if MC<=11202
        return getScoreboard()?.getObjectiveInDisplaySlot(1)
        //#else
        //$$ return getScoreboard()?.getDisplayObjective(1)
        //#endif
    }

    // TODO(BREAKING): Removed this
    // /**
    //  * Alias for [getTitle].
    //  *
    //  * @return the scoreboard title
    //  */
    // @JvmStatic
    // fun getScoreboardTitle(): UTextComponent = getTitle()

    /**
     * Gets the top-most string which is displayed on the scoreboard. (doesn't have a score on the side).
     * Be aware that this can contain color codes.
     *
     * @return the scoreboard title
     */
    // TODO(BREAKING): Return UTextComponent
    @JvmStatic
    fun getTitle(): UTextComponent {
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
    // TODO(BREAKING): Change parameter type to UTextComponent
    @JvmStatic
    fun setTitle(title: UTextComponent) {
        //#if MC<=11202
        getSidebar()?.displayName = title.formattedText
        //#else
        //$$ getSidebar()?.displayName = title
        //#endif
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

        //#if MC<=11202
        val scores = scoreboard.getSortedScores(sidebarObjective)

        if (override) {
            scores.filter {
                it.scorePoints == score
            }.forEach {
                scoreboard.removeObjectiveFromEntity(it.playerName, sidebarObjective)
            }
        }

        scoreboard.getValueFromObjective(line, sidebarObjective)!!.scorePoints = score
        //#else
        //$$ val scores = scoreboard.getPlayerScores(sidebarObjective)
        //$$
        //$$ if (override) {
        //$$     scores.filter {
        //$$         it.score == score
        //$$     }.forEach {
        //$$         scoreboard.resetPlayerScore(it.owner, sidebarObjective)
        //$$     }
        //$$ }
        //$$
        //$$ scoreboard.getOrCreatePlayerScore(line, sidebarObjective).score = score
        //#endif
    }

    //#if FORGE
    @JvmStatic
    fun getShouldRender(): Boolean {
        //#if MC<=11202
        return GuiIngameForge.renderObjective
        //#else
        //$$ return OverlayRegistry.getEntry(ForgeIngameGui.SCOREBOARD_ELEMENT)?.isEnabled ?: false
        //#endif
    }

    @JvmStatic
    fun setShouldRender(shouldRender: Boolean) {
        //#if MC<=11202
        GuiIngameForge.renderObjective = shouldRender
        //#else
        //$$ OverlayRegistry.enableOverlay(ForgeIngameGui.SCOREBOARD_ELEMENT, shouldRender)
        //#endif
    }
    //#endif

    private fun updateNames() {
        scoreboardNames.clear()
        scoreboardTitle = UTextComponent("")

        val scoreboard = getScoreboard() ?: return
        val sidebarObjective = getSidebar() ?: return
        scoreboardTitle = UTextComponent(sidebarObjective.displayName)

        //#if MC<=11202
        val scores = scoreboard.getSortedScores(sidebarObjective)
        //#else
        //$$ val scores = scoreboard.getPlayerScores(sidebarObjective)
        //#endif

        scoreboardNames = scores.map(::Score).toMutableList()
    }

    @JvmStatic
    fun resetCache() {
        needsUpdate = true
    }

    class Score(val score: net.minecraft.scoreboard.Score) {
        /**
         * Gets the score point value for this score,
         * i.e. the number on the right of the board
         *
         * @return the actual point value
         */
        fun getPoints(): Int {
            //#if MC<=11202
            return score.scorePoints
            //#else
            //$$ return score.score
            //#endif
        }

        /**
         * Gets the display string of this score
         *
         * @return the display name
         */
        // TODO(BREAKING): Return UTextComponent
        fun getName(): UTextComponent {
            //#if MC<=11202
            return UTextComponent(ScorePlayerTeam.formatPlayerName(
                getScoreboard()!!.getPlayersTeam(score.playerName),
                score.playerName
            ))
            //#else
            //$$ return UTextComponent(PlayerTeam.formatNameForTeam(
            //$$     getScoreboard()!!.getPlayersTeam(score.owner),
            //$$     UTextComponent(score.owner),
            //$$ ))
            //#endif
        }

        override fun toString(): String = getName().formattedText
    }
}
