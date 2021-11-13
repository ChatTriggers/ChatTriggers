package com.chattriggers.ctjs.utils.console

import java.awt.Color
import java.io.File
import java.io.OutputStream
import java.io.PrintStream
import javax.swing.JTextPane
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyleContext

// https://stackoverflow.com/a/9652143/8146249
class ColoredTextPane : JTextPane() {
    private val logFile = File("./logs/ctjs.log").let {
        it.delete()
        it.createNewFile()
        it.bufferedWriter()
    }

    fun append(text: String, color: Color) {
        logFile.append(text)

        val sc = StyleContext.getDefaultStyleContext()
        val aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color)

        caretPosition = document.length
        setCharacterAttributes(aset, false)
        replaceSelection(text)
    }

    fun appendln(text: String, color: Color) {
        append(text + '\n', color)
    }

    fun clear() {
        text = ""
    }
}