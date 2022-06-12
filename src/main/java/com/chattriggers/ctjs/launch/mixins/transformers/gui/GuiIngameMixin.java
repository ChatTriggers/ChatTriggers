package com.chattriggers.ctjs.launch.mixins.transformers.gui;

import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.scoreboard.ScoreObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC<=11202
import net.minecraft.client.gui.ScaledResolution;
//#elseif MC>=11701
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//#endif

//#if FABRIC
//$$ import com.chattriggers.ctjs.CTJS;
//$$ import com.chattriggers.ctjs.triggers.EventType;
//$$ import com.chattriggers.ctjs.triggers.TriggerType;
//$$ import com.chattriggers.ctjs.minecraft.listeners.events.CancellableEvent;
//$$ import net.minecraft.client.MinecraftClient;
//$$ import net.minecraft.client.gui.hud.BossBarHud;
//$$ import net.minecraft.client.gui.hud.ChatHud;
//$$ import net.minecraft.client.gui.hud.DebugHud;
//$$ import net.minecraft.client.gui.hud.PlayerListHud;
//$$ import net.minecraft.client.util.math.MatrixStack;
//$$ import net.minecraft.entity.player.PlayerEntity;
//$$ import net.minecraft.scoreboard.Scoreboard;
//$$ import net.minecraft.util.Identifier;
//$$ import org.spongepowered.asm.mixin.Final;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.Shadow;
//$$ import org.spongepowered.asm.mixin.injection.ModifyVariable;
//$$ import org.spongepowered.asm.mixin.injection.Redirect;
//$$ import org.spongepowered.asm.mixin.injection.Slice;
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
    //$$ @Final
    //$$ @Shadow
    //$$ private MinecraftClient client;
    //$$
    //$$ @Shadow
    //$$ private void renderOverlay(Identifier par1, float par2) {}
    //$$
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
    //$$ @ModifyVariable(method = "renderStatusBars", at = @At(value = "CONSTANT", args = "intValue=0", ordinal = 0))
    //$$ private int chattriggers_renderArmorTrigger(int original) {
    //$$     CancellableEvent event = new CancellableEvent();
    //$$     TriggerType.RenderArmor.triggerAll(event);
    //$$
    //$$     return event.isCanceled() ? 100 : original;
    //$$ }
    //$$
    //$$    // TODO(VERIFY)
    //$$ @ModifyVariable(
    //$$         method = "renderStatusBars",
    //$$         at = @At(value = "STORE", ordinal = 0),
    //$$         slice = @Slice(
    //$$                 from = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = "ldc=food"),
    //$$                 to = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = "ldc=air")
    //$$         ),
    //$$         ordinal = 0
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
    //$$// TODO(VERIFY)
    //$$ @Inject(
    //$$         method = "renderStatusBars",
    //$$         at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;getHeartRows(I)I"),
    //$$         cancellable = true
    //$$ )
    //$$ private void chattriggers_renderAirTrigger(MatrixStack matrices, CallbackInfo ci) {
    //$$    CancellableEvent event = new CancellableEvent();
    //$$    TriggerType.RenderAir.triggerAll(event);
    //$$
    //$$    if (event.isCanceled()) {
    //$$        client.getProfiler().pop();
    //$$        ci.cancel();
    //$$    }
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
    //$$ @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/util/Identifier;F)V", ordinal = 0))
    //$$ private void chattriggers_renderHelmetTrigger(InGameHud instance, Identifier texture, float opacity) {
    //$$     CancellableEvent event = new CancellableEvent();
    //$$     TriggerType.RenderHelmet.triggerAll(event);
    //$$
    //$$     if (!event.isCanceled()) {
    //$$         renderOverlay(texture, opacity);
    //$$     }
    //$$ }
    //$$
    //$$ @Final
    //$$ @Shadow
    //$$ private BossBarHud bossBarHud;
    //$$
    //$$ @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/BossBarHud;render(Lnet/minecraft/client/util/math/MatrixStack;)V"))
    //$$ private void chattriggers_renderBossHealthTrigger(BossBarHud instance, MatrixStack matrices) {
    //$$     CancellableEvent event = new CancellableEvent();
    //$$
    //$$     TriggerType.RenderBossHealth.triggerAll(event);
    //$$
    //$$     if (!event.isCanceled()) {
    //$$         bossBarHud.render(matrices);
    //$$     }
    //$$ }
    //$$
    //$$ @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;render(Lnet/minecraft/client/util/math/MatrixStack;I)V"))
    //$$ private void chattriggers_renderChatTrigger(ChatHud instance, MatrixStack matrices, int tickDelta) {
    //$$     CancellableEvent event = new CancellableEvent();
    //$$
    //$$     TriggerType.RenderChat.triggerAll(event);
    //$$
    //$$     if (!event.isCanceled()) {
    //$$         instance.render(matrices, tickDelta);
    //$$     }
    //$$ }
    //$$
    //$$ @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;render(Lnet/minecraft/client/util/math/MatrixStack;)V"))
    //$$ private void chattriggers_renderDebugTrigger(DebugHud instance, MatrixStack matrices) {
    //$$     CancellableEvent event = new CancellableEvent();
    //$$
    //$$     TriggerType.RenderDebug.triggerAll(event);
    //$$
    //$$     if (!event.isCanceled()) {
    //$$         instance.render(matrices);
    //$$     }
    //$$ }
    //$$
    //$$ @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/PlayerListHud;render(Lnet/minecraft/client/util/math/MatrixStack;ILnet/minecraft/scoreboard/Scoreboard;Lnet/minecraft/scoreboard/ScoreboardObjective;)V"))
    //$$ private void chattriggers_renderPlayerListTrigger(PlayerListHud instance, MatrixStack matrices, int scaledWindowWidth, Scoreboard scoreboard, ScoreboardObjective objective) {
    //$$     CancellableEvent event = new CancellableEvent();
    //$$
    //$$     TriggerType.RenderPlayerList.triggerAll(event);
    //$$
    //$$     if (!event.isCanceled()) {
    //$$         instance.render(matrices, scaledWindowWidth, scoreboard, objective);
    //$$     }
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
