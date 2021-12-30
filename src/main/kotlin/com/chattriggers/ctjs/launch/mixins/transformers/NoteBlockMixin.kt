package com.chattriggers.ctjs.launch.mixins.transformers

import com.chattriggers.ctjs.triggers.TriggerType
import net.minecraft.block.BlockState
import net.minecraft.block.NoteBlock
import net.minecraft.block.enums.Instrument
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3f
import net.minecraft.world.World
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import org.spongepowered.asm.mixin.injection.callback.LocalCapture

@Mixin(NoteBlock::class)
class NoteBlockMixin {
    @Inject(
        method = ["onUse"],
        at = [At("HEAD")],
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD,
    )
    fun injectOnUse(
        noteBlock: NoteBlock,
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult,
        cir: CallbackInfoReturnable<ActionResult>,
    ) {
        TriggerType.NoteBlockChange.triggerAll(
            Vec3f(pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat()),
            // TODO("fabric"): Note name and octave?
            cir,
        )
    }

    @Inject(
        method = ["onSyncedBlockEvent"],
        at = [At("HEAD")],
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD,
    )
    fun injectOnSyncedBlockEvent(
        noteBlock: NoteBlock,
        state: BlockState,
        world: World,
        pos: BlockPos,
        type: Int,
        data: Int,
        cir: CallbackInfoReturnable<Boolean>,
    ) {
        // val instrument = state.get(NoteBlock.INSTRUMENT) as Instrument
        // val note = state.get(NoteBlock.NOTE) as Int

        TriggerType.NoteBlockPlay.triggerAll(
            Vec3f(pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat()),
            // TODO("fabric"): Note name and octave?
            cir,
        )
    }
}
