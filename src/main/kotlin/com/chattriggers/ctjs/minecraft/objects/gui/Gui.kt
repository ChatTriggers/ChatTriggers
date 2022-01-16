package com.chattriggers.ctjs.minecraft.objects.gui

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.launch.mixins.asMixin
import com.chattriggers.ctjs.launch.mixins.transformers.ClickableWidgetAccessor
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.listeners.ClientListener
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.triggers.OnRegularTrigger
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.NotAbstract
import gg.essential.api.utils.GuiUtil
import gg.essential.universal.UKeyboard
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText

@External
@NotAbstract
abstract class Gui : Screen(LiteralText("CT GUI")) {
    private var onDraw: OnRegularTrigger? = null
    private var onClick: OnRegularTrigger? = null
    private var onScroll: OnRegularTrigger? = null
    private var onKeyTyped: OnRegularTrigger? = null
    private var onMouseReleased: OnRegularTrigger? = null
    private var onMouseDragged: OnRegularTrigger? = null
    private var onActionPerformed: OnRegularTrigger? = null

    private var mouseX = 0
    private var mouseY = 0

    private val buttons = mutableMapOf<Int, ButtonWidget>()
    private var doesPauseGame = false

    private var nextButtonId = 0

    init {
        ClientListener.registerScrollListener { x, y, delta ->
            onScroll?.trigger(arrayOf(x, y, delta))
        }
    }

    fun open() {
        GuiUtil.open(this)
    }

    fun close() {
        if (isOpen())
            Client.getMinecraft().currentScreen = null
    }

    fun isOpen(): Boolean = Client.getMinecraft().currentScreen === this

    fun isControlDown(): Boolean = Client.isControlDown()

    fun isShiftDown(): Boolean = Client.isShiftDown()

    fun isAltDown(): Boolean = Client.isAltDown()

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
        onDraw = OnRegularTrigger(method, TriggerType.Other, getLoader())
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
        onClick = OnRegularTrigger(method, TriggerType.Other, getLoader())
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
        onScroll = OnRegularTrigger(method, TriggerType.Other, getLoader())
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
        onKeyTyped = OnRegularTrigger(method, TriggerType.Other, getLoader())
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
        onMouseDragged = OnRegularTrigger(method, TriggerType.Other, getLoader())
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
        onMouseReleased = OnRegularTrigger(method, TriggerType.Other, getLoader())
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
        onActionPerformed = OnRegularTrigger(method, TriggerType.Other, getLoader())
        onActionPerformed
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (!super.mouseClicked(mouseX, mouseY, button))
            return false
        onClick?.trigger(arrayOf(mouseX, mouseY, button))
        return true
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (!super.mouseReleased(mouseX, mouseY, button))
            return false
        onMouseReleased?.trigger(arrayOf(mouseX, mouseY, button))
        return true
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        if (!super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY))
            return false
        // TODO(BREAKING): Remove timeSinceLastClick
        onMouseDragged?.trigger(arrayOf(mouseX, mouseY, button))
        return true
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun init() {
        super.init()
        buttons.values.forEach(::addElement)
    }

    private fun addElement(element: Element) {
        @Suppress("UNCHECKED_CAST")
        (children() as MutableList<Element>).add(element)
    }

    private fun removeElement(element: Element) {
        @Suppress("UNCHECKED_CAST")
        (children() as MutableList<Element>).remove(element)
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(matrices, mouseX, mouseY, delta)

        matrices.push()

        this.mouseX = mouseX
        this.mouseY = mouseY

        onDraw?.trigger(arrayOf(mouseX, mouseY, delta))

        matrices.pop()
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (!super.keyPressed(keyCode, scanCode, modifiers))
            return false
        onKeyTyped?.trigger(arrayOf(keyCode, scanCode))
        return true
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun isPauseScreen() = doesPauseGame

    fun setDoesPauseGame(doesPauseGame: Boolean) = apply { this.doesPauseGame = doesPauseGame }

    /**
     * Add a base Minecraft button to the gui
     *
     * @param button the button to add
     * @return the Gui for method chaining
     */
    // TODO(BREAKING): returns the button id
    fun addButton(button: ButtonWidget): Int {
        val id = nextButtonId++
        buttons[id] = button
        addElement(button)
        return id
    }

    /**
     * Add a base Minecraft button to the gui
     *
     * @param x the x position of the button
     * @param y the y position of the button
     * @param width the width of the button
     * @param height the height of the button
     * @param buttonText the label of the button
     * @return the Gui for method chaining
     */
    // TODO(BREAKING): Remove ID argument; returns the button ID
    @JvmOverloads
    fun addButton(x: Int, y: Int, width: Int = 200, height: Int = 20, buttonText: String): Int {
        val id = nextButtonId++
        val button = ButtonWidget(x, y, width, height, LiteralText(ChatLib.addColor(buttonText))) {
            onActionPerformed?.trigger(arrayOf(id))
        }
        buttons[id] = button
        return id
    }

    /**
     * Removes a button from the gui with the given id
     *
     * @param buttonId the id of the button to remove
     * @return the Gui for method chaining
     */
    fun removeButton(buttonId: Int) = apply {
        buttons.remove(buttonId)?.also(::removeElement)
    }

    fun clearButtons() = apply {
        buttons.values.forEach(::removeElement)
        buttons.clear()
    }

    fun getButton(buttonId: Int) = buttons[buttonId]

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

    fun getButtonEnabled(buttonId: Int): Boolean = getButton(buttonId)?.active ?: false

    /**
     * Sets the enabled state of a button
     *
     * @param buttonId the id of the button to set
     * @param enabled the enabled state of the button
     * @return the Gui for method chaining
     */
    fun setButtonEnabled(buttonId: Int, enabled: Boolean) = apply {
        getButton(buttonId)?.active = enabled
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
        getButton(buttonId)?.asMixin<ClickableWidgetAccessor>()?.setHeight(height)
    }

    fun getButtonX(buttonId: Int): Int = getButton(buttonId)?.x ?: 0

    /**
     * Sets the button's x position
     *
     * @param buttonId id of the button
     * @param x the new x position
     * @return the Gui for method chaining
     */
    fun setButtonX(buttonId: Int, x: Int) = apply {
        getButton(buttonId)?.x = x
    }

    fun getButtonY(buttonId: Int): Int = getButton(buttonId)?.y ?: 0

    /**
     * Sets the button's y position
     *
     * @param buttonId id of the button
     * @param y the new y position
     * @return the Gui for method chaining
     */
    fun setButtonY(buttonId: Int, y: Int) = apply {
        getButton(buttonId)?.y = y
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
        getButton(buttonId)?.also {
            it.x = x
            it.y = y
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
    fun drawString(text: String, x: Float, y: Float, color: Int) {
        TODO("fabric")
    }

    /**
     * Draws hovering text that follows the mouse
     *
     * @param text the text to draw
     * @param mouseX X position of mouse
     * @param mouseY Y position of mouse
     */
    fun drawCreativeTabHoveringString(text: String, mouseX: Int, mouseY: Int) {
        TODO("fabric")
    }

    /**
     * Draws hovering tex that doesn't follow the mouse
     *
     * @param text the text's to draw
     * @param x X position of the text
     * @param y Y position of the text
     */
    fun drawHoveringString(text: List<String>, x: Int, y: Int) {
        TODO("fabric")
    }

    internal abstract fun getLoader(): ILoader
}
