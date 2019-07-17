package com.chattriggers.ctjs.launch.tweaker

import net.minecraft.launchwrapper.ITweaker
import net.minecraft.launchwrapper.LaunchClassLoader
import net.minecraftforge.fml.relauncher.CoreModManager
import org.spongepowered.asm.launch.MixinBootstrap
import org.spongepowered.asm.mixin.MixinEnvironment
import org.spongepowered.asm.mixin.Mixins
import java.io.File
import java.net.MalformedURLException
import java.net.URISyntaxException

class CTJSTweaker : ITweaker {
    override fun acceptOptions(args: MutableList<String>?, gameDir: File?, assetsDir: File?, profile: String?) {}

    override fun injectIntoClassLoader(classLoader: LaunchClassLoader) {
        println("Setting up Mixins...")
        MixinBootstrap.init()
        Mixins.addConfiguration("mixins.ctjs.json")
        MixinEnvironment.getDefaultEnvironment().obfuscationContext = "searge"
        MixinEnvironment.getDefaultEnvironment().side = MixinEnvironment.Side.CLIENT

        // fixing mixins. code from https://github.com/ReplayMod/ReplayMod/blob/0a26f59c9c5ac6ec72b8bdb758ef126f2d10e28d/src/main/java/com/replaymod/core/LoadingPlugin.java#L26
        val codeSource = javaClass.protectionDomain.codeSource
        if (codeSource != null) {
            val location = codeSource.location
            try {
                val file = File(location.toURI())
                if (file.isFile) CoreModManager.getIgnoredMods().remove(file.name)
            } catch (exception: URISyntaxException) {
                exception.printStackTrace()
            }
        } else {
            println("No CodeSource, if this is not a development environment we might run into problems!")
            println(javaClass.protectionDomain)
        }

        try {
            classLoader.addURL(File(System.getProperty("java.home"), "lib/ext/nashorn.jar").toURI().toURL())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

    }

    override fun getLaunchTarget(): String {
        return "net.minecraft.client.main.Main"
    }

    override fun getLaunchArguments(): Array<String> {
        return emptyArray()
    }
}