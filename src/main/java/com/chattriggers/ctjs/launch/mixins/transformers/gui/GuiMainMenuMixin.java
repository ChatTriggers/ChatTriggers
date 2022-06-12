package com.chattriggers.ctjs.launch.mixins.transformers.gui;

//#if MC<=11202
import com.chattriggers.ctjs.utils.UpdateChecker;
import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public class GuiMainMenuMixin {
    @Inject(
        method = "drawScreen",
        at = @At("TAIL")
    )
    private void chattriggers_drawUpdateMessage(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        UpdateChecker.INSTANCE.drawUpdateMessage();
    }
}
//#endif
