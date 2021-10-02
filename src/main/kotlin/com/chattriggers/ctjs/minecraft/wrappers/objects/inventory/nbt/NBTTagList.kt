package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.nbt

import com.chattriggers.ctjs.utils.kotlin.MCNBTBase
import com.chattriggers.ctjs.utils.kotlin.MCNBTTagList

class NBTTagList(override val rawNBT: MCNBTTagList) : NBTBase(rawNBT) {
    val tagCount: Int
        //#if MC==11602
        //$$ get() = rawNBT.size
        //#else
        get() = rawNBT.tagCount()
        //#endif

    fun appendTag(nbt: NBTBase) = apply {
        //#if MC==11602
        //$$ rawNBT.add(nbt.rawNBT)
        //#else
        rawNBT.appendTag(nbt.rawNBT)
        //#endif
    }

    fun appendTag(nbt: MCNBTBase) = apply {
        //#if MC==11602
        //$$ rawNBT.add(nbt)
        //#else
        rawNBT.appendTag(nbt)
        //#endif
    }

    operator fun set(id: Int, nbt: NBTBase) {
        rawNBT.set(id, nbt.rawNBT)
    }

    operator fun set(id: Int, nbt: MCNBTBase) {
        rawNBT.set(id, nbt)
    }

    fun removeTag(index: Int) {
        //#if MC==11602
        //$$ rawNBT.removeAt(index)
        //#else
        rawNBT.removeTag(index)
        //#endif
    }

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
}
