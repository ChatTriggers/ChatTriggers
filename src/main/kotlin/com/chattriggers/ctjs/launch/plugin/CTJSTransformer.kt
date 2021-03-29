package com.chattriggers.ctjs.launch.plugin

import com.chattriggers.ctjs.engine.module.ModuleManager
import me.falsehonesty.asmhelper.BaseClassTransformer
import net.minecraft.launchwrapper.LaunchClassLoader
import org.apache.commons.lang3.SystemUtils

class CTJSTransformer : BaseClassTransformer() {
    private var transforming = false

    override fun setup(classLoader: LaunchClassLoader) {
        super.setup(classLoader)

        classLoader.addTransformerExclusion("ct.") // for proguard builds
        classLoader.addTransformerExclusion("com.chattriggers.ctjs.")
        classLoader.addTransformerExclusion("file__") // for rhino generated classes
        classLoader.addTransformerExclusion("com.google.gson.")
        classLoader.addTransformerExclusion("org.mozilla.javascript")
        classLoader.addTransformerExclusion("org.mozilla.classfile")
        classLoader.addTransformerExclusion("com.fasterxml.jackson.core.Version")
        classLoader.addTransformerExclusion("me.falsehonesty.asmhelper.")
        classLoader.addTransformerExclusion("org.fife.")
    }

    override fun makeTransformers() {
        if (transforming) return
        transforming = true

        try {
            injectCrashReport()
            injectEntityPlayer()
            injectGuiMainMenu()
            injectMinecraft()
            injectScreenshotHelper()
            injectNetHandlerPlayClient()
            injectRenderManager()
            injectPacketThreadUtil()
            injectEffectRenderer()
            makeGuiScreenInjections()

            ModuleManager.setup()
            ModuleManager.asmPass()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}
