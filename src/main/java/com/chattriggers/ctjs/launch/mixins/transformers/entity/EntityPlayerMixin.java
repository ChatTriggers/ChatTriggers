package com.chattriggers.ctjs.launch.mixins.transformers.entity;

import com.chattriggers.ctjs.minecraft.listeners.ClientListener;
import com.chattriggers.ctjs.minecraft.listeners.events.CancellableEvent;
import com.chattriggers.ctjs.minecraft.wrappers.inventory.Item;
import com.chattriggers.ctjs.triggers.TriggerType;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.util.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public class EntityPlayerMixin {
    @Inject(
            //#if MC<=11202
            method = "dropPlayerItemWithRandomChoice",
            //#elseif MC>=11701
            //$$ method = "drop(Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/item/ItemEntity;",
            //#endif
            at = @At("HEAD"),
            cancellable = true
    )
    private void chattriggers_dropItemTrigger(ItemStack itemStackIn, boolean unused, CallbackInfoReturnable<EntityItem> cir) {
        ClientListener.INSTANCE.onDropItem$chattriggers(itemStackIn, cir);
    }
}
