package com.chattriggers.ctjs.engine.loader

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.Reference
import com.chattriggers.ctjs.engine.module.*
import com.chattriggers.ctjs.printToConsole
import com.chattriggers.ctjs.printTraceToConsole
import com.chattriggers.ctjs.utils.kotlin.toVersion
import java.net.URL

object CTRepositoryHandler : RepositoryHandler {
    override fun matches(identifier: String): Boolean {
        return true
    }

    override fun shouldUpdate(module: Module): Boolean {
        return try {
            val metadata = module.metadata
            val url =
                "https://www.chattriggers.com/api/modules/${metadata.name}/metadata?modVersion=${Reference.MODVERSION}"

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
        val name = (module.metadata.repository as? CTRepositoryInfo)?.name ?: module.metadata.name ?: module.name

        downloadModule(name)
    }

    private fun downloadModule(name: String): String? {
        return try {
            val url = "https://www.chattriggers.com/api/modules/$name/scripts?modVersion=${Reference.MODVERSION}"
            val connection = URL(url).openConnection()
            connection.setRequestProperty("User-Agent", "Mozilla/5.0")

            RepositoryHandler.importModuleZip(connection.getInputStream()) { moduleFile, metadata ->
                CTRepositoryInfo(
                    moduleFile.name,
                    metadata.version ?: ""
                )
            }
        } catch (e: Exception) {
            e.printTraceToConsole()
            null
        }
    }
}
