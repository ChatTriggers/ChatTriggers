package com.chattriggers.ctjs.browser.components

import gg.essential.elementa.components.UIRoundedRectangle
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.dsl.*
import gg.essential.vigilance.gui.VigilancePalette

class Tag(text: String) : UIRoundedRectangle(6f) {
    private val textHolder by UIRoundedRectangle(4f).constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = ChildBasedSizeConstraint() + 4.pixels()
        height = ChildBasedSizeConstraint() + 4.pixels()
        color = VigilancePalette.getBackground().toConstraint()
    } childOf this

    private val text by UIText(text).constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        color = VigilancePalette.getMidText().toConstraint()
        textScale = 0.75f.pixels()
    } childOf textHolder

    init {
        constrain {
            height = ChildBasedSizeConstraint() + 2.pixels()
            width = ChildBasedSizeConstraint() + 2.pixels()
            color = VigilancePalette.getLightBackground().brighter().toConstraint()
        }
    }
}
