package com.chattriggers.ctjs.launch.mixins.transformers

import net.minecraft.nbt.NbtElement
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

@Mixin(NbtCompoundAccessor::class)
interface NbtCompoundAccessor {
    @Accessor
    fun getEntries(): MutableMap<String, NbtElement>
}
