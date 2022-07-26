package com.chattriggers.ctjs.mixins;

//#if MC>=11701
//$$ import com.chattriggers.ctjs.minecraft.listeners.MouseListener;
//$$ import com.chattriggers.ctjs.minecraft.wrappers.Client;
//$$ import net.minecraft.client.MouseHandler;
//$$ import net.minecraft.client.gui.screens.Screen;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.Redirect;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$ import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
//$$
//$$ @Mixin(MouseHandler.class)
//$$ public class MouseHandlerMixin {
//$$     @Redirect(
                 //#if FORGE
                 //$$ method = "lambda$onMove$11",
                 //#else
                 //$$ method = "method_1602",
                 //#endif
//$$             at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseDragged(DDIDD)Z"),
//$$             remap = false
//$$     )
//$$     private boolean onMouseDragged(Screen instance, double mouseX, double mouseY, int button, double deltaX, double deltaY) {
//$$         MouseListener.INSTANCE.dragged$chattriggers(deltaX, deltaY, mouseX, mouseY, button);
//$$         return instance.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
//$$     }
//$$
//$$     @Inject(
//$$             method = "onScroll",
//$$             at = @At(
//$$                     value = "FIELD",
//$$                     target = "Lnet/minecraft/client/MouseHandler;minecraft:Lnet/minecraft/client/Minecraft;",
//$$                     ordinal = 3
//$$             ),
//$$             locals = LocalCapture.CAPTURE_FAILHARD
//$$     )
//$$     private void chattriggers_scrollTrigger(long l, double d, double e, CallbackInfo ci, double delta) {
//$$         MouseListener.INSTANCE.scrolled$chattriggers(Client.getMouseX(), Client.getMouseY(), (int) Math.signum(delta));
//$$     }
//$$
//$$     @Inject(
//$$             method = "onPress",
//$$             at = @At(
//$$                     value = "FIELD",
//$$                     target = "Lnet/minecraft/client/Minecraft;screen:Lnet/minecraft/client/gui/screens/Screen;",
//$$                     ordinal = 0
//$$             )
//$$     )
//$$     private void chattriggers_clickedTrigger(long l, int button, int pressed, int mods, CallbackInfo ci) {
//$$         MouseListener.INSTANCE.clicked$chattriggers(Client.getMouseX(), Client.getMouseY(), button, pressed == 1);
//$$     }
//$$ }
//#endif