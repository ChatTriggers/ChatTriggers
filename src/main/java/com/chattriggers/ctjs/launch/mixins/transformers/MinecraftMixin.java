package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.triggers.TriggerType;
import gg.essential.universal.wrappers.message.UTextComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ScreenShotHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Final
    @Shadow
    public File mcDataDir;

    @Shadow
    public int displayWidth;

    @Shadow
    public int displayHeight;

    @Shadow
    private Framebuffer framebufferMc;

    @Inject(
        method = "dispatchKeypresses",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiIngame;getChatGUI()Lnet/minecraft/client/gui/GuiNewChat;",
            ordinal = 1,
            shift = At.Shift.BEFORE,
            by = 2
        ),
        cancellable = true
    )
    void injectDispatchKeypresses(CallbackInfo ci) {
        // TODO: What is the point of this Mixin?
        IChatComponent component = ScreenShotHelper.saveScreenshot(mcDataDir, displayWidth, displayHeight, framebufferMc);
        new UTextComponent(component).chat();
        ci.cancel();
    }

    @Inject(
        method = "displayGuiScreen",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiScreen;onGuiClosed()V"
        )
    )
    void injectDisplayGuiScreen(GuiScreen guiScreenIn, CallbackInfo ci) {
        TriggerType.GuiClosed.triggerAll(Minecraft.getMinecraft().currentScreen);
    }

    @Inject(method = "startGame", at = @At("HEAD"))
    void injectStartGame(CallbackInfo ci) {
        TriggerType.GameLoad.triggerAll();
    }
}
