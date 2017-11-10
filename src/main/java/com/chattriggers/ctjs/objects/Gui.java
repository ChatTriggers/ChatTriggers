package com.chattriggers.ctjs.objects;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.triggers.OnTrigger;
import com.chattriggers.ctjs.triggers.TriggerType;
import com.chattriggers.ctjs.utils.console.Console;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import javax.script.ScriptException;
import java.io.IOException;

public class Gui extends GuiScreen {
    private OnTrigger onDraw = null;
    private OnTrigger onClick = null;
    private OnTrigger onKeyTyped = null;

    private int mouseX = 0;
    private int mouseY = 0;

    public Gui() {
    }

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
        if (Minecraft.getMinecraft().currentScreen == this)
            Minecraft.getMinecraft().thePlayer.closeScreen();
    }

    /**
     * Get if the gui object is open.
     * @return true if this gui is open
     */
    public boolean isOpen() {
        return Minecraft.getMinecraft().currentScreen == this;
    }

    /**
     * Registers a method to be ran while gui is open.
     * Registered method runs on draw.
     * @param methodName the method to run
     * @return the trigger
     */
    public OnTrigger registerDraw(String methodName) {
        return onDraw = new OnTrigger(methodName, TriggerType.OTHER) {
            @Override
            public void trigger(Object... args) {
                if (!(args[0] instanceof Integer
                        && args[1] instanceof Integer
                        && args[2] instanceof Float)) {
                    throw new IllegalArgumentException("Arguments must be of type int, int, float");
                }

                int mouseX = (int) args[0];
                int mouseY = (int) args[1];
                float partialTicks = (float) args[2];

                try {
                    CTJS.getInstance().getModuleManager().invokeFunction(methodName, mouseX, mouseY, partialTicks);
                } catch (ScriptException | NoSuchMethodException e) {
                    onDraw = null;
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * Registers a method to be ran while gui is open.
     * Registered method runs on mouse click.
     * @param methodName the method to run
     * @return the trigger
     */
    public OnTrigger registerClicked(String methodName) {
        return onClick = new OnTrigger(methodName, TriggerType.OTHER) {
            @Override
            public void trigger(Object... args) {
                if (!(args[0] instanceof Integer
                        && args[1] instanceof Integer
                        && args[2] instanceof Integer)) {
                    throw new IllegalArgumentException("Arguments must be of type int, int, int");
                }

                int mouseX = (int) args[0];
                int mouseY = (int) args[1];
                int button = (int) args[2];

                try {
                    CTJS.getInstance().getModuleManager().invokeFunction(methodName, mouseX, mouseY, button);
                } catch (ScriptException | NoSuchMethodException e) {
                    onClick = null;
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * Registers a method to be ran while gui is open.
     * Registered method runs on key input.
     * @param methodName the method to run
     * @return the trigger
     */
    public OnTrigger registerKeyTyped(String methodName) {
        return onKeyTyped = new OnTrigger(methodName, TriggerType.OTHER) {
            @Override
            public void trigger(Object... args) {
                if (!(args[0] instanceof Character
                        && args[1] instanceof Integer)) {
                    throw new IllegalArgumentException("Arguments must be of type char, int");
                }

                char typedChar = (char) args[0];
                int keyCode = (int) args[1];

                try {
                    CTJS.getInstance().getModuleManager().invokeFunction(methodName, typedChar, keyCode);
                } catch (ScriptException | NoSuchMethodException e) {
                    Console.getConsole().printStackTrace(e);
                    onKeyTyped = null;
                }
            }
        };
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        super.mouseClicked(mouseX, mouseY, button);

        runOnClick(mouseX, mouseY, button);
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
}
