package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.wrappers.entity.Entity
import com.chattriggers.ctjs.minecraft.wrappers.entity.PlayerMP
import com.chattriggers.ctjs.minecraft.wrappers.inventory.Inventory
import com.chattriggers.ctjs.minecraft.wrappers.inventory.Item
import com.chattriggers.ctjs.minecraft.wrappers.entity.Team
import com.chattriggers.ctjs.minecraft.wrappers.world.block.*
import gg.essential.universal.wrappers.message.UTextComponent
import net.minecraft.block.BlockSign
import net.minecraft.client.entity.EntityPlayerSP
import java.util.*

//#if MC<=11202
import net.minecraft.util.MovingObjectPosition
//#else
//$$ import net.minecraft.world.phys.BlockHitResult
//$$ import net.minecraft.world.phys.EntityHitResult
//$$ import net.minecraft.world.item.ItemStack
//#endif

object Player {
    private var cachedPlayer: PlayerMP? = null

    @JvmStatic
    fun asPlayerMP(): PlayerMP? {
        val player = getPlayer() ?: return null
        if (cachedPlayer?.player != player)
            cachedPlayer = PlayerMP(player)
        return cachedPlayer
    }

    /**
     * Gets Minecraft's EntityPlayerSP object representing the user
     *
     * @return The Minecraft EntityPlayerSP object representing the user
     */
    @JvmStatic
    fun getPlayer(): EntityPlayerSP? {
        //#if MC<=10809
        return Client.getMinecraft().thePlayer
        //#else
        //$$ return Client.getMinecraft().player
        //#endif
    }

    @JvmStatic
    fun getTeam(): Team? {
        return Scoreboard.getScoreboard()?.getPlayersTeam(getName())?.let(::Team)
    }

    @JvmStatic
    fun getX(): Double = asPlayerMP()?.getX() ?: 0.0

    @JvmStatic
    fun getY(): Double = asPlayerMP()?.getY() ?: 0.0

    @JvmStatic
    fun getZ(): Double = asPlayerMP()?.getZ() ?: 0.0

    @JvmStatic
    fun getLastX(): Double = asPlayerMP()?.getLastX() ?: 0.0

    @JvmStatic
    fun getLastY(): Double = asPlayerMP()?.getLastY() ?: 0.0

    @JvmStatic
    fun getLastZ(): Double = asPlayerMP()?.getLastZ() ?: 0.0

    @JvmStatic
    fun getRenderX(): Double = asPlayerMP()?.getRenderX() ?: 0.0

    @JvmStatic
    fun getRenderY(): Double = asPlayerMP()?.getRenderY() ?: 0.0

    @JvmStatic
    fun getRenderZ(): Double = asPlayerMP()?.getRenderZ() ?: 0.0

    /**
     * Gets the player's x motion.
     * This is the amount the player will move in the x direction next tick.
     *
     * @return the player's x motion
     */
    @JvmStatic
    fun getMotionX(): Double = asPlayerMP()?.getMotionX() ?: 0.0

    /**
     * Gets the player's y motion.
     * This is the amount the player will move in the y direction next tick.
     *
     * @return the player's y motion
     */
    @JvmStatic
    fun getMotionY(): Double = asPlayerMP()?.getMotionY() ?: 0.0

    /**
     * Gets the player's z motion.
     * This is the amount the player will move in the z direction next tick.
     *
     * @return the player's z motion
     */
    @JvmStatic
    fun getMotionZ(): Double = asPlayerMP()?.getMotionZ() ?: 0.0

    /**
     * Gets the player's camera pitch.
     *
     * @return the player's camera pitch
     */
    @JvmStatic
    fun getPitch(): Double = asPlayerMP()?.getPitch() ?: 0.0

    /**
     * Gets the player's camera yaw.
     *
     * @return the player's camera yaw
     */
    @JvmStatic
    fun getYaw(): Double = asPlayerMP()?.getYaw() ?: 0.0

    /**
     * Gets the player's yaw rotation without wrapping.
     *
     * @return the yaw
     */
    // TODO(BREAKING): Removed this, there isn't really a point
    // @JvmStatic
    // fun getRawYaw(): Float = getPlayer()?.rotationYaw ?: 0f

