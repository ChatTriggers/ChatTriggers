package com.chattriggers.ctjs.minecraft.mixins;

import com.chattriggers.ctjs.minecraft.wrappers.objects.Entity;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.util.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({RenderManager.class})
public class MixinRenderManager {
    @Inject(
            method = "doRenderEntity",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderEntity(net.minecraft.entity.Entity entity, double x, double y, double z, float entityYaw, float partialTicks, boolean hideDebugBox, CallbackInfoReturnable ci) {
        TriggerType.RENDER_ENTITY.triggerAll(
                new Entity(entity),
                new Vector3f((float) x, (float) y, (float) z),
                partialTicks,
                ci
        );
    }
}
