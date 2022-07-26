package com.chattriggers.ctjs.mixins;

import com.chattriggers.ctjs.minecraft.listeners.events.CancellableEvent;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ScreenShotHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

//#if MC<=11202
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#elseif MC>=11701
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$ import java.util.function.Consumer;
//#endif

import java.io.File;

@Mixin(ScreenShotHelper.class)
public class ScreenShotHelperMixin {
    //#if MC<=11202
    @Shadow
    private static File getTimestampedPNGFileForDirectory(File gameDirectory) { return null; }

    @Inject(
        method = "saveScreenshot(Ljava/io/File;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/IChatComponent;",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void chattriggers_screenshotTakenTrigger(File gameDirectory, int width, int height, Framebuffer buffer, CallbackInfoReturnable<IChatComponent> cir) {
        CancellableEvent event = new CancellableEvent();
        String screenshotName = getTimestampedPNGFileForDirectory(new File(gameDirectory, "screenshots")).getName();
        TriggerType.ScreenshotTaken.triggerAll(screenshotName, event);
        if (event.isCanceled())
            cir.setReturnValue(null);
    }
    //#elseif MC>=11701
    //$$ @Shadow
    //$$ private static File getFile(File gameDirectory) { return null; }
    //$$
    //$$ @Inject(
    //$$         method = "grab(Ljava/io/File;Lcom/mojang/blaze3d/pipeline/RenderTarget;Ljava/util/function/Consumer;)V",
    //$$         at = @At("HEAD"),
    //$$         cancellable = true
    //$$ )
    //$$ private static void chattriggers_screenshotTakenTrigger(File file, RenderTarget arg, Consumer<Component> consumer, CallbackInfo ci) {
    //$$     CancellableEvent event = new CancellableEvent();
    //$$     String screenshotName = getFile(new File(file, "screenshots")).getName();
    //$$     TriggerType.ScreenshotTaken.triggerAll(screenshotName, event);
    //$$     if (event.isCanceled())
    //$$         ci.cancel();
    //$$ }
    //#endif
}
