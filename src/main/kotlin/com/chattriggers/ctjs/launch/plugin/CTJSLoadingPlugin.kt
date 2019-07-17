package com.chattriggers.ctjs.launch.plugin

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin

class CTJSLoadingPlugin : IFMLLoadingPlugin {
    override fun getModContainerClass() = ""

    override fun getASMTransformerClass(): Array<String> {
        println("get transformer class")
        return arrayOf("com.chattriggers.ctjs.launch.plugin.CTJSTransformer")
    }

    override fun getSetupClass() = ""

    override fun injectData(data: MutableMap<String, Any>?) {}

    override fun getAccessTransformerClass() = ""
}