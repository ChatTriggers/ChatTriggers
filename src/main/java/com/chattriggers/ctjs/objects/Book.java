package com.chattriggers.ctjs.objects;

import com.chattriggers.ctjs.utils.console.Console;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class Book {
    private GuiScreenBook bookScreen;
    private ItemStack book;
    private NBTTagCompound bookData;

    public Book(String bookName) {
        book = new ItemStack(Items.written_book);

        bookData = new NBTTagCompound();
        bookData.setTag("author", new NBTTagString(Minecraft.getMinecraft().getSession().getUsername()));
        bookData.setTag("title", new NBTTagString("CT-" + bookName));
        bookData.setTag("pages", new NBTTagList());

        book.setTagCompound(bookData);
    }

    /**
     * Add a page to the book.
     * @param message the entire message for what the page should be
     * @return the current book to allow method chaining
     */
    public Book addPage(Message message) {
        NBTTagList pages = (NBTTagList) bookData.getTag("pages");

        pages.appendTag(new NBTTagString(IChatComponent.Serializer.componentToJson(message.getChatMessage())));

        updateBookScreen(pages);

        return this;
    }

    /**
     * Overloaded method for adding a simple page to the book.
     * @param message a simple string to make the page
     * @return the current book to allow method chaining
     */
    public Book addPage(String message) {
        addPage(new Message(message));

        return this;
    }

    /**
     * Sets a page of the book to the specified message.
     * @param pageNumber the number of the page to set
     * @param message the message to set the page to
     * @return the current book to allow method chaining
     */
    public Book setPage(int pageNumber, Message message) {
        NBTTagList pages = (NBTTagList) bookData.getTag("pages");

        pages.set(pageNumber, new NBTTagString(IChatComponent.Serializer.componentToJson(message.getChatMessage())));

        updateBookScreen(pages);

        return this;
    }

    public void updateBookScreen(NBTTagList pages) {
        bookData.removeTag("pages");
        bookData.setTag("pages", pages);
        book.setTagCompound(bookData);

        if (bookScreen != null) {
            ReflectionHelper.setPrivateValue(GuiScreenBook.class, bookScreen, pages, "field_146483_y", "bookPages");
        }
    }

    public void display(int page) {
        if (bookScreen == null) {
            bookScreen = new GuiScreenBook(Minecraft.getMinecraft().thePlayer, book, false);
        }

        ReflectionHelper.setPrivateValue(GuiScreenBook.class, bookScreen, page, "currPage", "field_146484_x");

        new Thread(() -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Console.getConsole().printStackTrace(e);
            }
            Minecraft.getMinecraft().displayGuiScreen(bookScreen);
        }).start();
    }

    public void display() {
        display(0);
    }

    public boolean isOpen() {
        return bookScreen != null && Minecraft.getMinecraft().currentScreen == bookScreen;

    }

    public int getCurrentPage() {
        if (!isOpen()) return -1;

        return ReflectionHelper.getPrivateValue(GuiScreenBook.class, bookScreen, "currPage", "field_146484_x");
    }

}
