package com.chattriggers.ctjs.minecraft.objects.display

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.libs.renderer.Text
import com.chattriggers.ctjs.minecraft.listeners.MouseListener
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.triggers.OnRegularTrigger
import com.chattriggers.ctjs.triggers.OnTrigger
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.NotAbstract
import com.chattriggers.ctjs.utils.kotlin.getOption
import com.chattriggers.ctjs.utils.kotlin.getOptionNullable
import org.mozilla.javascript.NativeObject

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
    private var onMouseLeave: OnTrigger? = null

    internal var shouldRender: Boolean = true

    private var hovered: Boolean = false
    private var cachedX = 0.0
    private var cachedY = 0.0
    private var cachedWidth = 0.0
    private var cachedHeight = 0.0

    constructor(text: String) {
        setText(text)
    }

    constructor(text: String, config: NativeObject) {
        setText(text)
        setTextColor(config.getOptionNullable("textColor", null)?.toLong())
        setShadow(config.getOption("shadow", false).toBoolean())
        setScale(config.getOption("scale", 1f).toFloat())
        setAlign(config.getOptionNullable("align", null))
        setBackground(config.getOptionNullable("background", null))
        setBackgroundColor(config.getOptionNullable("backgroundColor", null)?.toLong())
    }

    init {
        MouseListener.registerClickListener { x, y, button, pressed ->
            if (
                shouldRender &&
                x in cachedX..cachedX + cachedWidth &&
                y in cachedY..cachedY + cachedHeight
            ) {
                onClicked?.trigger(arrayOf(x, y, button, pressed))
            }
        }

        MouseListener.registerDraggedListener { deltaX, deltaY, x, y, button ->
            if (shouldRender)
                onDragged?.trigger(arrayOf(deltaX, deltaY, x, y, button))
        }
    }

    fun getText(): Text = text

    fun setText(text: String) = apply {
        this.text = Text(text)
        textWidth = Renderer.getStringWidth(text) * this.text.getScale()
    }

    fun getTextColor(): Long? = textColor

    fun setTextColor(color: Long?) = apply {
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

    fun setBackgroundColor(backgroundColor: Long?) = apply {
        this.backgroundColor = backgroundColor
    }

    fun registerClicked(method: Any) = run {
        onClicked = OnRegularTrigger(method, TriggerType.Other, getLoader())
        onClicked
    }

    fun registerHovered(method: Any) = run {
        onHovered = OnRegularTrigger(method, TriggerType.Other, getLoader())
        onHovered
    }

    fun registerMouseLeave(method: Any) = run {
        onMouseLeave = OnRegularTrigger(method, TriggerType.Other, getLoader())
        onMouseLeave
    }

    fun registerDragged(method: Any) = run {
        onDragged = OnRegularTrigger(method, TriggerType.Other, getLoader())
        onDragged
    }

    fun draw(
        x: Float,
        y: Float,
        totalWidth: Float,
        background_: DisplayHandler.Background,
        backgroundColor_: Long,
        textColor_: Long,
        align: DisplayHandler.Align,
    ) {
        val background = this.background ?: background_
        val backgroundColor = this.backgroundColor ?: backgroundColor_
        val textColor = this.textColor ?: textColor_

        // X relative to the top left of the display
        val baseX = when (align) {
            DisplayHandler.Align.LEFT -> x
            DisplayHandler.Align.CENTER -> x - totalWidth / 2
            DisplayHandler.Align.RIGHT -> x - totalWidth
        }

        if (background == DisplayHandler.Background.FULL)
            Renderer.drawRect(backgroundColor, baseX - 1, y - 1, totalWidth + 1, text.getHeight())

        if (text.getString().isEmpty())
            return

        val xOffset = when (this.align ?: align) {
            DisplayHandler.Align.LEFT -> baseX
            DisplayHandler.Align.CENTER -> baseX + (totalWidth - textWidth) / 2
            DisplayHandler.Align.RIGHT -> baseX + (totalWidth - textWidth)
        }

        if (background == DisplayHandler.Background.PER_LINE)
            Renderer.drawRect(backgroundColor, xOffset - 1, y - 1, textWidth + 1, text.getHeight())

        text.setX(xOffset).setY(y).setColor(textColor).draw()

        cachedX = baseX - 1.0
        cachedY = y - 2.0
        cachedWidth = totalWidth + 1.0
        cachedHeight = text.getHeight() + 1.0

        if (!shouldRender)
            return

        if (
            Client.getMouseX() in cachedX..cachedX + cachedWidth &&
            Client.getMouseY() in cachedY..cachedY + cachedHeight
        ) {
            hovered = true
            onHovered?.trigger(
                arrayOf(
                    Client.getMouseX(),
                    Client.getMouseY()
                )
            )
        } else {
            if (hovered) {
                onMouseLeave?.trigger(
                    arrayOf(
                        Client.getMouseX(),
                        Client.getMouseY()
                    )
                )
            }

            hovered = false
        }
    }

    internal abstract fun getLoader(): ILoader

    override fun toString() =
        "DisplayLine{" +
                "text=$text, textColor=$textColor, align=$align, " +
                "background=$background, backgroundColor=$backgroundColor" +
                "}"
}