package com.chattriggers.ctjs.launch.mixins.transformers.entity;

//#if MC<=11202
import com.chattriggers.ctjs.minecraft.listeners.events.CancellableEvent;
import com.chattriggers.ctjs.minecraft.wrappers.Player;
import com.chattriggers.ctjs.minecraft.wrappers.World;
import com.chattriggers.ctjs.minecraft.wrappers.entity.Entity;
import com.chattriggers.ctjs.minecraft.wrappers.world.block.BlockFace;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class PlayerControllerMPMixin {
    @Inject(method = "attackEntity", at = @At("HEAD"), cancellable = true)
    private void chattriggers_attackEntityTrigger(EntityPlayer playerIn, net.minecraft.entity.Entity targetEntity, CallbackInfo ci) {
        TriggerType.AttackEntity.triggerAll(new Entity(targetEntity), ci);
    }

    @Inject(method = "clickBlock", at = @At("HEAD"), cancellable = true)
    private void chattriggers_hitBlockTrigger(BlockPos loc, EnumFacing face, CallbackInfoReturnable<Boolean> cir) {
        CancellableEvent event = new CancellableEvent();

        TriggerType.HitBlock.triggerAll(
            World.getBlockAt(loc.getX(), loc.getY(), loc.getZ()).withFace(BlockFace.fromMCEnumFacing(face)),
            Player.Hand.LEFT,
            event
        );

        if (event.isCanceled())
            cir.setReturnValue(false);
    }

    @Inject(method = "onPlayerDestroyBlock", at = @At("HEAD"))
    private void chattriggers_blockBreakTrigger(BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> cir) {
        TriggerType.BlockBreak.triggerAll(
            World.getBlockAt(pos.getX(), pos.getY(), pos.getZ()).withFace(BlockFace.fromMCEnumFacing(side))
        );
    }
}
//#endif
