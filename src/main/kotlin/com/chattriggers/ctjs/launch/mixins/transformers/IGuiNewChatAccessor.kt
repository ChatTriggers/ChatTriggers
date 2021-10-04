package com.chattriggers.ctjs.launch.mixins.transformers

import net.minecraft.client.gui.ChatLine
import net.minecraft.client.gui.GuiNewChat
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

//#if MC==11602
//$$ typealias MCChatLine = ChatLine<*>
//#else
typealias MCChatLine = ChatLine
//#endif

@Mixin(GuiNewChat::class)
interface IGuiNewChatAccessor {
    @Accessor
    fun getChatLines(): MutableList<MCChatLine>

    @Accessor
    fun getDrawnChatLines(): MutableList<MCChatLine>
}

fun GuiNewChat.asMixinAccessor() = this as IGuiNewChatAccessor