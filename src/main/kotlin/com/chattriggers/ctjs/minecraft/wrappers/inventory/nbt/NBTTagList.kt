package com.chattriggers.ctjs.minecraft.wrappers.inventory.nbt

import com.chattriggers.ctjs.utils.kotlin.MCNBTBase
import com.chattriggers.ctjs.utils.kotlin.MCNBTTagList
import org.mozilla.javascript.NativeArray

class NBTTagList(override val rawNBT: MCNBTTagList) : NBTBase(rawNBT) {
    val tagCount: Int
        get() = rawNBT.tagCount()

    fun appendTag(nbt: NBTBase) = appendTag(nbt.rawNBT)

    fun appendTag(nbt: MCNBTBase) = apply {
        rawNBT.appendTag(nbt)
    }

    operator fun set(id: Int, nbt: NBTBase) = set(id, nbt.rawNBT)

    operator fun set(id: Int, nbt: MCNBTBase) = apply {
        rawNBT.set(id, nbt)
    }

    fun insertTag(index: Int, nbt: NBTBase) = insertTag(index, nbt.rawNBT)

    fun insertTag(index: Int, nbt: MCNBTBase) = apply {
        rawNBT.tagList.add(index, nbt)
    }

    fun removeTag(index: Int) = rawNBT.removeTag(index)

    fun getCompoundTagAt(index: Int) = rawNBT.getCompoundTagAt(index)

    fun getIntArrayAt(index: Int) = rawNBT.getIntArrayAt(index)

    fun getDoubleAt(index: Int) = rawNBT.getDoubleAt(index)

    fun getFloatAt(index: Int) = rawNBT.getFloatAt(index)

    fun getStringTagAt(index: Int) = rawNBT.getStringTagAt(index)

    operator fun get(index: Int) = rawNBT[index]

    fun get(index: Int, type: NBTTagCompound.NBTDataType): Any? = when (type) {
        NBTTagCompound.NBTDataType.FLOAT -> getFloatAt(index)
        NBTTagCompound.NBTDataType.DOUBLE -> getDoubleAt(index)
        NBTTagCompound.NBTDataType.STRING -> getStringTagAt(index)
        NBTTagCompound.NBTDataType.INT_ARRAY -> getIntArrayAt(index)
        NBTTagCompound.NBTDataType.COMPOUND_TAG -> getCompoundTagAt(index)
        else -> get(index)
    }

    fun toArray(): NativeArray = rawNBT.toObject()
}
