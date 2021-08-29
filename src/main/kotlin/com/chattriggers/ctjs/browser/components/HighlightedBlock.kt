package com.chattriggers.ctjs.browser.components

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIRoundedRectangle
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.events.UIClickEvent
import gg.essential.elementa.state.BasicState
import gg.essential.elementa.state.toConstraint
import gg.essential.vigilance.utils.onLeftClick
import java.awt.Color

open class HighlightedBlock(
    backgroundColor: Color,
    highlightColor: Color = backgroundColor,
    backgroundHoverColor: Color = backgroundColor,
    highlightHoverColor: Color = highlightColor,
    protected val blockRadius: Float = 0f,
    protected val outlineWidth: Float = 1f,
    protected val clickBehavior: ClickBehavior = ClickBehavior.NONE
) : UIContainer() {
    protected var clicked: Boolean = false
    protected var clickAction: (UIClickEvent) -> Unit = {}

    protected val backgroundColorState: BasicState<Color> = BasicState(backgroundColor)
    protected val highlightColorState: BasicState<Color> = BasicState(highlightColor)
    protected val backgroundHoverColorState: BasicState<Color> = BasicState(backgroundHoverColor)
    protected val highlightHoverColorState: BasicState<Color> = BasicState(highlightHoverColor)

    private var onHighlightAction: () -> Unit = {}
    private var onUnhighlightAction: () -> Unit = {}

    val parentContainer = makeComponent(highlightColorState).constrain {
        width = 100.percent()
        height = 100.percent()
    }

    val contentContainer = makeComponent(backgroundColorState).constrain {
        x = outlineWidth.pixels()
        y = outlineWidth.pixels()
        width = 100.percent() - (outlineWidth * 2f).pixels()
        height = 100.percent() - (outlineWidth * 2f).pixels()
    } childOf parentContainer

    init {
        constrain {
            width = 100.percent()
            height = 100.percent()
        }

        super.addChild(parentContainer)

        onMouseEnter {
            highlight()
        }

        onMouseLeave {
            unhighlight()
        }

        onLeftClick {
            clickAction(it)
            clicked = true

            if (clickBehavior == ClickBehavior.UNHIGHLIGHT) {
                unhighlight()
            }
        }
    }

    fun constrainXBasedOnChildren() = apply {
        constrain {
            width = ChildBasedSizeConstraint()
        }

        parentContainer.constrain {
            width = ChildBasedSizeConstraint() + (outlineWidth * 2f).pixels()
        }

        contentContainer.constrain {
            x = outlineWidth.pixels()
            width = ChildBasedSizeConstraint()
        }
    }

    fun constrainYBasedOnChildren() = apply {
        constrain {
            height = ChildBasedSizeConstraint()
        }

        parentContainer.constrain {
            height = ChildBasedSizeConstraint() + (outlineWidth * 2f).pixels()
        }

        contentContainer.constrain {
            y = outlineWidth.pixels()
            height = ChildBasedSizeConstraint()
        }
    }

    fun constrainBasedOnChildren() = apply {
        constrainXBasedOnChildren()
        constrainYBasedOnChildren()
    }

    override fun addChild(component: UIComponent) = apply {
        component childOf contentContainer
    }

    fun onClick(action: (UIClickEvent) -> Unit) = apply {
        clickAction = action
    }

    fun onHighlight(action: () -> Unit) = apply {
        onHighlightAction = action
    }

    fun onUnhighlight(action: () -> Unit) = apply {
        onUnhighlightAction = action
    }

    protected open fun highlight() {
        parentContainer.animate {
            setColorAnimation(Animations.OUT_EXP, 0.5f, highlightHoverColorState.toConstraint())
        }
        contentContainer.animate {
            setColorAnimation(Animations.OUT_EXP, 0.5f, backgroundHoverColorState.toConstraint())
        }
        onHighlightAction()
    }

    protected open fun unhighlight() {
        if (clicked && clickBehavior == ClickBehavior.STAY_HIGHLIGHTED)
            return

        parentContainer.animate {
            setColorAnimation(Animations.OUT_EXP, 0.5f, highlightColorState.toConstraint())
        }
        contentContainer.animate {
            setColorAnimation(Animations.OUT_EXP, 0.5f, backgroundColorState.toConstraint())
        }

        onUnhighlightAction()
    }

    private fun makeComponent(blockColor: BasicState<Color>) = if (blockRadius == 0f) {
        UIBlock(blockColor)
    } else UIRoundedRectangle(blockRadius).constrain {
        color = blockColor.toConstraint()
    }

    enum class ClickBehavior {
        UNHIGHLIGHT,
        STAY_HIGHLIGHTED,
        NONE,
    }
}