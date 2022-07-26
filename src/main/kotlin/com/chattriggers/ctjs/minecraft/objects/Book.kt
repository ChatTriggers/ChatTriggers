package com.chattriggers.ctjs.minecraft.objects

import com.chattriggers.ctjs.mixins.gui.GuiScreenBookAccessor
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.inventory.nbt.NBTTagCompound
import com.chattriggers.ctjs.minecraft.wrappers.inventory.nbt.NBTTagList
import com.chattriggers.ctjs.utils.kotlin.*
import gg.essential.api.utils.GuiUtil
import gg.essential.universal.wrappers.message.UMessage
import net.minecraft.client.gui.GuiScreenBook
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent

//#if MC>=11701
//$$ import net.minecraft.world.InteractionHand
//$$ import net.minecraft.client.gui.screens.inventory.BookViewScreen
//#endif

class Book(bookName: String) {
    //#if MC<=11202
    private var bookScreen: GuiScreenBook? = null
    //#elseif MC>=11701
    //$$ private var bookScreen: BookViewScreen? = null
    //#endif
    private val book: ItemStack = let {
        //#if MC<=10809
        ItemStack(Items.written_book)
        //#else
        //$$ ItemStack(Items.WRITTEN_BOOK)
        //#endif
    }
    private val bookData: NBTTagCompound = NBTTagCompound(net.minecraft.nbt.NBTTagCompound())

    init {
        bookData["author"] = makeStringNbtTag(Player.getName())
        bookData["title"] = makeStringNbtTag("CT-$bookName")
        bookData["pages"] = net.minecraft.nbt.NBTTagList()

        //#if MC<=11202
        book.tagCompound = bookData.rawNBT
        //#else
        //$$ book.tag = bookData.rawNBT
        //#endif
    }

    /**
     * Add a page to the book.
     *
     * @param message the entire message for what the page should be
     * @return the current book to allow method chaining
     */
    fun addPage(message: UMessage) = apply {
        val data = bookData.get("pages", NBTTagCompound.NBTDataType.TAG_LIST, 8) ?: return@apply
        val pages = data as NBTTagList

        val text = ChatComponentText("")
        message.messageParts.map { it.component as ChatComponentText }.forEach {
            //#if MC<=11202
            text.appendSibling(it)
            //#elseif MC>=11701
            //$$ text.append(it)
            //#endif
        }

        pages.appendTag(makeStringNbtTag(componentToJson(text)))
        updateBookScreen(pages)
    }

    /**
     * Overloaded method for adding a simple page to the book.
     *
     * @param message a simple string to make the page
     * @return the current book to allow method chaining
     */
    fun addPage(message: String) = apply {
        addPage(UMessage(message))
    }

    /**
     * Sets a page of the book to the specified message.
     *
     * @param pageIndex the index of the page to set
     * @param message the message to set the page to
     * @return the current book to allow method chaining
     */
    fun setPage(pageIndex: Int, message: UMessage) = apply {
        val data = bookData.get("pages", NBTTagCompound.NBTDataType.TAG_LIST, 8) ?: return@apply
        val pages = data as NBTTagList

        for (i in pages.tagCount..pageIndex)
            addPage("")

        val text = ChatComponentText("")
        message.messageParts.map { it.component as ChatComponentText }.forEach {
            //#if MC<=11202
            text.appendSibling(it)
            //#elseif MC>=11701
            //$$ text.append(it)
            //#endif
        }

        pages[pageIndex] = makeStringNbtTag(componentToJson(text))

        updateBookScreen(pages)
    }

    fun updateBookScreen(pages: NBTTagList) {
        bookData.removeTag("pages")
        bookData["pages"] = pages
        //#if MC<=11202
        book.tagCompound = bookData.rawNBT
        //#else
        //$$ book.tag = bookData.rawNBT
        //#endif

        bookScreen?.asMixin<GuiScreenBookAccessor>()?.setBookPages(
            pages.rawNBT
            //#if MC>=11701
            //$$ .map { it.asString }
            //#endif
        )
    }

    @JvmOverloads
    fun display(pageIndex: Int = 0) {
        //#if MC<=11202
        bookScreen = GuiScreenBook(Player.getPlayer()!!, book, false)
        bookScreen!!.asMixin<GuiScreenBookAccessor>().currPage = pageIndex
        //#else
        //$$ bookScreen = BookViewScreen(BookViewScreen.WrittenBookAccess(book))
        //$$ bookScreen!!.setPage(pageIndex)
        //#endif
        GuiUtil.open(bookScreen ?: return)
    }

    fun isOpen(): Boolean {
        return GuiUtil.getOpenedScreen() === bookScreen
    }

    fun getCurrentPage(): Int {
        return if (isOpen() && bookScreen != null) {
            bookScreen!!.asMixin<GuiScreenBookAccessor>().currPage
        } else -1
    }

    private fun makeStringNbtTag(value: String): net.minecraft.nbt.NBTTagString {
        //#if MC<=11202
        return net.minecraft.nbt.NBTTagString(value)
        //#elseif MC>=11701
        //$$ return net.minecraft.nbt.StringTag.valueOf(value)
        //#endif
    }

    private fun componentToJson(component: IChatComponent): String {
        //#if MC<=11202
        return IChatComponent.Serializer.componentToJson(component)
        //#elseif MC>=11701
        //$$ return Component.Serializer.toJson(component)
        //#endif
    }
}
