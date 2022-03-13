package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.minecraft.objects.message.Message
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCGameType
import com.chattriggers.ctjs.utils.kotlin.MCITextComponent
import com.chattriggers.ctjs.utils.kotlin.MCScore
import com.google.common.collect.ComparisonChain
import com.google.common.collect.Ordering
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.scoreboard.ScorePlayerTeam

@External
object TabList {
    private val playerComparator = Ordering.from(PlayerComparator())

    /**
     * Gets names set in scoreboard objectives
     *
     * @return The formatted names
     */
    @JvmStatic
    fun getNamesByObjectives(): List<String> {
        val scoreboard = Scoreboard.getScoreboard() ?: return emptyList()
        val sidebarObjective = scoreboard.getObjectiveInDisplaySlot(0) ?: return emptyList()

        val scores: Collection<MCScore> = scoreboard.getSortedScores(sidebarObjective)

        return scores.map {
            val team = scoreboard.getPlayersTeam(it.playerName)
            ScorePlayerTeam.formatPlayerName(team, it.playerName)
        }
    }

    @JvmStatic
    fun getNames(): List<String> {
        if (Client.getTabGui() == null) return listOf()

        val playerList = playerComparator.sortedCopy(Player.getPlayer()!!.sendQueue.playerInfoMap)

        return playerList.map(Client.getTabGui()!!::getPlayerName)
    }

    /**
     * Gets all names in tabs without formatting
     *
     * @return the unformatted names
     */
    @JvmStatic
    fun getUnformattedNames(): List<String> {
        if (Player.getPlayer() == null) return listOf()

        return Client.getConnection()?.playerInfoMap?.let {
            playerComparator.sortedCopy(it)
        }?.map {
            it.gameProfile.name
        } ?: emptyList()
    }

    @JvmStatic
    fun getHeaderMessage() = Client.getTabGui()?.header?.let(::Message)

    @JvmStatic
    fun getHeader() = Client.getTabGui()?.header?.formattedText

    /**
     * Sets the header text for the TabList.
     * If [header] is null, it will remove the header entirely
     *
     * @param header the header to set, or null to clear
     */
    @JvmStatic
    fun setHeader(header: Any?) {
        when (header) {
            is String -> Client.getTabGui()?.header = Message(header).getChatMessage()
            is Message -> Client.getTabGui()?.header = header.getChatMessage()
            is MCITextComponent -> Client.getTabGui()?.header = header
            null -> Client.getTabGui()?.header = header
        }
    }

    @JvmStatic
    fun clearHeader() = setHeader(null)

    @JvmStatic
    fun getFooterMessage() = Client.getTabGui()?.footer?.let(::Message)

    @JvmStatic
    fun getFooter() = Client.getTabGui()?.footer?.formattedText

    /**
     * Sets the footer text for the TabList.
     * If [footer] is null, it will remove the footer entirely
     *
     * @param footer the footer to set, or null to clear
     */
    @JvmStatic
    fun setFooter(footer: Any?) {
        when (footer) {
            is String -> Client.getTabGui()?.footer = Message(footer).getChatMessage()
            is Message -> Client.getTabGui()?.footer = footer.getChatMessage()
            is MCITextComponent -> Client.getTabGui()?.footer = footer
            null -> Client.getTabGui()?.footer = footer
        }
    }

    @JvmStatic
    fun clearFooter() = setFooter(null)

    internal class PlayerComparator internal constructor() : Comparator<NetworkPlayerInfo> {
        override fun compare(playerOne: NetworkPlayerInfo, playerTwo: NetworkPlayerInfo): Int {
            val teamOne = playerOne.playerTeam
            val teamTwo = playerTwo.playerTeam

            return ComparisonChain
                .start()
                .compareTrueFirst(
                    playerOne.gameType != MCGameType.SPECTATOR,
                    playerTwo.gameType != MCGameType.SPECTATOR
                ).compare(
                    //#if MC<=10809
                    teamOne?.registeredName ?: "",
                    teamTwo?.registeredName ?: ""
                    //#else
                    //$$ teamOne?.name ?: "",
                    //$$ teamTwo?.name ?: ""
                    //#endif
                ).compare(
                    playerOne.gameProfile.name,
                    playerTwo.gameProfile.name
                ).result()
        }
    }
}
