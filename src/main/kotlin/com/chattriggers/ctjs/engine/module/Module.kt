package com.chattriggers.ctjs.engine.module

import java.io.File

class Module(val name: String, val metadata: ModuleMetadata, val folder: File) {
    fun getFilesWithExtension(type: String): List<File> {
        return this.folder.walkTopDown().filter {
            it.name.endsWith(type)
        }.filter {
            if (this.metadata.ignored == null) return@filter true

            for (ignore: String in this.metadata.ignored) {
                if (ignore in it.name) return@filter false
            }

            return@filter true
        }.filter {
            it.isFile
        }.toList()
    }

    override fun toString() = "Module{$name,${metadata.version}}"
}