package com.chattriggers.ctjs.launch.mixins.transformers;

//#if MC<=11202
import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent;
import com.chattriggers.ctjs.minecraft.wrappers.entity.Entity;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.util.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderManager.class)
public class RenderManagerMixin {
    @Inject(method = "doRenderEntity", at = @At("HEAD"), cancellable = true)
    void injectDoRenderEntityPre(
        net.minecraft.entity.Entity entity,
        double x,
        double y,
        double z,
        float entityYaw,
        float partialTicks,
        boolean p_147939_10_,
        CallbackInfoReturnable<Boolean> cir
    ) {
        CancellableEvent event = new CancellableEvent();

        TriggerType.RenderEntity.triggerAll(
            new Entity(entity),
            new Vector3f((float) x, (float) y, (float) z),
            partialTicks,
            event
        );

        if (event.isCanceled())
            cir.setReturnValue(false);
    }

    @Inject(
        method = "doRenderEntity",
        at = @At(value = "RETURN", ordinal = 1)
    )
    void injectDoRenderEntityPost(
        net.minecraft.entity.Entity entity,
        double x,
        double y,
        double z,
        float entityYaw,
        float partialTicks,
        boolean p_147939_10_,
        CallbackInfoReturnable<Boolean> cir
    ) {
        TriggerType.PostRenderEntity.triggerAll(
            new Entity(entity),
            new Vector3f((float) x, (float) y, (float) z),
            partialTicks
        );
    }
}
//#endif
