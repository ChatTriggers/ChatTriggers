package com.chattriggers.ctjs.minecraft.mixins;

import com.chattriggers.ctjs.minecraft.objects.message.TextComponent;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.IChatComponent;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen {
    @Shadow public Minecraft mc;

    @Inject(
            method = "sendChatMessage(Ljava/lang/String;Z)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onSendChatMessage(String msg, boolean addToChat, CallbackInfo ci) {
        TriggerType.MESSAGE_SENT.triggerAll(ci, msg);
    }

    @Inject(
            method = "handleKeyboardInput",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;keyTyped(CI)V"),
            cancellable = true
    )
    private void onKeyInput(CallbackInfo ci) {
        TriggerType.GUI_KEY.triggerAll(
                Keyboard.getEventCharacter(),
                Keyboard.getEventKey(),
                this,
                ci
        );
    }

    @Inject(
            method = "handleMouseInput",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;mouseClicked(III)V"),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void onMouseClick(CallbackInfo ci, int mouseX, int mouseY, int mouseButton) {
        TriggerType.GUI_MOUSE_CLICK.triggerAll(
                mouseX,
                mouseY,
                mouseButton,
                this,
                ci
        );
    }

    @Inject(
            method = "handleMouseInput",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;mouseReleased(III)V"),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void onMouseRelease(CallbackInfo ci, int mouseX, int mouseY, int mouseButton) {
        TriggerType.GUI_MOUSE_RELEASE.triggerAll(
                mouseX,
                mouseY,
                mouseButton,
                this,
                ci
        );
    }

    @Inject(
            method = "handleMouseInput",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;mouseClickMove(IIIJ)V"),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void onMouseDrag(CallbackInfo ci, int mouseX, int mouseY, int mouseButton, long timeSinceClicked) {
        TriggerType.GUI_MOUSE_DRAG.triggerAll(
                mouseX,
                mouseY,
                mouseButton,
                this,
                ci
        );
    }

    @Inject(
            method = "handleComponentClick(Lnet/minecraft/util/IChatComponent;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onHandleComponentClick(IChatComponent component, CallbackInfoReturnable ci) {
        TriggerType.CHAT_COMPONENT_CLICKED.trigger(
                component == null ? null : new TextComponent(component),
                ci
        );
    }

    @Inject(
            method = "handleComponentHover(Lnet/minecraft/util/IChatComponent;II)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onHandleComponentHover(IChatComponent component, int x, int y, CallbackInfo ci) {
        TriggerType.CHAT_COMPONENT_HOVERED.trigger(
                component == null ? null : new TextComponent(component),
                x,
                y,
                ci
        );
    }
}
