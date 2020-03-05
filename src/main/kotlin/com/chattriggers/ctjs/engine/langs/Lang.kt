package com.chattriggers.ctjs.engine.langs

import org.fife.ui.rsyntaxtextarea.SyntaxConstants

enum class Lang(val langName: String, val extension: String, val syntaxStyle: String) {
    JS("js", "js", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT)
}