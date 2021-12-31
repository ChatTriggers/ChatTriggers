package com.chattriggers.ctjs.launch.mixins.transformers

import com.chattriggers.ctjs.minecraft.wrappers.objects.entity.PlayerMP
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.kotlin.MCEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Desc
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.LocalCapture

@Mixin(ItemEntity::class)
class ItemEntityMixin {
    @Inject(
        method = ["onPlayerCollision"],
        at = [At(
            value = "INVOKE",
            desc = Desc("sendPickup", owner = PlayerEntity::class, args = [Entity::class, Int::class]),
        )],
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD,
    )
    fun injectOnPlayerCollision(entity: MCEntity, item: MCEntity, count: Int, ci: CallbackInfo) {
        // TODO("fabric"): Is entity always the player? If so, don't send
        //                 the player (breaking change).
        if (item is ItemEntity) {
            // TODO("fabric"): Pass item instead of just pos and velocity?
            TriggerType.PickupItem.triggerAll(
                Item(item.stack),
                item.pos,
                item.velocity,
                ci,
            )
        }
    }
}
