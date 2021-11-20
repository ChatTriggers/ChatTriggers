package com.chattriggers.ctjs.minecraft.objects.display

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.libs.renderer.Text
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.triggers.OnRegularTrigger
import com.chattriggers.ctjs.triggers.OnTrigger
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.NotAbstract
import org.lwjgl.input.Mouse
import org.mozilla.javascript.NativeObject
import javax.vecmath.Vector2d

@External
@NotAbstract
abstract class DisplayLine {
    private lateinit var text: Text
    private var textWidth = 0f
    private var textColor: Long? = null
    private var backgroundColor: Long? = null

    private var background: DisplayHandler.Background? = null
    private var align: DisplayHandler.Align? = null

    private var onClicked: OnTrigger? = null
    private var onHovered: OnTrigger? = null
    private var onDragged: OnTrigger? = null

    private var mouseState = HashMap<Int, Boolean>()
    private var draggedState = HashMap<Int, Vector2d>()

    constructor(text: String) {
        setText(text)
        for (i in 0..5) mouseState[i] = false
    }

    constructor(text: String, config: NativeObject) {
        setText(text)
        for (i in 0..5) mouseState[i] = false

        textColor = config.getOption("textColor", null)?.toLong()
        backgroundColor = config.getOption("backgroundColor", null)?.toLong()

        setAlign(config.getOption("align", null))
        setBackground(config.getOption("background", null))
    }

    private fun NativeObject?.getOption(key: String, default: Any?): String? {
        if (this == null) return default?.toString()
        return getOrDefault(key, default).toString()
    }

    fun getText(): Text = text

    fun setText(text: String) = apply {
        this.text = Text(text)
        textWidth = Renderer.getStringWidth(text) * this.text.getScale()
    }

    fun getTextColor(): Long? = textColor

    fun setTextColor(color: Long) = apply {
        textColor = color
    }

    fun getTextWidth(): Float = textWidth

    fun setShadow(shadow: Boolean) = apply { text.setShadow(shadow) }

    fun setScale(scale: Float) = apply {
        text.setScale(scale)
        textWidth = Renderer.getStringWidth(text.getString()) * scale
    }

    fun getAlign(): DisplayHandler.Align? = align

    fun setAlign(align: Any?) = apply {
        this.align = when (align) {
            is String -> DisplayHandler.Align.valueOf(align.uppercase())
            is DisplayHandler.Align -> align
            else -> null
        }
    }

    fun getBackground(): DisplayHandler.Background? = background

    fun setBackground(background: Any?) = apply {
        this.background = when (background) {
            is String -> DisplayHandler.Background.valueOf(background.uppercase().replace(" ", "_"))
            is DisplayHandler.Background -> background
            else -> null
        }
    }

    fun getBackgroundColor(): Long? = backgroundColor

    fun setBackgroundColor(color: Long) = apply {
        backgroundColor = color
    }

    fun registerClicked(method: Any) = run {
        onClicked = OnRegularTrigger(method, TriggerType.Other, getLoader())
        onClicked
    }

    fun registerHovered(method: Any) = run {
        onHovered = OnRegularTrigger(method, TriggerType.Other, getLoader())
        onHovered
    }

    fun registerDragged(method: Any) = run {
        onDragged = OnRegularTrigger(method, TriggerType.Other, getLoader())
        onDragged
    }

    private fun handleInput(x: Float, y: Float, width: Float, height: Float) {
        if (!Mouse.isCreated()) return

        for (button in 0..5) handleDragged(button)

        if (Client.getMouseX() > x && Client.getMouseX() < x + width
            && Client.getMouseY() > y && Client.getMouseY() < y + height
        ) {
            handleHovered()

            for (button in 0..5) {
                if (Mouse.isButtonDown(button) == mouseState[button]) continue
                handleClicked(button)
                mouseState[button] = Mouse.isButtonDown(button)
                if (Mouse.isButtonDown(button))
                    draggedState[button] = Vector2d(Client.getMouseX().toDouble(), Client.getMouseY().toDouble())
            }
        }

        for (button in 0..5) {
            if (Mouse.isButtonDown(button)) continue
            if (!draggedState.containsKey(button)) continue
            draggedState.remove(button)
        }
    }

    private fun handleClicked(button: Int) {
        onClicked?.trigger(arrayOf(
            Client.getMouseX(),
            Client.getMouseY(),
            button,
            Mouse.isButtonDown(button)
        ))
    }

    private fun handleHovered() {
        onHovered?.trigger(arrayOf(
            Client.getMouseX(),
            Client.getMouseY()
        ))
    }

