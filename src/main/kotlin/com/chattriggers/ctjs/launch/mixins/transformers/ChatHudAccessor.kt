package com.chattriggers.ctjs.launch.mixins.transformers

import net.minecraft.client.gui.hud.ChatHud
import net.minecraft.client.gui.hud.ChatHudLine
import net.minecraft.text.OrderedText
import net.minecraft.text.Text
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor
import org.spongepowered.asm.mixin.gen.Invoker

@Mixin(ChatHud::class)
interface ChatHudAccessor {
    @Accessor
    fun getMessageHistory(): MutableList<String>

    @Accessor
    fun getMessages(): MutableList<ChatHudLine<Text>>

    @Accessor
    fun getVisibleMessages(): MutableList<ChatHudLine<OrderedText>>

    @Invoker
    fun addMessage(message: Text, messageId: Int)

    @Invoker
    fun removeMessage(messageId: Int)
}
