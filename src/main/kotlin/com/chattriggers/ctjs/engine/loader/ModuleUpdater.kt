package com.chattriggers.ctjs.engine.loader

import com.chattriggers.ctjs.Reference
import com.chattriggers.ctjs.engine.module.Module
import com.chattriggers.ctjs.engine.module.ModuleManager
import com.chattriggers.ctjs.engine.module.ModuleManager.cachedModules
import com.chattriggers.ctjs.engine.module.ModuleManager.modulesFolder
import com.chattriggers.ctjs.engine.module.ModuleMetadata
import com.chattriggers.ctjs.minecraft.libs.FileLib
import com.chattriggers.ctjs.print
import com.chattriggers.ctjs.utils.kotlin.toVersion
import com.google.gson.Gson
import org.apache.commons.io.FileUtils
import java.io.File
import java.net.URL

object ModuleUpdater {
    val gson = Gson()

    fun importPendingModules() {
        val toDownload = File(modulesFolder, ".to_download.txt")
        if (!toDownload.exists()) return

        toDownload.readText().split(",").filter(String::isBlank).forEach(::downloadModule)

        toDownload.delete()
    }

    fun updateModule(module: Module) {
        val metadata = module.metadata

        try {
            if (metadata.name == null) return

            if (metadata.version == null) {
                "Module ${metadata.name} has no version in it's metadata, so it will not be updated!".print()
                return
            }

            "Checking for update in ${metadata.name}".print()

            val url = "https://www.chattriggers.com/api/modules/${metadata.name}/metadata?modVersion=${Reference.MODVERSION}"
            val connection = URL(url).openConnection().apply {
                setRequestProperty("User-Agent", "Mozilla/5.0")
            }

            val newMetadataText = connection.getInputStream().bufferedReader().readText()
            val newMetadata = gson.fromJson(newMetadataText, ModuleMetadata::class.java)

            if (newMetadata.version == null) {
                "Remote module ${metadata.name} has no version in it's metadata, so it will not be updated!".print()
                return
            }

            if (metadata.version.toVersion() >= newMetadata.version.toVersion()) return

            downloadModule(metadata.name)
            "Updated module ${metadata.name}".print()

            module.metadata = File(module.folder, "metadata.json").let {
                gson.fromJson(it.readText(), ModuleMetadata::class.java)
            }
        } catch (e: Exception) {
            "Can't find page for ${metadata.name}".print()
        }
    }

    fun importModule(moduleName: String): List<Module> {
        if (cachedModules.any { it.name == moduleName }) return emptyList()

        downloadModule(moduleName)

        val moduleDir = File(modulesFolder, moduleName)
        val module = ModuleManager.parseModule(moduleDir)

        cachedModules.add(module)

        return listOf(module) + (module.metadata.requires?.map(::importModule)?.flatten() ?: emptyList())
    }

    private fun downloadModule(name: String) {
        try {
            val downloadZip = File(modulesFolder, "currDownload.zip")

            val url = "https://www.chattriggers.com/api/modules/$name/scripts?modVersion=${Reference.MODVERSION}"
            val connection = URL(url).openConnection()
            connection.setRequestProperty("User-Agent", "Mozilla/5.0")
            FileUtils.copyInputStreamToFile(connection.getInputStream(), downloadZip)

            FileLib.unzip(downloadZip.absolutePath, modulesFolder.absolutePath)

            downloadZip.delete()
        } catch (exception: Exception) {
            exception.print()
        }
    }
}