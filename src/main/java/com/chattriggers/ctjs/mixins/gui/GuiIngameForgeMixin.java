package com.chattriggers.ctjs.mixins.gui;

//#if FORGE
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC>=11701
//$$ import com.chattriggers.ctjs.utils.NullableModifier;
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//$$ import net.minecraft.network.chat.Component;
//#endif

@Mixin(value = GuiIngameForge.class, remap = false)
public class GuiIngameForgeMixin extends GuiIngame {
    public GuiIngameForgeMixin(Minecraft minecraft) {
        super(minecraft);
    }

    @Inject(
        method = "renderTitle",
        at = @At(
            value = "INVOKE",
            //#if MC<=11202
            target = "Lnet/minecraft/client/renderer/GlStateManager;pushMatrix()V",
            //#elseif MC>=11701
            //$$ target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V",
            //#endif
            ordinal = 0
        ),
        cancellable = true,
        remap = false
    )
    //#if MC<=11202
    private void chattriggers_renderTitleTrigger(int width, int height, float partialTicks, CallbackInfo ci) {
        String title = super.displayedTitle;
        String subtitle = super.displayedSubTitle;

        // TODO(BREAKING): Title trigger can now trigger without a subtitle.
        if (!title.isEmpty() || !subtitle.isEmpty())
            TriggerType.RenderTitle.triggerAll(title, subtitle, ci);
    }
    //#elseif MC>=11701
    //$$ private void chattriggers_renderTitleTrigger(int width, int height, float partialTicks, PoseStack pStack, CallbackInfo ci) {
    //$$     Component title = super.title;
    //$$     Component subtitle = super.subtitle;
    //$$
    //$$     if (title != null || subtitle != null) {
    //$$         TriggerType.RenderTitle.triggerAll(
    //$$                 NullableModifier.modify(title, Component::getString),
    //$$                 NullableModifier.modify(subtitle, Component::getString),
    //$$                 ci
    //$$         );
    //$$     }
    //$$ }
    //$$
    //$$ @Inject(method = "lambda$static$6(Lnet/minecraftforge/client/gui/ForgeIngameGui;Lcom/mojang/blaze3d/vertex/PoseStack;FII)V", at = @At("HEAD"), cancellable = true)
    //$$ private static void chattriggers_renderCrosshairTrigger(CallbackInfo ci) {
    //$$     TriggerType.RenderCrosshair.triggerAll(ci);
    //$$ }
    //$$
    //$$ @Inject(method = "renderBossHealth", at = @At("HEAD"), cancellable = true)
    //$$ private void chattriggers_renderBossHealthTrigger(CallbackInfo ci) {
    //$$     TriggerType.RenderBossHealth.triggerAll(ci);
    //$$ }
    //$$
    //$$ @Inject(method = "renderHealth", at = @At("HEAD"), cancellable = true)
    //$$ private void chattriggers_renderHealthTrigger(CallbackInfo ci) {
    //$$     TriggerType.RenderHealth.triggerAll(ci);
    //$$ }
    //$$
    //$$ @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    //$$ private void chattriggers_renderArmorTrigger(CallbackInfo ci) {
    //$$     TriggerType.RenderArmor.triggerAll(ci);
    //$$ }
    //$$
    //$$ @Inject(method = "renderFood", at = @At("HEAD"), cancellable = true)
    //$$ private void chattriggers_renderFoodTrigger(CallbackInfo ci) {
    //$$     TriggerType.RenderFood.triggerAll(ci);
    //$$ }
    //$$
    //$$ @Inject(method = "renderHealthMount", at = @At("HEAD"), cancellable = true)
    //$$ private void chattriggers_renderHealthMountTrigger(CallbackInfo ci) {
    //$$     TriggerType.RenderMountHealth.triggerAll(ci);
    //$$ }
    //$$
    //$$ @Inject(method = "renderExperience", at = @At("HEAD"), cancellable = true)
    //$$ private void chattriggers_renderExperienceTrigger(CallbackInfo ci) {
    //$$     TriggerType.RenderExperience.triggerAll(ci);
    //$$ }
    //$$
    //$$ @Inject(method = "lambda$static$5(Lnet/minecraftforge/client/gui/ForgeIngameGui;Lcom/mojang/blaze3d/vertex/PoseStack;FII)V", at = @At("HEAD"), cancellable = true)
    //$$ private static void chattriggers_renderHotbarTrigger(CallbackInfo ci) {
    //$$     TriggerType.RenderHotbar.triggerAll(ci);
    //$$ }
    //$$
    //$$ @Inject(method = "renderAir", at = @At("HEAD"), cancellable = true)
    //$$ private void chattriggers_renderAirTrigger(CallbackInfo ci) {
    //$$     TriggerType.RenderAir.triggerAll(ci);
    //$$ }
    //$$
    //$$ @Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
    //$$ private void chattriggers_renderPortalTrigger(CallbackInfo ci) {
    //$$     TriggerType.RenderPortal.triggerAll(ci);
    //$$ }
    //$$
    //$$ @Inject(method = "renderJumpMeter", at = @At("HEAD"), cancellable = true)
    //$$ private void chattriggers_renderJumpBarTrigger(CallbackInfo ci) {
    //$$     TriggerType.RenderJumpBar.triggerAll(ci);
    //$$ }
    //$$
    //$$ @Inject(method = "renderHelmet", at = @At("HEAD"), cancellable = true)
    //$$ private void chattriggers_renderHelmetTrigger(CallbackInfo ci) {
    //$$     TriggerType.RenderHelmet.triggerAll(ci);
    //$$ }
    //#endif
}
//#endif
