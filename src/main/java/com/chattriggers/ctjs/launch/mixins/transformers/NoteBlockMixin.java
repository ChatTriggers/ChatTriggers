package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(NoteBlock.class)
public class NoteBlockMixin {
    @Inject(
        method = "onUse",
        at = @At("HEAD"),
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void injectOnUse(
        BlockState state,
        World world,
        BlockPos pos,
        PlayerEntity player,
        Hand hand,
        BlockHitResult hit,
        CallbackInfoReturnable<ActionResult> cir
    ) {
        TriggerType.NoteBlockChange.triggerAll(
            new Vec3f((float) pos.getX(), (float) pos.getY(), (float) pos.getZ()),
            // TODO("fabric"): Note name and octave?
            cir
        );
    }

    @Inject(
        method = "onSyncedBlockEvent",
        at = @At("HEAD"),
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void injectOnSyncedBlockEvent(
        BlockState state,
        World world,
        BlockPos pos,
        int type,
        int data,
        CallbackInfoReturnable<Boolean> cir
    ) {
        TriggerType.NoteBlockPlay.triggerAll(
            new Vec3f((float) pos.getX(), (float) pos.getY(), (float) pos.getZ()),
            // TODO("fabric"): Note name and octave?
            cir
        );
    }
}
