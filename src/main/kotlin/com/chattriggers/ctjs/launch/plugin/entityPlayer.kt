package com.chattriggers.ctjs.launch.plugin

import me.falsehonesty.asmhelper.dsl.At
import me.falsehonesty.asmhelper.dsl.InjectionPoint
import me.falsehonesty.asmhelper.dsl.inject
import me.falsehonesty.asmhelper.dsl.instructions.*

fun injectEntityPlayer() = inject {
    className = ENTITY_PLAYER
    methodName = "dropOneItem"
    methodDesc = "(Z)L$ENTITY_ITEM;"

    at = At(
        InjectionPoint.INVOKE(
            Descriptor(
                INVENTORY_PLAYER,
                "getCurrentItem",
                "()L$ITEM_STACK;"
            ),
            ordinal = 0
        ),
        shift = 2
    )

    fieldMaps = mapOf("inventory" to "field_71071_by")

    methodMaps = mapOf(
        "getCurrentItem" to "func_70448_g",
        "func_71040_bB" to "dropOneItem",
        "func_70448_g" to "getCurrentItem"
    )

    insnList {
        invokeKOBjectFunction(CLIENT_LISTENER, "onDropItem", "(L$ENTITY_PLAYER;L$ITEM_STACK;)Z") {
            aload(0)

            getLocalField(ENTITY_PLAYER, "inventory", "L$INVENTORY_PLAYER;")
            invokeVirtual(INVENTORY_PLAYER, "getCurrentItem", "()L$ITEM_STACK;")
        }

        ifClause(JumpCondition.FALSE) {
            aconst_null()
            areturn()
        }
    }
}
