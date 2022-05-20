package com.chattriggers.ctjs.launch.mixins.transformers.render;

//#if MC<=11202
import com.chattriggers.ctjs.minecraft.wrappers.entity.TileEntity;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import org.lwjgl.util.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityRendererDispatcher.class)
public class TileEntityRendererDispatcherMixin {
    @Inject(
        method = "renderTileEntityAt(Lnet/minecraft/tileentity/TileEntity;DDDFI)V",
        at = @At("HEAD"),
        cancellable = true
    )
    void injectRenderTileEntityAtPre(
        net.minecraft.tileentity.TileEntity tileEntityIn,
        double x,
        double y,
        double z,
        float partialTicks,
        int destroyStage,
        CallbackInfo ci
    ) {
        TriggerType.RenderTileEntity.triggerAll(
            new TileEntity(tileEntityIn),
            new Vector3f((float) x, (float) y, (float) z),
            partialTicks,
            ci
        );
    }

    @Inject(
        method = "renderTileEntityAt(Lnet/minecraft/tileentity/TileEntity;DDDFI)V",
        at = @At("TAIL")
    )
    void injectRenderTileEntityAtPost(
        net.minecraft.tileentity.TileEntity tileEntityIn,
        double x,
        double y,
        double z,
        float partialTicks,
        int destroyStage,
        CallbackInfo ci
    ) {
        TriggerType.PostRenderTileEntity.triggerAll(
            new TileEntity(tileEntityIn),
            new Vector3f((float) x, (float) y, (float) z),
            partialTicks
        );
    }
}
//#endif
