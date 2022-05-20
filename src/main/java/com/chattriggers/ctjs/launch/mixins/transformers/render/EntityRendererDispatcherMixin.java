package com.chattriggers.ctjs.launch.mixins.transformers.render;

//#if MC>=11701
//$$ import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
//$$ import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
//$$ import net.minecraft.client.renderer.entity.EntityRendererProvider;
//$$ import net.minecraft.server.packs.resources.ResourceManager;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$ import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
//$$
//$$ @Mixin(EntityRenderDispatcher.class)
//$$ public class EntityRendererDispatcherMixin {
//$$     @Inject(
//$$         method = "onResourceManagerReload",
//$$         at = @At(
//$$             value = "INVOKE",
//$$             target = "Lnet/minecraftforge/fml/ModLoader;get()Lnet/minecraftforge/fml/ModLoader;"
//$$         ),
//$$         locals = LocalCapture.CAPTURE_FAILHARD
//$$     )
//$$     public void injectOnResourceManagerReload(ResourceManager arg, CallbackInfo ci, EntityRendererProvider.Context context) {
//$$         Renderer.initializeRenderPlayers(context);
//$$     }
//$$ }
//#endif
