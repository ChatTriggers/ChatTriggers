package com.chattriggers.ctjs.launch.plugin

import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.triggers.TriggerType
import me.falsehonesty.asmhelper.BaseClassTransformer
import me.falsehonesty.asmhelper.dsl.At
import me.falsehonesty.asmhelper.dsl.InjectionPoint
import me.falsehonesty.asmhelper.dsl.inject
import me.falsehonesty.asmhelper.dsl.instructions.Descriptor
import net.minecraft.client.renderer.chunk.RenderChunk

class CTJSTransformer : BaseClassTransformer() {
    override fun makeTransformers() {
        injectBlockRender()
    }

    private fun injectBlockRender() = inject {
        className = "net/minecraft/client/renderer/RenderGlobal"
        at = At(
                value = InjectionPoint.INVOKE(
                        Descriptor("net/minecraft/client/renderer/chunk/RenderChunk", "getPosition", "()Lnet/minecraft/util/BlockPos;")
                ),
                before = false
        )
        methodName = "setupTerrain"
        methodDesc = "(Lnet/minecraft/entity/Entity;DLnet/minecraft/client/renderer/culling/ICamera;IZ)V"

        codeBlock {
            val local26 = shadowLocal<RenderChunk>()

            code {
                val pos = local26.position

                TriggerType.RENDER_BLOCK.triggerAll(
                        World.getBlockAt(pos.x, pos.y, pos.z),
                        pos
                )
            }
        }
    }
}