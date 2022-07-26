package com.chattriggers.ctjs.mixins.gui;

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
import com.chattriggers.ctjs.minecraft.listeners.events.CancellableEvent;
import com.chattriggers.ctjs.triggers.TriggerType;
import gg.essential.lib.mixinextras.injector.WrapWithCondition;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC>=11701
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//#endif

@Mixin(GuiContainer.class)
public class GuiContainerMixin {
    @Shadow
    //#if MC<=11202
    private Slot theSlot;
    //#elseif MC>=11701
    //$$ protected Slot hoveredSlot;
    //#endif

    @Inject(
            //#if MC<=11202
            method = "drawSlot",
            //#elseif MC>=11701
            //$$ method = "renderSlot",
            //#endif
            at = @At("HEAD"),
            cancellable = true
    )
    private void chattriggers_renderSlotTrigger(
            //#if MC>=11701
            //$$ PoseStack stack,
            //#endif
            Slot slotIn,
            CallbackInfo ci
    ) {
        Renderer.pushMatrix();
        TriggerType.RenderSlot.triggerAll(new com.chattriggers.ctjs.minecraft.wrappers.inventory.Slot(slotIn), this, ci);
        Renderer.popMatrix();
    }

    //#if MC<=11202
    // TODO(BREAKING): Remove this trigger, use postGuiRender instead
    @Inject(
        method = "drawScreen",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/inventory/GuiContainer;drawGuiContainerForegroundLayer(II)V"
        )
    )
    private void chattriggers_preItemRenderTrigger(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (theSlot != null) {
            GlStateManager.pushMatrix();
            // TODO(BREAKING): Wrap this in a CT slot
            TriggerType.PreItemRender.triggerAll(mouseX, mouseY, theSlot, this);
            GlStateManager.popMatrix();
        }
    }
    //#endif

    @WrapWithCondition(
            //#if MC<=11202
            method = "drawScreen",
            //#elseif MC>=11701
            //$$ method = "render",
            //#endif
            at = @At(
                    value = "INVOKE",
                    //#if MC<=11202
                    target = "Lnet/minecraft/client/gui/inventory/GuiContainer;drawGradientRect(IIIIII)V"
                    //#elseif MC>=11701
                    //#if FORGE
                    //$$ target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderSlotHighlight(Lcom/mojang/blaze3d/vertex/PoseStack;IIII)V"
                    //#else
                    //$$ target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawSlotHighlight(Lnet/minecraft/client/util/math/MatrixStack;III)V"
                    //#endif
                    //#endif
            )
    )
    //#if MC<=11202
    private boolean chattriggers_renderSlotHighlightTrigger(GuiContainer instance, int left, int top, int right, int bottom, int startColor, int endColor, int mouseX, int mouseY, float partialTicks) {
        if (theSlot != null) {
    //#elseif MC>=11701
    //$$ private boolean chattriggers_renderSlotHighlightTrigger(PoseStack arg, int i, int j, int k,
    //#if FORGE
    //$$ int slotColor,
    //#endif
    //$$ PoseStack arg2, int mouseX, int mouseY, float partialTicks) {
    //$$     if (this.hoveredSlot != null) {
    //#endif
            CancellableEvent event = new CancellableEvent();

            Renderer.pushMatrix();
            TriggerType.RenderSlotHighlight.triggerAll(
                    mouseX,
                    mouseY,
                    //#if MC<=11202
                    theSlot,
                    //#elseif MC>=11701
                    //$$ this.hoveredSlot,
                    //#endif
                    this,
                    event
            );
            Renderer.popMatrix();

            return !event.isCancelled();
        }
        return true;
    }
}
