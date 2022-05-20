package com.chattriggers.ctjs.launch.mixins.transformers.render;

import com.chattriggers.ctjs.minecraft.libs.renderer.CTRenderPlayer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC<=11202
//#else
//$$ import net.minecraft.client.model.HumanoidModel;
//$$ import net.minecraft.client.player.AbstractClientPlayer;
//#endif

@Mixin(RenderPlayer.class)
public class RenderPlayerMixin {
    @Inject(method = "setModelVisibilities", at = @At("HEAD"))
    void injectSetModelProperties(AbstractClientPlayer arg, CallbackInfo ci) {
        // TODO(VERIFY)
        //noinspection ConstantConditions
        if ((Object) this instanceof CTRenderPlayer && !((CTRenderPlayer) (Object) this).getShowHeldItem()) {
            //#if MC<=11202
            ((CTRenderPlayer) (Object) this).getMainModel().heldItemLeft = 0;
            ((CTRenderPlayer) (Object) this).getMainModel().heldItemRight = 0;
            //#else
            //$$ ((CTRenderPlayer) (Object) this).getModel().leftArmPose = HumanoidModel.ArmPose.EMPTY;
            //$$ ((CTRenderPlayer) (Object) this).getModel().rightArmPose = HumanoidModel.ArmPose.EMPTY;
            //#endif
        }
    }
}
