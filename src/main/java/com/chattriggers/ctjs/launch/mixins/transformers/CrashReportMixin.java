package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.engine.module.Module;
import com.chattriggers.ctjs.engine.module.ModuleManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.Collectors;

@Mixin(CrashReport.class)
public class CrashReportMixin {
    @Final
    @Shadow
    private CrashReportCategory theReportCategory;

    @Inject(method = "populateEnvironment", at = @At("HEAD"))
    void injectPopulateEnvironment(CallbackInfo ci) {
        this.theReportCategory.addCrashSectionCallable("ctjs", () ->
            ModuleManager.INSTANCE.getCachedModules()
                .stream()
                .map(Module::getName)
                .collect(Collectors.joining(", "))
        );
    }
}
