package com.chattriggers.ctjs.objects;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.libs.MinecraftVars;
import com.chattriggers.ctjs.triggers.OnTrigger;
import com.chattriggers.ctjs.triggers.TriggerType;
import com.chattriggers.ctjs.utils.console.Console;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import javax.script.ScriptException;
import java.io.IOException;

public class Gui extends GuiScreen {
    private OnTrigger onDraw = null;
    private OnTrigger onClick = null;
    private OnTrigger onKeyTyped = null;
    private OnTrigger onMouseReleased = null;
    private OnTrigger onMouseDragged = null;
    private OnTrigger onActionPerformed = null;

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
        if (MinecraftVars.getMinecraft().currentScreen == this)
            MinecraftVars.getPlayer().closeScreen();
    }

    /**
     * Get if the gui object is open.
     * @return true if this gui is open
     */
    public boolean isOpen() {
        return MinecraftVars.getMinecraft().currentScreen == this;
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
                } catch (ScriptException | NoSuchMethodException exception) {
                    onDraw = null;
                    Console.getConsole().printStackTrace(exception);
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
                } catch (ScriptException | NoSuchMethodException exception) {
                    onClick = null;
                    Console.getConsole().printStackTrace(exception);
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
                } catch (ScriptException | NoSuchMethodException exception) {
                    onKeyTyped = null;
                    Console.getConsole().printStackTrace(exception);
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
    public OnTrigger registerMouseDragged(String methodName) {
        return onMouseDragged = new OnTrigger(methodName, TriggerType.OTHER) {
            @Override
            public void trigger(Object... args) {
                int mouseX = (int) args[0];
                int mouseY = (int) args[1];
                int clickedMouseButton = (int) args[2];
                long timeSinceLastClick = (long) args[3];
                try {
                    CTJS.getInstance().getModuleManager().invokeFunction(methodName, mouseX, mouseY,
                            clickedMouseButton, timeSinceLastClick);
                } catch (ScriptException | NoSuchMethodException exception) {
                    onMouseDragged = null;
                    Console.getConsole().printStackTrace(exception);
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
    public OnTrigger registerMouseReleased(String methodName) {
        return onMouseReleased = new OnTrigger(methodName, TriggerType.OTHER) {
            @Override
            public void trigger(Object... args) {
                int mouseX = (int) args[0];
                int mouseY = (int) args[1];
                int state = (int) args[2];

                try {
                    CTJS.getInstance().getModuleManager().invokeFunction(methodName, mouseX, mouseY, state);
                } catch (ScriptException | NoSuchMethodException exception) {
                    onMouseReleased = null;
                    Console.getConsole().printStackTrace(exception);
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
    public OnTrigger registerActionPerformed(String methodName) {
        return onActionPerformed = new OnTrigger(methodName, TriggerType.OTHER) {
            @Override
            public void trigger(Object... args) {
                int buttonId = (int) args[0];

                try {
                    CTJS.getInstance().getModuleManager().invokeFunction(methodName, buttonId);
                } catch (ScriptException | NoSuchMethodException exception) {
                    onActionPerformed = null;
                    Console.getConsole().printStackTrace(exception);
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
