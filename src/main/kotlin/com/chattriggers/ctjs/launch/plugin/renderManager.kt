package com.chattriggers.ctjs.launch.plugin

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent
import com.chattriggers.ctjs.minecraft.wrappers.entity.Entity
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.kotlin.MCEntity
import dev.falsehonesty.asmhelper.dsl.At
import dev.falsehonesty.asmhelper.dsl.InjectionPoint
import dev.falsehonesty.asmhelper.dsl.code.CodeBlock.Companion.iReturn
import dev.falsehonesty.asmhelper.dsl.inject
import org.lwjgl.util.vector.Vector3f

fun injectRenderManager() {
    injectRenderEntity()
    injectRenderEntityPost()
}

fun injectRenderEntity() = inject {
    className = "net/minecraft/client/renderer/entity/RenderManager"
    methodName = "doRenderEntity"
    methodDesc = "(L$ENTITY;DDDFFZ)Z"
    at = At(InjectionPoint.HEAD)

    methodMaps = mapOf("func_147939_a" to "doRenderEntity")

    codeBlock {
        val local1 = shadowLocal<MCEntity>()
        val local2 = shadowLocal<Double>()
        val local4 = shadowLocal<Double>()
        val local6 = shadowLocal<Double>()
        val local9 = shadowLocal<Float>()

        code {
            val event = CancellableEvent()
            TriggerType.RenderEntity.triggerAll(
                Entity(local1),
                Vector3f(local2.toFloat(), local4.toFloat(), local6.toFloat()),
                local9,
                event
            )

            if (event.isCancelled())
                iReturn(0)
        }
    }
}

fun injectRenderEntityPost() = inject {
    className = "net/minecraft/client/renderer/entity/RenderManager"
    methodName = "doRenderEntity"
    methodDesc = "(L$ENTITY;DDDFFZ)Z"
    at = At(InjectionPoint.RETURN(2))

    methodMaps = mapOf("func_147939_a" to "doRenderEntity")

    codeBlock {
        val local1 = shadowLocal<MCEntity>()
        val local2 = shadowLocal<Double>()
        val local4 = shadowLocal<Double>()
        val local6 = shadowLocal<Double>()
        val local9 = shadowLocal<Float>()

        code {
            TriggerType.PostRenderEntity.triggerAll(
                Entity(local1),
                Vector3f(local2.toFloat(), local4.toFloat(), local6.toFloat()),
                local9
            )
        }
    }
}
