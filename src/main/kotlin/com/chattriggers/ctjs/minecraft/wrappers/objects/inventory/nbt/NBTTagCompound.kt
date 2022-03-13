package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.nbt

import com.chattriggers.ctjs.utils.kotlin.MCNBTBase
import com.chattriggers.ctjs.utils.kotlin.MCNBTTagCompound
import net.minecraft.nbt.NBTTagByteArray
import net.minecraft.nbt.NBTTagIntArray
import net.minecraftforge.common.util.Constants
import org.mozilla.javascript.NativeObject

class NBTTagCompound(override val rawNBT: MCNBTTagCompound) : NBTBase(rawNBT) {
    val tagMap: Map<String, MCNBTBase>
        get() = rawNBT.tagMap

    val keySet: Set<String>
        get() = rawNBT.keySet

    enum class NBTDataType {
        BYTE,
        SHORT,
        INTEGER,
        LONG,
        FLOAT,
        DOUBLE,
        STRING,
        BYTE_ARRAY,
        INT_ARRAY,
        BOOLEAN,
        COMPOUND_TAG,
        TAG_LIST,
    }

    fun getTag(key: String) = when (val tag = rawNBT.getTag(key)) {
        is MCNBTTagCompound -> NBTTagCompound(tag)
        is MCNBTBase -> NBTBase(tag)
        else -> null
    }

    fun getTagId(key: String) = rawNBT.getTagId(key)

    fun getByte(key: String) = rawNBT.getByte(key)

    fun getShort(key: String) = rawNBT.getShort(key)

    fun getInteger(key: String) = rawNBT.getInteger(key)

    fun getLong(key: String) = rawNBT.getLong(key)

    fun getFloat(key: String) = rawNBT.getFloat(key)

    fun getDouble(key: String) = rawNBT.getDouble(key)

    fun getString(key: String) = rawNBT.getString(key)

    fun getByteArray(key: String) = rawNBT.getByteArray(key)

    fun getIntArray(key: String) = rawNBT.getIntArray(key)

    fun getBoolean(key: String) = rawNBT.getBoolean(key)

    fun getCompoundTag(key: String) = NBTTagCompound(rawNBT.getCompoundTag(key))

    fun getTagList(key: String, type: Int) = rawNBT.getTagList(key, type)

    fun get(key: String, type: NBTDataType, tagType: Int?): Any? {
        return when (type) {
            NBTDataType.BYTE -> getByte(key)
            NBTDataType.SHORT -> getShort(key)
            NBTDataType.INTEGER -> getInteger(key)
            NBTDataType.LONG -> getLong(key)
            NBTDataType.FLOAT -> getFloat(key)
            NBTDataType.DOUBLE -> getDouble(key)
            NBTDataType.STRING -> {
                if (rawNBT.hasKey(key, Constants.NBT.TAG_STRING))
                    tagMap[key]?.let { NBTBase(it).toString() }
                else null
            }
            NBTDataType.BYTE_ARRAY -> {
                if (rawNBT.hasKey(key, Constants.NBT.TAG_BYTE_ARRAY))
                        (tagMap[key] as NBTTagByteArray).byteArray
                else null
            }
            NBTDataType.INT_ARRAY -> {
                if (rawNBT.hasKey(key, Constants.NBT.TAG_INT_ARRAY))
                        (tagMap[key] as NBTTagIntArray).intArray
                else null
            }
            NBTDataType.BOOLEAN -> getBoolean(key)
            NBTDataType.COMPOUND_TAG -> getCompoundTag(key)
            NBTDataType.TAG_LIST -> getTagList(
                key,
                tagType
                    ?: throw IllegalArgumentException("For accessing a tag list you need to provide the tagType argument")
            )
        }
    }

    operator fun get(key: String): NBTBase? = getTag(key)

    fun setNBTBase(key: String, value: NBTBase) = setNBTBase(key, value.rawNBT)

    fun setNBTBase(key: String, value: MCNBTBase) = apply {
        rawNBT.setTag(key, value)
    }

    fun setByte(key: String, value: Byte) = apply {
        rawNBT.setByte(key, value)
    }

    fun setShort(key: String, value: Short) = apply {
        rawNBT.setShort(key, value)
    }

    fun setInteger(key: String, value: Int) = apply {
        rawNBT.setInteger(key, value)
    }

    fun setLong(key: String, value: Long) = apply {
        rawNBT.setLong(key, value)
    }

    fun setFloat(key: String, value: Float) = apply {
        rawNBT.setFloat(key, value)
    }

    fun setDouble(key: String, value: Double) = apply {
        rawNBT.setDouble(key, value)
    }

    fun setString(key: String, value: String) = apply {
        rawNBT.setString(key, value)
    }

    fun setByteArray(key: String, value: ByteArray) = apply {
        rawNBT.setByteArray(key, value)
    }

    fun setIntArray(key: String, value: IntArray) = apply {
        rawNBT.setIntArray(key, value)
    }

    fun setBoolean(key: String, value: Boolean) = apply {
        rawNBT.setBoolean(key, value)
    }

    operator fun set(key: String, value: Any) = apply {
        when (value) {
            is NBTBase -> setNBTBase(key, value)
            is MCNBTBase -> setNBTBase(key, value)
            is Byte -> setByte(key, value)
            is Short -> setShort(key, value)
            is Int -> setInteger(key, value)
            is Long -> setLong(key, value)
            is Float -> setFloat(key, value)
            is Double -> setDouble(key, value)
            is String -> setString(key, value)
            is ByteArray -> setByteArray(key, value)
            is IntArray -> setIntArray(key, value)
            is Boolean -> setBoolean(key, value)
            else -> throw IllegalArgumentException("Unsupported NBT type: ${value.javaClass.simpleName}")
        }
    }

    fun removeTag(key: String) = apply {
        rawNBT.removeTag(key)
    }

    fun toObject(): NativeObject {
        return rawNBT.toObject()
    }
}
