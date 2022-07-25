package com.chattriggers.ctjs.mixins.gui;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.minecraft.listeners.MouseListener;
import com.chattriggers.ctjs.minecraft.listeners.events.CancellableEvent;
import com.chattriggers.ctjs.minecraft.wrappers.chat.ChatStyle;
import com.chattriggers.ctjs.minecraft.wrappers.inventory.Item;
import com.chattriggers.ctjs.triggers.EventType;
import com.chattriggers.ctjs.triggers.TriggerType;
import gg.essential.universal.UMouse;
import gg.essential.universal.wrappers.message.UTextComponent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

//#if MC<=11202
import org.lwjgl.input.Keyboard;
//#elseif MC>=11701
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//$$ import net.minecraft.network.chat.Style;
//$$ import org.spongepowered.asm.mixin.Shadow;
//#endif

@Mixin(GuiScreen.class)
public abstract class GuiScreenMixin {
    @Inject(
            //#if MC<=11202
            method = "sendChatMessage(Ljava/lang/String;)V",
            //#else
            //$$ method = "sendMessage(Ljava/lang/String;)V",
            //#endif
            at = @At("HEAD"),
            cancellable = true
    )
    private void chattriggers_messageSentTrigger(String msg, CallbackInfo ci) {
        TriggerType.MessageSent.triggerAll(msg, ci);
    }

