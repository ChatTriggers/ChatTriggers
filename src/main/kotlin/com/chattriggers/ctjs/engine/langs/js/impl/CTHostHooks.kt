package com.chattriggers.ctjs.engine.langs.js.impl

import com.chattriggers.ctjs.engine.langs.js.JSLoader
import com.reevajs.reeva.core.Agent
import com.reevajs.reeva.core.HostHooks
import com.reevajs.reeva.core.lifecycle.FileSourceInfo
import com.reevajs.reeva.core.lifecycle.SourceInfo
import com.reevajs.reeva.core.realm.Realm
import com.reevajs.reeva.core.realm.RealmExtension
import com.reevajs.reeva.runtime.JSValue
import com.reevajs.reeva.runtime.Operations
import com.reevajs.reeva.runtime.objects.JSObject
import com.reevajs.reeva.runtime.objects.SlotName
import com.reevajs.reeva.runtime.toJSString
import java.awt.Color
import java.io.File

class CTHostHooks : HostHooks() {
    override fun initializeHostDefinedRealm(realmExtensions: Map<Any, RealmExtension>): Realm {
        return super.initializeHostDefinedRealm(realmExtensions).also {
            // Add some extensions
            it.stringProto.defineBuiltin("addColor", 0, CTBuiltin.StringAddColor)
            it.stringProto.defineBuiltin("addFormatting", 0, CTBuiltin.StringAddFormatting)
            it.stringProto.defineBuiltin("removeFormatting", 0, CTBuiltin.StringRemoveFormatting)
            it.stringProto.defineBuiltin("replaceFormatting", 0, CTBuiltin.StringReplaceFormatting)
            it.numberProto.defineBuiltin("easeOut", 3, CTBuiltin.NumberEaseOut)
            it.numberProto.defineBuiltin("easeColor", 3, CTBuiltin.NumberEaseColor)
        }
    }

    override fun promiseRejectionTracker(realm: Realm, promise: JSObject, operation: String) {
        if (operation == "reject") {
            Agent.activeAgent.microtaskQueue.addMicrotask {
                // If promise does not have any handlers by the time this microtask is ran, it
                // will not have any handlers, and we can print a warning
                if (!promise.getSlotAs<Boolean>(SlotName.PromiseIsHandled)) {
                    val result = promise.getSlotAs<JSValue>(SlotName.PromiseResult)
                    JSLoader.console.textPane.appendln(
                        "Unhandled promise rejection: ${result.toJSString(realm).string}",
                        Color.RED,
                    )
                }
            }
        }
    }

    override fun makeSourceInfo(file: File): SourceInfo {
        // Treat everything as a module
        return FileSourceInfo(file, isModule = true)
    }
}
