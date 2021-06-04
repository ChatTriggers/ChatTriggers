package com.chattriggers.ctjs.engine.loader

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.engine.module.Module
import com.chattriggers.ctjs.engine.module.ModuleManager
import com.chattriggers.ctjs.engine.module.ModuleMetadata
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.printToConsole
import com.chattriggers.ctjs.utils.config.Config
import java.io.File

object ModuleUpdater {
    private val repositoryHandlers = listOf(CTRepositoryHandler)

    private fun repositoryForModule(module: Module): RepositoryHandler {
        return module.metadata.repository?.type?.handler ?: return CTRepositoryHandler
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

        val newMetadata = CTJS.gson.fromJson(
            File(module.folder, "metadata.json").readText(),
            ModuleMetadata::class.java
        )

        if (Config.moduleChangelog && newMetadata.changelog != null) {
            ChatLib.chat("&a[ChatTriggers] ${newMetadata.name} has updated to version ${newMetadata.version}")
            ChatLib.chat("&aChangelog: &r${newMetadata.changelog}")
        }

        module.metadata = newMetadata
    }

    fun import(identifier: String): List<Module> {
        if (ModuleManager.cachedModules.any { it.name == identifier }) return emptyList()

        val repositoryHandler = repositoryHandlers.first { it.matches(identifier) }

        val realModuleName = repositoryHandler.import(identifier) ?: return emptyList()

        val moduleDir = File(ModuleManager.modulesFolder, realModuleName)
        val module = ModuleManager.parseModule(moduleDir)

        ModuleManager.cachedModules.add(module)

        return listOf(module) + (module.metadata.requires?.map(::import)?.flatten() ?: emptyList())
    }
}
