package com.chattriggers.ctjs.browser

import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.animate
import gg.essential.elementa.dsl.pixels

class Panel : UIContainer() {
    private var opened = false

    init {

    }

    fun toggle() {
        if (opened) {
            animate {
                setWidthAnimation(Animations.OUT_EXP, 0.5f, 30.pixels())
            }
        } else {
            animate {
                setWidthAnimation(Animations.OUT_EXP, 0.5f, 150.pixels())
            }
        }

        opened = !opened
    }
}