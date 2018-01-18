package com.chattriggers.ctjs.minecraft.objects;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import com.chattriggers.ctjs.minecraft.wrappers.Player;
import com.chattriggers.ctjs.triggers.OnRegularTrigger;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class Gui extends GuiScreen {
    private OnRegularTrigger onDraw = null;
    private OnRegularTrigger onClick = null;
    private OnRegularTrigger onKeyTyped = null;
    private OnRegularTrigger onMouseReleased = null;
    private OnRegularTrigger onMouseDragged = null;
    private OnRegularTrigger onActionPerformed = null;

    private int mouseX = 0;
    private int mouseY = 0;

    /**
     * Displays the gui object to Minecraft.
     */
    public void open() {
        CTJS.getInstance().getGuiHandler().openGui(this);
    }

    /**
     * Closes this gui screen.
     */
    public void close() {
        if (Client.getMinecraft().currentScreen == this)
            Player.getPlayer().closeScreen();
    }

    /**
     * Get if the gui object is open.
     * @return true if this gui is open
     */
    public boolean isOpen() {
        return Client.getMinecraft().currentScreen == this;
    }

    /**
     * Registers a method to be ran while gui is open.
     * Registered method runs on draw.<br>
     * Arguments passed through to method:<br>
     *     int mouseX<br>
     *     int mouseY<br>
     *     float partialTicks
     * @param methodName the method to run
     * @return the trigger
     */
    public OnRegularTrigger registerDraw(String methodName) {
        onDraw = new OnRegularTrigger(methodName, TriggerType.OTHER);
        return onDraw;
    }

    /**
     * Registers a method to be ran while gui is open.
     * Registered method runs on mouse click.<br>
     * Arguments passed through to method:<br>
     *     int mouseX<br>
     *     int mouseY<br>
     *     int button
     * @param methodName the method to run
     * @return the trigger
     */
    public OnRegularTrigger registerClicked(String methodName) {
        onClick = new OnRegularTrigger(methodName, TriggerType.OTHER);
        return onClick;
    }

    /**
     * Registers a method to be ran while gui is open.
     * Registered method runs on key input.<br>
     * Arguments passed through to method:<br>
     *     char typed character<br>
     *     int key code
     * @param methodName the method to run
     * @return the trigger
     */
    public OnRegularTrigger registerKeyTyped(String methodName) {
        onKeyTyped = new OnRegularTrigger(methodName, TriggerType.OTHER);
        return onKeyTyped;
    }

    /**
     * Registers a method to be ran while gui is open.
     * Registered method runs on key input.
     * @param methodName the method to run
     * @return the trigger
     */
    public OnRegularTrigger registerMouseDragged(String methodName) {
        onMouseDragged = new OnRegularTrigger(methodName, TriggerType.OTHER);
        return onMouseDragged;
    }

    /**
     * Registers a method to be ran while gui is open.
     * Registered method runs on key input.
     * @param methodName the method to run
     * @return the trigger
     */
    public OnRegularTrigger registerMouseReleased(String methodName) {
        onMouseDragged = new OnRegularTrigger(methodName, TriggerType.OTHER);
        return onMouseDragged;
    }

    /**
     * Registers a method to be ran while gui is open.
     * Registered method runs on key input.
     * @param methodName the method to run
     * @return the trigger
     */
    public OnRegularTrigger registerActionPerformed(String methodName) {
        onActionPerformed = new OnRegularTrigger(methodName, TriggerType.OTHER);
        return onActionPerformed;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        super.mouseClicked(mouseX, mouseY, button);

        runOnClick(mouseX, mouseY, button);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);

        if (onMouseReleased != null)
            onMouseReleased.trigger(mouseX, mouseY, state);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (onActionPerformed != null)
            onActionPerformed.trigger(button.id);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

        if (onMouseDragged != null)
            onMouseDragged.trigger(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int i = Mouse.getEventDWheel();
        if (i == 0) return;

        if (i > 0) runOnClick(this.mouseX, this.mouseY, -1);
        if (i < 0) runOnClick(this.mouseX, this.mouseY, -2);
    }

    // helper method for running onClick
    private void runOnClick(int mouseX, int mouseY, int button) {
        if (onClick != null)
            onClick.trigger(mouseX, mouseY, button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        if (onDraw != null)
            onDraw.trigger(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        if (onKeyTyped != null)
            onKeyTyped.trigger(typedChar, keyCode);
    }

    /**
     * Add a base minecraft button to the gui
     * @param buttonId id for the button
     * @param x X position of the button
     * @param y Y position of the button
     * @param buttonText the label of the button
     */
    public void addButton(int buttonId, int x, int y, String buttonText) {
        this.buttonList.add(new GuiButton(buttonId, x, y, buttonText));
    }

    /**
     * Add a base minecraft button to the gui
     * @param buttonId id for the button
     * @param x X position of the button
     * @param y Y position of the button
     * @param width the width of the button
     * @param height the height of the button
     * @param buttonText the label of the button
     */
    public void addButton(int buttonId, int x, int y, int width, int height, String buttonText) {
        this.buttonList.add(new GuiButton(buttonId, x, y, width, height, buttonText));
    }

    public void setButtonVisibility(int buttonId, boolean visible) {
        for (GuiButton button : this.buttonList) {
            if (button.id == buttonId) {
                button.visible = visible;
                break;
            }
        }
    }
}
