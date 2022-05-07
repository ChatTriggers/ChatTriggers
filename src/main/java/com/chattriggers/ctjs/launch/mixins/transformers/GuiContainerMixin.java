package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public class GuiContainerMixin {
    @Shadow
    private Slot theSlot;

    @Inject(method = "drawSlot", at = @At("HEAD"), cancellable = true)
    void injectDrawSlot(Slot slotIn, CallbackInfo ci) {
        GlStateManager.pushMatrix();
        TriggerType.RenderSlot.triggerAll(new com.chattriggers.ctjs.minecraft.wrappers.inventory.Slot(slotIn), this, ci);
        GlStateManager.popMatrix();
    }

    @Inject(
        method = "drawScreen",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/inventory/GuiContainer;drawGuiContainerForegroundLayer(II)V"
        )
    )
    void injectDrawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (theSlot != null) {
            GlStateManager.pushMatrix();
            // TODO(BREAKING): Wrap this in a CT slot
            TriggerType.PreItemRender.triggerAll(mouseX, mouseY, theSlot, this);
            GlStateManager.popMatrix();
        }
    }

    @Redirect(
        method = "drawScreen",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/inventory/GuiContainer;drawGradientRect(IIIIII)V"
        )
    )
    void drawScreenProxy(GuiContainer instance, int left, int top, int right, int bottom, int startColor, int endColor, int mouseX, int mouseY, float partialTicks) {
        if (theSlot != null) {
            CancellableEvent event = new CancellableEvent();

            GlStateManager.pushMatrix();
            TriggerType.RenderSlotHighlight.triggerAll(mouseX, mouseY, theSlot, instance, event);
            GlStateManager.popMatrix();

            if (!event.isCanceled())
                ((GuiAccessor) instance).invokeDrawGradientRect(left, top, right, bottom, startColor, endColor);
        }
    }
}
