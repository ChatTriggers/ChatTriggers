package com.chattriggers.ctjs.minecraft.objects.gui

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.launch.mixins.transformers.gui.GuiButtonAccessor
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.listeners.MouseListener
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.triggers.RegularTrigger
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.kotlin.asMixin
import gg.essential.api.utils.GuiUtil
import gg.essential.universal.UKeyboard
import gg.essential.universal.UMatrixStack
import gg.essential.universal.UScreen
import net.minecraft.client.gui.GuiButton

//#if MC>=11701
//$$ import gg.essential.universal.wrappers.message.UTextComponent
//#endif

abstract class Gui : UScreen() {
    private var onDraw: RegularTrigger? = null
    private var onClick: RegularTrigger? = null
    private var onScroll: RegularTrigger? = null
    private var onKeyTyped: RegularTrigger? = null
    private var onMouseReleased: RegularTrigger? = null
    private var onMouseDragged: RegularTrigger? = null
    private var onActionPerformed: RegularTrigger? = null

    private var mouseX = 0
    private var mouseY = 0

    private var doesPauseGame = false

    //#if MC<=11202
    private val buttons = mutableListOf<GuiButton>()
    //#else
    //$$ private val buttons = mutableMapOf<Int, Button>()
    //#endif

    private var nextButtonId = 0

    init {
        //#if MC<=11202
        mc = Client.getMinecraft()
        //#endif
        MouseListener.registerScrollListener { x, y, delta ->
            if (isOpen())
                onScroll?.trigger(arrayOf(x, y, delta))
        }
    }

    fun open() {
        GuiUtil.open(this)
    }

    fun close() {
        if (isOpen()) {
            //#if MC<=11202
            Player.getPlayer()?.closeScreen()
            //#else
            //$$ Player.getPlayer()?.closeContainer()
            //#endif
        }
    }

    fun isOpen(): Boolean = GuiUtil.getOpenedScreen() === this

    fun isControlDown(): Boolean = UKeyboard.isCtrlKeyDown()

    fun isShiftDown(): Boolean = UKeyboard.isShiftKeyDown()

