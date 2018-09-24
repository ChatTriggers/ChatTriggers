package com.chattriggers.ctjs.engine.langs.py

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.engine.module.Module
import com.chattriggers.ctjs.triggers.OnTrigger
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.console.Console
import com.chattriggers.ctjs.utils.kotlin.ModuleLoader

@ModuleLoader
object PyLoader : ILoader {
    override val toRemove = mutableListOf<OnTrigger>()
    override var triggers = mutableListOf<OnTrigger>()

    private val cachedModules = mutableListOf<Module>()

    override fun load(modules: List<Module>) {
        cachedModules.clear()

        //TODO: JAR LOADING!

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadExtra(module: Module) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exec(type: TriggerType, vararg args: Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun eval(code: String): Any? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addTrigger(trigger: OnTrigger) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearTriggers() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLanguageName(): List<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun trigger(trigger: OnTrigger, method: Any, vararg args: Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeTrigger(trigger: OnTrigger) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getModules(): List<Module> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getConsole(): Console {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}