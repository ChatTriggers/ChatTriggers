package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent;
import com.chattriggers.ctjs.minecraft.wrappers.inventory.Item;
import com.chattriggers.ctjs.triggers.TriggerType;
import gg.essential.universal.wrappers.message.UTextComponent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(GuiScreen.class)
public class GuiScreenMixin {
    @Inject(method = "sendChatMessage(Ljava/lang/String;)V", at = @At("HEAD"), cancellable = true)
    void injectSendChatMessage(String msg, CallbackInfo ci) {
        TriggerType.MessageSent.triggerAll(msg, ci);
    }

    @Inject(
        method = "handleKeyboardInput",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiScreen;keyTyped(CI)V"
        ),
        cancellable = true
    )
    void injectHandleKeyboardInput(CallbackInfo ci) {
        TriggerType.GuiKey.triggerAll(
            Keyboard.getEventCharacter(),
            Keyboard.getEventKey(),
            this,
            ci
        );
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
    void injectMouseClick(CallbackInfo ci, int mouseX, int mouseY, int button) {
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
    void injectMouseRelease(CallbackInfo ci, int mouseX, int mouseY, int button) {
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
    void injectMouseDrag(CallbackInfo ci, int mouseX, int mouseY, int button) {
        TriggerType.GuiMouseDrag.triggerAll(mouseX, mouseY, button, this, ci);
    }

    @Inject(method = "handleComponentClick", at = @At("HEAD"), cancellable = true)
    void injectHandleComponentClick(IChatComponent component, CallbackInfoReturnable<Boolean> cir) {
        if (component != null) {
            CancellableEvent event = new CancellableEvent();
            TriggerType.ChatComponentClicked.triggerAll(new UTextComponent(component), event);
            if (event.isCanceled())
                cir.setReturnValue(false);
        }
    }

    @Inject(method = "handleComponentHover", at = @At("HEAD"), cancellable = true)
    void injectHandleComponentHover(IChatComponent component, int x, int y, CallbackInfo ci) {
        UTextComponent textComponent = null;
        if (component != null)
            textComponent = new UTextComponent(component);
        TriggerType.ChatComponentHovered.triggerAll(textComponent, x, y, ci);
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
    void injectRenderTooltip(ItemStack stack, int x, int y, CallbackInfo ci, List<String> list) {
        TriggerType.Tooltip.triggerAll(list, new Item(stack), ci);
    }

    @Inject(method = "drawDefaultBackground", at = @At("HEAD"), cancellable = true)
    void injectDrawDefaultBackground(CallbackInfo ci) {
        TriggerType.GuiDrawBackground.triggerAll(this, ci);
    }
}
