package com.chattriggers.ctjs.launch.mixins.transformers

import net.minecraft.client.option.GameOptions
import net.minecraft.client.option.KeyBinding
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Mutable

@Mixin(GameOptions::class)
class GameOptionsMixin {
    @Mutable
    lateinit var keysAll: Array<KeyBinding>
}