    //#if MC<=11202
    @Inject(
        method = "handleKeyboardInput",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiScreen;keyTyped(CI)V"
        ),
        cancellable = true
    )
    private void chattriggers_guiKeyTrigger(CallbackInfo ci) {
        TriggerType.GuiKey.triggerAll(
            Keyboard.getEventCharacter(),
            Keyboard.getEventKey(),
            this,
            ci
        );
    }

    @Inject(method = "handleMouseInput", at = @At("HEAD"))
    private void chattriggers_mouseEventTrigger(CallbackInfo ci) {
        MouseListener.INSTANCE.onGuiMouseInput$chattriggers();
    }

    @Inject(
        method = "handleMouseInput",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiScreen;mouseClicked(III)V"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD,
        cancellable = true
    )
    private void chattriggers_guiMouseClickTrigger(CallbackInfo ci, int mouseX, int mouseY, int button) {
        TriggerType.GuiMouseClick.triggerAll(mouseX, mouseY, button, this, ci);
    }

    @Inject(
        method = "handleMouseInput",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiScreen;mouseReleased(III)V"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD,
        cancellable = true
    )
    private void chattriggers_guiMouseReleaseTrigger(CallbackInfo ci, int mouseX, int mouseY, int button) {
        TriggerType.GuiMouseRelease.triggerAll(mouseX, mouseY, button, this, ci);
    }

    @Inject(
        method = "handleMouseInput",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiScreen;mouseClickMove(IIIJ)V"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD,
        cancellable = true
    )
    private void chattriggers_guiMouseDragTrigger(CallbackInfo ci, int mouseX, int mouseY, int button) {
        TriggerType.GuiMouseDrag.triggerAll(mouseX, mouseY, button, this, ci);
    }
    //#endif

    //#if MC<=11202
    @Inject(method = "handleComponentClick", at = @At("HEAD"), cancellable = true)
    private void chattriggers_chatComponentClickedTrigger(IChatComponent component, CallbackInfoReturnable<Boolean> cir) {
        if (component != null) {
            CancellableEvent event = new CancellableEvent();
            // TODO(BREAKING): Pass in ChatStyle instead of UTextComponent
            TriggerType.ChatComponentClicked.triggerAll(new ChatStyle(component.getChatStyle()), event);
            if (event.isCanceled())
                cir.setReturnValue(false);
        }
    }

    @Inject(method = "handleComponentHover", at = @At("HEAD"), cancellable = true)
    private void chattriggers_chatComponentHoveredTrigger(IChatComponent component, int x, int y, CallbackInfo ci) {
        if (component != null) {
            // TODO(BREAKING): Pass in ChatStyle instead of UTextComponent
            TriggerType.ChatComponentHovered.triggerAll(new ChatStyle(component.getChatStyle()), x, y, ci);
        }
    }

    @Inject(
        method = "renderToolTip",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;size()I"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD,
        cancellable = true
    )
    private void chattriggers_tooltipTrigger(ItemStack stack, int x, int y, CallbackInfo ci, List<String> list) {
        TriggerType.Tooltip.triggerAll(list, new Item(stack), ci);
    }

    @Inject(method = "drawDefaultBackground", at = @At("HEAD"), cancellable = true)
    private void chattriggers_guiDrawBackground(CallbackInfo ci) {
        TriggerType.GuiDrawBackground.triggerAll(this, ci);
    }

    @Inject(method = "drawDefaultBackground", at = @At("TAIL"))
    private void chattriggers_postGuiDrawBackground(CallbackInfo ci) {
        TriggerType.GuiRender.triggerAll(UMouse.Scaled.getX(), UMouse.Scaled.getY(), this);
    }

    @Inject(method = "drawScreen", at = @At("TAIL"))
    private void chattriggers_postGuiRenderTrigger(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        TriggerType.PostGuiRender.triggerAll(mouseX, mouseY, partialTicks, this);
        CTJS.Companion.getEventListeners(EventType.PostGuiRender).forEach(listener -> {
            listener.invoke(new Object[] {mouseX, mouseY, this});
        });
    }
    //#elseif MC>=11701
    //$$ @Shadow
    //$$ public abstract List<Component> getTooltipFromItem(ItemStack arg);
    //$$
    //#if FORGE
    //$$ @Shadow
    //$$ private ItemStack tooltipStack;
    //#endif
    //$$
    //$$ @Inject(method = "handleComponentClicked", at = @At("HEAD"), cancellable = true)
    //$$ private void chattriggers_chatComponentClickedTrigger(Style arg, CallbackInfoReturnable<Boolean> cir) {
    //$$     if (arg != null) {
    //$$         CancellableEvent event = new CancellableEvent();
    //$$         TriggerType.ChatComponentClicked.triggerAll(new ChatStyle(arg), event);
    //$$         if (event.isCanceled())
    //$$             cir.setReturnValue(false);
    //$$     }
    //$$ }
    //$$
    //$$ @Inject(method = "renderComponentHoverEffect", at = @At("HEAD"), cancellable = true)
    //$$ private void chattriggers_chatComponentHoveredTrigger(PoseStack arg, Style arg2, int i, int j, CallbackInfo ci) {
    //$$     if (arg != null) {
    //$$         TriggerType.ChatComponentHovered.triggerAll(new ChatStyle(arg2), i, j, ci);
    //$$     }
    //$$ }
    //$$
    //$$ @Inject(
    //$$     method = "renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/item/ItemStack;II)V",
    //$$     at = @At(
    //$$         value = "INVOKE",
    //$$         target = "Lnet/minecraft/client/gui/screens/Screen;renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/util/List;Ljava/util/Optional;II)V"
    //$$     ),
    //$$     cancellable = true
    //$$ )
    //$$ private void chattriggers_tooltipTrigger(PoseStack arg, ItemStack arg2, int i, int j, CallbackInfo ci) {
    //$$     TriggerType.Tooltip.triggerAll(getTooltipFromItem(arg2), new Item(arg2), ci);
             //#if FORGE
             //$$ if (ci.isCancelled()) {
             //$$     this.tooltipStack = ItemStack.EMPTY;
             //$$ }
             //#endif
    //$$ }
    //$$ @Inject(method = "renderBackground(Lcom/mojang/blaze3d/vertex/PoseStack;)V", at = @At("HEAD"), cancellable = true)
    //$$ private void chattriggers_guiDrawBackground(CallbackInfo ci) {
    //$$     TriggerType.GuiDrawBackground.triggerAll(this, ci);
    //$$ }
    //$$
    //$$ @Inject(method = "renderBackground(Lcom/mojang/blaze3d/vertex/PoseStack;)V", at = @At("TAIL"))
    //$$ private void chattriggers_postGuiDrawBackground(CallbackInfo ci) {
    //$$     TriggerType.GuiRender.triggerAll(UMouse.Scaled.getX(), UMouse.Scaled.getY(), this);
    //$$ }
    //$$
    //$$ @Inject(method = "render", at = @At("TAIL"))
    //$$ private void chattriggers_postGuiRenderTrigger(PoseStack arg, int i, int j, float f, CallbackInfo ci) {
    //$$     TriggerType.PostGuiRender.triggerAll(i, j, f, this);
    //$$     CTJS.Companion.getEventListeners(EventType.PostGuiRender).forEach(listener -> {
    //$$         listener.invoke(new Object[] {i, j, this});
    //$$     });
    //$$ }
    //#endif
}
