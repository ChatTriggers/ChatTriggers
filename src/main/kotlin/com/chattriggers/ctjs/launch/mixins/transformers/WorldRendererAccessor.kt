package com.chattriggers.ctjs.launch.mixins.transformers

import com.chattriggers.ctjs.utils.kotlin.MCParticle
import net.minecraft.client.render.WorldRenderer
import net.minecraft.particle.ParticleEffect
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Invoker

@Mixin(WorldRenderer::class)
interface WorldRendererAccessor {
    @Invoker
    fun spawnParticle(
        parameters: ParticleEffect,
        alwaysSpawn: Boolean,
        canSpawnOnMinimal: Boolean,
        x: Double,
        y: Double,
        z: Double,
        velocityX: Double,
        velocityY: Double,
        velocityZ: Double,
    ): MCParticle
}
