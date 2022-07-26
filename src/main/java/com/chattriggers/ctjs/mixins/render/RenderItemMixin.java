package com.chattriggers.ctjs.mixins.render;

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
import com.chattriggers.ctjs.minecraft.wrappers.inventory.Item;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC>=11701
//$$ import net.minecraft.world.entity.LivingEntity;
//#endif

@Mixin(RenderItem.class)
public class RenderItemMixin {
    @Inject(
            //#if MC<=11202
            method = "renderItemAndEffectIntoGUI",
            //#elseif MC>=11701
            //$$ method = "tryRenderGuiItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;IIII)V",
            //#endif
            at = @At("HEAD"),
            cancellable = true
    )
    private void chattriggers_renderItemIntoGuiTrigger(
            //#if MC>=11701
            //$$ LivingEntity entity,
            //#endif
            ItemStack stack,
            int xPosition,
            int yPosition,
            //#if MC>=11701
            //$$ int k,
            //$$ int l,
            //#endif
            CallbackInfo ci
    ) {
        if (stack != null) {
            Renderer.pushMatrix();
            TriggerType.RenderItemIntoGui.triggerAll(new Item(stack), xPosition, yPosition, ci);
            Renderer.popMatrix();
        }
    }

    @Inject(
            //#if MC<=11202
            method = "renderItemOverlayIntoGUI",
            //#elseif MC>=11701
            //$$ method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            //#endif
            at = @At("HEAD"),
            cancellable = true
    )
    private void chattriggers_renderItemOverlayIntoGuiTrigger(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, String text, CallbackInfo ci) {
        if (stack != null) {
            Renderer.pushMatrix();
            TriggerType.RenderItemOverlayIntoGui.triggerAll(new Item(stack), xPosition, yPosition, ci);
            Renderer.popMatrix();
        }
    }
}
