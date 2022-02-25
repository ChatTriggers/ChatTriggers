package com.chattriggers.ctjs.minecraft.wrappers.objects.entity

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.objects.Team
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraftforge.fml.relauncher.ReflectionHelper

@External
class PlayerMP(val player: EntityPlayer) : EntityLivingBase(player) {
    fun isSpectator() = player.isSpectator

    fun getPing(): Int {
        return getPlayerInfo()?.responseTime ?: -1
    }

    fun getTeam(): Team? {
        return getPlayerInfo()?.playerTeam?.let(::Team)
    }

    /**
     * Gets the display name for this player,
     * i.e. the name shown in tab list and in the player's nametag.
     * @return the display name
     */
    fun getDisplayName(): TextComponent {
        return TextComponent(getPlayerName(getPlayerInfo()))
    }

    fun setTabDisplayName(textComponent: TextComponent) {
        getPlayerInfo()?.displayName = textComponent.chatComponentText
    }

    /**
     * Sets the name for this player shown above their head,
     * in their name tag
     *
     * @param textComponent the new name to display
     */
    fun setNametagName(textComponent: TextComponent) {
        displayNameField.set(player, textComponent.chatComponentText.formattedText)
    }

    /**
     * Draws the player in the GUI
     */
    @JvmOverloads
    fun draw(
        player: Any,
        x: Int,
        y: Int,
        rotate: Boolean = false,
        showNametag: Boolean = false,
        showArmor: Boolean = true,
        showCape: Boolean = true,
        showHeldItem: Boolean = true,
        showArrows: Boolean = true
    ) = apply {
        Renderer.drawPlayer(player, x, y, rotate, showNametag, showArmor, showCape, showHeldItem, showArrows)
    }

    private fun getPlayerName(networkPlayerInfoIn: NetworkPlayerInfo?): String {
        return networkPlayerInfoIn?.displayName?.formattedText
            ?: ScorePlayerTeam.formatPlayerName(
                networkPlayerInfoIn?.playerTeam,
                networkPlayerInfoIn?.gameProfile?.name
            ) ?: ""
    }

    private fun getPlayerInfo(): NetworkPlayerInfo? = Client.getConnection()?.getPlayerInfo(player.uniqueID)

    override fun toString(): String {
        return "PlayerMP{name=" + getName() +
                ", ping=" + getPing() +
                ", entityLivingBase=" + super.toString() +
                "}"
    }

    override fun getName(): String = player.name

    companion object {
        private val displayNameField = ReflectionHelper.findField(
            EntityPlayer::class.java,
            "displayname"
        )
    }
}
