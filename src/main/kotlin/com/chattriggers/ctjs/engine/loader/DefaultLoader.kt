package com.chattriggers.ctjs.engine.loader

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.Reference
import com.chattriggers.ctjs.engine.loader.ILoader.Companion.getFoldersInDir
import com.chattriggers.ctjs.engine.loader.ILoader.Companion.modulesFolder
import com.chattriggers.ctjs.engine.module.Module
import com.chattriggers.ctjs.engine.module.ModuleManager
import com.chattriggers.ctjs.engine.module.ModuleMetadata
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.FileLib
import com.chattriggers.ctjs.print
import com.google.gson.Gson
import org.apache.commons.io.FileUtils
import java.io.File
import java.net.URL

object DefaultLoader {
    fun load(updateCheck: Boolean): List<Module> {
        loadAssets()

        val toDownload = File(modulesFolder, ".to_download.txt")

        if (toDownload.exists()) {
            val content = FileLib.read(toDownload)

            if (content != null) {
                for (module in content.split(",".toRegex())) {
                    if ("\n" == module || "" == module) continue

                    importModule(module, false)
                }

                toDownload.delete()
            }
        }

        // TODO: Multi-thread loading & updating
        //  this will probably involve making all of these completable-futures
        return findModulesAndUpdate(updateCheck)
    }

    fun importModule(name: String, extra: Boolean, isRequired: Boolean = false): List<Module>? {
        if (extra) {
            Reference.conditionalThread ct@{
                ChatLib.chat("&7Importing $name...")
                val res = doImport(name)

                if (!res) {
                    ChatLib.chat("&cCan't find module with name $name")
                    return@ct
                }

                val moduleFolder = getFoldersInDir(modulesFolder).first {
                    it.name.toLowerCase() == name.toLowerCase()
                }

                val modules = getModule(moduleFolder, false)

                modules.forEach {
                    ModuleManager.load(it)

                    if (isRequired) {
                        it.metadata.isRequired = true
                    }
                }

                ChatLib.chat("&aSuccessfully imported $name")
            }
        } else {
            val res = doImport(name)

            if (!res) {
                ChatLib.chat("&cCan't find module with name $name")
                return null
            }

            val moduleFolder = getFoldersInDir(modulesFolder).first {
                it.name.toLowerCase() == name.toLowerCase()
            }

            return getModule(moduleFolder, false)
        }

        return null
    }

    private fun doImport(name: String): Boolean {
        return downloadModule(name, true)
    }

    private fun loadAssets() {
        val toCopy = CTJS.assetsDir

        getFoldersInDir(modulesFolder).map {
            File(it, "assets")
        }.filter {
            it.exists() && !it.isFile
        }.map {
            it.listFiles()?.toList() ?: emptyList()
        }.flatten().forEach {
            FileUtils.copyFileToDirectory(it, toCopy)
        }
    }

    private fun findModulesAndUpdate(updateCheck: Boolean): List<Module> {
        modulesFolder.mkdirs()

        return getFoldersInDir(modulesFolder).map {
            getModule(it, updateCheck)
        }.flatten().distinctBy {
            it.name.toLowerCase()
        }
    }

    private fun getModule(dir: File, updateCheck: Boolean): List<Module> {
        val metadataFile = File(dir, "metadata.json")
        var metadata = ModuleMetadata()
        val modules = mutableListOf<Module>()

        if (metadataFile.exists()) {
            try {
                metadata = Gson().fromJson(FileLib.read(metadataFile), ModuleMetadata::class.java)
                metadata.fileName = dir.name
            } catch (exception: Exception) {
                exception.print()
            }
        }

        try {
            try {
                if (!metadata.isDefault && updateCheck) {
                    "checking for update in ${metadata.fileName}".print()

                    val newMetadataFile = File(dir, "updateMeta.json")

                    val connection =
                        URL("https://www.chattriggers.com/api/modules/${metadata.fileName}/metadata?modVersion=${Reference.MODVERSION}").openConnection()
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0")
                    FileUtils.copyInputStreamToFile(connection.getInputStream(), newMetadataFile)

                    val currVersion = metadata.version

                    val newMetadata = Gson().fromJson(newMetadataFile.readText(), ModuleMetadata::class.java)
                    val newVersion = newMetadata.version
                    val name = metadata.fileName

                    if (newVersion != currVersion && name != null) {
                        downloadModule(name, false)

                        ChatLib.chat("&6Updated " + metadata.name)
                    }

                    newMetadataFile.delete()
                }
            } catch (exception: Exception) {
                exception.print()
                "Can't find page for ${dir.name}".print()
            }

            modules.addAll(
                    getRequiredModules(metadata, updateCheck)
            )
        } catch (exception: Exception) {
            "Error loading module from $dir".print()
            exception.print()
        }

        modules.add(
            Module(
                dir.name,
                metadata,
                dir
            )
        )

        return modules
    }

    private fun downloadModule(name: String, existCheck: Boolean): Boolean {
        if (existCheck) {
            try {
                FileLib.getUrlContent("https://www.chattriggers.com/api/modules/$name/metadata?modVersion=${Reference.MODVERSION}")
            } catch (exception: Exception) {
                exception.print()
                return false
            }
        }

        try {
            val downloadZip = File(modulesFolder, "currDownload.zip")

            val connection = URL("https://www.chattriggers.com/api/modules/$name/scripts?modVersion=${Reference.MODVERSION}").openConnection()
            connection.setRequestProperty("User-Agent", "Mozilla/5.0")
            FileUtils.copyInputStreamToFile(connection.getInputStream(), downloadZip)

            FileLib.unzip(downloadZip.absolutePath, modulesFolder.absolutePath)

            downloadZip.delete()
        } catch (exception: Exception) {
            exception.print()
            return false
        }

        return true
    }

    private fun getRequiredModules(metadata: ModuleMetadata, updateCheck: Boolean): List<Module> {
        if (metadata.isDefault || metadata.requires == null) return listOf()
        val modules = mutableListOf<Module>()

        metadata.requires.map {
            File(modulesFolder, it)
        }.forEach {
            if (it.exists()) {
                val newModules = getModule(it, updateCheck)

                newModules.forEach { module ->
                    module.metadata.isRequired = true
                }

                modules.addAll(
                    newModules
                )
            } else {
                val newModules = importModule(it.name, false, isRequired = true)

                if (newModules != null) {
                    modules.addAll(newModules)
                }
            }
        }

        return modules
    }
}