package com.chattriggers.ctjs.launch.mixins.transformers.render;

//#if MC>=11701
//$$ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
//$$ import net.minecraft.client.particle.Particle;
//$$ import net.minecraft.client.renderer.LevelRenderer;
//$$ import net.minecraft.core.particles.ParticleOptions;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.gen.Accessor;
//$$ import org.spongepowered.asm.mixin.gen.Invoker;
//$$
//$$ import java.util.Set;
//$$
//$$ @Mixin(LevelRenderer.class)
//$$ public interface LevelRendererAccessor {
//$$     @Invoker
//$$     Particle invokeAddParticleInternal(
//$$             ParticleOptions options,
//$$             boolean alwaysCreateParticle,
//$$             boolean ignoreParticleStatus,
//$$             double x,
//$$             double y,
//$$             double z,
//$$             double xSpeed,
//$$             double ySpeed,
//$$             double zSpeed
//$$     );
//$$
//$$     @Accessor
//$$     ObjectArrayList<LevelRenderer.RenderChunkInfo> getRenderChunks();
//$$ }
//#endif