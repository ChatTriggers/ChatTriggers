package com.chattriggers.ctjs.mixins;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface MinecraftAccessor {
    //#if MC<=11202
    @Accessor("debugFPS")
    //#elseif MC>=11701
    //$$ @Accessor("fps")
    //#endif
    static int getFps() {
        throw new IllegalStateException("Mixin not applied!");
    }
}