    /**
     * Gets the player's username.
     *
     * @return the player's username
     */
    @JvmStatic
    fun getName(): String {
        //#if MC<=11202
        return Client.getMinecraft().session.username
        //#else
        //$$ return Client.getMinecraft().user.name
        //#endif
    }

    @JvmStatic
    fun getUUID(): String = getUUIDObj().toString()

    @JvmStatic
    fun getUUIDObj(): UUID {
        //#if MC<=11202
        return Client.getMinecraft().session.profile.id
        //#else
        //$$ return UUID.fromString(Client.getMinecraft().user.uuid)
        //#endif
    }

    @JvmStatic
    fun getHP(): Float = asPlayerMP()?.getHP() ?: 0f

    @JvmStatic
    fun getHunger(): Int {
        //#if MC<=11202
        return getPlayer()?.foodStats?.foodLevel ?: 0
        //#else
        //$$ return getPlayer()?.foodData?.foodLevel ?: 0
        //#endif
    }

    @JvmStatic
    fun getSaturation(): Float {
        //#if MC<=11202
        return getPlayer()?.foodStats?.saturationLevel ?: 0f
        //#else
        //$$ return getPlayer()?.foodData?.saturationLevel ?: 0f
        //#endif
    }

    @JvmStatic
    fun getArmorPoints(): Int = asPlayerMP()?.getArmorValue() ?: 0

    /**
     * Gets the player's air level.
     *
     * The returned value will be an integer. If the player is not taking damage, it
     * will be between 300 (not in water) and 0. If the player is taking damage, it
     * will be between -20 and 0, getting reset to 0 every time the player takes damage.
     *
     * @return the player's air level
     */
    @JvmStatic
    fun getAirLevel(): Int = asPlayerMP()?.getAir() ?: 0

    @JvmStatic
    fun getXPLevel(): Int = getPlayer()?.experienceLevel ?: 0

    @JvmStatic
    fun getXPProgress(): Float {
        //#if MC<=11202
        return getPlayer()?.experience ?: 0f
        //#else
        //$$ return getPlayer()?.experienceProgress ?: 0f
        //#endif
    }

    @JvmStatic
    fun getBiome(): String = asPlayerMP()?.getBiome() ?: ""

    /**
     * Gets the light level at the player's current position.
     *
     * @return the light level at the player's current position
     */
    @JvmStatic
    fun getLightLevel(): Int = asPlayerMP()?.getLightLevel() ?: 0

    @JvmStatic
    fun isSneaking(): Boolean = asPlayerMP()?.isSneaking() ?: false

    @JvmStatic
    fun isSprinting(): Boolean = asPlayerMP()?.isSprinting() ?: false

    /**
     * Checks if player is flying (or using an Elytra in newer versions).
     *
     * @return true if the player is flying, false otherwise
     */
    @JvmStatic
    fun isFlying(): Boolean {
        //#if MC<=11202
        return getPlayer()?.capabilities?.isFlying ?: false
        //#else
        //$$ return getPlayer()?.isFallFlying ?: getPlayer()?.abilities?.flying ?: false
        //#endif
    }

    @JvmStatic
    fun isSleeping(): Boolean {
        //#if MC<=11202
        return getPlayer()?.isPlayerSleeping ?: false
        //#else
        //$$ return getPlayer()?.isSleeping ?: false
        //#endif
    }

    /**
     * Gets the direction the player is facing.
     * Example: "South West"
     *
     * @return The direction the player is facing, one of the four cardinal directions
     */
    @JvmStatic
    fun facing() = asPlayerMP()?.facing() ?: ""

    @JvmStatic
    fun getActivePotionEffects() = asPlayerMP()?.getActivePotionEffects()

