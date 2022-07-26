package com.chattriggers.ctjs.mixins;

//#if MC>=11701
//$$ import com.chattriggers.ctjs.minecraft.listeners.events.CancellableEvent;
//$$ import com.chattriggers.ctjs.triggers.TriggerType;
//$$ import net.minecraft.client.KeyboardHandler;
//$$ import net.minecraft.client.gui.screens.Screen;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Redirect;
//$$
//$$ @Mixin(KeyboardHandler.class)
//$$ public class KeyboardHandlerMixin {
//$$     @Redirect(
                 //#if FORGE
                 //$$ method = "lambda$keyPress$4",
                 //#else
                 //$$ method = "method_1454",
                 //#endif
//$$             at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;keyPressed(III)Z"),
//$$             remap = false
//$$     )
//$$     private boolean chattriggers_guiKeyTrigger(Screen screen, int i, int j, int k) {
//$$         CancellableEvent event = new CancellableEvent();
//$$         TriggerType.GuiKey.triggerAll(i, j, event);
//$$
//$$         if (event.isCanceled()) {
//$$             return false;
//$$         } else {
//$$             return screen.keyPressed(i, j, k);
//$$         }
//$$     }
//$$ }
//#endif
