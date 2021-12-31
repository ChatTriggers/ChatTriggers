package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.nbt

import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.nbt.NBTBase.Companion.toObject
import net.minecraft.nbt.*
import org.mozilla.javascript.NativeArray
import org.mozilla.javascript.NativeObject

open class NBTBase(open val rawNBT: NbtElement) {
    // TODO(BREAKING): change name from id to type
    /**
     * Gets the type byte for the tag.
     */
    val type: Byte
        get() = rawNBT.type

    /**
     * Creates a clone of the tag.
     */
    fun copy() = rawNBT.copy()

    // TODO(BREAKING): Remove these
    // /**
    //  * Return whether this compound has no tags.
    //  */
    // fun hasNoTags() = rawNBT.hasNoTags()
    //
    // fun hasTags() = !rawNBT.hasNoTags()

    override fun equals(other: Any?) = rawNBT == other

    override fun hashCode() = rawNBT.hashCode()

    override fun toString() = rawNBT.toString()

    companion object {
        fun NbtElement.toObject(): Any? {
            return when (this) {
                is NbtString -> this.asString()
                is NbtByte -> this.byteValue()
                is NbtShort -> this.shortValue()
                is NbtInt -> this.intValue()
                is NbtLong -> this.longValue()
                is NbtFloat -> this.floatValue()
                is NbtDouble -> this.doubleValue()
                is NbtCompound -> this.toObject()
                is NbtByteArray -> NativeArray(this.byteArray.toTypedArray()).expose()
                is NbtIntArray -> NativeArray(this.intArray.toTypedArray()).expose()
                is NbtList -> this.toObject()
                else -> error("Unknown tag type $javaClass")
            }
        }

        fun NbtCompound.toObject(): NativeObject {
            val o = NativeObject()
            o.expose()

            this.keys
                .map { it to this[it] }
                .filter { it.second != null }
                .forEach { (key, value) -> o.put(key, o, value!!.toObject()) }

            return o
        }

        fun NbtList.toObject(): NativeArray {
            val array = NativeArray(this.map { it.toObject() }.toTypedArray())
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
