package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
        method = "onPlayerCollision",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;sendPickup(Lnet/minecraft/entity/Entity;I)V"
        ),
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void injectOnPlayerCollision(
        PlayerEntity player,
        CallbackInfo ci,
        ItemStack itemStack,
        Item item,
        int i
    ) {
        // TODO("fabric"): Is entity always the player? If so, don't send
        //                 the player (breaking change).
        // TODO("fabric"): Pass item instead of just pos and velocity?
        TriggerType.PickupItem.triggerAll(
            new com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item(itemStack),
            getPos(),
            getVelocity(),
            ci
        );
    }
}