    /**
     * Gets the current object that the player is looking at,
     * whether that be a block or an entity. Returns an air block when not looking
     * at anything.
     *
     * @return the [BlockType], [Sign], or [Entity] being looked at
     */
    @JvmStatic
    fun lookingAt(): Any {
        val world = World.getWorld() ?: return BlockType(0)

        //#if MC<=11202
        val mop = Client.getMinecraft().objectMouseOver ?: return BlockType(0)

        return when (mop.typeOfHit) {
            MovingObjectPosition.MovingObjectType.BLOCK -> {
                val block = Block(
                    BlockType(world.getBlockState(mop.blockPos).block),
                    BlockPos(mop.blockPos),
                    BlockFace.fromMCEnumFacing(mop.sideHit),
                )

                if (block.type.mcBlock is BlockSign) Sign(block) else block
            }
            MovingObjectPosition.MovingObjectType.ENTITY -> Entity(mop.entityHit)
            else -> BlockType(0)
        }
        //#else
        //$$ val mop = Client.getMinecraft().hitResult ?: return BlockType(0)
        //$$
        //$$ return when (mop) {
        //$$     is BlockHitResult -> {
        //$$         val block = Block(
        //$$             BlockType(world.getBlockState(mop.blockPos).block),
        //$$             BlockPos(mop.blockPos),
        //$$             BlockFace.fromMCEnumFacing(mop.direction),
        //$$         )
        //$$
        //$$         if (block.type.mcBlock is SignBlock) Sign(block) else block
        //$$     }
        //$$     is EntityHitResult -> Entity(mop.entity)
        //$$     else -> BlockType(0)
        //$$ }
        //#endif
    }

    @JvmStatic
    fun getHeldItem(): Item? {
        //#if MC<=11202
        return getPlayer()?.inventory?.getCurrentItem()?.let(::Item)
        //#else
        //$$ return getPlayer()?.inventory?.getSelected()?.let {
        //$$     if (it !== ItemStack.EMPTY) Item(it) else null
        //$$ }
        //#endif
    }

    @JvmStatic
    fun setHeldItemIndex(index: Int) {
        //#if MC<=11202
        getPlayer()?.inventory?.currentItem = index
        //#else
        //$$ getPlayer()?.inventory?.selected = index
        //#endif
    }

    @JvmStatic
    fun getHeldItemIndex(): Int {
        //#if MC<=11202
        return getPlayer()?.inventory?.currentItem ?: -1
        //#else
        //$$ return getPlayer()?.inventory?.selected ?: -1
        //#endif
    }

    /**
     * Gets the inventory of the player, i.e. the inventory accessed by 'e'.
     *
     * @return the player's inventory
     */
    @JvmStatic
    fun getInventory(): Inventory? = getPlayer()?.inventory?.let(::Inventory)

    /**
     * Gets the display name for the player,
     * i.e. the name shown in tab list and in the player's nametag.
     * @return the display name
     */
    @JvmStatic
    fun getDisplayName(): UTextComponent {
        return asPlayerMP()?.getDisplayName() ?: UTextComponent("")
    }

    /**
     * Sets the name for this player shown in tab list
     *
     * @param textComponent the new name to display
     */
    @JvmStatic
    fun setTabDisplayName(textComponent: UTextComponent) {
        asPlayerMP()?.setTabDisplayName(textComponent)
    }

    /**
     * Sets the name for this player shown above their head,
     * in their name tag
     *
     * @param textComponent the new name to display
     */
    @JvmStatic
    fun setNametagName(textComponent: UTextComponent) {
        asPlayerMP()?.setNametagName(textComponent)
    }

    @Deprecated("Use the better named method", ReplaceWith("getContainer()"))
    @JvmStatic
    fun getOpenedInventory(): Inventory? = getContainer()

    /**
     * Gets the container the user currently has open, i.e. a chest.
     *
     * @return the currently opened container
     */
    @JvmStatic
    fun getContainer(): Inventory? = getPlayer()?.openContainer?.let(::Inventory)

    /**
     * Draws the player in the GUI
     */
    @JvmStatic
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
        Renderer.drawPlayer(this, x, y, rotate, showNametag, showArmor, showCape, showHeldItem, showArrows)
    }

    object armor {
        /**
         * @return the item in the player's helmet slot
         */
        @JvmStatic
        fun getHelmet(): Item? = getInventory()?.getStackInSlot(39)

        /**
         * @return the item in the player's chestplate slot
         */
        @JvmStatic
        fun getChestplate(): Item? = getInventory()?.getStackInSlot(38)

        /**
         * @return the item in the player's leggings slot
         */
        @JvmStatic
        fun getLeggings(): Item? = getInventory()?.getStackInSlot(37)

        /**
         * @return the item in the player's boots slot
         */
        @JvmStatic
        fun getBoots(): Item? = getInventory()?.getStackInSlot(36)
    }
}
