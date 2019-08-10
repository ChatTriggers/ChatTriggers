package com.chattriggers.ctjs.minecraft.mixins;

import com.chattriggers.ctjs.minecraft.imixins.IMixinEntityPlayer;
import com.chattriggers.ctjs.minecraft.listeners.ClientListener;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer implements IMixinEntityPlayer {
    @Shadow(remap = false)
    private String displayname;

    @Shadow
    public InventoryPlayer inventory;

    @Override
    public void setDisplayName(String displayname) {
        this.displayname = displayname;
    }

    @Inject(
            method = "dropOneItem",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/InventoryPlayer;getCurrentItem()Lnet/minecraft/item/ItemStack;",
                    ordinal = 0,
                    shift = At.Shift.BY,
                    by = 2
            ),
            cancellable = true
    )
    private void injectItemThrowTrigger(boolean dropAll, CallbackInfoReturnable<EntityItem> cir) {
        if (ClientListener.INSTANCE.onDropItem((EntityPlayer) (Object) this, inventory.getCurrentItem())) cir.setReturnValue(null);
    }
}
