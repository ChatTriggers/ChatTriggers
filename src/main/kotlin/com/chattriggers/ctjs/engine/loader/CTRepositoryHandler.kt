package com.chattriggers.ctjs.engine.loader

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.Reference
import com.chattriggers.ctjs.engine.module.*
import com.chattriggers.ctjs.printToConsole
import com.chattriggers.ctjs.printTraceToConsole
import com.chattriggers.ctjs.utils.kotlin.fromJson
import com.chattriggers.ctjs.utils.kotlin.toVersion
import org.apache.commons.io.FileUtils
import java.io.File
import java.net.URL
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

object CTRepositoryHandler : RepositoryHandler {
    override fun matches(identifier: String): Boolean {
        return true
    }

    override fun shouldUpdate(module: Module): Boolean {
        return try {
            val metadata = module.metadata
            val url = "https://www.chattriggers.com/api/modules/${metadata.name}/metadata?modVersion=${Reference.MODVERSION}"

            val connection = URL(url).openConnection().apply { setRequestProperty("User-Agent", "Mozilla/5.0") }
            val newMetadataText = connection.getInputStream().bufferedReader().readText()
            val newMetadata = CTJS.gson.fromJson(newMetadataText, ModuleMetadata::class.java)

            if (newMetadata.version == null) {
                ("Remote version of module ${metadata.name} have no version numbers, so it will " +
                    "not be updated!").printToConsole()
                return false
            }

            metadata.version != null && metadata.version.toVersion() < newMetadata.version.toVersion()
        } catch (e: Exception) {
            false
        }
    }

    override fun import(identifier: String): String? {
        return downloadModule(identifier)
    }

    override fun update(module: Module) {
        val name = module.metadata.repository?.identifier ?: module.metadata.name ?: module.name

        downloadModule(name)
    }

    private fun downloadModule(name: String): String? {
        val downloadZip = File(ModuleManager.modulesFolder, "currDownload.zip")

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
                val realModuleFolder = File(ModuleManager.modulesFolder, realName).apply { mkdir() }
                Files.walk(moduleFolder).forEach { path ->
                    val resolvedPath = Paths.get(ModuleManager.modulesFolder.toString(), path.toString())
                    if (Files.isDirectory(resolvedPath)) {
                        return@forEach
                    }
                    Files.copy(path, resolvedPath, StandardCopyOption.REPLACE_EXISTING)
                }

                val metadataFile = File(realModuleFolder, "metadata.json")
                val metadata = CTJS.gson.fromJson<ModuleMetadata>(metadataFile.readText())
                val newMetadata = metadata.copy(repository = RepositoryInfo(
                    RepositoryType.CT,
                    realName,
                    metadata.version ?: ""
                ))
                metadataFile.writeText(CTJS.gson.toJson(newMetadata))
                return realName
            }
        } catch (exception: Exception) {
            exception.printTraceToConsole()
        } finally {
            downloadZip.delete()
        }

        return null
    }
}
