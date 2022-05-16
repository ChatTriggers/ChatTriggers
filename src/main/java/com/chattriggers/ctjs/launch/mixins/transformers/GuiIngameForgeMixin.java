package com.chattriggers.ctjs.launch.mixins.transformers;

//#if MC<=11202
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameForge.class)
public class GuiIngameForgeMixin {
    @Inject(
        method = "renderTitle",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/GlStateManager;pushMatrix()V",
            ordinal = 0
        ),
        cancellable = true
    )
    void injectRenderTitle(int width, int height, float partialTicks, CallbackInfo ci) {
        String title = ((GuiIngameAccessor) this).getDisplayedTitle();
        String subtitle = ((GuiIngameAccessor) this).getDisplayedSubTitle();

        if (!title.isEmpty() && !subtitle.isEmpty())
            TriggerType.RenderTitle.triggerAll(title, subtitle, ci);
    }
}
//#endif
