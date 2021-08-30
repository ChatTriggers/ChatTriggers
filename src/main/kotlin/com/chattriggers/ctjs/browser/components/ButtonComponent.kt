package com.chattriggers.ctjs.browser.components

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.utils.withAlpha
import gg.essential.universal.USound
import gg.essential.vigilance.gui.ExpandingClickEffect
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.utils.onLeftClick

class ButtonComponent(
    placeholder: String,
    block: HighlightedBlock = HighlightedBlock(
        VigilancePalette.getBackground(),
        VigilancePalette.getDivider(),
    ),
) : UIComponent() {
    private var callback: () -> Unit = {}
    private var active = false

    private val container by block childOf this

    private val text by UIText(placeholder).constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        color = VigilancePalette.getDarkText().toConstraint()
    } childOf container

    init {
        constrain {
            width = 65.pixels()
            height = 20.pixels()
        }

        enableEffect(ExpandingClickEffect(VigilancePalette.getAccent().withAlpha(0.5f), scissorBoundingBox = container))

        container.onMouseEnter {
            if (!active) {
                text.animate {
                    setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.getBrightText().toConstraint())
                }
            }
        }.onMouseLeave {
            if (!active) {
                text.animate {
                    setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.getDarkText().toConstraint())
                }
            }
        }.onLeftClick {
            USound.playButtonPress()
            callback()
        }
    }

    fun setActive(active: Boolean) {
        this.active = active
        val color = if (active) VigilancePalette.getAccent() else VigilancePalette.getDarkText()

        text.animate {
            setColorAnimation(Animations.OUT_EXP, 0.5f, color.toConstraint())
        }
    }

    fun onClick(action: () -> Unit) = apply {
        callback = action
    }
}
