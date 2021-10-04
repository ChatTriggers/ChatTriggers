package com.chattriggers.ctjs.launch.mixins.transformers

import net.minecraft.client.gui.GuiScreenBook
import net.minecraft.nbt.NBTTagList
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

@Mixin(GuiScreenBook::class)
interface IGuiScreenBookAccessor {
    //#if MC==10809
    @Accessor
    fun setBookPages(pages: NBTTagList)
    //#endif

    @Accessor
    fun getCurrPage(): Int

    @Accessor
    fun setCurrPage(currPage: Int)
}

fun GuiScreenBook.asMixinAccessor() = this as IGuiScreenBookAccessor