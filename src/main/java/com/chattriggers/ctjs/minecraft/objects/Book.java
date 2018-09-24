//package com.chattriggers.ctjs.minecraft.objects;
//
//import com.chattriggers.ctjs.minecraft.objects.message.Message;
//import com.chattriggers.ctjs.minecraft.wrappers.Client;
//import com.chattriggers.ctjs.minecraft.wrappers.Player;
//import com.chattriggers.ctjs.utils.console.Console;
//import net.minecraft.client.gui.GuiScreenBook;
//import net.minecraft.init.Items;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.nbt.NBTTagList;
//import net.minecraft.nbt.NBTTagString;
//import net.minecraftforge.fml.relauncher.ReflectionHelper;
//
////#if MC<=10809
//import net.minecraft.util.IChatComponent;
////#else
////$$ import net.minecraft.util.text.ITextComponent;
////#endif
//
//public class Book {
//    private GuiScreenBook bookScreen;
//    private ItemStack book;
//    private NBTTagCompound bookData;
//
//    public Book(String bookName) {
//        //#if MC<=10809
//        book = new ItemStack(Items.written_book);
//        //#else
//        //$$ book = new ItemStack(Items.WRITTEN_BOOK);
//        //#endif
//
//        bookData = new NBTTagCompound();
//        bookData.setTag("author", new NBTTagString(Client.getMinecraft().getSession().getUsername()));
//        bookData.setTag("title", new NBTTagString("CT-" + bookName));
//        bookData.setTag("pages", new NBTTagList());
//
//        book.setTagCompound(bookData);
//    }
//
//    /**
//     * Add a page to the book.
//     *
//     * @param message the entire message for what the page should be
//     * @return the current book to allow method chaining
//     */
//    public Book addPage(Message message) {
//        NBTTagList pages = (NBTTagList) bookData.getTag("pages");
//
//        pages.appendTag(new NBTTagString(
//                //#if MC<=10809
//                IChatComponent.Serializer.componentToJson(message.getChatMessage())
//                //#else
//                //$$ ITextComponent.Serializer.componentToJson(message.getChatMessage())
//                //#endif
//        ));
//
//        updateBookScreen(pages);
//
//        return this;
//    }
//
//    /**
//     * Overloaded method for adding a simple page to the book.
//     *
//     * @param message a simple string to make the page
//     * @return the current book to allow method chaining
//     */
//    public Book addPage(String message) {
//        addPage(new Message(message));
//
//        return this;
//    }
//
//    /**
//     * Sets a page of the book to the specified message.
//     *
//     * @param pageNumber the number of the page to set
//     * @param message    the message to set the page to
//     * @return the current book to allow method chaining
//     */
//    public Book setPage(int pageNumber, Message message) {
//        NBTTagList pages = (NBTTagList) bookData.getTag("pages");
//
//        pages.set(pageNumber, new NBTTagString(
//                //#if MC<=10809
//                IChatComponent.Serializer.componentToJson(message.getChatMessage())
//                //#else
//                //$$ ITextComponent.Serializer.componentToJson(message.getChatMessage())
//                //#endif
//        ));
//
//        updateBookScreen(pages);
//
//        return this;
//    }
//
//    public void updateBookScreen(NBTTagList pages) {
//        bookData.removeTag("pages");
//        bookData.setTag("pages", pages);
//        book.setTagCompound(bookData);
//
//        if (bookScreen != null) {
//            ReflectionHelper.setPrivateValue(GuiScreenBook.class, bookScreen, pages, "field_146483_y", "bookPages");
//        }
//    }
//
//    public void display(int page) {
//        if (bookScreen == null) {
//            bookScreen = new GuiScreenBook(Player.getPlayer(), book, false);
//        }
//
//        ReflectionHelper.setPrivateValue(GuiScreenBook.class, bookScreen, page, "currPage", "field_146484_x");
//
//        new Thread(() -> {
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                Console.getInstance().printStackTrace(e);
//            }
//            Client.getMinecraft().displayGuiScreen(bookScreen);
//        }).start();
//    }
//
//    public void display() {
//        display(0);
//    }
//
//    public boolean isOpen() {
//        return bookScreen != null && Client.getMinecraft().currentScreen == bookScreen;
//
//    }
//
//    public int getCurrentPage() {
//        if (!isOpen()) return -1;
//
//        return ReflectionHelper.getPrivateValue(GuiScreenBook.class, bookScreen, "currPage", "field_146484_x");
//    }
//
//}
