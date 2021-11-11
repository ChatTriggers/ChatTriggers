package com.chattriggers.ctjs.launch.plugin

import com.chattriggers.ctjs.minecraft.listeners.ClientListener
import dev.falsehonesty.asmhelper.dsl.At
import dev.falsehonesty.asmhelper.dsl.InjectionPoint
import dev.falsehonesty.asmhelper.dsl.code.CodeBlock.Companion.aReturn
import dev.falsehonesty.asmhelper.dsl.inject
import dev.falsehonesty.asmhelper.dsl.instructions.Descriptor
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer

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
        "func_71040_bB" to "dropOneItem"
    )

    codeBlock {
        val local0 = shadowLocal<EntityPlayer>()
        val inventory = shadowField<InventoryPlayer>()

        code {
            if (ClientListener.onDropItem(local0, inventory.getCurrentItem())) {
                aReturn(null)
            }
        }
    }
}
