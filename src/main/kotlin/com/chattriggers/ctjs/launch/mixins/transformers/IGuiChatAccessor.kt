package com.chattriggers.ctjs.launch.mixins.transformers

import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.GuiTextField
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

@Mixin(GuiChat::class)
interface IGuiChatAccessor {
    @Accessor
    fun getInputField(): GuiTextField
}

fun GuiChat.asMixinAccessor() = this as IGuiChatAccessor
