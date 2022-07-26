package com.chattriggers.ctjs.mixins.gui;

import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.nbt.NBTTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC>=11701
//$$ import java.util.List;
//#endif

@Mixin(GuiScreenBook.class)
public interface GuiScreenBookAccessor {
    //#if MC<=11202
    @Accessor
    //#elseif MC>=11701
    //$$ @Accessor("currentPage")
    //#endif
    int getCurrPage();

    //#if MC<=11202
    @Accessor
    //#elseif MC>=11701
    //$$ @Accessor("currentPage")
    //#endif
    void setCurrPage(int page);

    //#if MC<=11202
    @Accessor
    void setBookPages(NBTTagList list);
    //#elseif MC>=11701
    //$$ @Accessor("pages")
    //$$ void setBookPages(List<String> pages);
    //#endif
}
