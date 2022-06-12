package com.chattriggers.ctjs.launch.mixins.transformers.gui;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.minecraft.listeners.MouseListener;
import com.chattriggers.ctjs.minecraft.listeners.events.CancellableEvent;
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
//#endif

@Mixin(GuiScreen.class)
public class GuiScreenMixin {
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

    @Inject(method = "handleComponentClick", at = @At("HEAD"), cancellable = true)
    private void chattriggers_chatComponentClickedTrigger(IChatComponent component, CallbackInfoReturnable<Boolean> cir) {
        if (component != null) {
            CancellableEvent event = new CancellableEvent();
            TriggerType.ChatComponentClicked.triggerAll(new UTextComponent(component), event);
            if (event.isCanceled())
                cir.setReturnValue(false);
        }
    }

    @Inject(method = "handleComponentHover", at = @At("HEAD"), cancellable = true)
    private void chattriggers_chatComponentHoveredTrigger(IChatComponent component, int x, int y, CallbackInfo ci) {
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
    //#endif

}
