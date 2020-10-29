package com.chattriggers.ctjs.minecraft.objects

import com.chattriggers.ctjs.minecraft.objects.gui.GuiHandler
import com.chattriggers.ctjs.minecraft.objects.message.Message
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.nbt.NBTTagCompound
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.nbt.NBTTagList
import com.chattriggers.ctjs.utils.kotlin.*
import com.chattriggers.ctjs.utils.kotlin.MCNBTTagCompound
import com.chattriggers.ctjs.utils.kotlin.MCNBTTagString
import com.chattriggers.ctjs.utils.kotlin.TextComponentSerializer
import net.minecraft.client.gui.GuiScreenBook
import net.minecraft.init.Items
import net.minecraft.item.ItemStack

@External
class Book(bookName: String) {
    private var bookScreen: GuiScreenBook? = null
    private val book: ItemStack
    //#if MC<=10809
            = ItemStack(Items.written_book)
    //#else
    //$$ = ItemStack(Items.WRITTEN_BOOK)
    //#endif
    private val bookData: NBTTagCompound = NBTTagCompound(MCNBTTagCompound())

    init {
        bookData["author"] = MCNBTTagString(Player.getName())
        bookData["title"] = MCNBTTagString("CT-$bookName")
        bookData["pages"] = MCNBTTagList()

        book.tagCompound = bookData.rawNBT
    }

    /**
     * Add a page to the book.
     *
     * @param message the entire message for what the page should be
     * @return the current book to allow method chaining
     */
    fun addPage(message: Message) = apply {
        val pages = NBTTagList((bookData.get("pages", NBTTagCompound.NBTDataType.TAG_LIST, 8) ?: return@apply) as MCNBTTagList)
        pages.appendTag(
            MCNBTTagString(
                TextComponentSerializer.componentToJson(
                    message.getChatMessage()
                )
            )
        )

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
     * @param message    the message to set the page to
     * @return the current book to allow method chaining
     */
    fun setPage(pageNumber: Int, message: Message) = apply {
        val pages = NBTTagList((bookData.get("pages", NBTTagCompound.NBTDataType.TAG_LIST, 8) ?: return@apply) as MCNBTTagList)

        pages[pageNumber] = MCNBTTagString(
            TextComponentSerializer.componentToJson(
                    message.getChatMessage()
            )
        )

        updateBookScreen(pages)
    }

    fun updateBookScreen(pages: NBTTagList) {
        bookData.removeTag("pages")
        bookData["pages"] = pages
        book.tagCompound = bookData.rawNBT
        bookScreen?.bookPages = pages.rawNBT
    }

    @JvmOverloads
    fun display(page: Int = 0) {
        bookScreen = GuiScreenBook(Player.getPlayer(), book, false)

        bookScreen!!.currPage = page
        GuiHandler.openGui(bookScreen ?: return)
    }

    fun isOpen(): Boolean {
        return Client.getMinecraft().currentScreen === bookScreen
    }

    fun getCurrentPage(): Int {
        return if (!isOpen() || bookScreen == null) -1 else bookScreen!!.currPage
    }
}