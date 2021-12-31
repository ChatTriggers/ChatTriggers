package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent;
import com.chattriggers.ctjs.minecraft.wrappers.objects.entity.Entity;
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(
        method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;",
        at = @At(value = "RETURN", slice = "default"),
        slice = @Slice(
            id = "default",
            from = @At(
                value = "NEW",
                target = "Lnet/minecraft/entity/ItemEntity;<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V"
            )
        ),
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void injectDropItem(
        ItemStack stack,
        boolean throwRandomly,
        boolean retainOwnership,
        CallbackInfoReturnable<ItemEntity> cir,
        double d,
        ItemEntity itemEntity
    ) {
        CancellableEvent event = new CancellableEvent();
        TriggerType.DropItem.triggerAll(new Item(stack), new Entity(itemEntity), event);
        if (event.isCancelled()) {
            cir.setReturnValue(null);
            cir.cancel();
        }
    }
}
