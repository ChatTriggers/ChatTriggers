package com.chattriggers.ctjs.launch.mixins.transformers

import net.minecraft.client.audio.SoundHandler
import net.minecraft.client.audio.SoundManager
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

@Mixin(SoundHandler::class)
interface ISoundHandlerAccessor {
    @Accessor
    fun getSndManager(): SoundManager
}

fun SoundHandler.asMixinAccessor() = this as ISoundHandlerAccessor