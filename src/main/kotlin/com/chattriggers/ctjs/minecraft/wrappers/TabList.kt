package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.launch.mixins.transformers.entity.PlayerTabOverlayAccessor
import com.chattriggers.ctjs.utils.kotlin.asMixin
import com.google.common.collect.ComparisonChain
import com.google.common.collect.Ordering
import gg.essential.universal.wrappers.message.UMessage
import gg.essential.universal.wrappers.message.UTextComponent
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraft.util.IChatComponent

//#if MC<=11202
import net.minecraft.world.WorldSettings
//#elseif MC>=11701
//$$ import net.minecraft.world.level.GameType
//#endif

object TabList {
    private val playerComparator = Ordering.from(PlayerComparator())

    /**
     * Gets names set in scoreboard objectives
     *
     * @return The formatted names
     */
    // TODO(BREAKING): Returns UTextComponents instead of Strings
    @JvmStatic
    fun getNamesByObjectives(): List<UTextComponent> {
        val scoreboard = Scoreboard.getScoreboard() ?: return emptyList()

        //#if MC<=11202
        val sidebarObjective = scoreboard.getObjectiveInDisplaySlot(0) ?: return emptyList()
        val scores = scoreboard.getSortedScores(sidebarObjective)

        return scores.map {
            val team = scoreboard.getPlayersTeam(it.playerName)
            UTextComponent(ScorePlayerTeam.formatPlayerName(team, it.playerName))
        }
        //#else
        //$$ val sidebarObjective = scoreboard.getDisplayObjective(0) ?: return emptyList()
        //$$ val scores = scoreboard.getPlayerScores(sidebarObjective)
        //$$
        //$$ return scores.map {
        //$$     val team = scoreboard.getPlayersTeam(it.owner)
        //$$     UTextComponent(PlayerTeam.formatNameForTeam(team, UTextComponent(it.owner)))
        //$$ }
        //#endif
    }

    // TODO(BREAKING): Returns UTextComponents instead of Strings
    @JvmStatic
    fun getNames(): List<UTextComponent> {
        if (Client.getTabGui() == null) return listOf()

        //#if MC<=11202
        val playerList = playerComparator.sortedCopy(Client.getConnection()?.playerInfoMap ?: return emptyList())
        return playerList.map { UTextComponent(Client.getTabGui()?.getPlayerName(it) ?: return emptyList()) }
        //#else
        //$$ val playerList = playerComparator.sortedCopy(Client.getConnection()?.onlinePlayers ?: return emptyList())
        //$$ return playerList.map { UTextComponent(Client.getTabGui()?.getNameForDisplay(it) ?: return emptyList()) }
        //#endif
    }

    /**
     * Gets all names in tabs without formatting
     *
     * @return the unformatted names
     */
    @JvmStatic
    fun getUnformattedNames(): List<String> {
        if (Player.getPlayer() == null) return listOf()

        //#if MC<=11202
        return Client.getConnection()?.playerInfoMap
            ?.let { playerComparator.sortedCopy(it) }
            ?.map { it.gameProfile.name }
            ?: emptyList()
        //#else
        //$$ return Client.getConnection()?.onlinePlayers
        //$$     ?.let { playerComparator.sortedCopy(it) }
        //$$     ?.map { it.profile.name }
        //$$     ?: emptyList()
        //#endif
    }

    // TODO(BREAKING): Removed this
    // @JvmStatic
    // fun getHeaderMessage() = getTabGui()?.header?.let { UMessage(it) }

    // TODO(BREAKING): Return UTextComponent
    @JvmStatic
    fun getHeader() = UTextComponent.from(getTabGui()?.header ?: "")!!

    /**
     * Sets the header text for the TabList.
     * If [header] is null, it will remove the header entirely
     *
     * @param header the header to set, or null to clear
     */
    @JvmStatic
    fun setHeader(header: Any?) {
        when (header) {
            is String -> getTabGui()?.header = UMessage(header).chatMessage
            is UMessage -> getTabGui()?.header = header.chatMessage
            is IChatComponent -> getTabGui()?.header = header
            null -> getTabGui()?.header = header
        }
    }

    @JvmStatic
    fun clearHeader() = setHeader(null)

    // TODO(BREAKING): Removed this
    // @JvmStatic
    // fun getFooterMessage() = getTabGui()?.footer?.let { UMessage(it) }

    // TODO(BREAKING): Return UTextComponent
    @JvmStatic
    fun getFooter() = UTextComponent.from(getTabGui()?.footer ?: "")

    /**
     * Sets the footer text for the TabList.
     * If [footer] is null, it will remove the footer entirely
     *
     * @param footer the footer to set, or null to clear
     */
    @JvmStatic
    fun setFooter(footer: Any?) {
        when (footer) {
            is String -> getTabGui()?.footer = UMessage(footer).chatMessage
            is UMessage -> getTabGui()?.footer = footer.chatMessage
            is IChatComponent -> getTabGui()?.footer = footer
            null -> getTabGui()?.footer = footer
        }
    }

    private fun getTabGui() = Client.getTabGui()?.asMixin<PlayerTabOverlayAccessor>()

    @JvmStatic
    fun clearFooter() = setFooter(null)

    internal class PlayerComparator internal constructor() : Comparator<NetworkPlayerInfo> {
        override fun compare(playerOne: NetworkPlayerInfo, playerTwo: NetworkPlayerInfo): Int {
            //#if MC<=11202
            val teamOne = playerOne.playerTeam
            val teamTwo = playerTwo.playerTeam

            return ComparisonChain
                .start()
                .compareTrueFirst(playerOne.gameType != WorldSettings.GameType.SPECTATOR, playerTwo.gameType != WorldSettings.GameType.SPECTATOR)
                .compare(teamOne?.registeredName ?: "", teamTwo?.registeredName ?: "")
                .compare(playerOne.gameProfile.name, playerTwo.gameProfile.name)
                .result()
            //#elseif MC>=11701
            //$$ val teamOne = playerOne.team
            //$$ val teamTwo = playerTwo.team
            //$$
            //$$ return ComparisonChain
            //$$     .start()
            //$$     .compareTrueFirst(playerOne.gameMode != GameType.SPECTATOR, playerTwo.gameMode != GameType.SPECTATOR)
            //$$     .compare(teamOne?.name ?: "", teamTwo?.name ?: "")
            //$$     .compare(playerOne.profile.name, playerTwo.profile.name)
            //$$     .result()
            //#endif
        }
    }
}
