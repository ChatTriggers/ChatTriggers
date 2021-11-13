package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.nbt

import com.chattriggers.ctjs.utils.kotlin.MCNBTBase
import com.chattriggers.ctjs.utils.kotlin.MCNBTTagCompound
import com.chattriggers.ctjs.utils.kotlin.MCNBTTagList
import com.reevajs.reeva.core.Agent
import com.reevajs.reeva.jvmcompat.JVMValueMapper
import com.reevajs.reeva.runtime.JSValue
import com.reevajs.reeva.runtime.arrays.JSArrayObject
import com.reevajs.reeva.runtime.objects.JSObject
import net.minecraft.nbt.*

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
        fun MCNBTBase.toObject(): JSValue {
            return when (this) {
                is NBTTagString -> this.string
                is NBTTagByte -> this.byte
                is NBTTagShort -> this.short
                is NBTTagInt -> this.int
                is NBTTagLong -> this.long
                is NBTTagFloat -> this.float
                is NBTTagDouble -> this.double
                is MCNBTTagCompound -> return this.toObject()
                is MCNBTTagList -> return this.toObject()
                is NBTTagByteArray -> byteArray.toTypedArray()
                is NBTTagIntArray -> intArray.toTypedArray()
                else -> error("Unknown tag type ${this.javaClass}")
            }.let {
                JVMValueMapper.jvmToJS(Agent.activeRealm, it)
            }
        }

        fun MCNBTTagCompound.toObject(): JSValue {
            val obj = JSObject.create(Agent.activeRealm)

            for (key in keySet) {
                val value = tagMap[key]
                if (value != null)
                    obj.set(key, obj, value.toObject())
            }

            return obj
        }

        fun MCNBTTagList.toObject(): JSValue {
            val array = JSArrayObject.create(Agent.activeRealm)
            for (i in 0 until tagCount())
                array.set(i, get(i).toObject())
            return array
        }
    }
}