    private fun handleDragged(button: Int) {
        if (onDragged == null) return

        if (!draggedState.containsKey(button))
            return

        onDragged?.trigger(arrayOf(
            Client.getMouseX() - draggedState.getValue(button).x,
            Client.getMouseY() - draggedState.getValue(button).y,
            Client.getMouseX(),
            Client.getMouseY(),
            button
        ))

        draggedState[button] = Vector2d(Client.getMouseX().toDouble(), Client.getMouseY().toDouble())
    }

    private fun drawFullBG(
        bg: DisplayHandler.Background,
        color: Long,
        x: Float,
        y: Float,
        width: Float,
        height: Float
    ) {
        if (bg === DisplayHandler.Background.FULL)
            Renderer.drawRect(color, x, y, width, height)
    }

    private fun drawPerLineBG(
        bg: DisplayHandler.Background,
        color: Long,
        x: Float,
        y: Float,
        width: Float,
        height: Float
    ) {
        if (bg === DisplayHandler.Background.PER_LINE)
            Renderer.drawRect(color, x, y, width, height)
    }

    fun drawLeft(
        x: Float,
        y: Float,
        maxWidth: Float,
        background: DisplayHandler.Background,
        backgroundColor: Long,
        textColor: Long
    ) {
        val bg = this.background ?: background
        val bgColor = this.backgroundColor ?: backgroundColor
        val textCol = this.textColor ?: textColor

        // full background
        drawFullBG(bg, bgColor, x - 1, y - 1, maxWidth + 2, 10 * text.getScale())

        // blank line
        if ("" == text.getString()) return

        // text and per line background
        var xOff = x

        if (align === DisplayHandler.Align.RIGHT) {
            xOff = x - textWidth + maxWidth
        } else if (align === DisplayHandler.Align.CENTER) {
            xOff = x - textWidth / 2 + maxWidth / 2
        }

        drawPerLineBG(bg, bgColor, xOff - 1, y - 1, textWidth + 2, 10 * text.getScale())
        text.setX(xOff).setY(y).setColor(textCol).draw()

        handleInput(xOff - 1, y - 1, textWidth + 2, 10 * text.getScale())
    }

    fun drawRight(
        x: Float,
        y: Float,
        maxWidth: Float,
        background: DisplayHandler.Background,
        backgroundColor: Long,
        textColor: Long
    ) {
        val bg = this.background ?: background
        val bgColor = this.backgroundColor ?: backgroundColor
        val textCol = this.textColor ?: textColor

        // full background
        drawFullBG(bg, bgColor, x - maxWidth - 1f, y - 1, maxWidth + 2, 10 * text.getScale())

        // blank line
        if ("" == text.getString()) return

        // text and per line background\
        var xOff = x - textWidth

        if (align === DisplayHandler.Align.LEFT) {
            xOff = x - maxWidth
        } else if (align === DisplayHandler.Align.CENTER) {
            xOff = x - (textWidth / 2) - maxWidth / 2
        }

        drawPerLineBG(bg, bgColor, xOff - 1, y - 1, textWidth + 2, 10 * text.getScale())
        text.setX(xOff).setY(y).setColor(textCol).draw()

        handleInput(xOff - 1, y - 1, textWidth + 2, 10 * text.getScale())
    }

    fun drawCenter(
        x: Float,
        y: Float,
        maxWidth: Float,
        background: DisplayHandler.Background,
        backgroundColor: Long,
        textColor: Long
    ) {
        val bg = this.background ?: background
        val bgColor = this.backgroundColor ?: backgroundColor
        val textCol = this.textColor ?: textColor

        // full background
        drawFullBG(bg, bgColor, x - maxWidth / 2 - 1f, y - 1, maxWidth + 2, 10 * text.getScale())

        // blank line
        if ("" == text.getString()) return

        // text and per line background
        var xOff = x - textWidth / 2

        if (align === DisplayHandler.Align.LEFT) {
            xOff = x - maxWidth / 2
        } else if (align === DisplayHandler.Align.RIGHT) {
            xOff = x + maxWidth / 2 - textWidth
        }

        drawPerLineBG(bg, bgColor, xOff - 1, y - 1, textWidth + 2, 10 * text.getScale())
        text.setX(xOff).setY(y).setColor(textCol).draw()

        handleInput(xOff - 1, y - 1, textWidth + 2, 10 * text.getScale())
    }

    internal abstract fun getLoader(): ILoader

    override fun toString() =
        "DisplayLine{" +
                "text=$text, textColor=$textColor, align=$align, " +
                "background=$background, backgroundColor=$backgroundColor, " +
                "}"
}
