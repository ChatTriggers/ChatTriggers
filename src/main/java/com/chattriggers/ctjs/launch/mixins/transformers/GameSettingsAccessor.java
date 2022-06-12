package com.chattriggers.ctjs.launch.mixins.transformers;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameSettings.class)
public interface GameSettingsAccessor {
    @Final
    @Mutable
    @Accessor
    //#if MC<=11202
    void setKeyBindings(KeyBinding[] keyBindings);
    //#elseif MC>=11701
    //$$ void setKeyMappings(KeyMapping[] keyBindings);
    //#endif
}
