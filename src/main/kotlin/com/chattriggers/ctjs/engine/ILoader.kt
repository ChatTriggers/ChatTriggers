package com.chattriggers.ctjs.engine

import com.chattriggers.ctjs.engine.langs.Lang
import com.chattriggers.ctjs.engine.module.Module
import com.chattriggers.ctjs.triggers.Trigger
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.console.Console
import org.apache.commons.io.FileUtils
import java.io.File
import java.lang.invoke.MethodHandle
import java.net.URI
import java.net.URL

/**
 * @suppress This is internal and should not show in userdocs
 */
interface ILoader {
    val console: Console

    /**
     * Performs initial engine setup given a list of jars. Note that
     * these are all jars from all modules.
     */
    fun setup(jars: List<URL>)

    fun asmSetup()

    fun asmPass(module: Module, asmURI: URI)

    fun entrySetup()

    /**
     * Loads a list of modules into the loader. This method will only
     * ever be called with modules that have an entry point corresponding
     * to this loader's language's extension
     */
    fun entryPass(module: Module, entryURI: URI)

    /**
     * If we inject bytecode through ASM that wishes to callback to the user's script,
     * we need to link it to code that will actually make that call.
     *
     * This method lets each specific engine handle function invocation specifics themselves.
     *
     * @return a [MethodHandle] with type (Object[])Object
     */
    fun asmInvokeLookup(module: Module, functionURI: URI): MethodHandle

    /**
     * Tells the loader that it should activate all triggers
     * of a certain type with the specified arguments.
     */
    fun exec(type: TriggerType, args: Array<out Any?>)

    /**
     * Gets the result from evaluating a certain line of code in this loader
     */
    fun eval(code: String): String

    /**
     * Adds a trigger to this loader to be activated during the game
     */
    fun addTrigger(trigger: Trigger)

    /**
     * Removes all triggers
     */
    fun clearTriggers()

    /**
     * Returns the names of this specific loader's implemented languages
     */
    fun getLanguage(): Lang

    /**
     * Actually calls the method for this trigger in this loader
     */
    fun trigger(trigger: Trigger, method: Any, args: Array<out Any?>)

    /**
     * Removes a trigger from the current pool
     */
    fun removeTrigger(trigger: Trigger)

    /**
     * Save a resource to the OS's filesystem from inside the jar
     * @param resourceName name of the file inside the jar
     * @param outputFile file to save to
     * @param replace whether to replace the file being saved to
     */
    fun saveResource(resourceName: String?, outputFile: File, replace: Boolean): String {
        require(resourceName != null && resourceName != "") {
            "ResourcePath cannot be null or empty"
        }

        val parsedResourceName = resourceName.replace('\\', '/')
        val resource = javaClass.getResourceAsStream(parsedResourceName)
            ?: throw IllegalArgumentException("The embedded resource '$parsedResourceName' cannot be found.")

        val res = resource.bufferedReader().readText()
        FileUtils.write(outputFile, res)
        return res
    }
}
