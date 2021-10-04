package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.nbt

import com.chattriggers.ctjs.launch.mixins.transformers.asMixinAccessor
import com.chattriggers.ctjs.utils.kotlin.MCNBTBase
import com.chattriggers.ctjs.utils.kotlin.MCNBTTagCompound
import net.minecraft.nbt.NBTTagByteArray
import net.minecraft.nbt.NBTTagIntArray
import org.mozilla.javascript.NativeObject

class NBTTagCompound(override val rawNBT: MCNBTTagCompound) : NBTBase(rawNBT) {
    // TODO(1.16.2)
    //#if MC==10809
    val tagMap: Map<String, MCNBTBase>
        get() = rawNBT.asMixinAccessor().getTagMap()
    //#endif

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
        is MCNBTBase -> NBTBase(tag)
        is MCNBTTagCompound -> NBTTagCompound(tag)
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
            NBTDataType.BOOLEAN -> getBoolean(key)
            NBTDataType.COMPOUND_TAG -> getCompoundTag(key)
            NBTDataType.TAG_LIST -> getTagList(
                key,
                tagType ?: throw IllegalArgumentException("For accessing a tag list you need to provide the tagType argument"),
            )
            //#if MC==11602
            //$$ NBTDataType.STRING -> if (rawNBT.contains(key, 8)) (rawNBT.get(key) as NBTBase).toString() else null
            //$$ NBTDataType.BYTE_ARRAY -> if (rawNBT.contains(key, 7)) (rawNBT.get(key) as ByteArrayNBT).byteArray else null
            //$$ NBTDataType.INT_ARRAY -> if (rawNBT.contains(key, 11)) (rawNBT.get(key) as IntArrayNBT).intArray else null
            //#else
            NBTDataType.STRING -> if (rawNBT.hasKey(key, 8)) (tagMap[key] as NBTBase).toString() else null
            NBTDataType.BYTE_ARRAY -> if (rawNBT.hasKey(key, 7)) (tagMap[key] as NBTTagByteArray).byteArray else null
            NBTDataType.INT_ARRAY -> if (rawNBT.hasKey(key, 11)) (tagMap[key] as NBTTagIntArray).intArray else null
            //#endif
        }
    }

    operator fun get(key: String): NBTBase? = getTag(key)

    operator fun set(key: String, value: Any) = apply {
        when (value) {
            //#if MC==11602
            //$$ is NBTBase -> rawNBT.put(key, value.rawNBT)
            //$$ is MCNBTBase -> rawNBT.put(key, value)
            //#else
            is NBTBase -> rawNBT.setTag(key, value.rawNBT)
            is MCNBTBase -> rawNBT.setTag(key, value)
            //#endif
            is Byte -> rawNBT.setByte(key, value)
            is Short -> rawNBT.setShort(key, value)
            is Int -> rawNBT.setInteger(key, value)
            is Long -> rawNBT.setLong(key, value)
            is Float -> rawNBT.setFloat(key, value)
            is Double -> rawNBT.setDouble(key, value)
            is String -> rawNBT.setString(key, value)
            is ByteArray -> rawNBT.setByteArray(key, value)
            is IntArray -> rawNBT.setIntArray(key, value)
            is Boolean -> rawNBT.setBoolean(key, value)
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
