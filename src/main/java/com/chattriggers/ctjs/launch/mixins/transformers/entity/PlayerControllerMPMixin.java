package com.chattriggers.ctjs.launch.mixins.transformers.entity;

import com.chattriggers.ctjs.minecraft.listeners.ClientListener;
import com.chattriggers.ctjs.minecraft.listeners.events.CancellableEvent;
import com.chattriggers.ctjs.minecraft.wrappers.entity.Entity;
import com.chattriggers.ctjs.minecraft.wrappers.world.block.BlockFace;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC>=11701
//$$ import net.minecraft.world.InteractionHand;
//$$ import net.minecraft.world.InteractionResult;
//$$ import net.minecraft.world.level.Level;
//$$ import net.minecraft.world.phys.BlockHitResult;
//#endif

@Mixin(PlayerControllerMP.class)
public class PlayerControllerMPMixin {
    @Inject(
            //#if MC<=11202
            method = "attackEntity",
            //#elseif MC>=11701
            //$$ method = "attack",
            //#endif
            at = @At("HEAD"),
            cancellable = true
    )
    private void chattriggers_attackEntityTrigger(EntityPlayer playerIn, net.minecraft.entity.Entity targetEntity, CallbackInfo ci) {
        TriggerType.AttackEntity.triggerAll(new Entity(targetEntity), ci);
    }

    @Inject(
            //#if MC<=11202
            method = "clickBlock",
            //#elseif MC>=11701
            //$$ method = "startDestroyBlock",
            //#endif
            at = @At("HEAD"),
            cancellable = true
    )
    private void chattriggers_hitBlockTrigger(BlockPos loc, EnumFacing face, CallbackInfoReturnable<Boolean> cir) {
        CancellableEvent event = new CancellableEvent();

        TriggerType.HitBlock.triggerAll(
            com.chattriggers.ctjs.minecraft.wrappers.World.getBlockAt(loc.getX(), loc.getY(), loc.getZ()).withFace(BlockFace.fromMCEnumFacing(face)),
            com.chattriggers.ctjs.minecraft.wrappers.Player.Hand.LEFT,
            event
        );

        if (event.isCanceled())
            cir.setReturnValue(false);
    }

    @Inject(
            //#if MC<=11202
            method = "onPlayerDestroyBlock",
            //#elseif MC>=11701
            //$$ method = "destroyBlock",
            //#endif
            at = @At("HEAD")
    )
    private void chattriggers_blockBreakTrigger(
            BlockPos pos,
            //#if MC<=11202
            EnumFacing side,
            //#endif
            CallbackInfoReturnable<Boolean> cir
    ) {
        TriggerType.BlockBreak.triggerAll(
                com.chattriggers.ctjs.minecraft.wrappers.World.getBlockAt(pos.getX(), pos.getY(), pos.getZ())
                        //#if MC<=11202
                        .withFace(BlockFace.fromMCEnumFacing(side))
                        //#endif
        );
    }

    //#if MC<=11202
    @Inject(
            method = "onPlayerRightClick",
            at = @At("HEAD"),
            cancellable = true
    )
    private void chattriggers_playerInteractTriggerBlock(EntityPlayerSP player, WorldClient worldIn, ItemStack heldStack, BlockPos hitPos, EnumFacing side, Vec3 hitVec, CallbackInfoReturnable<Boolean> cir) {
        boolean cancelled = ClientListener.INSTANCE.onPlayerInteract$chattriggers(
                ClientListener.PlayerInteractAction.RIGHT_CLICK_BLOCK,
                hitPos
        );

        if (cancelled) {
            cir.setReturnValue(true);
        }
    }

    @Inject(
            method = "sendUseItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private void chattriggers_playerInteractTriggerAir(EntityPlayer playerIn, net.minecraft.world.World worldIn, ItemStack itemStackIn, CallbackInfoReturnable<Boolean> cir) {
        boolean cancelled = ClientListener.INSTANCE.onPlayerInteract$chattriggers(
                ClientListener.PlayerInteractAction.RIGHT_CLICK_EMPTY,
                null
        );

        if (cancelled) {
            cir.setReturnValue(true);
        }
    }
    //#elseif MC>=11701
    //$$ @Inject(
    //$$         method = "useItemOn",
    //$$         at = @At("HEAD"),
    //$$         cancellable = true
    //$$ )
    //$$ private void chattriggers_playerInteractTriggerBlock(LocalPlayer arg, ClientLevel arg2, InteractionHand arg3, BlockHitResult arg4, CallbackInfoReturnable<InteractionResult> cir) {
    //$$     boolean cancelled = ClientListener.INSTANCE.onPlayerInteract$chattriggers(
    //$$             ClientListener.PlayerInteractAction.RIGHT_CLICK_BLOCK,
    //$$             arg4.getBlockPos()
    //$$     );
    //$$
    //$$     if (cancelled) {
    //$$         cir.setReturnValue(InteractionResult.FAIL);
    //$$     }
    //$$ }
    //$$
    //$$ @Inject(
    //$$         method = "useItem",
    //$$         at = @At("HEAD"),
    //$$         cancellable = true
    //$$ )
    //$$ private void chattriggers_playerInteractTriggerAir(Player arg, Level arg2, InteractionHand arg3, CallbackInfoReturnable<InteractionResult> cir) {
    //$$     boolean cancelled = ClientListener.INSTANCE.onPlayerInteract$chattriggers(
    //$$             ClientListener.PlayerInteractAction.RIGHT_CLICK_EMPTY,
    //$$             null
    //$$     );
    //$$
    //$$     if (cancelled) {
    //$$         cir.setReturnValue(InteractionResult.FAIL);
    //$$     }
    //$$ }
    //#endif
}
