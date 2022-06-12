package com.chattriggers.ctjs.minecraft.wrappers.utils

data class ResourceLocation(val namespace: String, val path: String) {
    init {
        if (!VALID_NAMESPACE_REGEX.matches(namespace))
            throw IllegalArgumentException("Invalid ResourceLocation namespace: $namespace")
        if (!VALID_PATH_REGEX.matches(path))
            throw IllegalArgumentException("Invalid ResourceLocation path: $path")
    }

    constructor(resourceLocation: net.minecraft.util.ResourceLocation) :
    //#if MC<=11202
        this(resourceLocation.resourceDomain, resourceLocation.resourcePath)
    //#else
    //$$     this(resourceLocation.namespace, resourceLocation.path)
    //#endif

    constructor(path: String) : this(path.substringBefore(':', "minecraft"), path.substringAfter(':'))

    //#if MC<=11202
    fun toMC() = net.minecraft.util.ResourceLocation(namespace, path)
    //#elseif MC>=11701
    //#if FORGE
    //$$ fun toMC() = net.minecraft.resources.ResourceLocation(namespace, path)
    //#else
    //$$ fun toMC() = net.minecraft.util.Identifier(namespace, path)
    //#endif
    //#endif

    override fun toString() = "$namespace:$path"

    companion object {
        private val VALID_NAMESPACE_REGEX = """^[a-z\d_\-.]+$""".toRegex()
        private val VALID_PATH_REGEX = """[a-z\d_\-.\\]""".toRegex()
    }
}