package com.chattriggers.ctjs.minecraft.wrappers.world.block

import com.chattriggers.ctjs.minecraft.wrappers.World
import gg.essential.universal.wrappers.message.UTextComponent
import net.minecraft.tileentity.TileEntitySign

//#if MC>=11701
//$$ import com.chattriggers.ctjs.mixins.entity.SignBlockEntityAccessor
//$$ import com.chattriggers.ctjs.utils.kotlin.asMixin
//#endif

/**
 * Creates a new Sign object wrapper.
 * Returned with [com.chattriggers.ctjs.minecraft.wrappers.Player.lookingAt] when looking at a sign.
 * Extends [Block].
 *
 * @param block the [Block] to convert to a Sign
 */
class Sign(block: Block) : Block(block.type, block.pos, block.face) {
    private val sign: TileEntitySign = let {
        //#if MC<=11202
        World.getWorld()!!.getTileEntity(pos.toMCBlock()) as TileEntitySign
        //#else
        //$$ World.getWorld()!!.getBlockEntity(pos.toMCBlock()) as SignBlockEntity
        //#endif
    }

    // TODO(BREAKING): Change from List<UMessage> to List<UTextComponent>
    fun getLines(): List<UTextComponent> {
        //#if MC<=11202
        val signLines = sign.signText
        //#else
        //$$ // TODO(VERIFY): Whats the difference between filtered and non-filtered messages?
        //$$ val signLines = sign.asMixin<SignBlockEntityAccessor>().invokeGetMessages(false);
        //#endif

        return signLines.map { it?.let { UTextComponent(it) } ?: UTextComponent("") }
    }

    fun getFormattedLines(): List<String> = getLines().map { it.formattedText }

    fun getUnformattedLines(): List<String> = getLines().map { it.unformattedText }

    override fun toString(): String =
        "Sign{lines=${getLines()}, name=${type.getRegistryName()}, x=$x, y=$y, z=$z}"
}
