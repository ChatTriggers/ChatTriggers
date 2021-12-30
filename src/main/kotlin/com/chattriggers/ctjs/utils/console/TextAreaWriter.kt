package com.chattriggers.ctjs.utils.console

import java.io.PrintWriter
import java.io.Writer
import javax.swing.JTextArea

class TextAreaWriter(private val textArea: JTextArea) : Writer() {
    val printWriter = PrintWriter(this)

    override fun write(cbuf: CharArray, off: Int, len: Int) {
        val s = String(cbuf, off, len)
        textArea.append(s)
        textArea.caretPosition = textArea.document.length
    }

    override fun flush() {

    }

    override fun close() {

    }

    fun println(s: Any) = printWriter.println(s)

    fun clear() {
        textArea.text = ""
    }
}
