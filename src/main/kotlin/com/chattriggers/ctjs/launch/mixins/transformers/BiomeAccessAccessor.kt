package com.chattriggers.ctjs.launch.mixins.transformers

import net.minecraft.world.biome.source.BiomeAccess
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

@Mixin(BiomeAccess::class)
interface BiomeAccessAccessor {
    @Accessor
    fun getSeed(): Long
}
