package com.chattriggers.ctjs.minecraft.wrappers.world.block

import com.chattriggers.ctjs.minecraft.objects.message.Message
import com.chattriggers.ctjs.minecraft.wrappers.World
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

    fun getLines(): List<Message> = sign.signText.map { it?.let(::Message) ?: Message("") }

    fun getFormattedLines(): List<String> = sign.signText.map { it?.formattedText ?: "" }

    fun getUnformattedLines(): List<String> = sign.signText.map { it?.unformattedText ?: "" }

    override fun toString(): String =
        "Sign{lines=${getLines()}, name=${type.mcBlock.registryName}, x=$x, y=$y, z=$z}"
}
