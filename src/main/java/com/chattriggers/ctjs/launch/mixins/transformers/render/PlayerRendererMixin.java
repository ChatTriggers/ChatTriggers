package com.chattriggers.ctjs.launch.mixins.transformers.render;

//#if MC>=11701
//$$ import com.chattriggers.ctjs.minecraft.libs.renderer.CTRenderPlayer;
//$$ import net.minecraft.client.model.HumanoidModel;
//$$ import net.minecraft.client.player.AbstractClientPlayer;
//$$ import net.minecraft.client.renderer.entity.player.PlayerRenderer;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$
//$$ @Mixin(PlayerRenderer.class)
//$$ public class PlayerRendererMixin {
//$$     @Inject(method = "setModelProperties", at = @At("HEAD"))
//$$     void injectSetModelProperties(AbstractClientPlayer arg, CallbackInfo ci) {
//$$         // TODO(VERIFY)
//$$         //noinspection ConstantConditions
//$$         if ((Object) this instanceof CTRenderPlayer && !((CTRenderPlayer) (Object) this).getShowHeldItem()) {
//$$             ((CTRenderPlayer) (Object) this).getModel().leftArmPose = HumanoidModel.ArmPose.EMPTY;
//$$             ((CTRenderPlayer) (Object) this).getModel().rightArmPose = HumanoidModel.ArmPose.EMPTY;
//$$         }
//$$     }
//$$ }
//#endif
