package com.chattriggers.ctjs.engine.loader

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.engine.module.Module
import com.chattriggers.ctjs.engine.module.ModuleManager
import com.chattriggers.ctjs.engine.module.ModuleMetadata
import com.chattriggers.ctjs.printToConsole
import com.chattriggers.ctjs.utils.config.Config
import java.io.File

object ModuleUpdater {
    // CTRepositoryHandler must be last in this list
    private val repositoryHandlers = listOf(GitHubRepositoryHandler, CTRepositoryHandler)

    private fun repositoryForModule(module: Module): RepositoryHandler {
        return module.metadata.repository?.handler() ?: return CTRepositoryHandler
    }

    fun importPending() {
        val toDownload = File(ModuleManager.modulesFolder, ".to_download.txt")
        if (!toDownload.exists()) return

        toDownload.readText().split(",").filter(String::isBlank).forEach {
            import(it)
        }

        toDownload.delete()
    }

    fun update(module: Module) {
        if (!Config.autoUpdateModules) return

        "Checking for update in ${module.metadata.name}".printToConsole()

        val repositoryHandler = repositoryForModule(module)
        if (!repositoryHandler.shouldUpdate(module)) return

        repositoryHandler.update(module)

        "Updated module ${module.metadata.name}".printToConsole()

        module.metadata = CTJS.gson.fromJson(
            File(module.folder, "metadata.json").readText(),
            ModuleMetadata::class.java
        )

        // Try to import any dependencies, as they could have changed. Note that
        // we don't need to update them if they exist, since this is only called
        // during ct load which updates _all_ modules.
        module.metadata.requires?.forEach { import(it) }
    }

    fun import(identifier: String): List<Module> {
        if (ModuleManager.cachedModules.any { it.name == identifier }) return emptyList()

        val repositoryHandler = repositoryHandlers.first { it.matches(identifier) }

        val realModuleName = repositoryHandler.import(identifier) ?: return emptyList()

        val moduleDir = File(ModuleManager.modulesFolder, realModuleName)
        val module = ModuleManager.parseModule(moduleDir)

        ModuleManager.cachedModules.add(module)

        // Try to import any dependencies
        module.metadata.requires?.forEach { import(it) }

        return listOf(module) + (module.metadata.requires?.map(::import)?.flatten() ?: emptyList())
    }
}
