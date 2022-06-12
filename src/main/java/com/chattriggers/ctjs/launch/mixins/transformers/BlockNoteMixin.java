package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.minecraft.listeners.events.NoteBlockEvent;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.block.BlockNote;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.util.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC>=11701
//$$ import net.minecraft.world.InteractionHand;
//$$ import net.minecraft.world.InteractionResult;
//$$ import net.minecraft.world.entity.player.Player;
//$$ import net.minecraft.world.phys.BlockHitResult;
//#endif

@Mixin(BlockNote.class)
public class BlockNoteMixin {
    @Inject(
            //#if MC<=11202
            method = "onBlockEventReceived",
            //#elseif MC>=11701
            //$$ method = "triggerEvent",
            //#endif
            at = @At("HEAD"),
            cancellable = true
    )
    //#if MC<=11202
    private void chattriggers_noteBlockPlayTrigger(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam, CallbackInfoReturnable<Boolean> cir) {
    //#elseif MC>=11701
    //$$ private void chattriggers_noteBlockPlayTrigger(BlockState arg, Level arg2, BlockPos pos, int j, int k, CallbackInfoReturnable<Boolean> cir) {
    //#endif
        Vector3f vec = new Vector3f(pos.getX(), pos.getY(), pos.getZ());

        //#if MC<=11202
        NoteBlockEvent event = new NoteBlockEvent(eventID);
        event.setInstrument$chattriggers(eventParam);
        //#elseif MC>=11701
        //$$ NoteBlockEvent event = new NoteBlockEvent(arg.getValue(NoteBlock.NOTE));
        //$$ event.setInstrument$chattriggers(arg.getValue(NoteBlock.INSTRUMENT));
        //#endif

        TriggerType.NoteBlockPlay.triggerAll(vec, event.getNote().name(), event.getOctave(), event);

        if (event.isCanceled()) {
            cir.setReturnValue(false);
        }
    }

    //#if MC>=11701
    //$$ @Inject(
    //$$         method = "use",
    //$$         at = @At(
    //$$                 value = "INVOKE",
    //$$                 target = "Lnet/minecraft/world/level/block/state/BlockState;setValue(Lnet/minecraft/world/level/block/state/properties/Property;Ljava/lang/Comparable;)Ljava/lang/Object;"
    //$$         ),
    //$$         cancellable = true
    //$$ )
    //$$ private void chattriggers_noteBlockChangeTrigger(BlockState arg, Level arg2, BlockPos arg3, Player arg4, InteractionHand arg5, BlockHitResult arg6, CallbackInfoReturnable<InteractionResult> cir) {
    //$$     Vector3f vec = new Vector3f(arg3.getX(), arg3.getY(), arg3.getZ());
    //$$
    //$$     NoteBlockEvent event = new NoteBlockEvent(arg.getValue(NoteBlock.NOTE));
    //$$     event.setInstrument$chattriggers(arg.getValue(NoteBlock.INSTRUMENT));
    //$$
    //$$     TriggerType.NoteBlockPlay.triggerAll(vec, event.getNote().name(), event.getOctave(), event);
    //$$
    //$$     if (event.isCanceled()) {
    //$$         cir.setReturnValue(InteractionResult.FAIL);
    //$$     }
    //$$ }
    //#endif
}
