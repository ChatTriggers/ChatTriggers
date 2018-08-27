package com.chattriggers.ctjs.minecraft.wrappers.objects.block

import com.chattriggers.ctjs.minecraft.objects.message.Message
import com.chattriggers.ctjs.minecraft.wrappers.World
import net.minecraft.tileentity.TileEntitySign

/**
 * Creates a new Sign object wrapper.<br>
 * Returned with {@link Player#lookingAt()} when looking at a sign.<br>
 * Extends {@link Block}.
 *
 * @param block the {@link Block} to convert to a Sign
 */
class Sign(block: Block) : Block(block) {
    private val sign: TileEntitySign = World.getWorld()!!.getTileEntity(blockPos) as TileEntitySign

    fun getLines() = sign.signText.map { Message(it) }

    fun getFormattedLines() = sign.signText.map { it -> it.formattedText }

    fun getUnformattedLines() = sign.signText.map { it -> it.unformattedText }

    override fun toString() = "Sign{lines=${getLines()}, name=${block.registryName}, x=${getX()}, y=${getY()}, z=${getZ()}}"
}