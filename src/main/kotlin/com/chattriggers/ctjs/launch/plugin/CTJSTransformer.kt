package com.chattriggers.ctjs.launch.plugin

import com.chattriggers.ctjs.engine.module.ModuleManager
import me.falsehonesty.asmhelper.BaseClassTransformer
import net.minecraft.launchwrapper.LaunchClassLoader
import kotlin.properties.Delegates

class CTJSTransformer : BaseClassTransformer() {
    override fun setup(classLoader: LaunchClassLoader) {
        super.setup(classLoader)

        classLoader.addTransformerExclusion("ct.") // for proguard builds
        classLoader.addTransformerExclusion("file__") // for rhino generated classes
        classLoader.addTransformerExclusion("com.chattriggers.ctjs.")
        classLoader.addTransformerExclusion("com.google.gson.")
        classLoader.addTransformerExclusion("org.mozilla.javascript")
        classLoader.addTransformerExclusion("org.mozilla.classfile")
        classLoader.addTransformerExclusion("com.fasterxml.jackson.core.Version")
        classLoader.addTransformerExclusion("me.falsehonesty.asmhelper.")
    }

    override fun makeTransformers() {
        try {
            injectCrashReport()
            injectEntityPlayer()
            injectGuiMainMenu()
            injectMinecraft()
            injectScreenshotHelper()
            injectNetHandlerPlayClient()
            injectRenderManager()
            makeGuiScreenInjections()

            try {
                println("Trying to find frames+")
                Class.forName("io.framesplus.util.NativeUtil")
                println("Found frames+")
                HAS_FRAMES_PLUS = true
            } catch (e: Exception) {
                // If Frames+ is present, this injection causes MC
                // to crash
                println("Did not find frames+")
                injectEffectRenderer()
            }

            ModuleManager.setup()
            ModuleManager.asmPass()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    companion object {
        var HAS_FRAMES_PLUS = false
    }
}
