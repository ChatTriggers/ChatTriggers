package com.chattriggers.ctjs.launch.mixins.transformers;

import net.minecraft.client.gui.screen.ingame.BookScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BookScreen.class)
public interface BookScreenAccessor {
    @Accessor
    int getPageIndex();
}
