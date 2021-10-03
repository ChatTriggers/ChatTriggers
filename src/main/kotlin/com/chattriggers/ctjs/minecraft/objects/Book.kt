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
import net.minecraft.client.gui.GuiScreenBook
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.IChatComponent

@External
class Book(bookName: String) {
    private var bookScreen: GuiScreenBook? = null
    //#if MC<=10809
    private val book = ItemStack(Items.written_book)
    //#else
    //$$ private val book = ItemStack(Items.WRITTEN_BOOK)
    //#endif

    private val bookData: NBTTagCompound = NBTTagCompound(MCNBTTagCompound())

    init {
        bookData["author"] = makeNBTString(Player.getName())
        bookData["title"] = makeNBTString("CT-$bookName")
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
            makeNBTString(
                IChatComponent.Serializer.componentToJson(
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

        pages[pageNumber] = makeNBTString(
            IChatComponent.Serializer.componentToJson(
                    message.getChatMessage()
            )
        )

        updateBookScreen(pages)
    }

    fun updateBookScreen(pages: NBTTagList) {
        bookData.removeTag("pages")
        bookData["pages"] = pages
        book.tagCompound = bookData.rawNBT

        // TODO(1.16.2): Does this work (without a flicker)?
        //#if MC==11602
        //$$ bookScreen = ReadBookScreen(ReadBookScreen.IBookInfo.func_216917_a(book))
        //#else
        bookScreen?.bookPages = pages.rawNBT
        //#endif
    }

    @JvmOverloads
    fun display(page: Int = 0) {
        //#if MC==11602
        //$$ bookScreen = ReadBookScreen(ReadBookScreen.IBookInfo.func_216917_a(book))
        //$$ bookScreen!!.showPage(page)
        //#else
        bookScreen = GuiScreenBook(Player.getPlayer()!!, book, false)
        bookScreen!!.currPage = page
        //#endif

        GuiHandler.openGui(bookScreen ?: return)
    }

    fun isOpen(): Boolean {
        return Client.getMinecraft().currentScreen === bookScreen
    }

    // TODO(1.16.2)
    //#if MC==10809
    fun getCurrentPage(): Int {
        return if (!isOpen() || bookScreen == null) -1 else bookScreen!!.currPage
    }
    //#endif

    private fun makeNBTString(text: String): MCNBTTagString {
        //#if MC==11602
        //$$ return MCNBTTagString.valueOf(text)
        //#else
        return MCNBTTagString(text)
        //#endif
    }
}
