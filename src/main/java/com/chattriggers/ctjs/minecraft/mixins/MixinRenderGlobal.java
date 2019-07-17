// package com.chattriggers.ctjs.minecraft.mixins;
//
// import com.chattriggers.ctjs.minecraft.wrappers.World;
// import com.chattriggers.ctjs.triggers.TriggerType;
// import net.minecraft.client.renderer.RenderGlobal;
// import net.minecraft.client.renderer.chunk.RenderChunk;
// import net.minecraft.client.renderer.culling.ICamera;
// import net.minecraft.entity.Entity;
// import net.minecraft.util.BlockPos;
// import net.minecraft.util.EnumFacing;
// import org.spongepowered.asm.mixin.Mixin;
// import org.spongepowered.asm.mixin.injection.At;
// import org.spongepowered.asm.mixin.injection.Inject;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
// import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
//
// import java.util.Queue;
//
// @Mixin({RenderGlobal.class})
// public class MixinRenderGlobal {
//     @Inject(
//             method = "setupTerrain",
//             at = @At(
//                     value = "INVOKE",
//                     target = "Lnet/minecraft/client/renderer/chunk/RenderChunk;getPosition()Lnet/minecraft/util/BlockPos;",
//                     shift = At.Shift.AFTER
//             ),
//             cancellable = true,
//             locals = LocalCapture.CAPTURE_FAILHARD
//     )
//     private void onBlockRender(Entity viewEntity, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator, CallbackInfo ci, double d0, double d1, double d2, double d3, double d4, double d5, BlockPos blockpos1, RenderChunk renderchunk, BlockPos blockpos, boolean flag, Queue queue, boolean flag1, Object renderglobal$containerlocalrenderinformation1, RenderChunk renderchunk3, EnumFacing enumfacing2, RenderChunk var28) {
//         BlockPos pos = renderchunk3.getPosition();
//
//         TriggerType.RENDER_BLOCK.triggerAll(
//                 World.getBlockAt(pos.getX(), pos.getY(), pos.getZ()),
//                 renderchunk3.getPosition(),
//                 ci
//         );
//     }
// }
