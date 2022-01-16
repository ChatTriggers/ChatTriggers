package com.chattriggers.ctjs.minecraft.objects

import com.chattriggers.ctjs.launch.mixins.asMixin
import com.chattriggers.ctjs.launch.mixins.transformers.BookScreenAccessor
import com.chattriggers.ctjs.minecraft.objects.message.Message
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.nbt.NBTTagCompound
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.nbt.NBTTagList
import com.chattriggers.ctjs.utils.kotlin.*
import gg.essential.api.utils.GuiUtil
import net.minecraft.client.gui.screen.ingame.BookScreen
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtString

@External
class Book(bookName: String) {
    private var bookScreen: BookScreen? = null
    private val book: ItemStack = ItemStack(Items.WRITTEN_BOOK)
    private val bookData: NBTTagCompound = NBTTagCompound(NbtCompound())

    init {
        bookData["author"] = NbtString.of(Player.getName())
        bookData["title"] = NbtString.of("CT-$bookName")
        bookData["pages"] = NbtList()

        book.nbt = bookData.rawNBT
    }

    /**
     * Add a page to the book.
     *
     * @param message the entire message for what the page should be
     * @return the current book to allow method chaining
     */
    fun addPage(message: Message) = apply {
        val pages = bookData.get("pages", NBTTagCompound.NBTDataType.TAG_LIST, 8)?.let {
            NBTTagList(it as NbtList)
        } ?: return@apply

        pages.appendTag(NbtString.of(message.getFormattedText()))
        updateBookScreen(pages)
    }

    /**
     * Overloaded method for adding a simple page to the book.
     *
     * @param message a simple string to make the page
     * @return the current book to allow method chaining
     */
    fun addPage(message: String) = apply {
        addPage(Message(message))
    }

    /**
     * Sets a page of the book to the specified message.
     *
     * @param pageNumber the number of the page to set
     * @param message the message to set the page to
     * @return the current book to allow method chaining
     */
    fun setPage(pageNumber: Int, message: Message) = apply {
        val pages = bookData.get("pages", NBTTagCompound.NBTDataType.TAG_LIST, 8)?.let {
            NBTTagList(it as NbtList)
        } ?: return@apply

        pages[pageNumber] = NbtString.of(message.getFormattedText())
        updateBookScreen(pages)
    }

    fun updateBookScreen(pages: NBTTagList) {
        bookData.removeTag("pages")
        bookData["pages"] = pages
        book.nbt = bookData.rawNBT
        bookScreen?.setPageProvider(BookScreen.Contents.create(book))
    }

    @JvmOverloads
    fun display(page: Int = 0) {
        bookScreen = BookScreen(BookScreen.Contents.create(book))
        bookScreen!!.setPage(page)
        GuiUtil.open(bookScreen ?: return)
    }

    fun isOpen(): Boolean {
        return Client.getMinecraft().currentScreen === bookScreen
    }

    fun getCurrentPage(): Int {
        return if (!isOpen() || bookScreen == null) -1 else bookScreen!!.asMixin<BookScreenAccessor>().pageIndex
    }
}
