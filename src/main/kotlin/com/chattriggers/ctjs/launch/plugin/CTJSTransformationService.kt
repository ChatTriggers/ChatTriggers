package com.chattriggers.ctjs.launch.plugin

import dev.falsehonesty.asmhelper.ClassTransformationService

class CTJSTransformationService : ClassTransformationService {
    override fun transformerClasses(): List<String> {
        return listOf("com.chattriggers.ctjs.launch.plugin.CTJSTransformer")
    }
}
