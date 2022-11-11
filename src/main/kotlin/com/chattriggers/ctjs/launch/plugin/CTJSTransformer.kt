package com.chattriggers.ctjs.launch.plugin

import com.chattriggers.ctjs.engine.module.ModuleManager
import dev.falsehonesty.asmhelper.BaseClassTransformer
import net.minecraft.launchwrapper.LaunchClassLoader

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
        classLoader.addTransformerExclusion("dev.falsehonesty.asmhelper.")
        classLoader.addTransformerExclusion("org.fife.")
    }

    override fun makeTransformers() {
        if (transforming) return
        transforming = true

        try {
            injectCrashReport()
            injectGuiMainMenu()
            injectMinecraft()
            injectScreenshotHelper()
            injectRenderManager()
            injectEffectRenderer()
            injectGuiScreen()
            injectPlayerControllerMP()
            injectGuiContainer()
            injectTileEntityRendererDispatcher()
            injectGuiIngame()
            injectGuiIngameForge()
            injectRenderItem()
            injectEntityPlayerSP()
            ModuleManager.setup()
            ModuleManager.asmPass()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}
