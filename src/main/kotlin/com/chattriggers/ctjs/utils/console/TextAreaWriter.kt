package com.chattriggers.ctjs.utils.console

import com.chattriggers.ctjs.utils.Config
import java.awt.Color
import java.io.PrintWriter
import java.io.Writer
import javax.swing.JTextPane
import javax.swing.text.AttributeSet
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyleContext

class TextAreaWriter(private val textArea: JTextPane) : Writer() {
    val printWriter = PrintWriter(this)
    private var currentLogType: LogType = LogType.INFO
    private var customColor: Color? = null

    override fun write(cbuf: CharArray, off: Int, len: Int) {
        val s = String(cbuf, off, len)
        val sc: StyleContext = StyleContext.getDefaultStyleContext()

        val color = customColor ?: when (currentLogType) {
            LogType.INFO -> Config.consoleForegroundColor
            LogType.WARN -> Config.consoleWarningColor
            LogType.ERROR -> Config.consoleErrorColor
        }

        val attributes: AttributeSet = sc.addAttribute(
            SimpleAttributeSet.EMPTY,
            StyleConstants.Foreground,
            color,
        )

        textArea.document.insertString(textArea.document.length, s, attributes)
        textArea.caretPosition = textArea.document.length
        currentLogType = LogType.INFO
        customColor = null
    }

    override fun flush() {

    }

    override fun close() {

    }

    // TODO: Make println and roll this back before 3.0.0
    @JvmOverloads
    fun println(s: Any, logType: LogType = LogType.INFO, end: String = "\n", customColor: Color? = null) {
        if (Config.consoleErrorAndWarningColors) {
            currentLogType = logType
            this.customColor = customColor
        }
        printWriter.print(s)
        printWriter.print(end)
    }

    fun clear() {
        textArea.text = ""
    }
}
