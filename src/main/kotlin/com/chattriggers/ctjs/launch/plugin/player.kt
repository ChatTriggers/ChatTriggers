package com.chattriggers.ctjs.launch.plugin

import com.chattriggers.ctjs.minecraft.listeners.ClientListener
import me.falsehonesty.asmhelper.dsl.At
import me.falsehonesty.asmhelper.dsl.InjectionPoint
import me.falsehonesty.asmhelper.dsl.inject
import me.falsehonesty.asmhelper.dsl.instructions.Descriptor
import me.falsehonesty.asmhelper.dsl.writers.asm
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

fun makePlayerTransformers() {
//    injectItemDropEvent()
}

private fun injectItemDropEvent() = inject {
    // Lnet/minecraft/entity/player/EntityPlayer;dropOneItem(Z)Lnet/minecraft/entity/item/EntityItem;
    className = "net/minecraft/entity/player/EntityPlayer"
    methodName = "dropOneItem"
    methodDesc = "(Z)Lnet/minecraft/entity/item/EntityItem;"

    // net/minecraft/entity/player/InventoryPlayer.getCurrentItem ()Lnet/minecraft/item/ItemStack;
    val invoker = InjectionPoint.INVOKE(
        Descriptor(
            "net/minecraft/entity/player/InventoryPlayer",
            "getCurrentItem",
            "()Lnet/minecraft/item/ItemStack;"
        ), 0
    )

    /*
    GETFIELD net/minecraft/entity/player/EntityPlayer.inventory : Lnet/minecraft/entity/player/InventoryPlayer;
    INVOKEVIRTUAL net/minecraft/entity/player/InventoryPlayer.getCurrentItem ()Lnet/minecraft/item/ItemStack;
    ASTORE 2
               <-------------- Where we are placing our code
   L1
    LINENUMBER 828 L1
    ALOAD 2
     */
    at = At(invoker, shift = 1, before = false)

    codeBlock {
        val local0 = shadowLocal<EntityPlayer>()
        val local2 = shadowLocal<ItemStack>()

        code {
            if (ClientListener.onDropItem(local0, local2)) asm { methodReturn() }
        }
    }
}