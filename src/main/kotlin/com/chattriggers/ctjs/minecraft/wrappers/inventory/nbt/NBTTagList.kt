package com.chattriggers.ctjs.minecraft.wrappers.inventory.nbt

import com.chattriggers.ctjs.utils.kotlin.MCNBTBase
import com.chattriggers.ctjs.utils.kotlin.MCNBTTagList
import com.chattriggers.ctjs.utils.kotlin.asMixin

//#if MC<=11202
import com.chattriggers.ctjs.launch.mixins.transformers.NBTTagListAccessor
//#else
//$$ import com.chattriggers.ctjs.launch.mixins.transformers.ListTagAccessor
//$$ import com.chattriggers.ctjs.utils.kotlin.asMixin
//#endif

class NBTTagList(override val rawNBT: MCNBTTagList) : NBTBase(rawNBT) {
    val tagCount: Int
        get() {
            //#if MC<=11202
            return rawNBT.tagCount()
            //#else
            //$$ return rawNBT.size
            //#endif
        }

    fun appendTag(nbt: NBTBase) = appendTag(nbt.rawNBT)

    fun appendTag(nbt: MCNBTBase) = apply {
        //#if MC<=11202
        rawNBT.appendTag(nbt)
        //#else
        //$$ rawNBT.add(nbt)
        //#endif
    }

    operator fun set(id: Int, nbt: NBTBase) = set(id, nbt.rawNBT)

    operator fun set(id: Int, nbt: MCNBTBase) = apply {
        rawNBT.set(id, nbt)
    }

    fun insertTag(index: Int, nbt: NBTBase) = insertTag(index, nbt.rawNBT)

    fun insertTag(index: Int, nbt: MCNBTBase) = apply {
        //#if MC<=11202
        rawNBT.asMixin<NBTTagListAccessor>().tagList.add(index, nbt)
        //#else
        //$$ rawNBT.asMixin<ListTagAccessor>().list.add(index, nbt)
        //#endif
    }

    // TODO(BREAKING): Wrap return value
    fun removeTag(index: Int): NBTBase {
        //#if MC<=11202
        return NBTBase(rawNBT.removeTag(index))
        //#else
        //$$ return NBTBase(rawNBT.removeAt(index))
        //#endif
    }

    fun clear() {
        //#if MC<=11202
        rawNBT.asMixin<NBTTagListAccessor>().tagList.clear()
        //#else
        //$$ rawNBT.clear()
        //#endif
    }

    // TODO(BREAKING): Wrap return value
    fun getCompoundTagAt(index: Int): NBTTagCompound {
        //#if MC<=11202
        return NBTTagCompound(rawNBT.getCompoundTagAt(index))
        //#else
        //$$ return NBTTagCompound(rawNBT.getCompound(index))
        //#endif
    }

    fun getIntArrayAt(index: Int): IntArray {
        //#if MC<=11202
        return rawNBT.getIntArrayAt(index)
        //#else
        //$$ return rawNBT.getIntArray(index)
        //#endif
    }

    fun getDoubleAt(index: Int): Double {
        //#if MC<=11202
        return rawNBT.getDoubleAt(index)
        //#else
        //$$ return rawNBT.getDouble(index)
        //#endif
    }

    fun getFloatAt(index: Int): Float {
        //#if MC<=11202
        return rawNBT.getFloatAt(index)
        //#else
        //$$ return rawNBT.getFloat(index)
        //#endif
    }

    fun getStringTagAt(index: Int): String {
        //#if MC<=11202
        return rawNBT.getStringTagAt(index)
        //#else
        //$$ return rawNBT.getString(index)
        //#endif
    }

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
