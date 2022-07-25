package com.chattriggers.ctjs.mixins;

//#if MC>=11701
//$$ import com.chattriggers.ctjs.minecraft.listeners.MouseListener;
//$$ import com.chattriggers.ctjs.minecraft.listeners.events.CancellableEvent;
//$$ import com.chattriggers.ctjs.minecraft.wrappers.Client;
//$$ import com.chattriggers.ctjs.triggers.TriggerType;
//$$ import com.mojang.blaze3d.platform.Window;
//$$ import net.minecraft.client.MouseHandler;
//$$ import net.minecraft.client.gui.screens.Screen;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.Shadow;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.Redirect;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$ import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
//$$
//$$ @Mixin(MouseHandler.class)
//$$ public class MouseHandlerMixin {
//$$     @Shadow
//$$     private int activeButton;
//$$
//$$     @Shadow
//$$     private double xpos;
//$$     @Shadow
//$$     private double ypos;
//$$
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
//$$         CancellableEvent event = new CancellableEvent();
//$$         TriggerType.GuiMouseDrag.triggerAll(mouseX, mouseY, button, this, event);
//$$         if (event.isCanceled()) {
//$$             return false;
//$$         }
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
//$$
//$$     @Redirect(
//$$             //#if FORGE
//$$             method = "lambda$onPress$0",
//$$             //#else
//$$             //$$ method = "method_1611",
//$$             //#endif
//$$             at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseClicked(DDI)Z"),
//$$             remap = false
//$$     )
//$$     private
         //#if FABRIC
         //$$ static
         //#endif
//$$     boolean chattriggers_guiMouseClickTrigger(Screen instance, double mouseX, double mouseY, int button) {
//$$         CancellableEvent event = new CancellableEvent();
//$$         TriggerType.GuiMouseClick.triggerAll(mouseX, mouseY, button, event);
//$$         if (event.isCanceled()) {
//$$             return false;
//$$         }
//$$         return instance.mouseClicked(mouseX, mouseY, button);
//$$     }
//$$
//$$     @Redirect(
//$$             //#if FORGE
//$$             method = "lambda$onPress$1",
//$$             //#else
//$$             //$$ method = "method_1605",
//$$             //#endif
//$$             at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseReleased(DDI)Z"),
//$$             remap = false
//$$     )
//$$     private
         //#if FABRIC
         //$$ static
         //#endif
//$$     boolean chattriggers_guiMouseReleaseTrigger(Screen instance, double mouseX, double mouseY, int button) {
//$$         CancellableEvent event = new CancellableEvent();
//$$         TriggerType.GuiMouseRelease.triggerAll(mouseX, mouseY, button, event);
//$$         if (event.isCanceled()) {
//$$             return false;
//$$         }
//$$         return instance.mouseReleased(mouseX, mouseY, button);
//$$     }
//$$
//$$     @Inject(method = "onMove", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MouseHandler;ignoreFirstMove:Z", ordinal = 0))
//$$     private void chattriggers_draggedTrigger(long l, double d, double e, CallbackInfo ci) {
//$$         if (this.activeButton != -1) {
//$$             Window window = Client.getMinecraft().getWindow();
//$$             double dx = (d - this.xpos) * window.getGuiScaledWidth() / window.getScreenWidth();
//$$             double dy = (e - this.ypos) * window.getGuiScaledHeight() / window.getScreenHeight();
//$$             TriggerType.Dragged.triggerAll(dx, dy, Client.getMouseX(), Client.getMouseY(), this.activeButton);
//$$         }
//$$     }
//$$ }
//#endif