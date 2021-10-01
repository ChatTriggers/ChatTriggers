package com.chattriggers.ctjs.minecraft.wrappers.objects

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.scoreboard.ScorePlayerTeam

//#if MC==11602
//$$ import net.minecraft.inventory.EquipmentSlotType
//#else
import net.minecraftforge.fml.relauncher.ReflectionHelper
//#endif

@External
class PlayerMP(val player: EntityPlayer) : Entity(player) {
    //#if MC==10809
    val displayNameField = ReflectionHelper.findField(
            EntityPlayer::class.java,
            "displayname"
    )
    //#endif

    fun isSpectator() = this.player.isSpectator

    fun getActivePotionEffects(): List<PotionEffect> {
        return player.activePotionEffects.map {
            PotionEffect(it)
        }
    }

    fun getPing(): Int {
        return getPlayerInfo()?.responseTime ?: -1
    }

    /**
     * Gets the item currently in the player's specified inventory slot.
     * 0 for main hand, 1-4 for armor
     * (2 for offhand in 1.12.2, and everything else shifted over).
     *
     * @param slot the slot to access
     * @return the item in said slot
     */
    fun getItemInSlot(slot: Int): Item {
        //#if MC<=10809
        return Item(player.getEquipmentInSlot(slot))
        //#else
        //$$ return Item(player.getItemStackFromSlot(EquipmentSlotType.values()[slot]))
        //#endif
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
        getPlayerInfo()?.displayName = textComponent.component
    }

    /**
     * Sets the name for this player shown above their head,
     * in their name tag
     *
     * @param textComponent the new name to display
     */
    // TODO(1.16.2)
    //#if MC==10809
    fun setNametagName(textComponent: TextComponent) {
        displayNameField.set(player, textComponent.component.formattedText)
    }
    //#endif

    /**
     * Draws the player in the GUI
     */
    //#if MC==10809
    @JvmOverloads
    fun draw(x: Int, y: Int, rotate: Boolean = false) = apply {
        Renderer.drawPlayer(player, x, y, rotate)
    }
    //#endif

    private fun getPlayerName(networkPlayerInfoIn: NetworkPlayerInfo?): String {
        val name = networkPlayerInfoIn?.displayName?.let { TextComponent(it).getFormattedText() }
        if (name != null)
            return name

        //#if MC==11602
        //$$ return TextComponent(ScorePlayerTeam.func_237500_a_(
        //$$     networkPlayerInfoIn!!.playerTeam,
        //$$     TextComponent(networkPlayerInfoIn.gameProfile.name).component,
        //$$ )).getFormattedText()
        //#else
        return ScorePlayerTeam.formatPlayerName(
            networkPlayerInfoIn!!.playerTeam,
            networkPlayerInfoIn.gameProfile.name,
        ) ?: ""
        //#endif
    }

    private fun getPlayerInfo(): NetworkPlayerInfo? = Client.getConnection()?.getPlayerInfo(this.player.uniqueID)

    override fun toString(): String {
        return "PlayerMP{name:" + getName() +
                ",ping:" + getPing() +
                ",entity:" + super.toString() +
                "}"
    }

    //#if MC==11602
    //$$ override fun getName(): String = TextComponent(this.player.name).getFormattedText()
    //#else
    override fun getName(): String = this.player.name
    //#endif
}
