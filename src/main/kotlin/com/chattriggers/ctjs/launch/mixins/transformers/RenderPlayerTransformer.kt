package com.chattriggers.ctjs.launch.mixins.transformers

// TODO(1.16.2): Is this necessary?
//#if MC==10809
import com.chattriggers.ctjs.minecraft.libs.renderer.CTRenderPlayer
import net.minecraft.client.renderer.entity.RenderPlayer
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject

@Mixin(RenderPlayer::class)
class RenderPlayerTransformer {
    @Inject(method = ["setModelVisibilities"], at = [At(value = "HEAD")])
    fun injectSetModelVisibilities() {
        // cast to Any required to fool the compiler into allowing this "incompatible" cast
        val thiz = this as Any
        if (thiz is CTRenderPlayer) {
            if (!thiz.showHeldItem)
                thiz.mainModel.heldItemRight = 0
        }
    }
}
//#endif
