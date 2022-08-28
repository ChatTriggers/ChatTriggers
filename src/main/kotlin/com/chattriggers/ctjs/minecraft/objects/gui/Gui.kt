package com.chattriggers.ctjs.minecraft.objects.gui

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.minecraft.listeners.MouseListener
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.triggers.RegularTrigger
import com.chattriggers.ctjs.triggers.TriggerType
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.GlStateManager

abstract class Gui : GuiScreen() {
    private var onDraw: RegularTrigger? = null
    private var onClick: RegularTrigger? = null
    private var onScroll: RegularTrigger? = null
    private var onKeyTyped: RegularTrigger? = null
    private var onMouseReleased: RegularTrigger? = null
    private var onMouseDragged: RegularTrigger? = null
    private var onActionPerformed: RegularTrigger? = null
    private var onOpened: RegularTrigger? = null
    private var onClosed: RegularTrigger? = null

    private var mouseX = 0
    private var mouseY = 0

    private val buttons = mutableListOf<GuiButton>()
    private var doesPauseGame = false

    init {
        mc = Client.getMinecraft()
        MouseListener.registerScrollListener { x, y, delta ->
            if (isOpen())
                onScroll?.trigger(arrayOf(x, y, delta))
        }
    }

    fun open() {
        GuiHandler.openGui(this)
        onOpened?.trigger(arrayOf(this))
    }

    fun close() {
        if (isOpen()) Player.getPlayer()?.closeScreen()
    }

    fun isOpen(): Boolean = Client.getMinecraft().currentScreen === this

    fun isControlDown(): Boolean = isCtrlKeyDown()

    fun isShiftDown(): Boolean = isShiftKeyDown()

    fun isAltDown(): Boolean = isAltKeyDown()

    /**
     * Registers a method to be run while gui is open.
     * Registered method runs on draw.
     * Arguments passed through to method:
     * - int mouseX
     * - int mouseY
     * - float partialTicks
     *
     * @param method the method to run
     * @return the trigger
     */
    fun registerDraw(method: Any) = run {
        onDraw = RegularTrigger(method, TriggerType.Other, getLoader())
        onDraw
    }

    /**
     * Registers a method to be run while gui is open.
     * Registered method runs on mouse click.
     * Arguments passed through to method:
     * - int mouseX
     * - int mouseY
     * - int button
     *
     * @param method the method to run
     * @return the trigger
     */
    fun registerClicked(method: Any) = run {
        onClick = RegularTrigger(method, TriggerType.Other, getLoader())
        onClick
    }

    /**
     * Registers a method to be run while the gui is open.
     * Registered method runs on mouse scroll.
     * Arguments passed through to method:
     * - int mouseX
     * - int mouseY
     * - int scroll direction
     */
    fun registerScrolled(method: Any) = run {
        onScroll = RegularTrigger(method, TriggerType.Other, getLoader())
        onScroll
    }

    /**
     * Registers a method to be run while gui is open.
     * Registered method runs on key input.
     * Arguments passed through to method:
     * - char typed character
     * - int key code
     *
     * @param method the method to run
     * @return the trigger
     */
    fun registerKeyTyped(method: Any) = run {
        onKeyTyped = RegularTrigger(method, TriggerType.Other, getLoader())
        onKeyTyped
    }

    /**
     * Registers a method to be run while gui is open.
     * Registered method runs on key input.
     * Arguments passed through to method:
     * - int mouseX
     * - int mouseY
     * - int clickedMouseButton
     * - long timeSinceLastClick
     *
     * @param method the method to run
     * @return the trigger
     */
    fun registerMouseDragged(method: Any) = run {
        onMouseDragged = RegularTrigger(method, TriggerType.Other, getLoader())
        onMouseDragged
    }

