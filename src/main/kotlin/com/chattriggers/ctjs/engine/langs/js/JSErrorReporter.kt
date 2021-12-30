package com.chattriggers.ctjs.engine.langs.js

import org.mozilla.javascript.ErrorReporter
import org.mozilla.javascript.EvaluatorException
import java.io.PrintWriter

class JSErrorReporter(private val outputStream: PrintWriter) : ErrorReporter {
    override fun warning(message: String?, sourceName: String?, line: Int, lineSource: String?, lineOffset: Int) {
        reportErrorMessage(message, sourceName, line, lineSource, lineOffset, isWarning = true)
    }

    override fun error(message: String?, sourceName: String?, line: Int, lineSource: String?, lineOffset: Int) {
        reportErrorMessage(message, sourceName, line, lineSource, lineOffset, isWarning = false)
    }

    private fun reportErrorMessage(
        inputMessage: String?,
        sourceName: String?,
        line: Int,
        lineSource: String?,
        lineOffset: Int,
        isWarning: Boolean,
    ) {
        var message = if (line > 0) {
            if (sourceName == null) {
                "line $line: $inputMessage"
            } else "\"$sourceName\", line $line: $inputMessage"
        } else inputMessage

        if (isWarning)
            message = "warning: $message"

        outputStream.println(MESSAGE_PREFIX + message)
        if (lineSource != null) {
            outputStream.println(MESSAGE_PREFIX + lineSource)
            outputStream.println(MESSAGE_PREFIX + buildIndicator(lineOffset))
        }
    }

    private fun buildIndicator(offset: Int) = buildString {
        repeat(offset) { append('.') }
        append('^')
    }

    override fun runtimeError(
        message: String?,
        sourceName: String?,
        line: Int,
        lineSource: String?,
        lineOffset: Int
    ): EvaluatorException {
        return EvaluatorException(message, sourceName, line, lineSource, lineOffset)
    }

    companion object {
        const val MESSAGE_PREFIX = "js: "
    }
}
