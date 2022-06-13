package com.chattriggers.ctjs.engine.module

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.Reference
import com.chattriggers.ctjs.engine.module.ModuleManager.cachedModules
import com.chattriggers.ctjs.engine.module.ModuleManager.modulesFolder
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.listeners.Initializer
import com.chattriggers.ctjs.printToConsole
import com.chattriggers.ctjs.printTraceToConsole
import com.chattriggers.ctjs.triggers.EventType
import com.chattriggers.ctjs.utils.Config
import com.chattriggers.ctjs.utils.console.LogType
import com.chattriggers.ctjs.utils.kotlin.toVersion
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

object ModuleUpdater : Initializer {
    private val changelogs = mutableListOf<ModuleMetadata>()

    override fun init() {
        CTJS.addEventListener(EventType.WorldLoad) {
            changelogs.forEach(::reportChangelog)
            changelogs.clear()
        }
    }

    private fun reportChangelog(module: ModuleMetadata) {
        ChatLib.chat("&a[ChatTriggers] ${module.name} has updated to version ${module.version}")
        ChatLib.chat("&aChangelog: &r${module.changelog}")
    }

    fun importPendingModules() {
        val toDownload = File(modulesFolder, ".to_download.txt")
        if (!toDownload.exists()) return

        toDownload.readText().split(",").filter(String::isBlank).forEach(::importModule)

        toDownload.delete()
    }

    fun updateModule(module: Module) {
        if (!Config.autoUpdateModules) return

        val metadata = module.metadata

        try {
            if (metadata.name == null) return

            "Checking for update in ${metadata.name}".printToConsole()

            val url = "${CTJS.WEBSITE_ROOT}/api/modules/${metadata.name}/metadata?modVersion=${Reference.MODVERSION}"
            val connection = CTJS.makeWebRequest(url)

            val newMetadataText = connection.getInputStream().bufferedReader().readText()
            val newMetadata = CTJS.gson.fromJson(newMetadataText, ModuleMetadata::class.java)

            if (newMetadata.version == null) {
                ("Remote version of module ${metadata.name} has no version numbers, so it will " +
                        "not be updated!").printToConsole(logType = LogType.WARN)
                return
            } else if (metadata.version != null && metadata.version.toVersion() >= newMetadata.version.toVersion()) {
                return
            }

            downloadModule(metadata.name)
            "Updated module ${metadata.name}".printToConsole()

            module.metadata = File(module.folder, "metadata.json").let {
                CTJS.gson.fromJson(it.readText(), ModuleMetadata::class.java)
            }

            if (Config.moduleChangelog && module.metadata.changelog != null) {
                changelogs.add(module.metadata)
            }
        } catch (e: Exception) {
            "Can't find page for ${metadata.name}".printToConsole(logType = LogType.WARN)
        }
    }

    fun importModule(moduleName: String): List<Module> {
        if (cachedModules.any { it.name == moduleName }) return emptyList()

        val (realName, modVersion) = downloadModule(moduleName) ?: return emptyList()

        val moduleDir = File(modulesFolder, realName)
        val module = ModuleManager.parseModule(moduleDir)
        module.targetModVersion = modVersion.toVersion()

        cachedModules.add(module)

        return listOf(module) + (module.metadata.requires?.map(::importModule)?.flatten() ?: emptyList())
    }

    data class DownloadResult(val name: String, val modVersion: String)

    private fun downloadModule(name: String): DownloadResult? {
        val downloadZip = File(modulesFolder, "currDownload.zip")

        try {
            val url = "${CTJS.WEBSITE_ROOT}/api/modules/$name/scripts?modVersion=${Reference.MODVERSION}"
            val connection = CTJS.makeWebRequest(url)
            FileUtils.copyInputStreamToFile(connection.getInputStream(), downloadZip)
            FileSystems.newFileSystem(downloadZip.toPath(), null as ClassLoader?).use {
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
                return DownloadResult(realName, connection.getHeaderField("CT-Version"))
            }
        } catch (exception: Exception) {
            exception.printTraceToConsole()
        } finally {
            downloadZip.delete()
        }

        return null
    }
}
