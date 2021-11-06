package com.chattriggers.ctjs.launch.plugin

import dev.falsehonesty.asmhelper.ClassTransformationService

class CTClassTransformationService : ClassTransformationService {
    override fun transformerClasses() = listOf("com.chattriggers.ctjs.launch.plugin.CTJSTransformer")
}