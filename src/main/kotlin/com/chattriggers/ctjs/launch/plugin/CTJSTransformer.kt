package com.chattriggers.ctjs.launch.plugin

import me.falsehonesty.asmhelper.BaseClassTransformer
import net.minecraft.launchwrapper.LaunchClassLoader
import kotlin.properties.Delegates

class CTJSTransformer : BaseClassTransformer() {
    override fun setup(classLoader: LaunchClassLoader) {
        super.setup(classLoader)

        classLoader.addTransformerExclusion("ct.") // for proguard builds
        classLoader.addTransformerExclusion("com.chattriggers.ctjs.launch.plugin.")
        classLoader.addTransformerExclusion("org.fife.")
    }

    override fun makeTransformers() {
        injectCrashReport()
        injectEntityPlayer()
        injectGuiMainMenu()
        injectMinecraft()
        injectScreenshotHelper()
        injectNetHandlerPlayClient()
        injectRenderManager()
        makeGuiScreenInjections()
        makePlayerControllerMPInjections()

        try {
            println("Trying to find frames+")
            Class.forName("io.framesplus.util.NativeUtil")
            println("Found frames+")
            HAS_FRAMES_PLUS = true
        } catch (e: ClassNotFoundException) {
            // If Frames+ is present, this injection causes MC
            // to crash
            println("Did not find frames+")
            injectEffectRenderer()
        }
    }

    companion object {
        var HAS_FRAMES_PLUS = false
    }
}
