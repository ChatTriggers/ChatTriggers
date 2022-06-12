package com.chattriggers.ctjs.launch.mixins.transformers;

//#if MC<=11202
import com.chattriggers.ctjs.minecraft.listeners.events.SoundEvent;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundEventAccessorComposite;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.vector.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundManager.class)
public class SoundManagerMixin {
    @Final
    @Shadow
    public SoundHandler sndHandler;

    @Inject(
            method = "playSound",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/audio/ISound;getSoundLocation()Lnet/minecraft/util/ResourceLocation;",
                ordinal = 1
            ),
            cancellable = true
    )
    private void chattriggers_playSoundTrigger(ISound p_sound, CallbackInfo ci) {
        Vector3f pos = new Vector3f(p_sound.getXPosF(), p_sound.getYPosF(), p_sound.getZPosF());
        ResourceLocation location = p_sound.getSoundLocation();
        String name = location.getResourcePath();
        SoundEventAccessorComposite accessor = sndHandler.getSound(location);
        SoundCategory category = accessor.getSoundCategory();

        SoundEvent event = new SoundEvent(p_sound);

        TriggerType.SoundPlay.triggerAll(
                pos,
                name,
                p_sound.getVolume(),
                p_sound.getPitch(),
                category == null ? null : category.getCategoryName(),
                event
        );

        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
//#endif
