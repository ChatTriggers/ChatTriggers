package com.chattriggers.ctjs.launch.plugin

// TODO(1.16.2)
//#if MC==10809
import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent
import com.chattriggers.ctjs.minecraft.wrappers.objects.Entity
import com.chattriggers.ctjs.triggers.TriggerType
import dev.falsehonesty.asmhelper.dsl.At
import dev.falsehonesty.asmhelper.dsl.InjectionPoint
import dev.falsehonesty.asmhelper.dsl.code.CodeBlock.Companion.iReturn
import dev.falsehonesty.asmhelper.dsl.inject
import org.lwjgl.util.vector.Vector3f

fun injectRenderManager() = inject {
    className = "net/minecraft/client/renderer/entity/RenderManager"
    methodName = "doRenderEntity"
    methodDesc = "(L$ENTITY;DDDFFZ)Z"
    at = At(InjectionPoint.HEAD)

    methodMaps = mapOf("func_147939_a" to "doRenderEntity")

    codeBlock {
        val local1 = shadowLocal<net.minecraft.entity.Entity>()
        val local2 = shadowLocal<Double>()
        val local4 = shadowLocal<Double>()
        val local6 = shadowLocal<Double>()
        val local9 = shadowLocal<Float>()

        code {
            val event = CancellableEvent()
            TriggerType.RENDER_ENTITY.triggerAll(
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
//#endif
