package com.chattriggers.ctjs.minecraft.wrappers.objects.block

import com.chattriggers.ctjs.minecraft.objects.message.Message
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.tileentity.TileEntitySign
import net.minecraft.util.IChatComponent

/**
 * Creates a new Sign object wrapper.<br>
 * Returned with {@link Player#lookingAt()} when looking at a sign.<br>
 * Extends {@link Block}.
 *
 * @param block the {@link Block} to convert to a Sign
 */
@External
class Sign(block: Block) : Block(block.type, block.pos, block.face) {
    private val sign: TileEntitySign = World.getWorld()!!.getTileEntity(pos.toMCBlockPos()) as TileEntitySign

    //#if MC==11602
    //$$ private fun getLinesImpl(): List<ITextComponent> = (0..3).map(sign::getText)
    //#else
    private fun getLinesImpl(): List<IChatComponent> = sign.signText.toList()
    //#endif

    fun getLines(): List<Message> = getLinesImpl().map(::Message)

    fun getFormattedLines(): List<String> = getLinesImpl().map { TextComponent(it).getFormattedText() }

    fun getUnformattedLines(): List<String> = getLinesImpl().map { TextComponent(it).getUnformattedText() }

    override fun toString(): String =
        "Sign{lines=${getLines()}, name=${type.mcBlock.registryName}, x=$x, y=$y, z=$z}"
}
