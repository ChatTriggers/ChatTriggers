package com.chattriggers.ctjs.launch.mixins.transformers.render;

//#if MC>=11701
//$$ import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
//$$ import com.chattriggers.ctjs.triggers.TriggerType;
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//$$ import com.mojang.math.Vector3f;
//$$ import net.minecraft.client.renderer.MultiBufferSource;
//$$ import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
//$$ import net.minecraft.client.renderer.entity.EntityRendererProvider;
//$$ import net.minecraft.world.entity.Entity;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.ModifyVariable;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$
//$$ @Mixin(EntityRenderDispatcher.class)
//$$ public class EntityRendererDispatcherMixin {
//$$     @ModifyVariable(
//$$             method = "onResourceManagerReload",
//$$             at = @At(
//$$                     value = "INVOKE",
//$$                     target = "Lnet/minecraft/client/renderer/entity/EntityRenderers;createPlayerRenderers(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;)Ljava/util/Map;"
//$$             )
//$$     )
//$$     private EntityRendererProvider.Context chattriggers_initializeRenderPlayers(EntityRendererProvider.Context context) {
//$$         Renderer.initializeRenderPlayers$chattriggers(context);
//$$         return context;
//$$     }
//$$
//$$     @Inject(method = "render", at = @At("HEAD"), cancellable = true)
//$$     private void chattriggers_renderEntityTrigger(Entity arg, double x, double y, double z, float yaw, float tickDelta, PoseStack arg2, MultiBufferSource arg3, int j, CallbackInfo ci) {
//$$         TriggerType.RenderEntity.triggerAll(
//$$                 new com.chattriggers.ctjs.minecraft.wrappers.entity.Entity(arg),
//$$                 new Vector3f((float) x, (float) y, (float) z),
//$$                 tickDelta,
//$$                 ci
//$$         );
//$$     }
//$$
//$$     @Inject(method = "render", at = @At("TAIL"))
//$$     private void chattriggers_postRenderEntityTrigger(Entity arg, double x, double y, double z, float yaw, float tickDelta, PoseStack arg2, MultiBufferSource arg3, int j, CallbackInfo ci) {
//$$         TriggerType.PostRenderEntity.triggerAll(
//$$                 new com.chattriggers.ctjs.minecraft.wrappers.entity.Entity(arg),
//$$                 new Vector3f((float) x, (float) y, (float) z),
//$$                 tickDelta
//$$         );
//$$     }
//$$ }
//#endif
