package com.chattriggers.ctjs.minecraft.wrappers.inventory.nbt

import com.chattriggers.ctjs.utils.kotlin.MCNBTBase
import com.chattriggers.ctjs.utils.kotlin.MCNBTTagCompound
import com.chattriggers.ctjs.utils.kotlin.MCNBTTagList
import net.minecraft.nbt.*
import org.graalvm.polyglot.Value

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
        fun MCNBTBase.toObject(): Value {
            return when (this) {
                is NBTTagString -> Value.asValue(string)
                is NBTTagByte -> Value.asValue(byte)
                is NBTTagShort -> Value.asValue(short)
                is NBTTagInt -> Value.asValue(int)
                is NBTTagLong -> Value.asValue(long)
                is NBTTagFloat -> Value.asValue(float)
                is NBTTagDouble -> Value.asValue(double)
                is MCNBTTagCompound -> toObject()
                is MCNBTTagList -> toObject()
                is NBTTagByteArray -> Value.asValue(byteArray.toTypedArray())
                is NBTTagIntArray -> Value.asValue(intArray.toTypedArray())
                else -> error("Unknown tag type $javaClass")
            }
        }

        fun MCNBTTagCompound.toObject(): Value {
            val map = mutableMapOf<String, Value>()

            for (key in keySet) {
                val value = tagMap[key]
                if (value != null)
                    map[key] = value.toObject()
            }

            return Value.asValue(map)
        }

        fun MCNBTTagList.toObject(): Value {
            val tags = mutableListOf<Value>()
            for (i in 0 until tagCount())
                tags.add(get(i).toObject())
            return Value.asValue(tags)
        }
    }
}
