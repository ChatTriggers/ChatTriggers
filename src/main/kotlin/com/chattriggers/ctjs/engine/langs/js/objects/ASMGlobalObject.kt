package com.chattriggers.ctjs.engine.langs.js.objects

import com.chattriggers.ctjs.engine.langs.js.impl.CTBuiltin
import com.chattriggers.ctjs.engine.langs.js.JSLoader
import com.reevajs.reeva.core.realm.Realm
import com.reevajs.reeva.jvmcompat.JSClassInstanceObject
import com.reevajs.reeva.jvmcompat.JSClassObject
import com.reevajs.reeva.runtime.JSGlobalObject
import com.reevajs.reeva.runtime.JSValue
import com.reevajs.reeva.runtime.Operations
import com.reevajs.reeva.runtime.collections.JSArguments
import com.reevajs.reeva.runtime.primitives.JSUndefined
import com.reevajs.reeva.runtime.toPrintableString
import com.reevajs.reeva.utils.Errors

open class ASMGlobalObject protected constructor(realm: Realm) : JSGlobalObject(realm) {
    override fun init() {
        super.init()

        defineOwnProperty("Thread", JSClassObject.create(realm, Thread::class.java))
        defineOwnProperty("Console", JSClassInstanceObject.wrap(realm, JSLoader.console))

        defineBuiltin("sync", 2, CTBuiltin.GlobalSync)
        defineBuiltin("print", 2, CTBuiltin.GlobalPrint)
    }

    companion object {
        fun create(realm: Realm) = ASMGlobalObject(realm).initialize()

        @JvmStatic
        fun sync(realm: Realm, arguments: JSArguments): JSValue {
            val func = arguments.argument(0)
            val lock = arguments.argument(1)

            if (!Operations.isCallable(func))
                Errors.Custom("First argument to sync() must be a function").throwTypeError(realm)

            return synchronized(lock) {
                Operations.call(realm, func, JSUndefined)
            }
        }

        @JvmStatic
        fun print(realm: Realm, arguments: JSArguments): JSValue {
            JSLoader.console.println(arguments.argument(0).toPrintableString())
            return JSUndefined
        }
    }
}