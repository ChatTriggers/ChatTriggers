package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.launch.mixins.transformers.asMixinAccessor
import com.chattriggers.ctjs.minecraft.objects.message.Message
import com.chattriggers.ctjs.utils.kotlin.External
import com.google.common.collect.ComparisonChain
import com.google.common.collect.Ordering
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraft.util.IChatComponent
import java.util.*

//#if MC==11602
//$$ import net.minecraft.world.GameType
//$$ import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
//#else
import net.minecraft.world.WorldSettings.GameType
//#endif

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
        return try {
            val scoreboard = World.getWorld()?.scoreboard ?: return emptyList()
            @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
            val sidebarObjective = scoreboard.getObjectiveInDisplaySlot(0)!!
            val scores = scoreboard.getSortedScores(sidebarObjective)

            scores.map {
                val team = scoreboard.getPlayersTeam(it.playerName)
                //#if MC==11602
                //$$ TextComponent(ScorePlayerTeam.func_237500_a_(
                //$$     team,
                //$$     TextComponent(it.playerName).component,
                //$$ )).getFormattedText()
                //#else
                ScorePlayerTeam.formatPlayerName(team, it.playerName)
                //#endif
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    @JvmStatic
    fun getNames(): List<String> {
        if (Client.getTabGui() == null) return listOf()

        val playerList = playerComparator.sortedCopy(
            Client.getMinecraft().thePlayer?.sendQueue?.playerInfoMap ?: return listOf()
        )

        return playerList.map {
            //#if MC==11602
            //$$ TextComponent(Client.getTabGui()!!.getDisplayName(it)).getFormattedText()
            //#else
            Client.getTabGui()!!.getPlayerName(it)
            //#endif
        }
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
            Ordering.from(PlayerComparator()).sortedCopy(it)
        }?.map {
            it.gameProfile.name
        } ?: emptyList()
    }

    // TODO(1.16.2): ATs?
    //#if MC==10809
    @JvmStatic
    fun getHeaderMessage() = Client.getTabGui()?.asMixinAccessor()?.getHeader()?.let(::Message)

    @JvmStatic
    fun getHeader() = Client.getTabGui()?.asMixinAccessor()?.getHeader()?.formattedText
    //#endif

    @JvmStatic
    fun setHeader(header: Any) {
        when (header) {
            is String -> Client.getTabGui()?.setHeader(Message(header).getChatMessage())
            is Message -> Client.getTabGui()?.setHeader(header.getChatMessage())
            is IChatComponent -> Client.getTabGui()?.setHeader(header)
        }
    }

    // TODO(1.16.2): ATs?
    //#if MC==10809
    @JvmStatic
    fun getFooterMessage() = Client.getTabGui()?.asMixinAccessor()?.getFooter()?.let(::Message)

    @JvmStatic
    fun getFooter() = Client.getTabGui()?.asMixinAccessor()?.getFooter()?.formattedText
    //#endif

    @JvmStatic
    fun setFooter(footer: Any) {
        when (footer) {
            is String -> Client.getTabGui()?.setFooter(Message(footer).getChatMessage())
            is Message -> Client.getTabGui()?.setFooter(footer.getChatMessage())
            is IChatComponent -> Client.getTabGui()?.setFooter(footer)
        }
    }

    internal class PlayerComparator internal constructor() : Comparator<NetworkPlayerInfo> {
        override fun compare(playerOne: NetworkPlayerInfo, playerTwo: NetworkPlayerInfo): Int {
            val teamOne = playerOne.playerTeam
            val teamTwo = playerTwo.playerTeam

            return ComparisonChain
                .start()
                .compareTrueFirst(
                    playerOne.gameType != GameType.SPECTATOR,
                    playerTwo.gameType != GameType.SPECTATOR
                ).compare(
                    //#if MC==11602
                    //$$ teamOne?.name ?: "",
                    //$$ teamTwo?.name ?: ""
                    //#else
                    teamOne.registeredName,
                    teamTwo.registeredName
                    //#endif
                ).compare(
                    playerOne.gameProfile.name,
                    playerTwo.gameProfile.name
                ).result()
        }
    }
}
