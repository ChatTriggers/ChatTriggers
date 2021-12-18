package com.chattriggers.ctjs.engine.module

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.Reference
import com.chattriggers.ctjs.printTraceToConsole
import com.chattriggers.ctjs.utils.Config
import org.apache.commons.io.FileUtils
import java.io.File
import java.net.URL
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

class ImportRequest(val name: String) {
    private var tempZipFile: File? = null
    private var tempModuleFolder: File? = null
    private var metadata: ModuleMetadata? = null
    private var dependencies: MutableList<ImportRequest>? = null
    private var module: Module? = null

    fun import(): Module {
        module?.also { return it }

        ModuleImporter.installedModules.firstOrNull { it.name == name }?.also {
            module = it
            return it
        }

        if (tempZipFile == null)
            downloadZipFile()

        if (tempModuleFolder != null)

    }

    private fun downloadZipFile() {
        try {
            val url = "${CTJS.WEBSITE_ROOT}/api/modules/$name/scripts?modVersion=${Reference.MODVERSION}"
            val connection = URL(url).openConnection()
            connection.setRequestProperty("User-Agent", "Mozilla/5.0")
            tempZipFile = File(CTJS.tempDirectory, "temp-download-$name.zip")
            FileUtils.copyInputStreamToFile(connection.getInputStream(), tempZipFile)
        } catch (e: Throwable) {
            throw ModuleDoesNotExistException(name)
        }
    }

    private fun unzipAndParseMetadata() {
        val folder = File(CTJS.tempDirectory, "temp-$name")
        tempModuleFolder = folder

        try {
            if (folder.exists())
                folder.deleteRecursively()
            folder.mkdirs()

            FileSystems.newFileSystem(tempZipFile!!.toPath(), null).use {
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
            }
        } catch (e: Throwable) {
            throw MalformedModuleException(name)
        }
    }

    class ModuleDoesNotExistException(val moduleName: String) : Exception()

    class MalformedModuleException(val moduleName: String) : Exception()

    class ModuleRequiresVerificationException(val moduleName: String) : Exception()

    companion object {
        val forcedImports = mutableSetOf<String>()
    }
}
