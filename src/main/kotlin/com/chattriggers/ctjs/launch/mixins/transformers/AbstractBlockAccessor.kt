package com.chattriggers.ctjs.launch.mixins.transformers

import net.minecraft.block.AbstractBlock
import net.minecraft.block.Material
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

@Mixin(AbstractBlock::class)
interface AbstractBlockAccessor {
    @Accessor
    fun getMaterial(): Material
}
