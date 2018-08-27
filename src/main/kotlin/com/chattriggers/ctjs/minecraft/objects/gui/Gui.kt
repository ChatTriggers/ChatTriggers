package com.chattriggers.ctjs.minecraft.objects.gui

import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.triggers.OnRegularTrigger
import com.chattriggers.ctjs.triggers.TriggerType
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.input.Mouse
import java.io.IOException

class Gui : GuiScreen() {
    private var onDraw: OnRegularTrigger? = null
    private var onClick: OnRegularTrigger? = null
    private var onKeyTyped: OnRegularTrigger? = null
    private var onMouseReleased: OnRegularTrigger? = null
    private var onMouseDragged: OnRegularTrigger? = null
    private var onActionPerformed: OnRegularTrigger? = null

    private var mouseX = 0
    private var mouseY = 0

    var doesPauseGame = true

    /**
     * Displays the gui object to Minecraft.
     */
    fun open() {
        GuiHandler.openGui(this)
    }

    /**
     * Closes this gui screen, returning Minecraft to ingame.
     */
    fun close() {
        if (isOpen())
            Player.getPlayer()?.closeScreen()
    }

    /**
     * Get if the gui object is open.
     *
     * @return true if this gui is open
     */
    fun isOpen() = Client.getMinecraft().currentScreen === this

    /**
     * Get if the control key is being held down.
     *
     * @return true if the control key is held down
     */
    fun isControlDown() = GuiScreen.isCtrlKeyDown()

    /**
     * Get if the shift key is being held down.
     *
     * @return true if the shift key is held down
     */
    fun isShiftDown() = GuiScreen.isShiftKeyDown()

    /**
     * Get if the alt key is being held down.
     *
     * @return true if the alt key is held down
     */
    fun isAltDown() = GuiScreen.isAltKeyDown()

    /**
     * Registers a method to be ran while gui is open.<br></br>
     * Registered method runs on draw.<br></br>
     * Arguments passed through to method:<br></br>
     * int mouseX<br></br>
     * int mouseY<br></br>
     * float partialTicks
     *
     * @param method the method to run
     * @return the trigger
     */
    fun registerDraw(method: Any) = OnRegularTrigger(method, TriggerType.OTHER)

    /**
     * Registers a method to be ran while gui is open.<br></br>
     * Registered method runs on mouse click.<br></br>
     * Arguments passed through to method:<br></br>
     * int mouseX<br></br>
     * int mouseY<br></br>
     * int button
     *
     * @param method the method to run
     * @return the trigger
     */
    fun registerClicked(method: Any) = OnRegularTrigger(method, TriggerType.OTHER)

    /**
     * Registers a method to be ran while gui is open.<br></br>
     * Registered method runs on key input.<br></br>
     * Arguments passed through to method:<br></br>
     * char typed character<br></br>
     * int key code
     *
     * @param method the method to run
     * @return the trigger
     */
    fun registerKeyTyped(method: Any) = OnRegularTrigger(method, TriggerType.OTHER)

    /**
     * Registers a method to be ran while gui is open.<br></br>
     * Registered method runs on key input.<br></br>
     * Arguments passed through to method:<br></br>
     * mouseX<br></br>
     * mouseY<br></br>
     * clickedMouseButton<br></br>
     * timeSinceLastClick
     *
     * @param method the method to run
     * @return the trigger
     */
    fun registerMouseDragged(method: Any) = OnRegularTrigger(method, TriggerType.OTHER)

    /**
     * Registers a method to be ran while gui is open.<br></br>
     * Registered method runs on mouse release.<br></br>
     * Arguments passed through to method:<br></br>
     * mouseX<br></br>
     * mouseY<br></br>
     * button
     *
     * @param method the method to run
     * @return the trigger
     */
    fun registerMouseReleased(method: Any) = OnRegularTrigger(method, TriggerType.OTHER)

    /**
     * Registers a method to be ran while gui is open.<br></br>
     * Registered method runs when an action is performed (clicking a button)<br></br>
     * Arguments passed through to method:<br></br>
     * the button that is clicked
     *
     * @param method the method to run
     * @return the trigger
     */
    fun registerActionPerformed(method: Any) = OnRegularTrigger(method, TriggerType.OTHER)

    public override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        super.mouseClicked(mouseX, mouseY, button)

        runOnClick(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, button: Int) {
        super.mouseReleased(mouseX, mouseY, button)

        onMouseReleased?.trigger(mouseX, mouseY, button)
    }

    override fun actionPerformed(button: GuiButton) {
        super.actionPerformed(button)

        onActionPerformed?.trigger(button.id)
    }

    override fun mouseClickMove(mouseX: Int, mouseY: Int, clickedMouseButton: Int, timeSinceLastClick: Long) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick)

        onMouseDragged?.trigger(mouseX, mouseY, clickedMouseButton, timeSinceLastClick)
    }

    override fun handleMouseInput() {
        super.handleMouseInput()

        val i = Mouse.getEventDWheel()

        when {
            i > 0 -> runOnClick(mouseX, mouseY, -1)
            i < 0 -> runOnClick(mouseX, mouseY, -2)
        }
    }

    // helper method for running onClick
    private fun runOnClick(mouseX: Int, mouseY: Int, button: Int) {
        onClick?.trigger(mouseX, mouseY, button)
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)

        GlStateManager.pushMatrix()

        this.mouseX = mouseX
        this.mouseY = mouseY

        onDraw?.trigger(mouseX, mouseY, partialTicks)

        GlStateManager.popMatrix()
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        super.keyTyped(typedChar, keyCode)

        onKeyTyped?.trigger(typedChar, keyCode)
    }

    override fun doesGuiPauseGame() = this.doesPauseGame

    /**
     * Add a base Minecraft button to the gui
     *
     * @param buttonId   id for the button
     * @param x          X position of the button
     * @param y          Y position of the button
     * @param buttonText the label of the button
     */
    fun addButton(buttonId: Int, x: Int, y: Int, buttonText: String) {
        this.buttonList.add(GuiButton(buttonId, x, y, buttonText))
    }

    /**
     * Add a base Minecraft button to the gui
     *
     * @param buttonId   id for the button
     * @param x          X position of the button
     * @param y          Y position of the button
     * @param width      the width of the button
     * @param height     the height of the button
     * @param buttonText the label of the button
     */
    fun addButton(buttonId: Int, x: Int, y: Int, width: Int = 200, height: Int = 20, buttonText: String) {
        this.buttonList.add(GuiButton(buttonId, x, y, width, height, buttonText))
    }

    fun setButtonVisibility(buttonId: Int, visible: Boolean) {
        this.buttonList.firstOrNull {
            it.id == buttonId
        }?.visible = visible
    }
}