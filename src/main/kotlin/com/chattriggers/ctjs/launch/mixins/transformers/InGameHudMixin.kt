package com.chattriggers.ctjs.launch.mixins.transformers

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.triggers.TriggerType
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.profiler.Profiler
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.*
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.LocalCapture

@Mixin(InGameHud::class)
class InGameHudMixin {
    @Inject(method = ["renderCrosshair"], at = [At("HEAD")], cancellable = true)
    fun injectRenderCrosshair(ci: CallbackInfo) {
        TriggerType.RenderCrosshair.triggerAll(ci)
    }

    @Inject(method = ["renderHealthBar"], at = [At("HEAD")], cancellable = true)
    fun injectRenderHealthBar(ci: CallbackInfo) {
        TriggerType.RenderHealth.triggerAll(ci)
    }

    @Inject(method = ["renderMountHealth"], at = [At("HEAD")], cancellable = true)
    fun injectRenderMountHealth(ci: CallbackInfo) {
        TriggerType.RenderMountHealth.triggerAll(ci)
    }

    @Inject(method = ["renderExperienceBar"], at = [At("HEAD")], cancellable = true)
    fun injectRenderExperienceBar(ci: CallbackInfo) {
        TriggerType.RenderExperience.triggerAll(ci)
    }

    @Inject(method = ["renderHotbar"], at = [At("HEAD")], cancellable = true)
    fun injectRenderHotbar(ci: CallbackInfo) {
        TriggerType.RenderHotbar.triggerAll(ci)
    }

    @Redirect(
        method = ["renderStatusBars"],
        at = At("INVOKE", target = "drawTexture"),
        slice = Slice(
            from = At(
                value = "INVOKE",
                desc = Desc(owner = Profiler::class, value = "push", args = [String::class]),
            ),
            to = At(
                value = "INVOKE",
                desc = Desc(owner = Profiler::class, value = "swap", args = [String::class]),
            )
        )
    )
    fun redirectArmorDrawTextureCalls(
        hud: InGameHud,
        matrices: MatrixStack,
        x: Int,
        y: Int,
        u: Int,
        v: Int,
        width: Int,
        height: Int,
        ci: CallbackInfo,
    ) {
        TriggerType.RenderArmor.triggerAll(ci)
        if (!ci.isCancelled)
            hud.drawTexture(matrices, x, y, u, v, width, height)
    }

    @Redirect(
        method = ["renderStatusBars"],
        at = At("INVOKE", target = "drawTexture"),
        slice = Slice(
            from = At(
                value = "INVOKE",
                desc = Desc(owner = InGameHud::class, value = "getRiddenEntity", ret = LivingEntity::class),
            ),
            to = At(
                value = "INVOKE",
                desc = Desc(owner = PlayerEntity::class, value = "getMaxAir", ret = Int::class),
            )
        )
    )
    fun redirectFoodDrawTextureCalls(
        hud: InGameHud,
        matrices: MatrixStack,
        x: Int,
        y: Int,
        u: Int,
        v: Int,
        width: Int,
        height: Int,
        ci: CallbackInfo,
    ) {
        TriggerType.RenderFood.triggerAll(ci)
        if (!ci.isCancelled)
            hud.drawTexture(matrices, x, y, u, v, width, height)
    }

    @Redirect(
        method = ["renderStatusBars"],
        at = At("INVOKE", target = "drawTexture"),
        slice = Slice(
            from = At(
                value = "INVOKE",
                desc = Desc(owner = PlayerEntity::class, value = "getMaxAir", ret = Int::class),
            )
        )
    )
    fun redirectAirDrawTextureCalls(
        hud: InGameHud,
        matrices: MatrixStack,
        x: Int,
        y: Int,
        u: Int,
        v: Int,
        width: Int,
        height: Int,
        ci: CallbackInfo,
    ) {
        TriggerType.RenderAir.triggerAll(ci)
        if (!ci.isCancelled)
            hud.drawTexture(matrices, x, y, u, v, width, height)
    }

    @Inject(method = ["render"], at = [At("HEAD")], locals = LocalCapture.CAPTURE_FAILHARD)
    fun injectRender(hud: InGameHud, matrices: MatrixStack, partialTicks: Float) {
        Renderer.boundMatrixStack = matrices
        TriggerType.RenderOverlay.triggerAll()
    }
}
