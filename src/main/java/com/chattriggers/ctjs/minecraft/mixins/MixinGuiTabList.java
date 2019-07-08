package com.chattriggers.ctjs.minecraft.mixins;

import net.minecraft.client.gui.GuiPlayerTabOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC<=10809
import net.minecraft.util.IChatComponent;
//#else
//$$ import net.minecraft.util.text.ITextComponent;
//#endif

@Mixin(GuiPlayerTabOverlay.class)
public interface MixinGuiTabList {
    @Accessor(value = "header")
    //#if MC<=10809
    IChatComponent getHeader();
    //#else
    //$$ ITextComponent getHeader();
    //#endif

    @Accessor(value = "footer")
    //#if MC<=10809
    IChatComponent getFooter();
    //#else
    //$$ ITextComponent getFooter();
    //#endif
}
