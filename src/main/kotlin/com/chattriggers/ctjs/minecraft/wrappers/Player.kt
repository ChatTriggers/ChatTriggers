package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.minecraft.libs.Tessellator
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.minecraft.wrappers.objects.PotionEffect
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.*
import com.chattriggers.ctjs.minecraft.wrappers.objects.entity.Entity
import com.chattriggers.ctjs.minecraft.wrappers.objects.entity.PlayerEntity
import com.chattriggers.ctjs.minecraft.wrappers.objects.entity.PlayerMP
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Inventory
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.block.SignBlock
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.network.PlayerListEntry
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.MathHelper

@External
object Player : PlayerEntity(null) {
    /**
     * Gets Minecraft's EntityPlayerSP object representing the user
     *
     * @return The Minecraft EntityPlayerSP object representing the user
     */
    override val player: AbstractClientPlayerEntity?
        get() = Client.getMinecraft().player

    internal fun getPlayer() = player

    // TODO(BREAKING): Remove this
    // @JvmStatic
    // fun asPlayerMP() = getPlayer()?.let(::PlayerMP)

    /**
     * Gets the current object that the player is looking at,
     * whether that be a block or an entity. Returns an air block when not looking
     * at anything.
     *
     * @return the [BlockType], [Sign], or [Entity] being looked at
     */
    @JvmStatic
    fun lookingAt(): Any {
        val mop = Client.getMinecraft().crosshairTarget ?: return BlockType(0)
        val world = World.getWorld() ?: return BlockType(0)

        return when (mop) {
            is BlockHitResult -> {
                val block = Block(
                    BlockType(world.getBlockState(mop.blockPos).block),
                    BlockPos(mop.blockPos),
                    BlockFace.fromDirection(mop.side),
                )

                if (block.type.mcBlock is SignBlock) Sign(block) else block
            }
            is EntityHitResult -> Entity(mop.entity)
            else -> BlockType(0)
        }
    }

    @JvmStatic
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
    // fun getOpenedInventory(): Inventory? = getPlayer()?.openContainer?.let(::Inventory)
}
