package com.chattriggers.ctjs.engine.module

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.Reference
import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.engine.langs.js.JSContextFactory
import com.chattriggers.ctjs.engine.langs.js.JSLoader
import com.chattriggers.ctjs.launch.IndySupport
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.FileLib
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.printTraceToConsole
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.Config
import com.chattriggers.ctjs.utils.console.Console
import org.apache.commons.io.FileUtils
import org.mozilla.javascript.Context
import java.io.File
import java.lang.invoke.MethodHandle
import java.net.URLClassLoader

object ModuleManager {
    private val loaders = listOf(JSLoader)
    val generalConsole = Console(null)
    val cachedModules = mutableListOf<Module>()
    val modulesFolder = run {
        Config.loadData()
        File(Config.modulesFolder)
    }
    val pendingOldModules = mutableListOf<Module>()

    fun setup() {
        modulesFolder.mkdirs()

        // Download pending modules
        ModuleUpdater.importPendingModules()

        // Get existing modules
        val installedModules = getFoldersInDir(modulesFolder).map(::parseModule).distinctBy {
            it.name.lowercase()
        }

        // Check if those modules have updates
        installedModules.forEach(ModuleUpdater::updateModule)
        cachedModules.addAll(installedModules)

        // Import required modules
        installedModules.asSequence().mapNotNull {
            it.metadata.requires
        }.flatten().distinct().forEach(ModuleUpdater::importModule)

        // Load their assets
        loadAssets(cachedModules)

        // Normalize all metadata
        cachedModules.forEach {
            it.metadata.entry = it.metadata.entry?.replace('/', File.separatorChar)?.replace('\\', File.separatorChar)
        }

        // Get all jars
        val jars = cachedModules.map { module ->
            module.folder.walk().filter {
                it.isFile && it.extension == "jar"
            }.map {
                it.toURI().toURL()
            }.toList()
        }.flatten()

        // Setup all loaders
        loaders.forEach {
            it.setup(jars)
        }

        // We're finished setting up all of our loaders,
        //  which means they can now have their ASM invocation re-lookups happen
        IndySupport.invalidateInvocations()
    }

    fun asmPass() {
        loaders.forEach(ILoader::asmSetup)

        // Load the modules
        loaders.forEach { loader ->
            cachedModules.filter {
                File(it.folder, it.metadata.asmEntry ?: return@filter false).extension == loader.getLanguage().extension
            }.forEach {
                loader.asmPass(it, File(it.folder, it.metadata.asmEntry!!).toURI())
            }
        }
    }

    fun entryPass(modules: List<Module> = cachedModules, completionListener: (percentComplete: Float) -> Unit = {}) {
        loaders.forEach(ILoader::entrySetup)

        val total = modules.count { it.metadata.entry != null }
        var completed = 0

        // Load the modules
        loaders.forEach { loader ->
            modules.filter {
                File(it.folder, it.metadata.entry ?: return@filter false).extension == loader.getLanguage().extension
            }.forEach {
                loader.entryPass(it, File(it.folder, it.metadata.entry!!).toURI())

                completed++
                completionListener(completed.toFloat() / total)
            }
        }
    }

    fun asmInvokeLookup(moduleName: String, functionID: String): MethodHandle {
        // Find the targeted module
        val module = cachedModules.first { it.name == moduleName }

        // Get the target function file from the metadata lookup table
        val funcPath = module.metadata.asmExposedFunctions?.get(functionID) ?: throw IllegalArgumentException(
            "Module $module contains no asm exported function with id $functionID"
        )

        val funcFile = File(module.folder, funcPath.replace('/', File.separatorChar).replace('\\', File.separatorChar))

        return loaders.first {
            it.getLanguage().extension == funcFile.extension
        }.asmInvokeLookup(module, funcFile.toURI())
    }

    private fun getFoldersInDir(dir: File): List<File> {
        if (!dir.isDirectory) return emptyList()

        return dir.listFiles()?.filter {
            it.isDirectory
        } ?: listOf()
    }

    fun parseModule(directory: File): Module {
        val metadataFile = File(directory, "metadata.json")
        var metadata = ModuleMetadata()

        if (metadataFile.exists()) {
            try {
                metadata = CTJS.gson.fromJson(FileLib.read(metadataFile), ModuleMetadata::class.java)
            } catch (exception: Exception) {
                exception.printTraceToConsole()
            }
        }

        return Module(directory.name, metadata, directory)
    }

    data class ImportedModule(val module: Module?, val dependencies: List<Module>)

    fun importModule(moduleName: String): ImportedModule {
        val newModules = ModuleUpdater.importModule(moduleName)

        // Load their assets
        loadAssets(newModules)

        // Normalize all metadata
        newModules.forEach {
            it.metadata.entry = it.metadata.entry?.replace('/', File.separatorChar)?.replace('\\', File.separatorChar)
        }

        // TODO: Print warning to console if metadatas contain an asm key

        entryPass(newModules)

        return ImportedModule(newModules.getOrNull(0), newModules.drop(1))
    }

    fun deleteModule(name: String): Boolean {
        val module = cachedModules.find { it.name.lowercase() == name.lowercase() } ?: return false

        val file = File(modulesFolder, module.name)
        if (!file.exists()) throw IllegalStateException("Expected module to have an existing folder!")

        val context = JSContextFactory.enterContext()
        try {
            val classLoader = context.applicationClassLoader as URLClassLoader

            classLoader.close()

            if (file.deleteRecursively()) {
                Reference.loadCT()
                return true
            }
        } finally {
            Context.exit()
        }

        return false
    }

    fun tryReportOldVersion(module: Module) {
        if (World.isLoaded()) {
            reportOldVersion(module)
        } else {
            pendingOldModules.add(module)
        }
    }

    fun reportOldVersion(module: Module) {
        ChatLib.chat("&cWarning: the module \"${module.name}\" was made for an older version of CT, " +
                "so it may not work correctly.")
    }

    private fun loadAssets(modules: List<Module>) {
        modules.map {
            File(it.folder, "assets")
        }.filter {
            it.exists() && !it.isFile
        }.map {
            it.listFiles()?.toList() ?: emptyList()
        }.flatten().forEach {
            FileUtils.copyFileToDirectory(it, CTJS.assetsDir)
        }
    }

    fun teardown() {
        cachedModules.clear()

        loaders.forEach {
            it.clearTriggers()

            if (Config.clearConsoleOnLoad) {
                it.console.clearConsole()
            }
        }

        if (Config.clearConsoleOnLoad)
            generalConsole.clearConsole()
    }

    fun trigger(type: TriggerType, arguments: Array<out Any?>) {
        loaders.forEach {
            it.exec(type, arguments)
        }
    }

    fun getConsole(language: String): Console {
        return loaders.firstOrNull {
            it.getLanguage().langName == language
        }?.console ?: generalConsole
    }
}
