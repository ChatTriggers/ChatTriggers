package com.chattriggers.ctjs.launch.mixins.transformers

import com.mojang.authlib.minecraft.client.MinecraftClient
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

@Mixin(MinecraftClient::class)
interface MinecraftClientAccessor {
    @Accessor
    fun getCurrentFps(): Int
}
