package com.chattriggers.ctjs.launch.mixins.transformers.render;

//#if MC>=11701
//$$ import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
//$$ import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
//$$ import net.minecraft.client.renderer.entity.EntityRendererProvider;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.ModifyVariable;
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
//$$ }
//#endif
