package com.chattriggers.ctjs.mixins.gui;

import com.chattriggers.ctjs.utils.UpdateChecker;
import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC>=11701
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//#endif

@Mixin(GuiMainMenu.class)
public class GuiMainMenuMixin {
    @Inject(
            //#if MC<=11202
            method = "drawScreen",
            //#elseif MC>=11701
            //$$ method = "render",
            //#endif
            at = @At("TAIL")
    )
    //#if MC<=11202
    private void chattriggers_drawUpdateMessage(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
    //#elseif MC>=11701
    //$$ private void chattriggers_drawUpdateMessage(PoseStack arg, int m, int n, float g, CallbackInfo ci) {
    //#endif
        UpdateChecker.INSTANCE.drawUpdateMessage();
    }
}

