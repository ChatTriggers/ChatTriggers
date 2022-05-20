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
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraftforge/client/ForgeHooksClient;renderMainMenu(Lnet/minecraft/client/gui/GuiMainMenu;Lnet/minecraft/client/gui/FontRenderer;II)V"
        )
    )
    void injectDrawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        UpdateChecker.INSTANCE.drawUpdateMessage();
    }
}
//#endif
