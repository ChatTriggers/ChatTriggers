package com.chattriggers.ctjs.launch.plugin

import me.falsehonesty.asmhelper.BaseClassTransformer
import net.minecraft.launchwrapper.LaunchClassLoader

class CTJSTransformer : BaseClassTransformer() {
    override fun setup(classLoader: LaunchClassLoader) {
        super.setup(classLoader)

        classLoader.addTransformerExclusion("com.chattriggers.ctjs.launch.plugin.")
    }

    override fun makeTransformers() {
        makePlayerTransformers()
        makeGuiTransformers()
    }
}