    fun isAltDown(): Boolean = UKeyboard.isAltKeyDown()

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
    //#if MC<=11202
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
        // TODO(BREAKING): Remove timeSinceLastClicked
        onMouseDragged?.trigger(arrayOf(mouseX, mouseY, clickedButton))
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun initScreen(width: Int, height: Int) {
        super.initScreen(width, height)
        //#if MC<=11202
        buttonList.addAll(buttons)
        //#elseif MC>=11701
        //$$ buttons.values.forEach(::addWidget)
        //#endif
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun onDrawScreen(matrixStack: UMatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.onDrawScreen(matrixStack, mouseX, mouseY, partialTicks)
        Renderer.partialTicks = partialTicks
        Renderer.pushMatrix()

        this.mouseX = mouseX
        this.mouseY = mouseY

        onDraw?.trigger(arrayOf(mouseX, mouseY, partialTicks))

        Renderer.popMatrix()
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
    //#if MC<=11202
    override fun doesGuiPauseGame() = doesPauseGame
    //#else
    //$$ override fun isPauseScreen() = doesPauseGame
    //#endif

    fun setDoesPauseGame(doesPauseGame: Boolean) = apply { this.doesPauseGame = doesPauseGame }

    /**
     * Add a base Minecraft button to the gui. The return value must be
     * stored and used in the actionPerformed trigger for cross-version
     * compatibility.
     *
     * @param button the button to add
     * @return the id of the button, to be used in the actionPerformed trigger.
     */
    // TODO(BREAKING): Returns the button id
    //#if MC<=11202
    fun addButton(button: GuiButton): Int {
        button.id = nextButtonId++
        buttons.add(button)
        onResize(mc, width, height)
        return button.id
    }
    //#else
    //$$ fun addButton(button: Button): Int {
    //$$     val id = nextButtonId++
    //$$     buttons[id] = button
    //$$     addWidget(button)
    //$$     return id
    //$$ }
    //#endif

    /**
     * Add a base Minecraft button to the gui. The return value must be
     * stored and used in the actionPerformed trigger for cross-version
     * compatibility.
     *
     * @param x the x position of the button
     * @param y the y position of the button
     * @param width the width of the button
     * @param height the height of the button
     * @param buttonText the label of the button
     * @return the id of the button, to the used in the actionPerformed trigger.
     */
    // TODO(BREAKING): Remove id argument, returns the button id
    @JvmOverloads
    fun addButton(x: Int, y: Int, width: Int = 200, height: Int = 20, buttonText: String): Int {
        //#if MC<=11202
        return addButton(GuiButton(nextButtonId++, x, y, width, height, ChatLib.addColor(buttonText)))
        //#else
        //$$ val id = nextButtonId++
        //$$ val button = Button(x, y, width, height, UTextComponent(ChatLib.addColor(buttonText)).component) {
        //$$     onActionPerformed?.trigger(arrayOf(id))
        //$$ }
        //$$ buttons[id] = button
        //$$ addWidget(button)
        //$$ return id
        //#endif
    }

    /**
     * Removes a button from the gui with the given id
     *
     * @param buttonId the id of the button to remove
     * @return the Gui for method chaining
     */
    fun removeButton(buttonId: Int) = apply {
        //#if MC<=11202
        buttons.removeIf { it.id == buttonId }
        onResize(mc, width, height)
        //#else
        //$$ buttons.remove(buttonId)
        //$$ resize(Client.getMinecraft(), width, height)
        //#endif
    }

    fun clearButtons() = apply {
        buttons.clear()
        //#if MC<=11202
        onResize(mc, width, height)
        //#else
        //$$ resize(Client.getMinecraft(), width, height)
        //#endif
    }

    fun getButton(buttonId: Int): GuiButton? {
        //#if MC<=11202
        return buttons.firstOrNull { it.id == buttonId }
        //#else
        //$$ return buttons[buttonId]
        //#endif
    }

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

    fun getButtonEnabled(buttonId: Int): Boolean {
        //#if MC<=11202
        return getButton(buttonId)?.enabled ?: false
        //#else
        //$$ return getButton(buttonId)?.active ?: false
        //#endif
    }

    /**
     * Sets the enabled state of a button
     *
     * @param buttonId the id of the button to set
     * @param enabled the enabled state of the button
     * @return the Gui for method chaining
     */
    fun setButtonEnabled(buttonId: Int, enabled: Boolean) = apply {
        //#if MC<=11202
        getButton(buttonId)?.enabled = enabled
        //#else
        //$$ getButton(buttonId)?.active = enabled
        //#endif
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
        getButton(buttonId)?.asMixin<GuiButtonAccessor>()?.setHeight(height)
    }

    fun getButtonX(buttonId: Int): Int {
        //#if MC<=11202
        return getButton(buttonId)?.xPosition ?: 0
        //#else
        //$$ return getButton(buttonId)?.x ?: 0
        //#endif
    }

    /**
     * Sets the button's x position
     *
     * @param buttonId id of the button
     * @param x the new x position
     * @return the Gui for method chaining
     */
    fun setButtonX(buttonId: Int, x: Int) = apply {
        //#if MC<=11202
        getButton(buttonId)?.xPosition = x
        //#else
        //$$ getButton(buttonId)?.x = x
        //#endif
    }

    fun getButtonY(buttonId: Int): Int {
        //#if MC<=11202
        return getButton(buttonId)?.yPosition ?: 0
        //#else
        //$$ return getButton(buttonId)?.y ?: 0
        //#endif
    }

    /**
     * Sets the button's y position
     *
     * @param buttonId id of the button
     * @param y the new y position
     * @return the Gui for method chaining
     */
    fun setButtonY(buttonId: Int, y: Int) = apply {
        //#if MC<=11202
        getButton(buttonId)?.yPosition = y
        //#else
        //$$ getButton(buttonId)?.y = y
        //#endif
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
        setButtonX(buttonId, x)
        setButtonY(buttonId, y)
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
        //#if MC<=11202
        drawString(Renderer.getFontRenderer(), text, x, y, color)
        //#else
        //$$ drawString(Renderer.matrixStack.toMC(), Renderer.getFontRenderer(), text, x, y, color)
        //#endif
    }

    // TODO(BREAKING): Removed drawCreativeTabHoveringString(text: String, mouseX: Int, mouseY: Int) .
    //                 It just called drawHoveringString(listOf(text), mouseX, mouseY)

    /**
     * Draws hovering text that doesn't follow the mouse
     *
     * @param text the text's to draw
     * @param x X position of the text
     * @param y Y position of the text
     */
    fun drawHoveringString(text: List<String>, x: Int, y: Int) {
        //#if MC<=11202
        drawHoveringText(text, x, y, mc.fontRendererObj)
        //#else
        //$$ renderTooltip(
        //$$     Renderer.matrixStack.toMC(),
        //$$     text.map { (UTextComponent(it).visualOrderText) },
        //$$     x,
        //$$     y
        //$$ )
        //#endif
    }

    internal abstract fun getLoader(): ILoader
}
