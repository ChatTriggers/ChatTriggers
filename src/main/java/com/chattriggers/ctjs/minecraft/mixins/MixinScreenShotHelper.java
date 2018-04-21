package com.chattriggers.ctjs.minecraft.mixins;

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
public abstract class MixinScreenShotHelper {
    @Shadow
    private static File getTimestampedPNGFileForDirectory(File gameDirectory) { return null; }

    @Inject(
            method = "saveScreenshot(Ljava/io/File;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/IChatComponent;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void saveScreenshot(File gameDirectory, int width, int height, Framebuffer buffer, CallbackInfoReturnable<IChatComponent> ci) {
        File file = getTimestampedPNGFileForDirectory(new File(gameDirectory, "screenshots"));
        TriggerType.SCREENSHOT_TAKEN.triggerAll(file.getName(), ci);
    }
}
