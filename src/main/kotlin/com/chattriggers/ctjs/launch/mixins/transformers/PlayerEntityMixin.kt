package com.chattriggers.ctjs.launch.mixins.transformers

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent
import com.chattriggers.ctjs.minecraft.wrappers.objects.entity.Entity
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.triggers.TriggerType
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.Slice
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import org.spongepowered.asm.mixin.injection.callback.LocalCapture

@Mixin(PlayerEntity::class)
class PlayerEntityMixin {
    @Inject(
        method = ["dropItem"],
        slice = [Slice(
            id = "default",
            from = At(
                value = "NEW",
                target = "net/minecraft/entity/ItemEntity(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V",
            ),
        )],
        at = [At(value = "RETURN", slice = "default")],
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD,
    )
    fun injectDropItem(
        entity: PlayerEntity,
        stack: ItemStack,
        throwRandomly: Boolean,
        retainOwnership: Boolean,
        itemEntity: ItemEntity,
        cir: CallbackInfoReturnable<ItemStack?>,
    ) {
        val event = CancellableEvent()
        TriggerType.DropItem.triggerAll(Item(stack), Entity(itemEntity), event)
        if (event.isCanceled()) {
            cir.returnValue = null
            cir.cancel()
        }
    }
}
