package com.chattriggers.ctjs.engine.langs.js.objects

import com.chattriggers.ctjs.engine.langs.js.*
import com.chattriggers.ctjs.minecraft.libs.*
import com.chattriggers.ctjs.minecraft.libs.renderer.*
import com.reevajs.reeva.core.Agent
import com.reevajs.reeva.core.realm.Realm
import com.reevajs.reeva.jvmcompat.JSClassInstanceObject
import com.reevajs.reeva.runtime.*
import com.reevajs.reeva.runtime.collections.JSArguments
import com.reevajs.reeva.runtime.primitives.JSNumber
import com.reevajs.reeva.runtime.primitives.JSTrue
import com.reevajs.reeva.runtime.primitives.JSUndefined
import com.reevajs.reeva.runtime.singletons.JSMathObject
import com.reevajs.reeva.utils.Errors
import com.reevajs.reeva.utils.key
import com.reevajs.reeva.utils.toValue

class ModuleGlobalObject private constructor(realm: Realm) : ASMGlobalObject(realm) {
    override fun init() {
        super.init()

        moduleGlobalObjectInitHelper(this)
    }

    companion object {
        fun create(realm: Realm) = ModuleGlobalObject(realm).initialize()

        @JvmStatic
        fun setTimeout(realm: Realm, arguments: JSArguments): JSValue {
            val delay = arguments.argument(1).toNumber(realm).number.toLong()
            scheduleFunction(realm, arguments, delay)
            return JSUndefined
        }

        @JvmStatic
        fun setImmediate(realm: Realm, arguments: JSArguments): JSValue {
            scheduleFunction(realm, arguments, 0)
            return JSUndefined
        }

        private fun scheduleFunction(realm: Realm, arguments: JSArguments, delay: Long) {
            val function = arguments.argument(0)
            if (!Operations.isCallable(function))
                Errors.NotCallable(function.toJSString(realm).string).throwTypeError(realm)

            JSLoader.eventLoop.scheduleTask(realm, delay) {
                Operations.call(realm, function, arguments.thisValue)
            }
        }

        @JvmStatic
        fun cancel(realm: Realm, arguments: JSArguments): JSValue {
            val arg = arguments.argument(0)
            try {
                EventLib.cancel(arg)
            } catch (e: IllegalArgumentException) {
                val isCancelable = Operations.invoke(realm, arg, "isCancelable".key())
                if (isCancelable.toBoolean())
                    Operations.invoke(realm, arg, "setCanceled".key(), listOf(JSTrue))
            }

            return JSUndefined
        }

        @JvmStatic
        fun register(realm: Realm, arguments: JSArguments): JSValue {
            val triggerType = arguments.argument(0).toJSString(realm).string
            val function = arguments.argument(1)

            if (!Operations.isCallable(function))
                Errors.NotCallable(function.toJSString(realm).string).throwTypeError(realm)

            val trigger = JSRegister.register(triggerType, function)
            return JSClassInstanceObject.wrap(realm, trigger)
        }

        @JvmStatic
        fun easeOut(realm: Realm, arguments: JSArguments): JSValue {
            val start = arguments.argument(0).toNumber(realm)
            val finish = arguments.argument(1).toNumber(realm)
            val speed = arguments.argument(2).toNumber(realm)
            val jump = arguments.argument(3).let {
                if (it == JSUndefined) JSNumber(1) else it.toNumber(realm)
            }

            val finishMinusStart = finish.sub(realm, start)
            val abs = JSMathObject.abs(realm, JSArguments(listOf(finishMinusStart)))
            val absDivJump = abs.div(realm, jump)
            val floor = JSMathObject.floor(realm, JSArguments(listOf(absDivJump)))
            val gt = floor.isGreaterThan(realm, JSNumber.ZERO)

            if (gt)
                return start.add(realm, finishMinusStart.div(realm, speed))
            return finish
        }

        @JvmStatic
        fun easeColor(realm: Realm, arguments: JSArguments): JSValue {
            val start = arguments.argument(0).toNumber(realm)
            val finish = arguments.argument(1).toNumber(realm)
            val speed = arguments.argument(2).toNumber(realm)
            val jump = arguments.argument(3)

            val ff = JSNumber(0xff)

            fun easeOutHelper(n: Int): Long {
                return easeOut(realm, JSArguments(listOf(
                    start.shr(realm, JSNumber(n)).and(realm, ff),
                    finish.shr(realm, JSNumber(n)).and(realm, ff),
                    speed,
                    jump
                ))).toNumber(realm).number.toLong()
            }

            val a1 = easeOutHelper(16)
            val a2 = easeOutHelper(8)
            val a3 = easeOutHelper(0)
            val a4 = easeOutHelper(24)

            return Renderer.color(a1, a2, a3, a4).toValue()
        }
    }
}