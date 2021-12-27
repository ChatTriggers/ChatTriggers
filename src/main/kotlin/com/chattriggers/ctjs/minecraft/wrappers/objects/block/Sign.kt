package com.chattriggers.ctjs.minecraft.wrappers.objects.block

import com.chattriggers.ctjs.launch.mixins.asMixin
import com.chattriggers.ctjs.launch.mixins.transformers.SignBlockEntityAccessor
import com.chattriggers.ctjs.minecraft.objects.message.Message
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.block.entity.SignBlockEntity

/**
 * Creates a new Sign object wrapper.
 * Returned with [com.chattriggers.ctjs.minecraft.wrappers.Player.lookingAt] when looking at a sign.
 * Extends [Block].
 *
 * @param block the [Block] to convert to a Sign
 */
@External
class Sign(block: Block) : Block(block.type, block.pos, block.face) {
    private val sign = World.getWorld()!!.getBlockEntity(pos.toMCBlockPos()) as SignBlockEntity

    fun getLines(): List<Message> = sign
        .asMixin<SignBlockEntityAccessor>()
        .getTexts(false) // TODO("fabric"): What does the filtered parameter do?
        .map(::Message) // TODO("fabric"): Is the text nullable? If not, perhaps make
                        //                 blank entries null?

    fun getFormattedLines(): List<String> = getLines().map { it.getFormattedText() }

    fun getUnformattedLines(): List<String> = getLines().map { it.getUnformattedText() }

    override fun toString(): String =
        "Sign{lines=${getLines()}, name=${type.getRegistryName()}, x=$x, y=$y, z=$z}"
}
