package com.chattriggers.ctjs.launch.mixins.transformers

import net.minecraft.client.gui.hud.PlayerListHud
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

@Mixin(PlayerListHud::class)
interface PlayerListHudAccessor {
    @Accessor()
    fun isVisible(): Boolean
}
