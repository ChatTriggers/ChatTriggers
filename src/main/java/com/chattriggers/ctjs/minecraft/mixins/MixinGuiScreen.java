package com.chattriggers.ctjs.minecraft.mixins;

import com.chattriggers.ctjs.minecraft.objects.message.TextComponent;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

//#if MC<=10809
import net.minecraft.util.IChatComponent;
//#else
//$$ import net.minecraft.util.text.ITextComponent;
//#endif

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen {
    @Shadow public Minecraft mc;

    @Inject(
            method = "sendChatMessage(Ljava/lang/String;Z)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onSendChatMessage(String msg, boolean addToChat, CallbackInfo ci) {
        TriggerType.MESSAGE_SENT.triggerAll(msg, ci);
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
            method =
                    //#if MC<=10809
                    "handleComponentClick(Lnet/minecraft/util/IChatComponent;)Z",
                    //#else
                    //$$ "handleComponentClick(Lnet/minecraft/util/text/ITextComponent;)Z",
                    //#endif
            at = @At("HEAD"),
            cancellable = true
    )
    private void onHandleComponentClick(
            //#if MC<=10809
            IChatComponent component,
            //#else
            //$$ ITextComponent component,
            //#endif
            CallbackInfoReturnable ci) {
        TriggerType.CHAT_COMPONENT_CLICKED.trigger(
                component == null ? null : new TextComponent(component),
                ci
        );
    }

    @Inject(
            method =
                    //#if MC<=10809
                    "handleComponentHover(Lnet/minecraft/util/IChatComponent;II)V",
                    //#else
                    //$$ "handleComponentHover(Lnet/minecraft/util/text/ITextComponent;II)V",
                    //#endif
            at = @At("HEAD"),
            cancellable = true
    )
    private void onHandleComponentHover(
            //#if MC<=10809
            IChatComponent component,
            //#else
            //$$ ITextComponent component,
            //#endif
            int x, int y, CallbackInfo ci) {
        TriggerType.CHAT_COMPONENT_HOVERED.trigger(
                component == null ? null : new TextComponent(component),
                x,
                y,
                ci
        );
    }
}