    /**
     * Registers a method to be run while gui is open.
     * Registered method runs on mouse release.
     * Arguments passed through to method:
     * - int mouseX
     * - int mouseY
     * - int button
     *
     * @param method the method to run
     * @return the trigger
     */
    fun registerMouseReleased(method: Any) = run {
        onMouseReleased = RegularTrigger(method, TriggerType.Other, getLoader())
        onMouseReleased
    }

    /**
     * Registers a method to be run while gui is open.
     * Registered method runs when an action is performed (clicking a button)
     * Arguments passed through to method:
     * - the button that is clicked
     *
     * @param method the method to run
     * @return the trigger
     */
    fun registerActionPerformed(method: Any) = run {
        onActionPerformed = RegularTrigger(method, TriggerType.Other, getLoader())
        onActionPerformed
    }

    /**
     * Registers a method to be run when the gui is opened.
     * Arguments passed through to method:
     * - the gui that is opened
     *
     * @param method the method to run
     * @return the trigger
     */
    fun registerOpened(method: Any) = run {
        onOpened = RegularTrigger(method, TriggerType.Other, getLoader())
        onOpened
    }

    /**
     * Registers a method to be run when the gui is closed.
     * Arguments passed through to method:
     * - the gui that is closed
     *
     * @param method the method to run
     * @return the trigger
     */
    fun registerClosed(method: Any) = run {
        onClosed = RegularTrigger(method, TriggerType.Other, getLoader())
        onClosed
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun onGuiClosed() {
        super.onGuiClosed()
        onClosed?.trigger(arrayOf(this))
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        super.mouseClicked(mouseX, mouseY, button)
        onClick?.trigger(arrayOf(mouseX, mouseY, button))
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun mouseReleased(mouseX: Int, mouseY: Int, button: Int) {
        super.mouseReleased(mouseX, mouseY, button)
        onMouseReleased?.trigger(arrayOf(mouseX, mouseY, button))
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun actionPerformed(button: GuiButton) {
        super.actionPerformed(button)
        onActionPerformed?.trigger(arrayOf(button.id))
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun mouseClickMove(mouseX: Int, mouseY: Int, clickedMouseButton: Int, timeSinceLastClick: Long) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick)
        onMouseDragged?.trigger(arrayOf(mouseX, mouseY, clickedMouseButton, timeSinceLastClick))
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun initGui() {
        super.initGui()
        buttonList.addAll(buttons)
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)

        GlStateManager.pushMatrix()

        this.mouseX = mouseX
        this.mouseY = mouseY

        onDraw?.trigger(arrayOf(mouseX, mouseY, partialTicks))

        GlStateManager.popMatrix()
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun keyTyped(typedChar: Char, keyCode: Int) {
        super.keyTyped(typedChar, keyCode)

        onKeyTyped?.trigger(arrayOf(typedChar, keyCode))
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun doesGuiPauseGame() = doesPauseGame

    fun setDoesPauseGame(doesPauseGame: Boolean) = apply { this.doesPauseGame = doesPauseGame }

    /**
     * Add a base Minecraft button to the gui
     *
     * @param button the button to add
     * @return the Gui for method chaining
     */
    fun addButton(button: GuiButton) = apply {
        buttons.add(button)
        onResize(mc, width, height)
    }

    /**
     * Add a base Minecraft button to the gui
     *
     * @param buttonId id for the button
     * @param x the x position of the button
     * @param y the y position of the button
     * @param width the width of the button
     * @param height the height of the button
     * @param buttonText the label of the button
     * @return the Gui for method chaining
     */
    @JvmOverloads
    fun addButton(buttonId: Int, x: Int, y: Int, width: Int = 200, height: Int = 20, buttonText: String) = apply {
        addButton(GuiButton(buttonId, x, y, width, height, buttonText))
    }

    /**
     * Removes a button from the gui with the given id
     *
     * @param buttonId the id of the button to remove
     * @return the Gui for method chaining
     */
    fun removeButton(buttonId: Int) = apply {
        buttons.removeIf { it.id == buttonId }
        onResize(mc, width, height)
    }

    fun clearButtons() = apply {
        buttons.clear()
        onResize(mc, width, height)
    }

    fun getButton(buttonId: Int): GuiButton? = buttons.firstOrNull { it.id == buttonId }

    fun getButtonVisibility(buttonId: Int): Boolean = getButton(buttonId)?.visible ?: false

    /**
     * Sets the visibility of a button
     *
     * @param buttonId the id of the button to change
     * @param visible the new visibility of the button
     * @return the Gui for method chaining
     */
    fun setButtonVisibility(buttonId: Int, visible: Boolean) = apply {
        getButton(buttonId)?.visible = visible
    }

    fun getButtonEnabled(buttonId: Int): Boolean = getButton(buttonId)?.enabled ?: false

    /**
     * Sets the enabled state of a button
     *
     * @param buttonId the id of the button to set
     * @param enabled the enabled state of the button
     * @return the Gui for method chaining
     */
    fun setButtonEnabled(buttonId: Int, enabled: Boolean) = apply {
        getButton(buttonId)?.enabled = enabled
    }

    fun getButtonWidth(buttonId: Int): Int = getButton(buttonId)?.width ?: 0

    /**
     * Sets the button's width. Button textures break if the width is greater than 200
     *
     * @param buttonId id of the button
     * @param width the new width
     * @return the Gui for method chaining
     */
    fun setButtonWidth(buttonId: Int, width: Int) = apply {
        getButton(buttonId)?.width = width
    }

    fun getButtonHeight(buttonId: Int): Int = getButton(buttonId)?.height ?: 0

    /**
     * Sets the button's height. Button textures break if the height is not 20
     *
     * @param buttonId id of the button
     * @param height the new height
     * @return the Gui for method chaining
     */
    fun setButtonHeight(buttonId: Int, height: Int) = apply {
        getButton(buttonId)?.height = height
    }

    fun getButtonX(buttonId: Int): Int = getButton(buttonId)?.xPosition ?: 0

    /**
     * Sets the button's x position
     *
     * @param buttonId id of the button
     * @param x the new x position
     * @return the Gui for method chaining
     */
    fun setButtonX(buttonId: Int, x: Int) = apply {
        getButton(buttonId)?.xPosition = x
    }

    fun getButtonY(buttonId: Int): Int = getButton(buttonId)?.yPosition ?: 0

    /**
     * Sets the button's y position
     *
     * @param buttonId id of the button
     * @param y the new y position
     * @return the Gui for method chaining
     */
    fun setButtonY(buttonId: Int, y: Int) = apply {
        getButton(buttonId)?.yPosition = y
    }

    /**
     * Sets the button's position
     *
     * @param buttonId id of the button
     * @param x the new x position
     * @param y the new y position
     * @return the Gui for method chaining
     */
    fun setButtonLoc(buttonId: Int, x: Int, y: Int) = apply {
        getButton(buttonId)?.apply {
            xPosition = x
            yPosition = y
        }
    }

    /**
     * Draws text on screen
     *
     * @param text the text to draw
     * @param x X position of the text
     * @param y Y position of the text
     * @param color color of the text
     */
    fun drawString(text: String, x: Int, y: Int, color: Int) {
        drawString(mc.fontRendererObj, text, x, y, color)
    }

    /**
     * Draws hovering text that follows the mouse
     *
     * @param text the text to draw
     * @param mouseX X position of mouse
     * @param mouseY Y position of mouse
     */
    fun drawCreativeTabHoveringString(text: String, mouseX: Int, mouseY: Int) {
        drawCreativeTabHoveringText(text, mouseX, mouseY)
    }

    /**
     * Draws hovering text that doesn't follow the mouse
     *
     * @param text the text's to draw
     * @param x X position of the text
     * @param y Y position of the text
     */
    fun drawHoveringString(text: List<String>, x: Int, y: Int) {
        drawHoveringText(text, x, y, mc.fontRendererObj)
    }

    internal abstract fun getLoader(): ILoader
}
