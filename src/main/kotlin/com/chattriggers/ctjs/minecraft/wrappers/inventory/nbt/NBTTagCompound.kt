package com.chattriggers.ctjs.minecraft.wrappers.inventory.nbt

import com.chattriggers.ctjs.launch.mixins.transformers.NBTTagCompoundAccessor
import com.chattriggers.ctjs.utils.kotlin.asMixin
import net.minecraft.nbt.NBTTagByteArray
import net.minecraft.nbt.NBTTagIntArray
import org.mozilla.javascript.NativeObject

class NBTTagCompound(override val rawNBT: net.minecraft.nbt.NBTTagCompound) : NBTBase(rawNBT) {
    val tagMap: Map<String, net.minecraft.nbt.NBTBase> = rawNBT.asMixin<NBTTagCompoundAccessor>().tagMap

    val keySet: Set<String>
        get() {
            //#if MC<=11202
            return rawNBT.keySet
            //#else
            //$$ return rawNBT.allKeys
            //#endif
        }

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

    fun getTag(key: String): NBTBase? {
        //#if MC<=11202
        return when (val tag = rawNBT.getTag(key)) {
        //#else
        //$$ return when (val tag = rawNBT.get(key)) {
        //#endif
            is net.minecraft.nbt.NBTTagCompound -> NBTTagCompound(tag)
            is net.minecraft.nbt.NBTTagList -> NBTTagList(tag)
            is net.minecraft.nbt.NBTBase -> NBTBase(tag)
            else -> null
        }
    }

    fun getTagId(key: String): Byte {
        //#if MC<=11202
        return rawNBT.getTagId(key)
        //#else
        //$$ return rawNBT.getTagType(key)
        //#endif
    }

    fun getByte(key: String) = rawNBT.getByte(key)

    fun getShort(key: String) = rawNBT.getShort(key)

    fun getInteger(key: String): Int {
        //#if MC<=11202
        return rawNBT.getInteger(key)
        //#else
        //$$ return rawNBT.getInt(key)
        //#endif
    }

    fun getLong(key: String) = rawNBT.getLong(key)

    fun getFloat(key: String) = rawNBT.getFloat(key)

    fun getDouble(key: String) = rawNBT.getDouble(key)

    fun getString(key: String) = rawNBT.getString(key)

    fun getByteArray(key: String) = rawNBT.getByteArray(key)

    fun getIntArray(key: String) = rawNBT.getIntArray(key)

    fun getBoolean(key: String) = rawNBT.getBoolean(key)

    fun getCompoundTag(key: String): NBTTagCompound {
        //#if MC<=11202
        return NBTTagCompound(rawNBT.getCompoundTag(key))
        //#else
        //$$ return NBTTagCompound(rawNBT.getCompound(key))
        //#endif
    }

    // TODO(BREAKING): Wrap result
    fun getTagList(key: String, type: Int): NBTTagList {
        //#if MC<=11202
        return NBTTagList(rawNBT.getTagList(key, type))
        //#else
        //$$ return NBTTagList(rawNBT.getList(key, type))
        //#endif
    }

    fun get(key: String, type: NBTDataType, tagType: Int?): Any? {
        return when (type) {
            NBTDataType.BYTE -> getByte(key)
            NBTDataType.SHORT -> getShort(key)
            NBTDataType.INTEGER -> getInteger(key)
            NBTDataType.LONG -> getLong(key)
            NBTDataType.FLOAT -> getFloat(key)
            NBTDataType.DOUBLE -> getDouble(key)
            NBTDataType.STRING -> {
                //#if MC<=11202
                if (rawNBT.hasKey(key, 8)) {
                //#else
                //$$ if (rawNBT.contains(key, 8)) {
                //#endif
                    tagMap[key]?.let { NBTBase(it).toString() }
                } else null
            }
            NBTDataType.BYTE_ARRAY -> {
                //#if MC<=11202
                if (rawNBT.hasKey(key, 7)) {
                    (tagMap[key] as NBTTagByteArray).byteArray
                } else null
                //#else
                //$$ if (rawNBT.contains(key, 7)) {
                //$$     (tagMap[key] as ByteArrayTag).asByteArray
                //$$ } else null
                //#endif
            }
            NBTDataType.INT_ARRAY -> {
                //#if MC<=11202
                if (rawNBT.hasKey(key, 11)) {
                    (tagMap[key] as NBTTagIntArray).intArray
                } else null
                //#else
                //$$ if (rawNBT.contains(key, 11)) {
                //$$     (tagMap[key] as IntArrayTag).asIntArray
                //$$ } else null
                //#endif
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

    fun setNBTBase(key: String, value: net.minecraft.nbt.NBTBase) = apply {
        //#if MC<=11202
        rawNBT.setTag(key, value)
        //#else
        //$$ rawNBT.put(key, value)
        //#endif
    }

    fun setByte(key: String, value: Byte) = apply {
        //#if MC<=11202
        rawNBT.setByte(key, value)
        //#else
        //$$ rawNBT.putByte(key, value)
        //#endif
    }

    fun setShort(key: String, value: Short) = apply {
        //#if MC<=11202
        rawNBT.setShort(key, value)
        //#else
        //$$ rawNBT.putShort(key, value)
        //#endif
    }

    fun setInteger(key: String, value: Int) = apply {
        //#if MC<=11202
        rawNBT.setInteger(key, value)
        //#else
        //$$ rawNBT.putInt(key, value)
        //#endif
    }

    fun setLong(key: String, value: Long) = apply {
        //#if MC<=11202
        rawNBT.setLong(key, value)
        //#else
        //$$ rawNBT.putLong(key, value)
        //#endif
    }

    fun setFloat(key: String, value: Float) = apply {
        //#if MC<=11202
        rawNBT.setFloat(key, value)
        //#else
        //$$ rawNBT.putFloat(key, value)
        //#endif
    }

    fun setDouble(key: String, value: Double) = apply {
        //#if MC<=11202
        rawNBT.setDouble(key, value)
        //#else
        //$$ rawNBT.putDouble(key, value)
        //#endif
    }

    fun setString(key: String, value: String) = apply {
        //#if MC<=11202
        rawNBT.setString(key, value)
        //#else
        //$$ rawNBT.putString(key, value)
        //#endif
    }

    fun setByteArray(key: String, value: ByteArray) = apply {
        //#if MC<=11202
        rawNBT.setByteArray(key, value)
        //#else
        //$$ rawNBT.putByteArray(key, value)
        //#endif
    }

    fun setIntArray(key: String, value: IntArray) = apply {
        //#if MC<=11202
        rawNBT.setIntArray(key, value)
        //#else
        //$$ rawNBT.putIntArray(key, value)
        //#endif
    }

    fun setBoolean(key: String, value: Boolean) = apply {
        //#if MC<=11202
        rawNBT.setBoolean(key, value)
        //#else
        //$$ rawNBT.putBoolean(key, value)
        //#endif
    }

    operator fun set(key: String, value: Any) = apply {
        when (value) {
            is NBTBase -> setNBTBase(key, value)
            is net.minecraft.nbt.NBTBase -> setNBTBase(key, value)
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
        //#if MC<=11202
        rawNBT.removeTag(key)
        //#else
        //$$ rawNBT.remove(key)
        //#endif
    }

    fun toObject(): NativeObject {
        return rawNBT.toObject()
    }
}
