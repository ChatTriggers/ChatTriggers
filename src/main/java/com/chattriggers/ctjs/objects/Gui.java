package com.chattriggers.ctjs.objects;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.triggers.OnTrigger;
import com.chattriggers.ctjs.utils.console.Console;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import javax.script.ScriptException;
import java.io.IOException;

public class Gui extends GuiScreen {
    private OnTrigger onDraw = null;
    private OnTrigger onClick = null;
    private OnTrigger onKeyTyped = null;

    public Gui() {}

    /**
     * Displays the gui object to Minecraft.
     */
    public void open() {
        Minecraft.getMinecraft().thePlayer.closeScreen();
        Minecraft.getMinecraft().displayGuiScreen(this);
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
     */
    public void registerOnDraw(String methodName) {
        onDraw = new OnTrigger(methodName) {
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
                    CTJS.getInstance().getInvocableEngine().invokeFunction(methodName, mouseX, mouseY, partialTicks);
                } catch (ScriptException | NoSuchMethodException e) {
                    Console.getConsole().printStackTrace(e);
                }
            }
        };
    }

    /**
     * Registers a method to be ran while gui is open.
     * Registered method runs on mouse click.
     * @param methodName the method to run
     */
    public void registerOnClicked(String methodName) {
        onClick = new OnTrigger(methodName) {
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
                    CTJS.getInstance().getInvocableEngine().invokeFunction(methodName, mouseX, mouseY, button);
                } catch (ScriptException | NoSuchMethodException e) {
                    Console.getConsole().printStackTrace(e);
                }
            }
        };
    }

    /**
     * Registers a method to be ran while gui is open.
     * Registered method runs on key input.
     * @param methodName the method to run
     */
    public void registerOnKeyTyped(String methodName) {
        onKeyTyped = new OnTrigger(methodName) {
            @Override
            public void trigger(Object... args) {
                if (!(args[0] instanceof Character
                        && args[1] instanceof Integer)) {
                    throw new IllegalArgumentException("Arguments must be of type char, int");
                }

                char typedChar = (char) args[0];
                int keyCode = (int) args[1];

                try {
                    CTJS.getInstance().getInvocableEngine().invokeFunction(methodName, typedChar, keyCode);
                } catch (ScriptException | NoSuchMethodException e) {
                    Console.getConsole().printStackTrace(e);
                }
            }
        };
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        if (onClick != null)
            onClick.trigger(mouseX, mouseY, button);
        super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (onDraw != null)
            onDraw.trigger(mouseX, mouseY, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (onKeyTyped != null)
            onKeyTyped.trigger(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }
}
