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
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

// TODO("fabric"): Make these cancellable
@Mixin(Mouse.class)
public abstract class MouseMixin {
    @Accessor
    public abstract int getActiveButton();

    @ModifyArg(
        method = "onMouseScroll",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/Screen;mouseScrolled(DDD)Z"
        ),
        index = 0
    )
    public double injectOnMouseScroll(
        double amount,
        double mouseX,
        double mouseY
    ) {
        ClientListener.onScroll(mouseX, mouseY, amount);
        return amount;
    }

    @ModifyArg(
        method = "method_1611",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;mouseClicked(DDI)Z"),
        index = 0
    )
    private static double inject_method_1611(
        double mouseX,
        double mouseY,
        int button
    ) {
        ClientListener.onClick(mouseX, mouseY, button, /* pressed = */ true);
        return mouseX;
    }

    @ModifyArg(
        method = "method_1605",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;mouseReleased(DDI)Z"),
        index = 0
    )
    private static double inject_method_1605(
        double mouseX,
        double mouseY,
        int button
    ) {
        ClientListener.onClick(mouseX, mouseY, button, /* pressed = */ false);
        return mouseX;
    }

    @ModifyArg(
        method = "method_1602",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;mouseDragged(DDIDD)Z"),
        index = 0
    )
    private double inject_method_1602(
        double mouseX,
        double mouseY,
        int button,
        double deltaX,
        double deltaY
    ) {
        ClientListener.onDragged(
            deltaX,
            deltaY,
            mouseX,
            mouseY,
            button
        );
        return mouseX;
    }
}
