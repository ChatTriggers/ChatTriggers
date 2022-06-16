package com.chattriggers.ctjs.launch.mixins.transformers.gui;

import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.scoreboard.ScoreObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC<=11202
import net.minecraft.client.gui.ScaledResolution;
//#elseif MC>=11701
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//#endif

//#if FABRIC
//$$ import com.chattriggers.ctjs.CTJS;
//$$ import com.chattriggers.ctjs.triggers.EventType;
//$$ import com.chattriggers.ctjs.minecraft.listeners.events.CancellableEvent;
//$$ import gg.essential.lib.mixinextras.injector.ModifyExpressionValue;
//$$ import gg.essential.lib.mixinextras.injector.WrapWithCondition;
//$$ import net.minecraft.client.gui.hud.BossBarHud;
//$$ import net.minecraft.client.gui.hud.ChatHud;
//$$ import net.minecraft.client.gui.hud.DebugHud;
//$$ import net.minecraft.client.gui.hud.PlayerListHud;
//$$ import net.minecraft.entity.player.PlayerEntity;
//$$ import net.minecraft.scoreboard.Scoreboard;
//#endif

@Mixin(GuiIngame.class)
public class GuiIngameMixin {
    @Inject(
            //#if MC<=11202
            method = "renderScoreboard",
            //#elseif MC>=11701
            //$$ method = "displayScoreboardSidebar",
            //#endif
            at = @At("HEAD"),
            cancellable = true
    )
    private void chattriggers_renderScoreboardTrigger(
            //#if MC>=11701
            //$$ PoseStack poseStack,
            //#endif
            ScoreObjective objective,
            //#if MC<=11202
            ScaledResolution scaledRes,
            //#endif
            CallbackInfo ci
    ) {
        TriggerType.RenderScoreboard.triggerAll(ci);
    }

