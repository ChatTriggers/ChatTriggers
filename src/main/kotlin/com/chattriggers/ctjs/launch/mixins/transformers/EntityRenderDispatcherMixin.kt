package com.chattriggers.ctjs.launch.mixins.transformers

import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.entity.Entity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(EntityRenderDispatcher::class)
class EntityRenderDispatcherMixin {
    var customEntityRenderer: EntityRenderer<*>? = null

    @Inject(method = ["getRenderer"], at = [At("HEAD")], cancellable = true)
    fun <T : Entity> injectGetRenderer(cir: CallbackInfoReturnable<EntityRenderer<T>>) {
        if (customEntityRenderer != null) {
            @Suppress("UNCHECKED_CAST")
            cir.returnValue = customEntityRenderer as EntityRenderer<T>
            cir.cancel()
        }
    }
}
