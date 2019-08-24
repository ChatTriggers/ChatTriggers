package com.chattriggers.ctjs.engine.langs.js

import net.minecraft.client.Minecraft
import org.mozilla.javascript.Context
import org.mozilla.javascript.Context.FEATURE_LOCATION_INFORMATION_IN_ERROR
import org.mozilla.javascript.ContextFactory
import org.mozilla.javascript.WrapFactory
import org.mozilla.javascript.tools.ToolErrorReporter
import java.net.URL
import java.net.URLClassLoader

object JSContextFactory : ContextFactory() {
    private val classLoader = ModifiedURLClassLoader()
    var optimize = true

    fun addAllURLs(urls: List<URL>) = classLoader.addAllURLs(urls)

    override fun onContextCreated(cx: Context) {
        super.onContextCreated(cx)

        cx.applicationClassLoader = classLoader
        cx.optimizationLevel = if (optimize) 9 else 0
        cx.languageVersion = Context.VERSION_ES6
        cx.errorReporter = ToolErrorReporter(true, JSLoader.console.out)
        cx.wrapFactory = WrapFactory().apply { isJavaPrimitiveWrap = false }
    }

    override fun hasFeature(cx: Context?, featureIndex: Int): Boolean {
        when (featureIndex) {
            FEATURE_LOCATION_INFORMATION_IN_ERROR -> return true
        }

        return super.hasFeature(cx, featureIndex)
    }

    private class ModifiedURLClassLoader : URLClassLoader(arrayOf(), Minecraft::class.java.classLoader) {
        val sources = mutableListOf<URL>()

        fun addAllURLs(urls: List<URL>) {
            (urls - sources).forEach(::addURL)
        }

        public override fun addURL(url: URL) {
            super.addURL(url)
            sources.add(url)
        }
    }
}