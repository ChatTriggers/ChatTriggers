package com.chattriggers.ctjs.engine.loader

import com.chattriggers.ctjs.Reference
import com.chattriggers.ctjs.engine.module.Module
import com.chattriggers.ctjs.engine.module.ModuleManager
import com.chattriggers.ctjs.engine.module.ModuleManager.cachedModules
import com.chattriggers.ctjs.engine.module.ModuleManager.modulesFolder
import com.chattriggers.ctjs.engine.module.ModuleMetadata
import com.chattriggers.ctjs.print
import com.chattriggers.ctjs.utils.config.Config
import com.chattriggers.ctjs.utils.kotlin.toVersion
import com.google.gson.Gson
import org.apache.commons.io.FileUtils
import java.io.File
import java.net.URL
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

object ModuleUpdater {
    val gson = Gson()

    fun importPendingModules() {
        val toDownload = File(modulesFolder, ".to_download.txt")
        if (!toDownload.exists()) return

        toDownload.readText().split(",").filter(String::isBlank).forEach {
            importModule(it)
        }

        toDownload.delete()
    }

    fun updateModule(module: Module) {
        if (!Config.autoUpdateModules) return
        val metadata = module.metadata

        try {
            if (metadata.name == null) return

            "Checking for update in ${metadata.name}".print()

            val url = "https://www.chattriggers.com/api/modules/${metadata.name}/metadata?modVersion=${Reference.MODVERSION}"
            val connection = URL(url).openConnection().apply {
                setRequestProperty("User-Agent", "Mozilla/5.0")
            }

            val newMetadataText = connection.getInputStream().bufferedReader().readText()
            val newMetadata = gson.fromJson(newMetadataText, ModuleMetadata::class.java)

            if (newMetadata.version == null) {
                ("Remote version of module ${metadata.name} have no version numbers, so it will " +
                        "not be updated!").print()
                return
            } else if (metadata.version != null && metadata.version.toVersion() >= newMetadata.version.toVersion()) {
                return
            }

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

        val realName = downloadModule(moduleName) ?: return emptyList()

        val moduleDir = File(modulesFolder, realName)
        val module = ModuleManager.parseModule(moduleDir)

        cachedModules.add(module)

        return listOf(module) + (module.metadata.requires?.map(::importModule)?.flatten() ?: emptyList())
    }

    private fun downloadModule(name: String): String? {
        val downloadZip = File(modulesFolder, "currDownload.zip")

        try {

            val url = "https://www.chattriggers.com/api/modules/$name/scripts?modVersion=${Reference.MODVERSION}"
            val connection = URL(url).openConnection()
            connection.setRequestProperty("User-Agent", "Mozilla/5.0")
            FileUtils.copyInputStreamToFile(connection.getInputStream(), downloadZip)
            FileSystems.newFileSystem(downloadZip.toPath(), null).use {
                val rootFolder = Files.newDirectoryStream(it.rootDirectories.first()).iterator()
                if (!rootFolder.hasNext()) throw Exception("Too small")
                val moduleFolder = rootFolder.next()
                if (rootFolder.hasNext()) throw Exception("Too big")

                val realName = moduleFolder.fileName.toString().trimEnd(File.separatorChar)
                File(modulesFolder, realName).apply { mkdir() }
                Files.walk(moduleFolder).forEach { path ->
                    val resolvedPath = Paths.get(modulesFolder.toString(), path.toString())
                    if (Files.isDirectory(resolvedPath)) {
                        return@forEach
                    }
                    Files.copy(path, resolvedPath, StandardCopyOption.REPLACE_EXISTING)
                }
                return realName
            }

        } catch (exception: Exception) {
            exception.print()
        } finally {
            downloadZip.delete()
        }

        return null
    }
}
