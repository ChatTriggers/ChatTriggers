package com.chattriggers.ctjs.launch.mixins.transformers

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.listeners.ClientListener
import com.chattriggers.ctjs.minecraft.listeners.WorldListener
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.triggers.TriggerType
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.util.math.MatrixStack
import org.spongepowered.asm.mixin.Final
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.LocalCapture

// TODO("fabric"): This definitely is not going to work, since fabric mixes
//                 into the exact same place
@Mixin(GameRenderer::class)
internal abstract class GameRendererMixin {
    @Shadow
    @Final
    private lateinit var client: MinecraftClient

    @Inject(
        method = ["render"],
        at = [At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V"
        )],
        locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private fun onBeforeRenderScreen(
        tickDelta: Float,
        startTime: Long,
        tick: Boolean,
        ci: CallbackInfo,
        mouseX: Int,
        mouseY: Int,
        matrices: MatrixStack
    ) {
        Renderer.boundMatrixStack = matrices
        TriggerType.GuiRender.triggerAll(mouseX, mouseY, Client.getMinecraft().currentScreen)
        TriggerType.Step.triggerAll()
        WorldListener.onPreOverlayRender()
    }

    @Inject(
        method = ["render"],
        at = [At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V",
            shift = At.Shift.AFTER
        )],
        locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private fun onAfterRenderScreen(
        tickDelta: Float,
        startTime: Long,
        tick: Boolean,
        ci: CallbackInfo,
        mouseX: Int,
        mouseY: Int,
        matrices: MatrixStack
    ) {
        Renderer.boundMatrixStack = matrices
        TriggerType.PostGuiRender.triggerAll(mouseX, mouseY, Client.getMinecraft().currentScreen)
    }
}
