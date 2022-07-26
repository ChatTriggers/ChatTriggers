package com.chattriggers.ctjs.mixins.render;

import com.chattriggers.ctjs.minecraft.libs.renderer.CTRenderPlayer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC>=11701
//$$ import net.minecraft.client.model.HumanoidModel;
//$$ import net.minecraft.client.renderer.entity.EntityRendererProvider;
//$$ import net.minecraft.client.player.AbstractClientPlayer;
//#endif

@Mixin(RenderPlayer.class)
public abstract class RenderPlayerMixin extends RendererLivingEntity<
        AbstractClientPlayer
        //#if MC>=11701
        //$$ , PlayerModel<AbstractClientPlayer>
        //#endif
> {
    //#if MC<=11202
    public RenderPlayerMixin(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }
    //#elseif MC>=11701
    //$$ public RenderPlayerMixin(EntityRendererProvider.Context arg, PlayerModel<AbstractClientPlayer> arg2, float f) {
    //$$     super(arg, arg2, f);
    //$$ }
    //#endif

    @Inject(
            //#if MC<=11202
            method = "setModelVisibilities",
            //#elseif MC>=11701
            //$$ method = "setModelProperties",
            //#endif
            at = @At("HEAD")
    )
    private void chattriggers_ctRenderPlayerClearHands(AbstractClientPlayer arg, CallbackInfo ci) {
        //noinspection ConstantConditions
        if ((Object) this instanceof CTRenderPlayer && !((CTRenderPlayer) (Object) this).getShowHeldItem()) {
            //#if MC<=11202
            ((ModelPlayer) getMainModel()).heldItemLeft = 0;
            ((ModelPlayer) getMainModel()).heldItemRight = 0;
            //#else
            //$$ getModel().leftArmPose = HumanoidModel.ArmPose.EMPTY;
            //$$ getModel().rightArmPose = HumanoidModel.ArmPose.EMPTY;
            //#endif
        }
    }
}
