package com.chattriggers.ctjs.minecraft.wrappers.world.block

import com.chattriggers.ctjs.minecraft.wrappers.World
import gg.essential.universal.wrappers.message.UMessage
import net.minecraft.tileentity.TileEntitySign

/**
 * Creates a new Sign object wrapper.
 * Returned with [com.chattriggers.ctjs.minecraft.wrappers.Player.lookingAt] when looking at a sign.
 * Extends [Block].
 *
 * @param block the [Block] to convert to a Sign
 */
class Sign(block: Block) : Block(block.type, block.pos, block.face) {
    private val sign: TileEntitySign = World.getWorld()!!.getTileEntity(pos.toMCBlock()) as TileEntitySign

    fun getLines() = sign.signText.map { it?.let { UMessage(it) } ?: UMessage("") }

    fun getFormattedLines(): List<String> = sign.signText.map { it?.formattedText ?: "" }

    fun getUnformattedLines(): List<String> = sign.signText.map { it?.unformattedText ?: "" }

    override fun toString(): String =
        "Sign{lines=${getLines()}, name=${type.mcBlock.registryName}, x=$x, y=$y, z=$z}"
}
