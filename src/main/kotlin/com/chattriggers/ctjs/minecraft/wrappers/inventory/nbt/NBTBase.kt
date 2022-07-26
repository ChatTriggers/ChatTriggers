package com.chattriggers.ctjs.minecraft.wrappers.inventory.nbt

import com.chattriggers.ctjs.utils.kotlin.asMixin
import net.minecraft.nbt.*
import org.mozilla.javascript.NativeArray
import org.mozilla.javascript.NativeObject

//#if MC<=11202
import com.chattriggers.ctjs.mixins.NBTTagCompoundAccessor
//#endif

open class NBTBase(open val rawNBT: net.minecraft.nbt.NBTBase) {
    /**
     * Gets the type byte for the tag.
     */
    val id: Byte
        get() = rawNBT.id

    /**
     * Creates a clone of the tag.
     */
    fun copy() = rawNBT.copy()

    override fun equals(other: Any?) = rawNBT == other

    override fun hashCode() = rawNBT.hashCode()

    override fun toString() = rawNBT.toString()

    companion object {
        fun net.minecraft.nbt.NBTBase.toObject(): Any? {
            return when (this) {
                //#if MC<=11202
                is NBTTagString -> string
                is NBTTagByte -> byte
                is NBTTagShort -> short
                is NBTTagInt -> int
                is NBTTagLong -> long
                is NBTTagFloat -> float
                is NBTTagDouble -> double
                is net.minecraft.nbt.NBTTagCompound -> toObject()
                is net.minecraft.nbt.NBTTagList -> toObject()
                is NBTTagByteArray -> NativeArray(byteArray.toTypedArray()).expose()
                is NBTTagIntArray -> NativeArray(intArray.toTypedArray()).expose()
                else -> error("Unknown tag type $javaClass")
                //#else
                //$$ is StringTag -> asString
                //$$ is ByteTag -> asByte
                //$$ is ShortTag -> asShort
                //$$ is IntTag -> asInt
                //$$ is LongTag -> asLong
                //$$ is FloatTag -> asFloat
                //$$ is DoubleTag -> asDouble
                //$$ is net.minecraft.nbt.CompoundTag -> toObject()
                //$$ is net.minecraft.nbt.ListTag -> toObject()
                //$$ is ByteArrayTag -> NativeArray(asByteArray.toTypedArray()).expose()
                //$$ is IntArrayTag -> NativeArray(asIntArray.toTypedArray()).expose()
                //$$ else -> error("Unknown tag type $javaClass")
                //#endif
            }
        }

        fun net.minecraft.nbt.NBTTagCompound.toObject(): NativeObject {
            val o = NativeObject()
            o.expose()

            //#if MC<=11202
            for (key in keySet) {
                val value = asMixin<NBTTagCompoundAccessor>().tagMap[key]
            //#else
            //$$ for (key in allKeys) {
            //$$     val value = get(key)
            //#endif
                if (value != null) {
                    o.put(key, o, value.toObject())
                }
            }

            return o
        }

        fun net.minecraft.nbt.NBTTagList.toObject(): NativeArray {
            val tags = mutableListOf<Any?>()
            //#if MC<=11202
            for (i in 0 until tagCount())
            //#else
            //$$ for (i in 0 until size)
            //#endif
                tags.add(get(i).toObject())
            val array = NativeArray(tags.toTypedArray())
            array.expose()
            return array
        }

        private fun NativeArray.expose() = apply {
            // Taken from the private NativeArray#init method
            exportAsJSClass(32, this, false)
        }

        private fun NativeObject.expose() = apply {
            // Taken from the private NativeObject#init method
            exportAsJSClass(12, this, false)
        }
    }
}
