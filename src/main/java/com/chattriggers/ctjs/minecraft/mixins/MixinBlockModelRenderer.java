package com.chattriggers.ctjs.minecraft.mixins;

import com.chattriggers.ctjs.minecraft.wrappers.objects.block.Block;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({BlockModelRenderer.class})
public class MixinBlockModelRenderer {
    @Inject(
            method = "renderModel(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/resources/model/IBakedModel;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/BlockPos;Lnet/minecraft/client/renderer/WorldRenderer;Z)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onBlockRender(IBlockAccess blockAccessIn, IBakedModel modelIn, IBlockState blockStateIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, boolean checkSides, CallbackInfoReturnable<Boolean> cir) {
        TriggerType.RENDER_BLOCK.triggerAll(
                new Block(blockStateIn.getBlock()),
                blockPosIn,
                cir
        );
    }
}