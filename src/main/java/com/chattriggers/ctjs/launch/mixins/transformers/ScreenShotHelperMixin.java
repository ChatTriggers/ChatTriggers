package com.chattriggers.ctjs.launch.mixins.transformers;

//#if MC<=11202
import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ScreenShotHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(ScreenShotHelper.class)
public class ScreenShotHelperMixin {
    @Shadow
    private static File getTimestampedPNGFileForDirectory(File gameDirectory) { return null; }

    @Inject(
        method = "saveScreenshot(Ljava/io/File;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/IChatComponent;",
        at = @At("HEAD"),
        cancellable = true
    )
    static void injectSaveScreenshot(File gameDirectory, int width, int height, Framebuffer buffer, CallbackInfoReturnable<IChatComponent> cir) {
        CancellableEvent event = new CancellableEvent();
        String screenshotName = getTimestampedPNGFileForDirectory(new File(gameDirectory, "screenshots")).getName();
        TriggerType.ScreenshotTaken.triggerAll(screenshotName, event);
        if (event.isCanceled())
            cir.setReturnValue(null);
    }
}
//#endif
