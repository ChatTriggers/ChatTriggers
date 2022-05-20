package com.chattriggers.ctjs.launch.mixins.transformers.gui;

import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.nbt.NBTTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC<=11202
@Mixin(GuiScreenBook.class)
public interface GuiScreenBookAccessor {
    @Accessor
    int getCurrPage();

    @Accessor
    void setCurrPage(int page);

    @Accessor
    NBTTagList getBookPages();

    @Accessor
    void setBookPages(NBTTagList list);
}
//#endif
