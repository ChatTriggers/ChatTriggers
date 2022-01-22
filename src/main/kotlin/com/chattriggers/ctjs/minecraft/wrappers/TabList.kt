package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.launch.mixins.asMixin
import com.chattriggers.ctjs.launch.mixins.transformers.PlayerListHudMixin
import com.chattriggers.ctjs.minecraft.objects.message.Message
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCGameType
import com.google.common.collect.ComparisonChain
import com.google.common.collect.Ordering
import net.minecraft.client.network.PlayerListEntry
import net.minecraft.scoreboard.Team
import net.minecraft.text.LiteralText
import net.minecraft.text.Text

@External
object TabList {
    private val playerComparator = Ordering.from(PlayerComparator())

    /**
     * Gets names set in scoreboard objectives
     *
     * @return The formatted names
     */
    // TODO(BREAKING): Return List<TextComponent> instead of List<String>
    @JvmStatic
    fun getNamesByObjectives(): List<TextComponent> {
        return try {
            val scoreboard = World.getWorld()?.scoreboard ?: return emptyList()
            val sidebarObjective = scoreboard.getObjectiveForSlot(0)
            val scores = scoreboard.getAllPlayerScores(sidebarObjective)

            scores.map {
                val team = scoreboard.getTeam(it.playerName)
                TextComponent(Team.decorateName(team, LiteralText(it.playerName)))
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // TODO(BREAKING): Return List<TextComponent> instead of List<String>
    @JvmStatic
    fun getNames(): List<TextComponent> {
        if (Client.getTabGui() == null)
            return emptyList()

        val playerList = playerComparator.sortedCopy(Client.getConnection()?.playerList ?: return emptyList())

        return playerList.map(Client.getTabGui()!!::getPlayerName).map(::TextComponent)
    }

    /**
     * Gets all names in tabs without formatting
     *
     * @return the unformatted names
     */
    @JvmStatic
    fun getUnformattedNames(): List<String> {
        if (Player.getPlayer() == null) return listOf()

        return Client.getConnection()?.playerList?.let {
            Ordering.from(PlayerComparator()).sortedCopy(it)
        }?.map {
            it.profile.name
        } ?: emptyList()
    }

    // TODO(BREAKING): Remove this
    // @JvmStatic
    // fun getHeaderMessage() = Client.getTabGui()?.header?.let(::Message)

    // TODO(BREAKING): Return TextComponent? instead of String?
    @JvmStatic
    fun getHeader() = Client.getTabGui()?.asMixin<PlayerListHudMixin>()?.header?.let(::TextComponent)

    /**
     * Sets the header text for the TabList.
     * If [header] is null, it will remove the header entirely
     *
     * @param header the header to set, or null to clear
     */
    @JvmStatic
    fun setHeader(header: Any?) {
        when (header) {
            is String -> Client.getTabGui()?.setHeader(TextComponent(header).component)
            is Message -> Client.getTabGui()?.setHeader(header.getChatMessage())
            is Text -> Client.getTabGui()?.setHeader(header)
            null -> Client.getTabGui()?.setHeader(null)
        }
    }

    @JvmStatic
    fun clearHeader() = setHeader(null)

    // TODO(BREAKING): Remove this
    // @JvmStatic
    // fun getFooterMessage() = Client.getTabGui()?.footer?.let(::Message)

    // TODO(BREAKING): Return TextComponent? instead of String?
    @JvmStatic
    fun getFooter() = Client.getTabGui()?.asMixin<PlayerListHudMixin>()?.footer?.let(::TextComponent)

    /**
     * Sets the footer text for the TabList.
     * If [footer] is null, it will remove the footer entirely
     *
     * @param footer the footer to set, or null to clear
     */
    @JvmStatic
    fun setFooter(footer: Any?) {
        when (footer) {
            is String -> Client.getTabGui()?.setFooter(TextComponent(footer).component)
            is Message -> Client.getTabGui()?.setFooter(footer.getChatMessage())
            is Text -> Client.getTabGui()?.setFooter(footer)
            null -> Client.getTabGui()?.setFooter(null)
        }
    }

    @JvmStatic
    fun clearFooter() = setFooter(null)

    internal class PlayerComparator internal constructor() : Comparator<PlayerListEntry> {
        override fun compare(playerOne: PlayerListEntry, playerTwo: PlayerListEntry): Int {
            val teamOne = playerOne.scoreboardTeam
            val teamTwo = playerTwo.scoreboardTeam

            return ComparisonChain
                .start()
                .compareTrueFirst(
                    playerOne.gameMode != MCGameType.SPECTATOR,
                    playerTwo.gameMode != MCGameType.SPECTATOR
                ).compare(
                    //#if MC<=10809
                    teamOne?.name ?: "",
                    teamTwo?.name ?: ""
                    //#else
                    //$$ teamOne?.name ?: "",
                    //$$ teamTwo?.name ?: ""
                    //#endif
                ).compare(
                    playerOne.profile.name,
                    playerTwo.profile.name
                ).result()
        }
    }
}
