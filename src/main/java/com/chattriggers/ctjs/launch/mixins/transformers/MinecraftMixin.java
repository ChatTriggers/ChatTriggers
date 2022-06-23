package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.minecraft.listeners.MouseListener;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import com.chattriggers.ctjs.minecraft.wrappers.World;
import com.chattriggers.ctjs.minecraft.wrappers.inventory.Inventory;
import com.chattriggers.ctjs.triggers.EventType;
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

//#if MC<=11202
import org.lwjgl.input.Mouse;
//#endif

@Mixin(Minecraft.class)
public class MinecraftMixin {
    //#if MC<=11202
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
    private void chattriggers_fixScreenshotTriggerCancelNPE(CallbackInfo ci) {
        // Solves null pointer exception when ScreenshotTrigger event is canceled.
        IChatComponent component = ScreenShotHelper.saveScreenshot(mcDataDir, displayWidth, displayHeight, framebufferMc);
        new UTextComponent(component).chat();
        ci.cancel();
    }
    //#endif

    @Inject(
            //#if MC<=11202
            method = "displayGuiScreen",
            //#elseif MC>=11701
            //$$ method = "setScreen",
            //#endif
            at = @At(
                    value = "FIELD",
                    //#if MC<=11202
                    target = "Lnet/minecraft/client/Minecraft;currentScreen:Lnet/minecraft/client/gui/GuiScreen;",
                    //#elseif MC>=11701
                    //$$ target = "Lnet/minecraft/client/Minecraft;screen:Lnet/minecraft/client/gui/screens/Screen;",
                    //#endif
                    ordinal = 0
            ),
            cancellable = true
    )
    private void chattriggers_guiOpenedTrigger(GuiScreen gui, CallbackInfo ci) {
        if (gui != null) {
            TriggerType.GuiOpened.triggerAll(gui, ci);
        }
    }

    @Inject(
        //#if MC<=11202
        method = "displayGuiScreen",
        //#elseif MC>=11701
        //$$ method = "setScreen",
        //#endif
        at = @At(
            value = "INVOKE",
            //#if MC<=11202
            target = "Lnet/minecraft/client/gui/GuiScreen;onGuiClosed()V"
            //#elseif MC>=11701
            //$$ target = "Lnet/minecraft/client/gui/screens/Screen;removed()V"
            //#endif
        )
    )
    private void chattriggers_guiClosedTrigger(GuiScreen guiScreenIn, CallbackInfo ci) {
        TriggerType.GuiClosed.triggerAll(Client.currentGui.get());
        Inventory.Companion.setOpenedInventoryName$chattriggers(null);
    }

    @Inject(
            //#if MC<=11202
            method = "startGame",
            //#elseif MC>=11701
            //$$ method = "<init>",
            //#endif
            at = @At("TAIL")
    )
    private void chattriggers_gameLoadTrigger(CallbackInfo ci) {
        TriggerType.GameLoad.triggerAll();
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    private void chattriggers_tickTrigger(CallbackInfo ci) {
        CTJS.Companion.getEventListeners(EventType.Tick).forEach(listener -> {
            listener.invoke(null);
        });
    }

    @Inject(
            //#if MC<=11202
            method = "runGameLoop",
            //#elseif MC>=11701
            //$$ method = "runTick",
            //#endif
            at = @At(value = "CONSTANT", args = "stringValue=gameRenderer")
    )
    private void chattriggers_stepTrigger(CallbackInfo ci) {
        TriggerType.Step.triggerAll();
        //#if MC<=11202
        if (World.isLoaded())
            MouseListener.INSTANCE.handleDragged$chattriggers();
        //#endif
    }

    //#if MC<=11202
    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Mouse;next()Z", shift = At.Shift.BY, by = 1))
    private void chattriggers_mouseClickTrigger(CallbackInfo ci) {
        MouseListener.INSTANCE.process$chattriggers(Mouse.getEventButton(), Mouse.getEventDWheel());
    }
    //#endif
}

