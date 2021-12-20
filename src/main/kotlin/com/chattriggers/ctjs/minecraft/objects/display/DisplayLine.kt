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
import org.mozilla.javascript.NativeObject

@External
@NotAbstract
abstract class DisplayLine {
    private lateinit var text: Text

    private var textWidth = 0f

    internal var textColor: Long? = null
    internal var backgroundColor: Long? = null
    internal var background: DisplayHandler.Background? = null
    internal var align: DisplayHandler.Align? = null

    private var onClicked: OnTrigger? = null
    private var onHovered: OnTrigger? = null
    private var onDragged: OnTrigger? = null

    private var cachedX = 0.0
    private var cachedY = 0.0
    private var cachedWidth = 0.0
    private var cachedHeight = 0.0

    constructor(text: String) {
        setText(text)
    }

    constructor(text: String, config: NativeObject) {
        setText(text)

        textColor = config.getOption("textColor", null)?.toLong()
        backgroundColor = config.getOption("backgroundColor", null)?.toLong()

        setAlign(config.getOption("align", null))
        setBackground(config.getOption("background", null))
    }

    private fun NativeObject?.getOption(key: String, default: Any?): String? {
        return (this?.get(key) ?: default)?.toString()
    }

    init {
        MouseListener.registerClickListener { x, y, button, pressed ->
            if (x in cachedX..cachedX + cachedWidth && y in cachedY..cachedY + cachedHeight)
                onClicked?.trigger(arrayOf(x, y, button, pressed))
        }

        MouseListener.registerDraggedListener { deltaX, deltaY, x, y, button ->
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
            Renderer.drawRect(backgroundColor, baseX - 1, y - 1, totalWidth + 2, 10 * text.getScale())

        if (text.getString().isEmpty())
            return

        val xOffset = when (this.align ?: align) {
            DisplayHandler.Align.LEFT -> baseX
            DisplayHandler.Align.CENTER -> baseX + (totalWidth - textWidth) / 2
            DisplayHandler.Align.RIGHT -> baseX + (totalWidth - textWidth)
        }

        if (background == DisplayHandler.Background.PER_LINE)
            Renderer.drawRect(backgroundColor, xOffset - 1, y - 1, textWidth + 2, 10 * text.getScale())

        text.setX(xOffset).setY(y).setColor(textColor).draw()

        cachedX = xOffset - 1.0
        cachedY = y - 1.0
        cachedWidth = textWidth + 2.0
        cachedHeight = 10.0 * text.getScale()

        if (Client.getMouseX() > x && Client.getMouseX() < x + cachedWidth
            && Client.getMouseY() > y && Client.getMouseY() < y + cachedHeight
        ) {
            onHovered?.trigger(arrayOf(
                Client.getMouseX(),
                Client.getMouseY()
            ))
        }
    }

    internal abstract fun getLoader(): ILoader

    override fun toString() =
        "DisplayLine{" +
                "text=$text, textColor=$textColor, align=$align, " +
                "background=$background, backgroundColor=$backgroundColor, " +
                "}"
}