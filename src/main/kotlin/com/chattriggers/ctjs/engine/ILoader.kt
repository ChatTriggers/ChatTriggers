package com.chattriggers.ctjs.engine

import com.chattriggers.ctjs.engine.module.Module
import com.chattriggers.ctjs.triggers.OnTrigger
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.config.Config
import com.chattriggers.ctjs.utils.console.Console
import org.apache.commons.io.FileUtils
import java.io.File

interface ILoader {
    var triggers: MutableList<OnTrigger>
    val toRemove: MutableList<OnTrigger>

    /**
     * Loads a list of modules into the loader. This is meant to be called on
     * a full load, which is different from [loadExtra], as this method
     * should clear old modules.
     */
    fun load(modules: List<Module>)

    /**
     * Loads a single Module into the loader. This differs from [load] in that
     * it is meant to be called only when importing modules as it differs
     * semantically in that it shouldn't clear out old modules.
     */
    fun loadExtra(module: Module)

    /**
     * Tells the loader that it should activate all triggers
     * of a certain type with the specified arguments.
     */
    fun exec(type: TriggerType, vararg args: Any?) {
        val newTriggers = triggers.toMutableList()
        newTriggers.removeAll(toRemove)
        toRemove.clear()

        newTriggers.filter {
            it.type == type
        }.forEach {
            it.trigger(*args)
        }

        triggers = newTriggers
    }

    /**
     * Gets the result from evaluating a certain line of code in this loader
     */
    fun eval(code: String): Any?

    /**
     * Adds a trigger to this loader to be activated during the game
     */
    fun addTrigger(trigger: OnTrigger) {
        triggers.add(trigger)

        triggers.sortBy {
            it.priority.ordinal
        }
    }

    /**
     * Removes all triggers
     */
    fun clearTriggers() {
        triggers.clear()
    }

    /**
     * Returns the names of this specific loader's implemented languages
     */
    fun getLanguageName(): List<String>

    /**
     * Actually calls the method for this trigger in this loader
     */
    fun trigger(trigger: OnTrigger, method: Any, vararg args: Any?)

    /**
     * Removes a trigger from the current pool
     */
    fun removeTrigger(trigger: OnTrigger) {
        toRemove.add(trigger)
    }

    /**
     * Gets a list of all currently loaded modules
     */
    fun getModules(): List<Module>

    /**
     * Gets the console for this language that will show errors and
     * be able to (optionally) evaluate code.
     */
    fun getConsole(): Console

    /**
     * Save a resource to the OS's filesystem from inside the jar
     * @param resourceName name of the file inside the jar
     * @param outputFile file to save to
     * @param replace whether or not to replace the file being saved to
     */
    fun saveResource(resourceName: String?, outputFile: File, replace: Boolean): String {
        if (resourceName == null || resourceName == "") {
            throw IllegalArgumentException("ResourcePath cannot be null or empty")
        }

        val parsedResourceName = resourceName.replace('\\', '/')
        val resource = this.javaClass.getResourceAsStream(parsedResourceName)
                ?: throw IllegalArgumentException("The embedded resource '$parsedResourceName' cannot be found.")

        val res = resource.bufferedReader().readText()
        FileUtils.write(outputFile, res)
        return res
    }

    companion object {
        internal val modulesFolder = File(Config.modulesFolder)

        internal fun getFoldersInDir(dir: File): List<File> {
            if (!dir.isDirectory) return emptyList()

            return dir.listFiles().filter {
                it.isDirectory
            }
        }
    }
}
