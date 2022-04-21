package com.chattriggers.ctjs.minecraft.wrappers.inventory.nbt

import com.chattriggers.ctjs.utils.kotlin.MCNBTBase
import com.chattriggers.ctjs.utils.kotlin.MCNBTTagCompound
import com.chattriggers.ctjs.utils.kotlin.MCNBTTagList
import net.minecraft.nbt.*
import org.mozilla.javascript.NativeArray
import org.mozilla.javascript.NativeObject

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

    companion object {
        fun MCNBTBase.toObject(): Any? {
            return when (this) {
                is NBTTagString -> string
                is NBTTagByte -> byte
                is NBTTagShort -> short
                is NBTTagInt -> int
                is NBTTagLong -> long
                is NBTTagFloat -> float
                is NBTTagDouble -> double
                is MCNBTTagCompound -> toObject()
                is MCNBTTagList -> toObject()
                is NBTTagByteArray -> NativeArray(byteArray.toTypedArray()).expose()
                is NBTTagIntArray -> NativeArray(intArray.toTypedArray()).expose()
                else -> error("Unknown tag type $javaClass")
            }
        }

        fun MCNBTTagCompound.toObject(): NativeObject {
            val o = NativeObject()
            o.expose()

            for (key in keySet) {
                val value = tagMap[key]
                if (value != null) {
                    o.put(key, o, value.toObject())
                }
            }

            return o
        }

        fun MCNBTTagList.toObject(): NativeArray {
            val tags = mutableListOf<Any?>()
            for (i in 0 until tagCount()) {
                tags.add(get(i).toObject())
            }
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
