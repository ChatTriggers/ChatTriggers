package com.chattriggers.ctjs.launch.mixins

import com.chattriggers.ctjs.engine.module.ModuleManager
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

// Simple function that makes the cast intent clear
inline fun <reified T> Any.asMixin() = this as T

class Plugin : IMixinConfigPlugin {
    override fun onLoad(mixinPackage: String?) {
        // TODO: It'd be better to call these functions after our mixins have applied
        //       so that all of the CT module asm injections happen after the builtin
        //       injections
        // ModuleManager.setup()
        // ModuleManager.asmPass()
    }

    override fun getRefMapperConfig(): String? = null

    override fun shouldApplyMixin(targetClassName: String?, mixinClassName: String?): Boolean = true

    override fun acceptTargets(myTargets: MutableSet<String>?, otherTargets: MutableSet<String>?) {
    }

    override fun getMixins(): MutableList<String>? = null

    override fun preApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ) {
    }

    override fun postApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ) {
    }
}