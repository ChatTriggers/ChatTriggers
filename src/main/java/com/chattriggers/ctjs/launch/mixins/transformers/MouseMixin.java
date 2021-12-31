package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.minecraft.listeners.ClientListener;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

// TODO("fabric"): Make these cancellable
@Mixin(Mouse.class)
public abstract class MouseMixin {
    @Accessor
    public abstract int getActiveButton();

    @Inject(
        method = "onMouseScroll",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/Screen;mouseScrolled(DDD)Z"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void injectOnMouseScroll(
        long window,
        double horizontal,
        double vertical,
        CallbackInfo ci,
        double vertical2,
        double amount,
        double mouseX,
        double mouseY
    ) {
        ClientListener.onScroll(mouseX, mouseY, amount);
    }

    @Inject(
        method = "method_1611",
        at = @At("HEAD"),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void inject_method_1611(
        boolean[] bls,
        Screen screen,
        double mouseX,
        double mouseY,
        int button,
        CallbackInfo ci,
        int arg4
    ) {
        ClientListener.onClick(mouseX, mouseY, button, /* pressed = */ true);
    }

    @Inject(
        method = "method_1605",
        at = @At("HEAD"),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void inject_method_1605(
        boolean[] bls,
        Screen screen,
        double mouseX,
        double mouseY,
        int button,
        CallbackInfo ci,
        int arg4
    ) {
        ClientListener.onClick(mouseX, mouseY, button, /* pressed = */ false);
    }

    @Inject(
        method = "method_1602",
        at = @At("HEAD"),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void inject_method_1602(
        Screen screen,
        double mouseX,
        double mouseY,
        double deltaX,
        double deltaY,
        CallbackInfo ci,
        double arg3,
        double arg4
    ) {
        ClientListener.onDragged(
            deltaX,
            deltaY,
            mouseX,
            mouseY,
            ((MouseMixin) (Object) Client.getMinecraft().mouse).getActiveButton()
        );
    }
}
