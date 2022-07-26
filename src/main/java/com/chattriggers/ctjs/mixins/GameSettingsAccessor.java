package com.chattriggers.ctjs.mixins;

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
    //#if MC<=11202
    @Accessor
    //#elseif MC>=11701
    //$$ @Accessor("keyMappings")
    //#endif
    void setKeyBindings(KeyBinding[] keyBindings);
}
