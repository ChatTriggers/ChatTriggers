package com.chattriggers.ctjs.engine

import com.chattriggers.ctjs.engine.module.Module
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.console.Console

object ModuleManager {
    val loaders = mutableListOf<ILoader>()
    val generalConsole = Console(null)
    var cachedModules = listOf<Module>()

    fun importModule(moduleName: String) {
        DefaultLoader.importModule(moduleName, true)
    }

    fun load(updateCheck: Boolean) {
        val modules = DefaultLoader.load(updateCheck)
        cachedModules = modules

        loaders.forEach {
            it.load(modules)
        }
    }

    fun load(module: Module) {
        loaders.forEach {
            it.load(module)
        }
    }

    fun unload() {
        loaders.forEach {
            it.clearTriggers()
        }
    }

    fun trigger(type: TriggerType, vararg arguments: Any) {
        loaders.forEach {
            it.exec(type, *arguments)
        }
    }

    fun getConsole(language: String): Console {
        return loaders.firstOrNull {
            it.getLanguageName() == language
        }?.getConsole() ?: generalConsole
    }
}