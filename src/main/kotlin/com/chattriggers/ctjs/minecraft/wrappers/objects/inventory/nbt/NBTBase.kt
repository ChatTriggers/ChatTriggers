package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.nbt

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
    fun hasNoTags(): Boolean {
        //#if MC==11602
        //$$ return when (val nbt = rawNBT) {
        //$$     is StringNBT -> nbt.string.isEmpty()
        //$$     is CompoundNBT -> nbt.isEmpty
        //$$     is ListNBT -> nbt.isEmpty()
        //$$     else -> false
        //$$ }
        //#else
        return rawNBT.hasNoTags()
        //#endif
    }

    fun hasTags() = !hasNoTags()

    override fun equals(other: Any?) = rawNBT == other

    override fun hashCode() = rawNBT.hashCode()

    override fun toString() = rawNBT.toString()

    companion object {
        fun MCNBTBase.toObject(): Any? {
            return when (this) {
                is NBTTagString -> this.string
                is NBTTagByte -> this.byte
                is NBTTagShort -> this.short
                is NBTTagInt -> this.int
                is NBTTagLong -> this.long
                is NBTTagFloat -> this.float
                is NBTTagDouble -> this.double
                is MCNBTTagCompound -> this.toObject()
                is MCNBTTagList -> this.toObject()
                is NBTTagByteArray -> NativeArray(byteArray.toTypedArray()).expose()
                is NBTTagIntArray -> NativeArray(intArray.toTypedArray()).expose()
                else -> error("Unknown tag type ${this.javaClass}")
            }
        }

        fun MCNBTTagCompound.toObject(): NativeObject {
            val o = NativeObject()
            o.expose()

            for (key in keySet) {
                //#if MC==11602
                //$$ val value = get(key)
                //#else
                val value = tagMap[key]
                //#endif
                if (value != null) {
                    o.put(key, o, value.toObject())
                }
            }

            return o
        }

        fun MCNBTTagList.toObject(): NativeArray {
            val tags = mutableListOf<Any?>()
            //#if MC==11602
            //$$ for (tag in this)
            //$$     tags.add(tag.toObject())
            //#else
            for (i in 0 until tagCount())
                tags.add(get(i).toObject())
            //#endif
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
