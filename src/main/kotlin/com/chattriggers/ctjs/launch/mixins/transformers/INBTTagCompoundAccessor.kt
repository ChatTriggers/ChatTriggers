package com.chattriggers.ctjs.launch.mixins.transformers

import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

@Mixin(NBTTagCompound::class)
interface INBTTagCompoundAccessor {
    @Accessor
    fun getTagMap(): MutableMap<String, NBTBase>
}

fun NBTTagCompound.asMixinAccessor() = this as INBTTagCompoundAccessor
