package com.chattriggers.ctjs.minecraft.objects.display

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.NotAbstract
import org.mozilla.javascript.NativeObject
import java.util.concurrent.CopyOnWriteArrayList

@External
@NotAbstract
abstract class Display {
    private var lines = CopyOnWriteArrayList<DisplayLine>()

    private var renderX = 0f
    private var renderY = 0f
    private var shouldRender = true
    private var order = DisplayHandler.Order.DOWN

    private var backgroundColor: Long = 0x50000000
    private var textColor: Long = 0xffffffff
    private var background = DisplayHandler.Background.NONE
    private var align = DisplayHandler.Align.LEFT

    private var minWidth = 0f
    private var width = 0f
    private var height = 0f

    internal var registerType = DisplayHandler.RegisterType.RENDER_OVERLAY

    constructor() {
        @Suppress("LeakingThis")
        DisplayHandler.registerDisplay(this)
    }

    constructor(config: NativeObject?) {
        shouldRender = config.getOption("shouldRender", true).toBoolean()
        renderX = config.getOption("renderX", 0).toFloat()
        renderY = config.getOption("renderY", 0).toFloat()

        setBackgroundColor(config.getOption("backgroundColor", 0x50000000).toLong())
        setBackground(config.getOption("background", DisplayHandler.Background.NONE))
        setTextColor(config.getOption("textColor", 0xffffffff).toLong())
        setAlign(config.getOption("align", DisplayHandler.Align.LEFT))
        setOrder(config.getOption("order", DisplayHandler.Order.DOWN))

        minWidth = config.getOption("minWidth", 0f).toFloat()

        setRegisterType(config.getOption("registerType", DisplayHandler.RegisterType.RENDER_OVERLAY))

        @Suppress("LeakingThis")
        DisplayHandler.registerDisplay(this)
    }

    private fun NativeObject?.getOption(key: String, default: Any): String {
        return (this?.get(key) ?: default).toString()
    }

    fun getBackgroundColor(): Long = backgroundColor

    fun setBackgroundColor(backgroundColor: Long) = apply {
        this.backgroundColor = backgroundColor
    }

    fun getTextColor(): Long = textColor

    fun setTextColor(textColor: Long) = apply {
        this.textColor = textColor
    }

    fun getBackground(): DisplayHandler.Background = background

    fun setBackground(background: Any) = apply {
        this.background = when (background) {
            is String -> DisplayHandler.Background.valueOf(background.uppercase().replace(" ", "_"))
            is DisplayHandler.Background -> background
            else -> DisplayHandler.Background.NONE
        }
    }

    fun getAlign(): DisplayHandler.Align = align

    fun setAlign(align: Any) = apply {
        this.align = when (align) {
            is String -> DisplayHandler.Align.valueOf(align.uppercase())
            is DisplayHandler.Align -> align
            else -> DisplayHandler.Align.LEFT
        }
    }

    fun getOrder(): DisplayHandler.Order = order

    fun setOrder(order: Any) = apply {
        this.order = when (order) {
            is String -> DisplayHandler.Order.valueOf(order.uppercase())
            is DisplayHandler.Order -> order
            else -> DisplayHandler.Order.DOWN
        }
    }

    fun setLine(index: Int, line: Any) = apply {
        while (lines.size - 1 < index)
            lines.add(createDisplayLine(""))

        lines[index] = when (line) {
            is String -> createDisplayLine(line)
            is DisplayLine -> line
            else -> createDisplayLine("")
        }
    }

    fun getLine(index: Int): DisplayLine = lines[index]

    fun getLines(): List<DisplayLine> = lines

    fun setLines(lines: MutableList<DisplayLine>) = apply {
        this.lines = CopyOnWriteArrayList(lines)
    }

    @JvmOverloads
    fun addLine(index: Int = -1, line: Any) = apply {
        val toAdd = when (line) {
            is String -> createDisplayLine(line)
            is DisplayLine -> line
            else -> createDisplayLine("")
        }

        if (index == -1) {
            lines.add(toAdd)
        } else lines.add(index, toAdd)
    }

    fun addLines(vararg lines: Any) = apply {
        this.lines.addAll(lines.map {
            when (it) {
                is String -> createDisplayLine(it)
                is DisplayLine -> it
                else -> createDisplayLine("")
            }
        })
    }

    fun clearLines() = apply {
        lines.clear()
    }

    fun getRenderX(): Float = renderX

    fun setRenderX(renderX: Float) = apply {
        this.renderX = renderX
    }

    fun getRenderY(): Float = renderY

    fun setRenderY(renderY: Float) = apply {
        this.renderY = renderY
    }

    fun setRenderLoc(renderX: Float, renderY: Float) = apply {
        this.renderX = renderX
        this.renderY = renderY
    }

    fun getShouldRender(): Boolean = shouldRender

    fun setShouldRender(shouldRender: Boolean) = apply {
        this.shouldRender = shouldRender
        lines.forEach { it.shouldRender = shouldRender }
    }

    fun getWidth(): Float = width

    fun getHeight(): Float = height

    fun getMinWidth(): Float = minWidth

    fun setMinWidth(minWidth: Float) = apply {
        this.minWidth = minWidth
    }

    /**
     * Gets the type of register the display will render under.
     *
     * The returned value will be a DisplayHandler.RegisterType
     *      renderOverlayEvent: render overlay
     *      postGuiRenderEvent: post gui render
     *
     * @return the register type
     */
    fun getRegisterType() : DisplayHandler.RegisterType = registerType


    /**
     * Sets the type of register the display will render under.
     *
     * Possible input values are:.
     *      renderOverlayEvent: render overlay
     *      postGuiRenderEvent: post gui render
     */
    fun setRegisterType(registerType: Any) = apply {
        this.registerType = when (registerType) {
            is String -> DisplayHandler.RegisterType.valueOf(registerType.uppercase().replace(" ", "_"))
            is DisplayHandler.RegisterType -> registerType
            else -> DisplayHandler.RegisterType.RENDER_OVERLAY
        }
    }

    fun render() {
        if (!shouldRender)
            return

        width = lines.maxOfOrNull { it.getTextWidth() }?.coerceAtLeast(minWidth) ?: minWidth

        var i = 0f
        lines.forEach {
            it.draw(renderX, renderY + i, width, background, backgroundColor, textColor, align)

            when (order) {
                DisplayHandler.Order.DOWN -> i += it.getText().getHeight()
                DisplayHandler.Order.UP -> i -= it.getText().getHeight()
            }
        }

        height = i
    }

    internal abstract fun createDisplayLine(text: String): DisplayLine

    override fun toString() =
        "Display{" +
                "shouldRender=$shouldRender, registerType=$registerType, " +
                "renderX=$renderX, renderY=$renderY, " +
                "background=$background, backgroundColor=$backgroundColor, " +
                "textColor=$textColor, align=$align, order=$order, " +
                "minWidth=$minWidth, width=$width, height=$height, " +
                "lines=$lines" +
                "}"

}
