package com.chattriggers.ctjs.browser.components

import gg.essential.elementa.components.*
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.ScissorEffect
import gg.essential.elementa.markdown.*
import gg.essential.elementa.state.BasicState
import gg.essential.vigilance.gui.VigilancePalette

class SubmitButtonError : UIContainer() {
    private val errorTextState = BasicState("")
    private var showingError = false

    private val container by UIContainer().constrain {
        width = ChildBasedSizeConstraint()
        height = ChildBasedSizeConstraint()
    } effect ScissorEffect() childOf this

    private val errorContainer by HighlightedBlock(
        VigilancePalette.getBackground(),
        VigilancePalette.getHighlight(),
    ).constrainBasedOnChildren()

    private val errorContainer2 by UIContainer().constrain {
        width = 150.pixels()
        height = ChildBasedSizeConstraint() + 10.pixels()
    } childOf errorContainer

    private val errorText by MarkdownComponent("", markdownConfig).constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 150.pixels()
    } childOf errorContainer2

    private val image by UIImage.ofResource("/images/error.png").constrain {
        x = 0.pixels()
        width = 16.pixels()
        height = 16.pixels()
        color = VigilancePalette.getWarning().toConstraint()
    }.onMouseEnter {
        showingError = true
        Window.enqueueRenderOperation {
            errorContainer childOf Window.of(this)
            errorContainer.setFloating(true)
            errorText.bindText(errorTextState)
        }
    }.onMouseLeave {
        showingError = false
        Window.enqueueRenderOperation {
            errorContainer.setFloating(false)
            Window.of(this).removeChild(errorContainer)
        }
    } childOf container

    init {
        constrain {
            width = ChildBasedSizeConstraint()
            height = ChildBasedSizeConstraint()
        }
    }

    override fun animationFrame() {
        super.animationFrame()
        if (showingError) {
            val (mouseX, mouseY) = getMousePosition()
            errorContainer.constrain {
                x = mouseX.pixels()
                y = mouseY.pixels()
            }
        }
    }

    fun setErrors(errors: List<String>) {
        errorTextState.set(errors.joinToString("\n") {
            "- $it"
        })
    }

    companion object {
        private val markdownConfig = MarkdownConfig(
            textConfig = TextConfig(
                color = VigilancePalette.getWarning(),
                hasShadow = false,
            ),
            listConfig = ListConfig(
                fontColor = VigilancePalette.getWarning(),
            )
        )
    }
}
