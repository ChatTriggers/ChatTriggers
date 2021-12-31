package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.nbt

import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList

class NBTTagList(override val rawNBT: NbtList) : NBTBase(rawNBT) {
    val tagCount: Int
        get() = rawNBT.size

    fun appendTag(nbt: NBTBase) = apply {
        rawNBT.add(nbt.rawNBT)
    }

    fun appendTag(nbt: NbtElement) = apply {
        rawNBT.add(nbt)
    }

    operator fun set(id: Int, nbt: NBTBase) {
        rawNBT[id] = nbt.rawNBT
    }

    operator fun set(id: Int, nbt: NbtElement) {
        rawNBT[id] = nbt
    }

    fun removeTag(index: Int) = rawNBT.removeAt(index)

    fun getCompoundTagAt(index: Int) = rawNBT.getCompound(index)

    fun getIntArrayAt(index: Int) = rawNBT.getIntArray(index)

    fun getDoubleAt(index: Int) = rawNBT.getDouble(index)

    fun getFloatAt(index: Int) = rawNBT.getFloat(index)

    fun getStringTagAt(index: Int) = rawNBT.getString(index)

    operator fun get(index: Int) = rawNBT[index]

    fun get(index: Int, type: NBTTagCompound.NBTDataType): Any? = when (type) {
        NBTTagCompound.NBTDataType.FLOAT -> getFloatAt(index)
        NBTTagCompound.NBTDataType.DOUBLE -> getDoubleAt(index)
        NBTTagCompound.NBTDataType.STRING -> getStringTagAt(index)
        NBTTagCompound.NBTDataType.INT_ARRAY -> getIntArrayAt(index)
        NBTTagCompound.NBTDataType.COMPOUND_TAG -> getCompoundTagAt(index)
        else -> get(index)
    }
}
