package com.chattriggers.ctjs.engine.loader

import com.chattriggers.ctjs.engine.module.Module

interface RepositoryHandler {
    fun matches(identifier: String): Boolean
    fun shouldUpdate(module: Module): Boolean

    /**
     * @return the real name of the module, i.e. the folder name in the modules folder
     */
    fun import(identifier: String): String?
    fun update(module: Module)
}
