package com.chattriggers.ctjs.engine.langs.js.objects

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.reevajs.reeva.core.realm.Realm
import com.reevajs.reeva.runtime.JSValue
import com.reevajs.reeva.runtime.collections.JSArguments
import com.reevajs.reeva.runtime.toJSString
import com.reevajs.reeva.runtime.toNumber
import com.reevajs.reeva.utils.toValue

object CTExtensions {
    @JvmStatic
    fun stringAddColor(realm: Realm, arguments: JSArguments): JSValue {
        val thisString = arguments.argument(0).toJSString(realm).string
        return ChatLib.addColor(thisString).toValue()
    }

    @JvmStatic
    fun stringAddFormatting(realm: Realm, arguments: JSArguments): JSValue {
        return stringAddColor(realm, arguments)
    }

    @JvmStatic
    fun stringRemoveFormatting(realm: Realm, arguments: JSArguments): JSValue {
        val thisString = arguments.argument(0).toJSString(realm).string
        return ChatLib.removeFormatting(thisString).toValue()
    }

    @JvmStatic
    fun stringReplaceFormatting(realm: Realm, arguments: JSArguments): JSValue {
        val thisString = arguments.argument(0).toJSString(realm).string
        return ChatLib.replaceFormatting(thisString).toValue()
    }

    @JvmStatic
    fun numberEaseOut(realm: Realm, arguments: JSArguments): JSValue {
        val thisNumber = arguments.argument(0).toNumber(realm)
        return ModuleGlobalObject.easeOut(
            realm,
            JSArguments(
                listOf(thisNumber) + arguments,
                arguments.thisValue,
            )
        )
    }

    @JvmStatic
    fun numberEaseColor(realm: Realm, arguments: JSArguments): JSValue {
        val thisNumber = arguments.argument(0).toNumber(realm)
        return ModuleGlobalObject.easeColor(
            realm,
            JSArguments(
                listOf(thisNumber) + arguments,
                arguments.thisValue,
            )
        )
    }
}