package com.chattriggers.ctjs.utils.console

import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.io.PrintStream
import javax.swing.JTextArea

class TextAreaOutputStream(private val textArea: JTextArea, lang: String) : OutputStream() {
    private val buffer = StringBuilder(128)
    private val file = File("./logs/ctjs-$lang.log")

    init {
        file.delete()
        file.createNewFile()
    }

    val printStream = PrintStream(this)

    override fun write(b: Int) {
        val letter = b.toChar()
        buffer.append(letter)

        if (letter == '\n') {
            val line = buffer.toString()

            textArea.append(line)
            FileOutputStream(file, true).bufferedWriter().use { it.append(line) }
            textArea.caretPosition = textArea.document.length

            buffer.delete(0, buffer.length)
        }
    }

    fun println(line: Any) {
        printStream.println(line)
    }

    fun clear() {
        textArea.text = ""
    }
}
