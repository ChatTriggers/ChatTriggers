package com.chattriggers.ctjs.launch

import net.minecraft.launchwrapper.ITweaker
import net.minecraft.launchwrapper.LaunchClassLoader
import org.spongepowered.asm.launch.MixinBootstrap
import org.spongepowered.asm.mixin.MixinEnvironment
import org.spongepowered.asm.mixin.Mixins
import java.io.File
import java.net.MalformedURLException

class CTJSTweaker : ITweaker {
    override fun acceptOptions(args: MutableList<String>?, gameDir: File?, assetsDir: File?, profile: String?) {}

    override fun injectIntoClassLoader(classLoader: LaunchClassLoader) {
        println("Setting up Mixins...")
        MixinBootstrap.init()
        Mixins.addConfiguration("mixins.ctjs.json")
        MixinEnvironment.getDefaultEnvironment().obfuscationContext = "searge"
        MixinEnvironment.getDefaultEnvironment().side = MixinEnvironment.Side.CLIENT

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