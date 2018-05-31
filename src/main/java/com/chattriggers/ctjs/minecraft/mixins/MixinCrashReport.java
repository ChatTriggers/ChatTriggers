package com.chattriggers.ctjs.minecraft.mixins;

import com.chattriggers.ctjs.loader.ModuleManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrashReport.class)
public class MixinCrashReport {
    @Final @Shadow
    private CrashReportCategory theReportCategory;

    @Inject(method = "populateEnvironment", at = @At("HEAD"))
    private void injectCTModules(CallbackInfo ci) {
        this.theReportCategory.addCrashSectionCallable(
            "ct.js modules",
                () -> ModuleManager.getInstance().getModules().toString()
        );
    }
}
