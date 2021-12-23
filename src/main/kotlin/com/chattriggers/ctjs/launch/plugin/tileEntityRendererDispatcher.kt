package com.chattriggers.ctjs.launch.plugin

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent
import com.chattriggers.ctjs.triggers.TriggerType
import dev.falsehonesty.asmhelper.dsl.At
import dev.falsehonesty.asmhelper.dsl.InjectionPoint
import dev.falsehonesty.asmhelper.dsl.code.CodeBlock.Companion.methodReturn
import dev.falsehonesty.asmhelper.dsl.inject
import net.minecraft.tileentity.TileEntity
import org.lwjgl.util.vector.Vector3f

fun injectTileEntityRendererDispatcher() {
    injectRenderTileEntity()
    injectRenderTileEntityPost()
}

fun injectRenderTileEntity() = inject {
    className = "net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher"
    methodName = "renderTileEntityAt"
    methodDesc = "(L$TILE_ENTITY;DDDFI)V"
    at = At(InjectionPoint.HEAD)

    methodMaps = mapOf("func_178469_a" to "renderTileEntityAt")

    codeBlock {
        val local1 = shadowLocal<TileEntity>()
        val local2 = shadowLocal<Double>()
        val local4 = shadowLocal<Double>()
        val local6 = shadowLocal<Double>()
        val local8 = shadowLocal<Float>()

        code {
            val event = CancellableEvent()
            TriggerType.RenderTileEntity.triggerAll(
                local1,
                Vector3f(local2.toFloat(), local4.toFloat(), local6.toFloat()),
                local8,
                event
            )

            if (event.isCancelled())
                methodReturn()
        }
    }
}

fun injectRenderTileEntityPost() = inject {
    className = "net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher"
    methodName = "renderTileEntityAt"
    methodDesc = "(L$TILE_ENTITY;DDDFI)V"
    at = At(InjectionPoint.TAIL)

    methodMaps = mapOf("func_178469_a" to "renderTileEntityAt")

    codeBlock {
        val local1 = shadowLocal<TileEntity>()
        val local2 = shadowLocal<Double>()
        val local4 = shadowLocal<Double>()
        val local6 = shadowLocal<Double>()
        val local8 = shadowLocal<Float>()

        code {
            TriggerType.PostRenderTileEntity.triggerAll(
                local1,
                Vector3f(local2.toFloat(), local4.toFloat(), local6.toFloat()),
                local8
            )
        }
    }
}
