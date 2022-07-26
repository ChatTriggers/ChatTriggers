package com.chattriggers.ctjs.mixins.render;

import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.util.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityRendererDispatcher.class)
public class TileEntityRendererDispatcherMixin {
    @Inject(
            //#if MC<=11202
            method = "renderTileEntityAt(Lnet/minecraft/tileentity/TileEntity;DDDFI)V",
            //#elseif MC>=11701
            //$$ method = "tryRender",
            //#endif
            at = @At("HEAD"),
            cancellable = true
    )
    //#if MC<=11202
    private void chattriggers_renderTileEntityTrigger(TileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage, CallbackInfo ci) {
    //#elseif MC>=11701
    //$$ private static void chattriggers_renderTileEntityTrigger(BlockEntity tileEntityIn, Runnable runnable, CallbackInfo ci) {
    //#endif
        // TODO(BREAKING) Removed partialTicks parameter
        TriggerType.RenderTileEntity.triggerAll(
                new com.chattriggers.ctjs.minecraft.wrappers.entity.TileEntity(tileEntityIn),
                //#if MC<=11202
                new Vector3f((float) x, (float) y, (float) z),
                //#elseif MC>=11701
                //$$ new Vector3f(tileEntityIn.getBlockPos().getX(), tileEntityIn.getBlockPos().getY(), tileEntityIn.getBlockPos().getZ()),
                //#endif
                ci
        );
    }

    @Inject(
            //#if MC<=11202
            method = "renderTileEntityAt(Lnet/minecraft/tileentity/TileEntity;DDDFI)V",
            //#elseif MC>=11701
            //$$ method = "tryRender",
            //#endif
            at = @At("TAIL")
    )
    //#if MC<=11202
    private void chattriggers_postRenderTileEntityTrigger(TileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage, CallbackInfo ci) {
    //#elseif MC>=11701
    //$$ private static void chattriggers_postRenderTileEntityTrigger(BlockEntity tileEntityIn, Runnable runnable, CallbackInfo ci) {
    //#endif
        // TODO(BREAKING) Removed partialTicks parameter
        TriggerType.PostRenderTileEntity.triggerAll(
                new com.chattriggers.ctjs.minecraft.wrappers.entity.TileEntity(tileEntityIn),
                //#if MC<=11202
                new Vector3f((float) x, (float) y, (float) z)
                //#elseif MC>=11701
                //$$ new Vector3f(tileEntityIn.getBlockPos().getX(), tileEntityIn.getBlockPos().getY(), tileEntityIn.getBlockPos().getZ())
                //#endif
        );
    }
}
