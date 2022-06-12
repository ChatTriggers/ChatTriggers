package com.chattriggers.ctjs.launch.mixins.transformers.entity;

import com.chattriggers.ctjs.minecraft.listeners.ClientListener;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC>=11701
//$$ import net.minecraft.core.BlockPos;
//#endif

@Mixin(EntityPlayerSP.class)
public abstract class EntityPlayerSPMixin extends EntityPlayer {
    //#if MC<=11202
    public EntityPlayerSPMixin(World worldIn, GameProfile gameProfileIn) {
        super(worldIn, gameProfileIn);
    }
    //#elseif MC>=11701
    //$$ public EntityPlayerSPMixin(Level arg, BlockPos arg2, float f, GameProfile gameProfile) {
    //$$     super(arg, arg2, f, gameProfile);
    //$$ }
    //#endif

    @Inject(
            //#if MC<=11202
            method = "dropOneItem",
            //#elseif MC>=11701
            //$$ method = "drop",
            //#endif
            at = @At("HEAD"),
            cancellable = true
    )
    private void chattriggers_dropItemTriggerSP(boolean dropAll, CallbackInfoReturnable<EntityItem> cir) {
        ClientListener.INSTANCE.onDropItem$chattriggers(
                //#if MC<=11202
                super.inventory.getCurrentItem(),
                //#elseif MC>=11701
                //$$ super.getInventory().getSelected(),
                //#endif
                cir
        );
    }
}
