package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class EntityRendererDispatcherMixin {
    @Inject(
        method = "getRenderer",
        at = @At("HEAD"),
        cancellable = true
    )
    public <T extends Entity> void injectGetRenderer(CallbackInfoReturnable<EntityRenderer<T>> cir) {
        if (Renderer.getCustomEntityRenderer() != null) {
            Renderer.setCustomEntityRenderer(null);
            //noinspection unchecked
            cir.setReturnValue((EntityRenderer<T>) Renderer.getCustomEntityRenderer());
            cir.cancel();
        }
    }
}
