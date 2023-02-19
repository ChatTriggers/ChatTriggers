package com.chattriggers.ctjs.minecraft.wrappers.inventory.nbt

import com.chattriggers.ctjs.utils.kotlin.MCNBTBase
import com.chattriggers.ctjs.utils.kotlin.MCNBTTagCompound
import com.chattriggers.ctjs.utils.kotlin.MCNBTTagList
import com.chattriggers.ctjs.utils.kotlin.getOption
import net.minecraft.nbt.*
import org.mozilla.javascript.NativeArray
import org.mozilla.javascript.NativeObject

object NBT {
    /**
     * Creates a new [NBTBase] from the given [nbt]
     *
     * @param nbt the value to convert to NBT
     * @param options optional argument to allow refinement of the NBT data.
     * Possible options include:
     * - coerceNumericStrings: Boolean, default false.
     * E.g. "10b" as a byte, "20s" as a short, "30f" as a float, "40d" as a double,
     * "50l" as a long
     * - preferArraysOverLists: Boolean, default false
     * E.g. a list with all bytes or integers will be converted to an NBTTagByteArray or
     * NBTTagIntArray accordingly
     *
     * @throws [NBTException] if [nbt] can not be parsed as valid NBT
     *
     * @return [NBTTagCompound] if [nbt] is an object, [NBTTagList] if [nbt]
     * is an array and preferArraysOverLists is false, or [NBTBase] otherwise.
     */
    @JvmOverloads
    @JvmStatic
    @Throws(NBTException::class)
    fun parse(nbt: Any, options: NativeObject? = null): NBTBase {
        return when (nbt) {
            is NativeObject -> NBTTagCompound(nbt.toNBT(options) as MCNBTTagCompound)
            is NativeArray -> {
                nbt.toNBT(options).let {
                    if (it is MCNBTTagList) {
                        NBTTagList(it)
                    } else {
                        NBTBase(it)
                    }
                }
            }
            else -> NBTBase(nbt.toNBT(options))
        }
    }

    @JvmStatic
    fun toObject(nbt: NBTTagCompound): NativeObject = nbt.toObject()

    @JvmStatic
    fun toArray(nbt: NBTTagList): NativeArray = nbt.toArray()

    @Throws(NBTException::class)
    private fun Any.toNBT(options: NativeObject?): MCNBTBase {
        val preferArraysOverLists = options.getOption("preferArraysOverLists", false).toBoolean()
        val coerceNumericStrings = options.getOption("coerceNumericStrings", false).toBoolean()

        return when (this) {
            is NativeObject -> MCNBTTagCompound().apply {
                entries.forEach { entry ->
                    tagMap[entry.key.toString()] = entry.value.toNBT(options)
                }
            }
            is NativeArray -> {
                val normalized = map { it?.toNBT(options) }

                if (!preferArraysOverLists || normalized.isEmpty()) {
                    return MCNBTTagList().apply { tagList = normalized }
                }

                return when {
                    (normalized.all { it is NBTTagByte }) -> {
                        NBTTagByteArray(normalized.map { (it as NBTTagByte).byte }.toByteArray())
                    }

                    (normalized.all { it is NBTTagInt }) -> {
                        NBTTagIntArray(normalized.map { (it as NBTTagInt).int }.toIntArray())
                    }

                    else -> MCNBTTagList().apply { tagList = normalized }
                }
            }
            is Boolean -> NBTTagByte(if (this) 1 else 0)
            is String -> parseString(this, coerceNumericStrings)
            is Byte -> NBTTagByte(this)
            is Short -> NBTTagShort(this)
            is Int -> NBTTagInt(this)
            is Long -> NBTTagLong(this)
            is Float -> NBTTagFloat(this)
            is Double -> NBTTagDouble(this)
            else -> throw NBTException("Invalid NBT. Value provided: $this")
        }
    }

    private val numberNBTFormat = Regex("^([+-]?\\d+\\.?\\d*)([bslfd])?\$", RegexOption.IGNORE_CASE)

    private fun parseString(nbtData: String, coerceNumericStrings: Boolean): MCNBTBase {
        if (!coerceNumericStrings) {
            return NBTTagString(nbtData)
        }

        val res = numberNBTFormat.matchEntire(nbtData)?.groupValues ?: return NBTTagString(nbtData)

        val number = res[1]
        val suffix = res[2]

        return when (suffix.lowercase()) {
            "" -> {
                if (number.contains(".")) {
                    NBTTagDouble(number.toDouble())
                } else {
                    NBTTagInt(number.toInt())
                }
            }
            "b" -> NBTTagByte(number.toByte())
            "s" -> NBTTagShort(number.toShort())
            "l" -> NBTTagLong(number.toLong())
            "f" -> NBTTagFloat(number.toFloat())
            "d" -> NBTTagDouble(number.toDouble())
            else -> NBTTagString(nbtData)
        }
    }
}
