package com.chattriggers.ctjs.engine.module

import java.io.File

class Module(val name: String, var metadata: ModuleMetadata, val folder: File) {
    override fun toString() = "Module{name=$name,version=${metadata.version}}"
}
