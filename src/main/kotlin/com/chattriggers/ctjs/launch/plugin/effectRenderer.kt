package com.chattriggers.ctjs.launch.plugin

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent
import com.chattriggers.ctjs.minecraft.wrappers.objects.Particle
import com.chattriggers.ctjs.triggers.TriggerType
import dev.falsehonesty.asmhelper.dsl.At
import dev.falsehonesty.asmhelper.dsl.InjectionPoint
import dev.falsehonesty.asmhelper.dsl.code.CodeBlock.Companion.aReturn
import dev.falsehonesty.asmhelper.dsl.inject
import dev.falsehonesty.asmhelper.dsl.instructions.*
import net.minecraft.client.particle.EntityFX
import net.minecraft.util.EnumParticleTypes

fun injectEffectRenderer() = inject {
    className = EFFECT_RENDERER
    methodName = "spawnEffectParticle"
    methodDesc = "(IDDDDDD[I)L$ENTITY_FX;"
    at = At(
        InjectionPoint.INVOKE(
            Descriptor(
                EFFECT_RENDERER,
                "addEffect",
                "(L$ENTITY_FX;)V"
            )
        )
    )

    methodMaps = mapOf(
        "getParticleFromId" to "func_179342_a",
        "func_178927_a" to "spawnEffectParticle",
        "func_78873_a" to "addEffect"
    )

    codeBlock {
        val local1 = shadowLocal<Int>()
        val local16 = shadowLocal<EntityFX>()

        code {
            val event = CancellableEvent()
            TriggerType.SpawnParticle.triggerAll(Particle(local16), EnumParticleTypes.getParticleFromId(local1), event)
            if (event.isCancelled())
                aReturn(null)
        }
    }
}
