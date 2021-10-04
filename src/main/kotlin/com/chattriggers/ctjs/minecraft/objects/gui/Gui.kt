package com.chattriggers.ctjs.minecraft.objects.gui

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.listeners.MouseListener
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.triggers.OnRegularTrigger
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.NotAbstract
import gg.essential.universal.UKeyboard
import gg.essential.universal.UMatrixStack
import gg.essential.universal.UScreen
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.GlStateManager

//#if MC==11602
//$$ import net.minecraft.client.gui.widget.Widget
//#endif

@External
@NotAbstract
abstract class Gui : UScreen() {
    private var onDraw: OnRegularTrigger? = null
    private var onClick: OnRegularTrigger? = null
    private var onScroll: OnRegularTrigger? = null
    private var onKeyTyped: OnRegularTrigger? = null
    private var onMouseReleased: OnRegularTrigger? = null
    private var onMouseDragged: OnRegularTrigger? = null
    private var onActionPerformed: OnRegularTrigger? = null

    private var mouseX = 0
    private var mouseY = 0

    var doesPauseGame = false

    //#if MC==11602
    //$$ private val buttonIdMap = mutableMapOf<Int, Button>()
    //#endif

    init {
        MouseListener.registerScrollListener { x, y, delta ->
            onScroll?.trigger(arrayOf(x, y, delta))
        }
    }

    fun open() {
        GuiHandler.openGui(this)
    }

    fun close() {
        if (isOpen()) Player.getPlayer()?.closeScreen()
    }

    fun isOpen(): Boolean = Client.getMinecraft().currentScreen === this

    fun isControlDown(): Boolean = UKeyboard.isCtrlKeyDown()
    fun isShiftDown(): Boolean = UKeyboard.isShiftKeyDown()
    fun isAltDown(): Boolean = UKeyboard.isAltKeyDown()

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
    fun registerDraw(method: Any): OnRegularTrigger? {
        onDraw = OnRegularTrigger(method, TriggerType.OTHER, getLoader())
        return onDraw
    }

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
    fun registerClicked(method: Any): OnRegularTrigger? {
        onClick = OnRegularTrigger(method, TriggerType.OTHER, getLoader())
        return onClick
    }

    /**
     * Registers a method to be ran while the gui is open.<br></br>
     * Registered method runs on mouse scroll.<br></br>
     * Arguments passed through to method:<br></br>
     * int mouseX<br></br>
     * int mouseY<br></br>
     * int scroll direction
     */
    fun registerScrolled(method: Any): OnRegularTrigger? {
        onScroll = OnRegularTrigger(method, TriggerType.OTHER, getLoader())
        return onScroll
    }

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
    fun registerKeyTyped(method: Any): OnRegularTrigger? {
        onKeyTyped = OnRegularTrigger(method, TriggerType.OTHER, getLoader())
        return onKeyTyped
    }

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
    fun registerMouseDragged(method: Any): OnRegularTrigger? {
        onMouseDragged = OnRegularTrigger(method, TriggerType.OTHER, getLoader())
        return onMouseDragged
    }

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
    fun registerMouseReleased(method: Any): OnRegularTrigger? {
        onMouseReleased = OnRegularTrigger(method, TriggerType.OTHER, getLoader())
        return onMouseReleased
    }

