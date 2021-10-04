package com.chattriggers.ctjs.launch.mixins.transformers

import net.minecraft.client.gui.GuiPlayerTabOverlay
import net.minecraft.util.IChatComponent
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

@Mixin(GuiPlayerTabOverlay::class)
interface IGuiPlayerTabOverlayAccessor {
    @Accessor
    fun getHeader(): IChatComponent

    @Accessor
    fun getFooter(): IChatComponent
}

fun GuiPlayerTabOverlay.asMixinAccessor() = this as IGuiPlayerTabOverlayAccessor
