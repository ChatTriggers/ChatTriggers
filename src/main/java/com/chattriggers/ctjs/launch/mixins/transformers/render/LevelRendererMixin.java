package com.chattriggers.ctjs.launch.mixins.transformers.render;

//#if MC>=11701
//$$ import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
//$$ import com.chattriggers.ctjs.triggers.TriggerType;
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//$$ import com.mojang.blaze3d.vertex.VertexConsumer;
//$$ import com.mojang.math.Matrix4f;
//$$ import com.mojang.math.Vector3f;
//$$ import net.minecraft.client.Camera;
//$$ import net.minecraft.client.renderer.GameRenderer;
//$$ import net.minecraft.client.renderer.LevelRenderer;
//$$ import net.minecraft.client.renderer.LightTexture;
//$$ import net.minecraft.core.BlockPos;
//$$ import net.minecraft.world.entity.Entity;
//$$ import net.minecraft.world.level.block.state.BlockState;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$
//$$ @Mixin(LevelRenderer.class)
//$$ public class LevelRendererMixin {
//$$     @Inject(method = "renderHitOutline", at = @At("HEAD"), cancellable = true)
//$$     private void chattriggers_drawBlockHighlightTrigger(PoseStack arg, VertexConsumer arg2, Entity arg3, double d, double e, double f, BlockPos arg4, BlockState arg5, CallbackInfo ci) {
//$$         if (arg2 == null) {
//$$             return;
//$$         }
//$$
//$$         Vector3f pos = new Vector3f(arg4.getX(), arg4.getY(), arg4.getZ());
//$$
//$$         TriggerType.BlockHighlight.triggerAll(pos, ci);
//$$     }
//$$
//$$     @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderDebug(Lnet/minecraft/client/Camera;)V"))
//$$     private void chattriggers_renderWorldTrigger(PoseStack arg, float g, long m, boolean bl, Camera arg2, GameRenderer arg3, LightTexture arg4, Matrix4f arg5, CallbackInfo ci) {
//$$         Renderer.setPartialTicks$chattriggers(g);
//$$         TriggerType.RenderWorld.triggerAll(g);
//$$     }
//$$ }
//#endif