    //#if FABRIC
    //$$ @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    //$$ private void chattriggers_renderCrosshairTrigger(MatrixStack matrices, CallbackInfo ci) {
    //$$     TriggerType.RenderCrosshair.triggerAll(ci);
    //$$ }
    //$$
    //$$ @Inject(method = "renderHealthBar", at = @At("HEAD"), cancellable = true)
    //$$ private void chattriggers_renderHealthBarTrigger(MatrixStack matrices, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
    //$$     TriggerType.RenderHealth.triggerAll(ci);
    //$$ }
    //$$
    //$$ @ModifyConstant(
    //$$         method = "renderStatusBars",
    //$$         constant = @Constant(intValue = 0, ordinal = 0),
    //$$         slice = @Slice(
    //$$                 from = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V", args = "ldc=armor"),
    //$$                 to = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = "ldc=health")
    //$$         )
    //$$ )
    //$$ private int chattriggers_renderArmorTrigger(int original) {
    //$$     CancellableEvent event = new CancellableEvent();
    //$$     TriggerType.RenderArmor.triggerAll(event);
    //$$
    //$$     return event.isCanceled() ? 100 : original;
    //$$ }
    //$$
    //$$ @ModifyConstant(
    //$$         method = "renderStatusBars",
    //$$         constant = @Constant(intValue = 0, ordinal = 0),
    //$$         slice = @Slice(
    //$$                 from = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = "ldc=food"),
    //$$                 to = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = "ldc=air")
    //$$         )
    //$$ )
    //$$ private int chattriggers_renderFoodTrigger(int original) {
    //$$     CancellableEvent event = new CancellableEvent();
    //$$     TriggerType.RenderFood.triggerAll(event);
    //$$
    //$$     return event.isCanceled() ? 100 : original;
    //$$ }
    //$$
    //$$ @Inject(method = "renderMountHealth", at = @At("HEAD"), cancellable = true)
    //$$ private void chattriggers_renderMountHealthTrigger(MatrixStack matrices, CallbackInfo ci) {
    //$$     TriggerType.RenderMountHealth.triggerAll(ci);
    //$$ }
    //$$
    //$$ @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    //$$ private void chattriggers_renderExperienceBarTrigger(MatrixStack matrices, int x, CallbackInfo ci) {
    //$$     TriggerType.RenderExperience.triggerAll(ci);
    //$$ }
    //$$
    //$$ @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    //$$ private void chattriggers_renderHotbarTrigger(float tickDelta, MatrixStack matrices, CallbackInfo ci) {
    //$$     TriggerType.RenderHotbar.triggerAll(ci);
    //$$ }
    //$$
    //$$ @ModifyConstant(
    //$$         method = "renderStatusBars",
    //$$         constant = @Constant(intValue = 0, ordinal = 0),
    //$$         slice = @Slice(
    //$$                 from = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = "ldc=air")
    //$$         )
    //$$ )
    //$$ private int chattriggers_renderAirTrigger(int original) {
    //$$    CancellableEvent event = new CancellableEvent();
    //$$    TriggerType.RenderAir.triggerAll(event);
    //$$
    //$$    return event.isCanceled() ? 100 : original;
    //$$ }
    //$$
    //$$ @Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
    //$$ private void chattriggers_renderPortalOverlayTrigger(float nauseaStrength, CallbackInfo ci) {
    //$$     TriggerType.RenderPortal.triggerAll(ci);
    //$$ }
    //$$
    //$$ @Inject(method = "renderMountJumpBar", at = @At("HEAD"), cancellable = true)
    //$$ private void chattriggers_renderMountJumpBarTrigger(MatrixStack matrices, int x, CallbackInfo ci) {
    //$$     TriggerType.RenderJumpBar.triggerAll(ci);
    //$$ }
    //$$
    //$$ @ModifyExpressionValue(
    //$$         method = "render",
    //$$         at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z")
    //$$ )
    //$$ private boolean chattriggers_renderHelmetTrigger(boolean original) {
    //$$     CancellableEvent event = new CancellableEvent();
    //$$     TriggerType.RenderHelmet.triggerAll(event);
    //$$
    //$$     return !event.isCanceled() && original;
    //$$ }
    //$$
    //$$ @WrapWithCondition(
    //$$         method = "render",
    //$$         at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/BossBarHud;render(Lnet/minecraft/client/util/math/MatrixStack;)V")
    //$$ )
    //$$ private boolean chattriggers_renderBossHealthTrigger(BossBarHud instance, MatrixStack matrices) {
    //$$     CancellableEvent event = new CancellableEvent();
    //$$     TriggerType.RenderBossHealth.triggerAll(event);
    //$$
    //$$     return !event.isCanceled();
    //$$ }
    //$$
    //$$ @WrapWithCondition(
    //$$         method = "render",
    //$$         at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;render(Lnet/minecraft/client/util/math/MatrixStack;I)V")
    //$$ )
    //$$ private boolean chattriggers_renderChatTrigger(ChatHud instance, MatrixStack matrices, int tickDelta) {
    //$$     CancellableEvent event = new CancellableEvent();
    //$$     TriggerType.RenderChat.triggerAll(event);
    //$$
    //$$     return !event.isCanceled();
    //$$ }
    //$$
    //$$ @WrapWithCondition(
    //$$         method = "render",
    //$$         at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;render(Lnet/minecraft/client/util/math/MatrixStack;)V")
    //$$ )
    //$$ private boolean chattriggers_renderDebugTrigger(DebugHud instance, MatrixStack matrices) {
    //$$     CancellableEvent event = new CancellableEvent();
    //$$     TriggerType.RenderDebug.triggerAll(event);
    //$$
    //$$     return !event.isCanceled();
    //$$ }
    //$$
    //$$ @WrapWithCondition(
    //$$         method = "render",
    //$$         at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/PlayerListHud;render(Lnet/minecraft/client/util/math/MatrixStack;ILnet/minecraft/scoreboard/Scoreboard;Lnet/minecraft/scoreboard/ScoreboardObjective;)V")
    //$$ )
    //$$ private boolean chattriggers_renderPlayerListTrigger(PlayerListHud instance, MatrixStack matrices, int scaledWindowWidth, Scoreboard scoreboard, ScoreboardObjective objective) {
    //$$     CancellableEvent event = new CancellableEvent();
    //$$     TriggerType.RenderPlayerList.triggerAll(event);
    //$$
    //$$     return !event.isCanceled();
    //$$ }
    //$$
    //$$ @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V", ordinal = 1, remap = false))
    //$$ private void chattriggers_renderOverlayTrigger(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
    //$$     CancellableEvent event = new CancellableEvent();
    //$$     TriggerType.RenderOverlay.triggerAll(event);
    //$$
    //$$     CTJS.Companion.getEventListeners(EventType.RenderOverlay).forEach(listener -> {
    //$$         listener.invoke(new Object[]{event});
    //$$     });
    //$$ }
    //$$
    //$$ @Inject(method = "renderScoreboardSidebar", at = @At("HEAD"), cancellable = true)
    //$$ private void chattriggers_toggleSidebar(MatrixStack matrices, ScoreboardObjective objective, CallbackInfo ci) {
    //$$     boolean shouldRender = com.chattriggers.ctjs.minecraft.wrappers.Scoreboard.getShouldRender$chattriggers();
    //$$
    //$$     if (!shouldRender) {
    //$$         ci.cancel();
    //$$     }
    //$$ }
    //#endif
}