    /**
     * Registers a method to be ran while gui is open.<br></br>
     * Registered method runs when an action is performed (clicking a button)<br></br>
     * Arguments passed through to method:<br></br>
     * the button that is clicked
     *
     * @param method the method to run
     * @return the trigger
     */
    fun registerActionPerformed(method: Any): OnRegularTrigger? {
        onActionPerformed = OnRegularTrigger(method, TriggerType.OTHER, getLoader())
        return onActionPerformed
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun onMouseClicked(mouseX: Double, mouseY: Double, mouseButton: Int) {
        super.onMouseClicked(mouseX, mouseY, mouseButton)
        onClick?.trigger(arrayOf(mouseX, mouseY, mouseButton))
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun onMouseReleased(mouseX: Double, mouseY: Double, state: Int) {
        super.onMouseReleased(mouseX, mouseY, state)
        onMouseReleased?.trigger(arrayOf(mouseX, mouseY, state))
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    //#if MC==10809
    override fun actionPerformed(button: GuiButton) {
        super.actionPerformed(button)
        onActionPerformed?.trigger(arrayOf(button.id))
    }
    //#endif

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun onMouseDragged(x: Double, y: Double, clickedButton: Int, timeSinceLastClick: Long) {
        super.onMouseDragged(x, y, clickedButton, timeSinceLastClick)
        onMouseDragged?.trigger(arrayOf(mouseX, mouseY, clickedButton, timeSinceLastClick))
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun onDrawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.onDrawScreen(mouseX, mouseY, partialTicks)

        GlStateManager.pushMatrix()

        this.mouseX = mouseX
        this.mouseY = mouseY

        onDraw?.trigger(arrayOf(mouseX, mouseY, partialTicks))

        GlStateManager.popMatrix()
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun onKeyPressed(keyCode: Int, typedChar: Char, modifiers: UKeyboard.Modifiers?) {
        super.onKeyPressed(keyCode, typedChar, modifiers)
        onKeyTyped?.trigger(arrayOf(typedChar, keyCode))
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    //#if MC==11602
    //$$ override fun isPauseScreen() = doesPauseGame
    //#else
    override fun doesGuiPauseGame() = doesPauseGame
    //#endif

    fun setDoesPauseGame(doesPauseGame: Boolean) = apply {
        this.doesPauseGame = doesPauseGame
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
    @JvmOverloads
    fun addButton(buttonId: Int, x: Int, y: Int, width: Int = 200, height: Int = 20, buttonText: String) {
        //#if MC==11602
        //$$ val button = Button(x, y, width, height, TextComponent(buttonText).component) {
        //$$     onActionPerformed?.trigger(arrayOf(buttonId))
        //$$ }
        //$$ buttonIdMap[buttonId] = button
        //$$ getButtonList().add(button)
        //#else
        getButtonList().add(GuiButton(buttonId, x, y, width, height, buttonText))
        //#endif
    }

    fun setButtonVisibility(buttonId: Int, visible: Boolean) {
        //#if MC==11602
        //$$ buttonIdMap[buttonId]?.visible = visible
        //#else
        getButtonList().firstOrNull { it.id == buttonId }?.visible = visible
        //#endif
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
        //#if MC==11602
        //$$ drawString(Renderer.getMatrixStack(), Renderer.getFontRenderer(), text, x, y, color)
        //#else
        drawString(Renderer.getFontRenderer(), text, x, y, color)
        //#endif
    }

    /**
     * Draws hovering text that follows the mouse
     *
     * @param text the text to draw
     * @param mouseX X position of mouse
     * @param mouseY Y position of mouse
     */
    @Deprecated("Does the same thing as drawHoveringString", ReplaceWith("drawHoveringString(text, mouseX, mouseY)"))
    fun drawCreativeTabHoveringString(text: String, mouseX: Int, mouseY: Int) {
        drawHoveringString(text, mouseX, mouseY)
    }

    /**
     * Draws hovering tex that doesn't follow the mouse
     *
     * @param text the text's to draw
     * @param x X position of the text
     * @param y Y position of the text
     */
    fun drawHoveringString(text: List<String>, x: Int, y: Int) {
        //#if MC==11602
        //$$ renderToolTip(
        //$$     Renderer.getMatrixStack(),
        //$$     text.map { TextComponent(it).component.func_241878_f() },
        //$$     x,
        //$$     y,
        //$$     Renderer.getFontRenderer(),
        //$$ )
        //#else
        drawHoveringText(text, x, y, Renderer.getFontRenderer())
        //#endif
    }

    fun drawHoveringString(text: String, x: Int, y: Int) {
        drawHoveringString(listOf(text), x, y)
    }

    //#if MC==11602
    //$$ private fun getButtonList(): MutableList<Widget> {
    //$$     return buttons
    //#else
    private fun getButtonList(): MutableList<GuiButton> {
        return buttonList
    //#endif
    }

    internal abstract fun getLoader(): ILoader
}
