package com.chattriggers.ctjs.minecraft.wrappers.objects.block

import com.chattriggers.ctjs.minecraft.objects.message.Message
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.tileentity.TileEntitySign

/**
 * Creates a new Sign object wrapper.<br>
 * Returned with {@link Player#lookingAt()} when looking at a sign.<br>
 * Extends {@link Block}.
 *
 * @param block the {@link Block} to convert to a Sign
 */
@External
class Sign(block: Block) : Block(block) {
    private val sign: TileEntitySign = World.getWorld()!!.getTileEntity(blockPos) as TileEntitySign

    fun getLines(): List<Message> = sign.signText.map { it?.let(::Message) ?: Message("") }

    fun getFormattedLines(): List<String> = sign.signText.map { it?.formattedText ?: "" }

    fun getUnformattedLines(): List<String> = sign.signText.map { it?.unformattedText ?: "" }

    override fun toString(): String =
        "Sign{lines=${getLines()}, name=${block.registryName}, x=${getX()}, y=${getY()}, z=${getZ()}}"
}
