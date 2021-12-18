package com.chattriggers.ctjs.engine.module

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.Reference
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.printToConsole
import com.chattriggers.ctjs.printTraceToConsole
import com.chattriggers.ctjs.utils.Config
import com.chattriggers.ctjs.utils.kotlin.toVersion
import org.apache.commons.io.FileUtils
import java.io.File
import java.net.URL
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

object ModuleImporter {
    val installedModules = mutableListOf<Module>()

    fun importPendingModules() {
        if (!Config.pendingDownloadFile.exists()) return

        Config.pendingDownloadFile.readText().split(",").filter(String::isBlank).forEach(::importModule)

        Config.pendingDownloadFile.delete()
    }

    fun updateModule(module: Module) {
        val metadata = module.metadata

        if (!Config.autoUpdateModules || metadata.name == null)
            return


        try {
            "Checking for update in ${metadata.name}".printToConsole()

            val url = "${CTJS.WEBSITE_ROOT}/api/modules/${metadata.name}/metadata?modVersion=${Reference.MODVERSION}"
            val connection = URL(url).openConnection().apply {
                setRequestProperty("User-Agent", "Mozilla/5.0")
            }

            val newMetadataText = connection.getInputStream().bufferedReader().readText()
            val newMetadata = CTJS.gson.fromJson(newMetadataText, ModuleMetadata::class.java)

            if (newMetadata.version == null) {
                ("Remote version of module ${metadata.name} has no version numbers, so it will " +
                        "not be updated!").printToConsole()
                return
            } else if (metadata.version != null && metadata.version.toVersion() >= newMetadata.version.toVersion()) {
                return
            }

            downloadModule(metadata.name)
            "Updated module ${metadata.name}".printToConsole()

            if (Config.moduleChangelog && newMetadata.changelog != null) {
                ChatLib.chat("&a[ChatTriggers] ${metadata.name} has updated to version ${newMetadata.version}")
                ChatLib.chat("&aChangelog: &r${newMetadata.changelog}")
            }

            module.metadata = File(module.folder, "metadata.json").let {
                CTJS.gson.fromJson(it.readText(), ModuleMetadata::class.java)
            }
        } catch (e: Exception) {
            "Can't find page for ${metadata.name}".printToConsole()
        }
    }

    fun importModule(moduleName: String): List<Module> {
        if (installedModules.any { it.name == moduleName }) return emptyList()

        val realName = downloadModule(moduleName) ?: return emptyList()

        val moduleDir = File(Config.modulesFolder, realName)
        val module = ModuleManager.parseModule(moduleDir)

        installedModules.add(module)

        return listOf(module) + (module.metadata.requires?.map(ModuleImporter::importModule)?.flatten() ?: emptyList())
    }

    private fun downloadModule(name: String): String? {
        try {
            val url = "${CTJS.WEBSITE_ROOT}/api/modules/$name/scripts?modVersion=${Reference.MODVERSION}"
            val connection = URL(url).openConnection()
            connection.setRequestProperty("User-Agent", "Mozilla/5.0")
            FileUtils.copyInputStreamToFile(connection.getInputStream(), Config.pendingDownloadZip)
            FileSystems.newFileSystem(Config.pendingDownloadZip.toPath(), null).use {
                val rootFolder = Files.newDirectoryStream(it.rootDirectories.first()).iterator()
                if (!rootFolder.hasNext()) throw Exception("Too small")
                val moduleFolder = rootFolder.next()
                if (rootFolder.hasNext()) throw Exception("Too big")

                val realName = moduleFolder.fileName.toString().trimEnd(File.separatorChar)
                File(Config.modulesFolder, realName).apply { mkdir() }
                Files.walk(moduleFolder).forEach { path ->
                    val resolvedPath = Paths.get(Config.modulesFolder.toString(), path.toString())
                    if (Files.isDirectory(resolvedPath)) {
                        return@forEach
                    }
                    Files.copy(path, resolvedPath, StandardCopyOption.REPLACE_EXISTING)
                }
                return realName
            }
        } catch (exception: Exception) {
            exception.printTraceToConsole()
        } finally {
            Config.pendingDownloadZip.delete()
        }

        return null
    }
}
