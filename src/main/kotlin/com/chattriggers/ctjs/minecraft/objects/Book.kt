package com.chattriggers.ctjs.minecraft.objects

import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.inventory.nbt.NBTTagCompound
import com.chattriggers.ctjs.minecraft.wrappers.inventory.nbt.NBTTagList
import com.chattriggers.ctjs.utils.kotlin.*
import gg.essential.api.utils.GuiUtil
import gg.essential.universal.wrappers.message.UMessage
import gg.essential.universal.wrappers.message.UTextComponent
import net.minecraft.client.gui.GuiScreenBook
import net.minecraft.init.Items
import net.minecraft.item.ItemStack

//#if MC>=11701
//$$ import net.minecraft.world.InteractionHand
//$$ import com.chattriggers.ctjs.launch.mixins.transformers.BookEditScreenAccessor
//#endif

class Book(bookName: String) {
    private var bookScreen: GuiScreenBook? = null
    private val book: ItemStack = let {
        //#if MC<=10809
        ItemStack(Items.written_book)
        //#else
        //$$ ItemStack(Items.WRITTEN_BOOK)
        //#endif
    }
    private val bookData: NBTTagCompound = NBTTagCompound(MCNBTTagCompound())

    init {
        bookData["author"] = makeStringNbtTag(Player.getName())
        bookData["title"] = makeStringNbtTag("CT-$bookName")
        bookData["pages"] = MCNBTTagList()

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
        val pages = NBTTagList(data as MCNBTTagList)
        pages.appendTag(makeStringNbtTag(componentToJson(message.chatMessage)))
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
        val pages = NBTTagList(data as MCNBTTagList)

        for (i in pages.tagCount..pageIndex)
            addPage("")

        pages[pageIndex] = makeStringNbtTag(componentToJson(message.chatMessage))

        updateBookScreen(pages)
    }

    fun updateBookScreen(pages: NBTTagList) {
        bookData.removeTag("pages")
        bookData["pages"] = pages
        //#if MC<=11202
        book.tagCompound = bookData.rawNBT
        bookScreen?.bookPages = pages.rawNBT
        //#else
        //$$ book.tag = bookData.rawNBT
        //$$ // TODO(VERIFY)
        //$$ bookScreen?.asMixin<BookEditScreenAccessor>()?.pages = pages.rawNBT.map { it.asString }
        //#endif
    }

    @JvmOverloads
    fun display(pageIndex: Int = 0) {
        //#if MC<=11202
        bookScreen = GuiScreenBook(Player.getPlayer()!!, book, false)
        bookScreen!!.currPage = pageIndex
        //#else
        //$$ bookScreen = BookEditScreen(Player.getPlayer()!!, book, InteractionHand.MAIN_HAND)
        //$$ bookScreen!!.asMixin<BookEditScreenAccessor>().currentPage = pageIndex
        //#endif
        GuiUtil.open(bookScreen ?: return)
    }

    fun isOpen(): Boolean {
        return GuiUtil.getOpenedScreen() === bookScreen
    }

    fun getCurrentPage(): Int {
        return if (isOpen() && bookScreen != null) {
            //#if MC<=11202
            bookScreen!!.currPage
            //#else
            //$$ bookScreen!!.asMixin<BookEditScreenAccessor>().currentPage
            //#endif
        } else -1
    }

    private fun makeStringNbtTag(value: String): MCNBTTagString {
        //#if MC<=11202
        return MCNBTTagString(value)
        //#else
        //$$ return MCNBTTagString.valueOf(value)
        //#endif
    }

    private fun componentToJson(component: UTextComponent): String {
        //#if MC<=11202
        return MCTextComponentSerializer.componentToJson(component)
        //#else
        //$$ return MCTextComponentSerializer.toJson(component)
        //#endif
    }
}
