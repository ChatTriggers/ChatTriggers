package com.chattriggers.ctjs.minecraft.wrappers.objects.entity

import com.chattriggers.ctjs.minecraft.libs.Tessellator
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.minecraft.wrappers.objects.PotionEffect
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Inventory
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.network.PlayerListEntry
import net.minecraft.util.math.MathHelper

@External
open class PlayerEntity(player: AbstractClientPlayerEntity?) : LivingEntity(player) {
    open val player: AbstractClientPlayerEntity? = player

    fun isSpectator() = player?.isSpectator

    fun isCreative() = player?.isCreative

    fun getHunger(): Int = player?.hungerManager?.foodLevel ?: 0

    fun getSaturation(): Float = player?.hungerManager?.saturationLevel ?: 0f

    fun getArmorPoints(): Int = player?.armor ?: 0

    /**
     * Gets the player's air level.
     *
     * The returned value will be an integer. If the player is not taking damage, it
     * will be between 300 (not in water) and 0. If the player is taking damage, it
     * will be between -20 and 0, getting reset to 0 every time the player takes damage.
     *
     * @return the player's air level
     */
    fun getAirLevel(): Int = player?.air ?: 0

    fun getXPLevel(): Int = player?.experienceLevel ?: 0

    fun getXPProgress(): Float = player?.experienceProgress ?: 0f

    fun getBiome(): String {
        return player?.blockPos?.let {
            World.getWorld()?.getBiome(it)?.toString()
        } ?: ""
    }

    /**
     * Gets the light level at the player's current position.
     *
     * @return the light level at the player's current position
     */
    fun getLightLevel(): Int {
        return player?.blockPos?.let {
            World.getWorld()?.getLightLevel(it)
        } ?: 0
    }

    /**
     * Checks if player can be pushed by water.
     *
     * @return true if the player is flying, false otherwise
     */
    fun isFlying(): Boolean = player?.isOnGround?.not() ?: false

    fun isSleeping(): Boolean = player?.isSleeping ?: false

    /**
     * Gets the direction the player is facing.
     * Example: "South West"
     *
     * @return The direction the player is facing, one of the four cardinal directions
     */
    fun facing(): String {
        val yaw = getYaw()

        return when {
            yaw in -22.5..22.5 -> "South"
            yaw in 22.5..67.5 -> "South West"
            yaw in 67.5..112.5 -> "West"
            yaw in 112.5..157.5 -> "North West"
            yaw < -157.5 || yaw > 157.5 -> "North"
            yaw in -157.5..-112.5 -> "North East"
            yaw in -112.5..-67.5 -> "East"
            yaw in -67.5..-22.5 -> "South East"
            else -> ""
        }
    }

    fun getHeldItem(): Item? = player?.inventory?.mainHandStack?.let(::Item)

    fun setHeldItemIndex(index: Int) {
        player?.inventory?.selectedSlot = index
    }

    fun getHeldItemIndex(): Int = player?.inventory?.selectedSlot ?: -1

    /**
     * Gets the inventory of the player, i.e. the inventory accessed by 'e'.
     *
     * @return the player's inventory
     */
    fun getInventory(): Inventory? = player?.inventory?.let(::Inventory)

    /**
     * Gets the display name for the player,
     * i.e. the name shown in tab list and in the player's nametag.
     * @return the display name
     */
    fun getDisplayName(): TextComponent = getPlayerInfo()?.let {
        TextComponent(getPlayerName(it))
    } ?: TextComponent(getName())

    /**
     * Sets the name for this player shown in tab list
     *
     * @param textComponent the new name to display
     */
    fun setTabDisplayName(textComponent: TextComponent) {
        getPlayerInfo()?.displayName = textComponent.component
    }

    /**
     * Draws the player in the GUI
     */
    @JvmOverloads
    fun draw(
        x: Int,
        y: Int,
        rotate: Boolean = false,
        showNametag: Boolean = false,
        showArmor: Boolean = true,
        showCape: Boolean = true,
        showHeldItem: Boolean = true,
        showArrows: Boolean = true
    ) = apply {
        Renderer.drawEntity(player ?: return@apply, x, y, rotate, showNametag, showArmor, showCape, showHeldItem, showArrows)
    }

    private fun getPlayerName(entry: PlayerListEntry): String {
        return entry.displayName?.let {
            TextComponent(it).getFormattedText()
        } ?: entry.profile.name
    }

    private fun getPlayerInfo(): PlayerListEntry? = Client.getConnection()?.getPlayerListEntry(getUUID())

    /**
     * Gets the inventory the user currently has open, i.e. a chest.
     *
     * @return the currently opened inventory
     */
    // TODO("fabric")
    // @JvmStatic
    // fun getOpenedInventory(): Inventory? = player.openContainer?.let(::Inventory)

    val armor = object {
        /**
         * @return the item in the player's helmet slot
         */
        fun getHelmet(): Item? = player?.armorItems?.firstOrNull()?.let(::Item)

        /**
         * @return the item in the player's chestplate slot
         */
        fun getChestplate(): Item? = player?.armorItems?.drop(1)?.firstOrNull()?.let(::Item)

        /**
         * @return the item in the player's leggings slot
         */
        fun getLeggings(): Item? = player?.armorItems?.drop(2)?.firstOrNull()?.let(::Item)

        /**
         * @return the item in the player's boots slot
         */
        fun getBoots(): Item? = player?.armorItems?.drop(3)?.firstOrNull()?.let(::Item)
    }
}
