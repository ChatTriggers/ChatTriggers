package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.nbt

import com.chattriggers.ctjs.launch.mixins.asMixin
import com.chattriggers.ctjs.launch.mixins.transformers.NbtCompoundAccessor
import net.minecraft.nbt.*
import org.mozilla.javascript.NativeObject

class NBTTagCompound(override val rawNBT: NbtCompound) : NBTBase(rawNBT) {
    val tagMap: MutableMap<String, NbtElement>
        get() = rawNBT.asMixin<NbtCompoundAccessor>().getEntries()

    val keySet: Set<String>
        get() = rawNBT.keys

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

    fun getTag(key: String) = when (val tag = rawNBT[key]) {
        is NbtCompound -> NBTTagCompound(tag)
        is NbtList -> NBTTagList(tag)
        is NbtElement -> NBTBase(tag)
        null -> null
        else -> throw IllegalStateException("Unknown nbt class ${tag::class.simpleName}")
    }

    fun getTagId(key: String) = rawNBT.getType(key)

    fun getByte(key: String) = rawNBT.getByte(key)

    fun getShort(key: String) = rawNBT.getShort(key)

    fun getInteger(key: String) = rawNBT.getInt(key)

    fun getLong(key: String) = rawNBT.getLong(key)

    fun getFloat(key: String) = rawNBT.getFloat(key)

    fun getDouble(key: String) = rawNBT.getDouble(key)

    fun getString(key: String) = rawNBT.getString(key)

    fun getByteArray(key: String) = rawNBT.getByteArray(key)

    fun getIntArray(key: String) = rawNBT.getIntArray(key)

    fun getBoolean(key: String) = rawNBT.getBoolean(key)

    fun getCompoundTag(key: String) = NBTTagCompound(rawNBT.getCompound(key))

    fun getTagList(key: String, type: Int) = rawNBT.getList(key, type)

    fun get(key: String, type: NBTDataType, tagType: Int?): Any? {
        return when (type) {
            NBTDataType.BYTE -> getByte(key)
            NBTDataType.SHORT -> getShort(key)
            NBTDataType.INTEGER -> getInteger(key)
            NBTDataType.LONG -> getLong(key)
            NBTDataType.FLOAT -> getFloat(key)
            NBTDataType.DOUBLE -> getDouble(key)
            NBTDataType.STRING -> if (rawNBT.contains(key, 8)) tagMap[key]?.let { NBTBase(it).toString() } else null
            NBTDataType.BYTE_ARRAY -> if (rawNBT.contains(key, 7)) (tagMap[key] as NbtByteArray).byteArray else null
            NBTDataType.INT_ARRAY -> if (rawNBT.contains(key, 11)) (tagMap[key] as NbtIntArray).intArray else null
            NBTDataType.BOOLEAN -> getBoolean(key)
            NBTDataType.COMPOUND_TAG -> getCompoundTag(key)
            NBTDataType.TAG_LIST -> getTagList(
                key,
                tagType ?: throw IllegalArgumentException("For accessing a tag list you need to provide the tagType argument")
            )
        }
    }

    operator fun get(key: String): NBTBase? = getTag(key)

    operator fun set(key: String, value: Any) = apply {
        when (value) {
            is NBTBase -> rawNBT.put(key, value.rawNBT)
            is Byte -> rawNBT.putByte(key, value)
            is Short -> rawNBT.putShort(key, value)
            is Int -> rawNBT.putInt(key, value)
            is Long -> rawNBT.putLong(key, value)
            is Float -> rawNBT.putFloat(key, value)
            is Double -> rawNBT.putDouble(key, value)
            is String -> rawNBT.putString(key, value)
            is ByteArray -> rawNBT.putByteArray(key, value)
            is IntArray -> rawNBT.putIntArray(key, value)
            is Boolean -> rawNBT.putBoolean(key, value)
            else -> throw IllegalArgumentException("Unsupported NBT type: ${value.javaClass.simpleName}")
        }
    }

    fun removeTag(key: String) = apply {
        rawNBT.remove(key)
    }

    fun toObject(): NativeObject {
        return rawNBT.toObject()
    }
}
