package com.chattriggers.ctjs.browser.components

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.transitions.RecursiveFadeInTransition
import gg.essential.elementa.utils.withAlpha
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.utils.onLeftClick

open class Modal(
    container: HighlightedBlock = HighlightedBlock(
        backgroundColor = VigilancePalette.getDarkBackground(),
        highlightColor = VigilancePalette.getDarkHighlight(),
        highlightHoverColor = VigilancePalette.getAccent()
    ),
    backgroundAlpha: Int = 150
) : UIBlock(VigilancePalette.getModalBackground().withAlpha(backgroundAlpha)) {
    protected val container by container.constrain {
        x = CenterConstraint()
        y = CenterConstraint()
    } childOf this

    init {
        constrain {
            x = 0.percentOfWindow()
            y = 0.percentOfWindow()
            width = 100.percentOfWindow()
            height = 100.percentOfWindow()
        }

        container.constrainBasedOnChildren()
        container.contentContainer.constrain {
            width = ChildBasedSizeConstraint() + 20.pixels()
            height = ChildBasedSizeConstraint() + 20.pixels()
        }

        onLeftClick { event ->
            if (event.target == this)
                fadeOut()
        }

        onKeyType { _, keyCode ->
            if (keyCode == 1) // Escape
                fadeOut()
        }
    }

    override fun afterInitialization() {
        setFloating(true)
    }

    open fun fadeIn(callback: (() -> Unit)? = null) {
        unhide()
        setFloating(true)
        callback?.invoke()
    }

    open fun fadeOut(callback: (() -> Unit)? = null) {
        hide(instantly = true)
        setFloating(false)
        callback?.invoke()
    }
}