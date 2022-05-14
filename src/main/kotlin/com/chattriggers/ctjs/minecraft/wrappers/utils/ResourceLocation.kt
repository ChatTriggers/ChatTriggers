package com.chattriggers.ctjs.minecraft.wrappers.utils

import com.chattriggers.ctjs.utils.kotlin.MCResourceLocation

data class ResourceLocation(val namespace: String, val path: String) {
    init {
        if (!VALID_NAMESPACE_REGEX.matches(namespace))
            throw IllegalArgumentException("Invalid ResourceLocation namespace: $namespace")
        if (!VALID_PATH_REGEX.matches(path))
            throw IllegalArgumentException("Invalid ResourceLocation path: $path")
    }

    constructor(resourceLocation: MCResourceLocation) : this(resourceLocation.resourceDomain, resourceLocation.resourcePath)

    constructor(path: String) : this(path.substringBefore(':', "minecraft"), path.substringAfter(':'))

    fun toMC() = MCResourceLocation(namespace, path)

    override fun toString() = "$namespace:$path"

    companion object {
        private val VALID_NAMESPACE_REGEX = """^[a-z\d_\-.]+$""".toRegex()
        private val VALID_PATH_REGEX = """[a-z\d_\-.\\]""".toRegex()
    }
}