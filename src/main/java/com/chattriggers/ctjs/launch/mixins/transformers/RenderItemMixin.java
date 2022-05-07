package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.minecraft.wrappers.inventory.Item;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItem.class)
public class RenderItemMixin {
    @Inject(method = "renderItemAndEffectIntoGUI", at = @At("HEAD"), cancellable = true)
    void injectRenderItemAndEffectIntoGUI(ItemStack stack, int xPosition, int yPosition, CallbackInfo ci) {
        if (stack != null) {
            GlStateManager.pushMatrix();
            TriggerType.RenderItemIntoGui.triggerAll(new Item(stack), xPosition, yPosition, ci);
            GlStateManager.popMatrix();
        }
    }

    @Inject(method = "renderItemOverlayIntoGUI", at = @At("HEAD"), cancellable = true)
    void injectRenderItemOverlayIntoGUI(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, String text, CallbackInfo ci) {
        if (stack != null) {
            GlStateManager.pushMatrix();
            TriggerType.RenderItemOverlayIntoGui.triggerAll(new Item(stack), xPosition, yPosition, ci);
            GlStateManager.popMatrix();
        }
    }
}
