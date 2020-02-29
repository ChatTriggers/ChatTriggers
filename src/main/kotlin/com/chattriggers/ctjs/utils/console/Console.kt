package com.chattriggers.ctjs.utils.console

import com.chattriggers.ctjs.engine.loader.ILoader
import com.chattriggers.ctjs.triggers.OnTrigger
import com.chattriggers.ctjs.utils.config.Config
import java.awt.*
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.WindowEvent
import java.io.PrintStream
import javax.swing.*
import javax.swing.text.DefaultCaret

class Console(val loader: ILoader?) {
    private val frame: JFrame = JFrame("ct.js ${loader?.getLanguage()?.langName ?: "Default"} Console")
    private val taos: TextAreaOutputStream
    private val components = mutableListOf<Component>()
    private val history = mutableListOf<String>()
    private var historyOffset = 0

    val out: PrintStream
    val textArea = JTextArea()
    val inputField = JTextField(1)

    init {
        this.frame.defaultCloseOperation = JFrame.HIDE_ON_CLOSE

        this.taos = TextAreaOutputStream(textArea, loader?.getLanguage()?.langName ?: "default")
        textArea.isEditable = false

        inputField.isFocusable = true
        textArea.autoscrolls = true
        val caret = textArea.caret as DefaultCaret
        caret.updatePolicy = DefaultCaret.ALWAYS_UPDATE
        inputField.caretColor = Color.WHITE

        inputField.margin = Insets(5, 5, 5, 5)
        textArea.margin = Insets(5, 5, 5, 5)

        components.add(textArea)
        components.add(inputField)

        out = taos.printStream

        inputField.addKeyListener(object : KeyListener {
            override fun keyTyped(e: KeyEvent) {}

            override fun keyPressed(e: KeyEvent) {}

            override fun keyReleased(e: KeyEvent) {
                when (e.keyCode) {
                    KeyEvent.VK_ENTER -> {
                        val command = inputField.text
                        inputField.text = ""
                        history.add(command)
                        historyOffset = 0

                        taos.println("eval> $command")

                        try {
                            taos.println(loader?.eval(command) ?: return)
                        } catch (e: Throwable) {
                            printStackTrace(e)
                        }
                    }

                    KeyEvent.VK_UP -> {
                        historyOffset++

                        try {
                            val message = history[history.size - historyOffset]
                            inputField.text = message
                        } catch (exception: Exception) {
                            historyOffset--
                        }
                    }

                    KeyEvent.VK_DOWN -> {
                        historyOffset--

                        if (historyOffset < 0) historyOffset = 0

                        try {
                            val message = history[history.size - historyOffset]
                            inputField.text = message
                        } catch (exception: Exception) {
                            historyOffset = 0
                            inputField.text = ""
                        }
                    }
                }
            }
        })

        frame.add(JScrollPane(textArea))
        frame.add(inputField, BorderLayout.SOUTH)
        frame.pack()
        frame.isVisible = false
        frame.setSize(800, 600)
    }

    fun clearConsole() {
        this.taos.clear()
    }

    fun printStackTrace(error: Throwable) {
        if (Config.openConsoleOnError) {
            showConsole()
        }

        val index = error.stackTrace.indexOfFirst {
            it?.fileName?.toLowerCase()?.contains("jsloader") ?: false
        }

        error.stackTrace = error.stackTrace.dropLast(error.stackTrace.size - index - 1).map {
            val fileNameIndex = it.fileName?.indexOf("ChatTriggers/modules/") ?: return@map it
            val classNameIndex = it.className.indexOf("ChatTriggers_modules_")

            if (fileNameIndex != -1) {
                StackTraceElement(it.className.substring(classNameIndex + 21), it.methodName, it.fileName!!.substring(fileNameIndex + 21), it.lineNumber)
            } else {
                it
            }
        }.toTypedArray()

        error.printStackTrace(out)
    }

    fun showConsole() {
        this.frame.isVisible = true

        val bg: Color
        val fg: Color

        if (Config.customTheme) {
            bg = Config.consoleBackgroundColor
            fg = Config.consoleForegroundColor
        } else {
            when (Config.consoleTheme) {
                "ashes.dark" -> {
                    bg = Color(28, 32, 35)
                    fg = Color(199, 204, 209)
                }
                "atelierforest.dark" -> {
                    bg = Color(28, 32, 35)
                    fg = Color(199, 204, 209)
                }
                "isotope.dark" -> {
                    bg = Color(0, 0, 0)
                    fg = Color(208, 208, 208)
                }
                "codeschool.dark" -> {
                    bg = Color(22, 27, 29)
                    fg = Color(126, 162, 180)
                }
                "gotham" -> {
                    bg = Color(10, 15, 20)
                    fg = Color(152, 209, 206)
                }
                "hybrid" -> {
                    bg = Color(29, 31, 33)
                    fg = Color(197, 200, 198)
                }
                "3024.light" -> {
                    bg = Color(247, 247, 247)
                    fg = Color(74, 69, 67)
                }
                "chalk.light" -> {
                    bg = Color(245, 245, 245)
                    fg = Color(48, 48, 48)
                }
                "blue" -> {
                    bg = Color(15, 18, 32)
                    fg = Color(221, 223, 235)
                }
                "slate" -> {
                    bg = Color(33, 36, 41)
                    fg = Color(193, 199, 208)
                }
                "red" -> {
                    bg = Color(26, 9, 11)
                    fg = Color(231, 210, 212)
                }
                "green" -> {
                    bg = Color(6, 10, 10)
                    fg = Color(47, 227, 149)
                }
                "aids" -> {
                    bg = Color(251, 251, 28)
                    fg = Color(192, 20, 214)
                }
                "default.dark" -> {
                    bg = Color(21, 21, 21)
                    fg = Color(208, 208, 208)
                }
                else -> {
                    bg = Color(21, 21, 21)
                    fg = Color(208, 208, 208)
                }
            }
        }

        for (comp in this.components) {
            comp.background = bg
            comp.foreground = fg
        }

        val chosenFont = if (Config.consolePrettyFont) FIRA_FONT.deriveFont(Config.consoleFontSize.toFloat()) else Font("DejaVu Sans Mono", Font.PLAIN, 15).deriveFont(Config.consoleFontSize.toFloat())

        textArea.font = chosenFont
        // TODO: Ligatures make everything extremely slow for some reason. Is this fixable?
//        val attrs = FIRA_FONT.attributes.apply { (this as MutableMap<TextAttribute, Any>)[TextAttribute.LIGATURES] = TextAttribute.LIGATURES_ON }
//        inputField.font = FIRA_FONT.deriveFont(attrs)
        inputField.font = chosenFont

        this.frame.toFront()
        this.frame.repaint()
    }

    protected fun finalize() {
        this.frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        this.frame.dispatchEvent(WindowEvent(frame, WindowEvent.WINDOW_CLOSING))
    }

    companion object {
        val FIRA_FONT: Font

        init {
            FIRA_FONT = Font.createFont(
                Font.TRUETYPE_FONT,
                this::class.java.getResourceAsStream("/FiraCode-Regular.otf")
            ).deriveFont(9f)

            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(FIRA_FONT)
        }
    }
}
