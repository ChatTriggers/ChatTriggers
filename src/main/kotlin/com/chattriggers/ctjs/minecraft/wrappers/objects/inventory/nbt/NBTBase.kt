package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.nbt

import com.chattriggers.ctjs.utils.kotlin.MCNBTBase

open class NBTBase(open val rawNBT: MCNBTBase) {
    /**
     * Gets the type byte for the tag.
     */
    val id: Byte
        get() = rawNBT.id

    /**
     * Creates a clone of the tag.
     */
    fun copy() = rawNBT.copy()

    /**
     * Return whether this compound has no tags.
     */
    fun hasNoTags() = rawNBT.hasNoTags()

    fun hasTags() = !rawNBT.hasNoTags()

    override fun equals(other: Any?) = rawNBT == other

    override fun hashCode() = rawNBT.hashCode()

    override fun toString() = rawNBT.toString()
}
