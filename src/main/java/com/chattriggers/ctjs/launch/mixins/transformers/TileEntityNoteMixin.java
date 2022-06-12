package com.chattriggers.ctjs.launch.mixins.transformers;

//#if MC<=11202
import com.chattriggers.ctjs.minecraft.listeners.events.NoteBlockEvent;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.BlockPos;
import org.lwjgl.util.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityNote.class)
public class TileEntityNoteMixin extends TileEntity {
    @Shadow
    public byte note;

    @Inject(
            method = "changePitch",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/tileentity/TileEntityNote;markDirty()V"
            ),
            cancellable = true
    )
    private void chattriggers_noteBlockChangeEvent(CallbackInfo ci) {
        BlockPos pos = super.getPos();
        Vector3f vec = new Vector3f(pos.getX(), pos.getY(), pos.getZ());
        NoteBlockEvent event = new NoteBlockEvent(note);

        TriggerType.NoteBlockPlay.triggerAll(vec, event.getNote().name(), event.getOctave(), event);

        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
//#endif