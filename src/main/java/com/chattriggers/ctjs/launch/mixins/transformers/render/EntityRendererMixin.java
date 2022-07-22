package com.chattriggers.ctjs.launch.mixins.transformers.render;

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
import com.chattriggers.ctjs.minecraft.listeners.events.CancellableEvent;
import com.chattriggers.ctjs.triggers.TriggerType;
import gg.essential.lib.mixinextras.injector.WrapWithCondition;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import org.lwjgl.util.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC<=11202
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.MovingObjectPosition;
//#elseif MC>=11701
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//$$ import net.minecraft.client.Camera;
//$$ import net.minecraft.client.renderer.GameRenderer;
//#endif

//#if MC<=11202
@Mixin(EntityRenderer.class)
//#elseif MC>=11701
//$$ @Mixin(GameRenderer.class)
//#endif
public class EntityRendererMixin {
    //#if MC<=11202
    @Inject(
            method = "renderWorldPass",
            at = @At(
                    value = "INVOKE_STRING",
                    target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V",
                    args = "ldc=hand"
            )
    )
    private void chattriggers_renderWorldTrigger(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {
        Renderer.setPartialTicks$chattriggers(partialTicks);
        TriggerType.RenderWorld.triggerAll(partialTicks);
    }
    //#endif

    //#if MC<=11202
    @WrapWithCondition(
            method = "renderWorldPass",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/RenderGlobal;drawSelectionBox(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/MovingObjectPosition;IF)V"
            )
    )
    private boolean chattriggers_drawBlockHighlightTrigger(RenderGlobal renderGlobal, EntityPlayer player, MovingObjectPosition movingObjectPositionIn, int execute, float partialTicks) {
        if (movingObjectPositionIn == null || movingObjectPositionIn.getBlockPos() == null) {
            return true;
        }

        BlockPos blockPos = movingObjectPositionIn.getBlockPos();
        Vector3f pos = new Vector3f(blockPos.getX(), blockPos.getY(), blockPos.getZ());

        CancellableEvent event = new CancellableEvent();
        TriggerType.BlockHighlight.triggerAll(pos, event);

        return !event.isCanceled();
    }
    //#endif

    @Inject(
            //#if MC<=11202
            method = "renderHand",
            //#elseif MC>=11701
            //$$ method = "renderItemInHand",
            //#endif
            at = @At("HEAD"),
            cancellable = true
    )
    //#if MC<=11202
    private void chattriggers_renderHandTrigger(float partialTicks, int xOffset, CallbackInfo ci) {
    //#elseif MC>=11701
    //$$ private void chattriggers_renderHandTrigger(PoseStack arg, Camera arg2, float f, CallbackInfo ci) {
    //#endif
        TriggerType.RenderHand.triggerAll(ci);
    }
}
