package com.chattriggers.ctjs.browser

import com.chattriggers.ctjs.browser.components.Tag
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedRangeConstraint
import gg.essential.elementa.constraints.CopyConstraintFloat
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import gg.essential.vigilance.gui.VigilancePalette
import java.awt.Color

class ModulePage(private val module: WebsiteModule) : UIContainer() {
    private val header by UIContainer().constrain {
        width = 100.percent()
        height = ChildBasedRangeConstraint()
    } childOf this

    private val title by UIText(module.name, shadow = false).constrain {
        x = 20.pixels()
        y = 10.pixels()
        textScale = 2.pixels()
        color = VigilancePalette.getAccent().toConstraint()
    } childOf header

    private val authorBy by UIText("by ", shadow = false).constrain {
        x = 35.pixels()
        y = SiblingConstraint(5f)
        textScale = 1.2.pixels()
        color = VigilancePalette.getDarkText().toConstraint()
    } childOf header

    private val author by UIText(module.owner.name, shadow = false).constrain {
        x = SiblingConstraint() boundTo authorBy
        y = CopyConstraintFloat() boundTo authorBy
        textScale = 1.2.pixels()
    } childOf header

    private val tagContainer by UIContainer().constrain {
        x = SiblingConstraint(20f) boundTo title
        y = CenterConstraint() boundTo title
        width = ChildBasedRangeConstraint()
        height = 20.pixels()
    } childOf header

    private val settings by UIImage.ofResource("/images/settings.png").constrain {
        x = 10.pixels(alignOpposite = true)
        y = 10.pixels()
        width = 16.pixels()
        height = 16.pixels()
        color = Color.WHITE.toConstraint()
    } childOf header

    init {
        module.tags.forEach {
            Tag(it).constrain {
                x = SiblingConstraint(5f)
                y = CenterConstraint()
            } childOf tagContainer
        }

        UIBlock(VigilancePalette.getDivider()).constrain {
            x = 15.pixels()
            y = SiblingConstraint(15f)
            width = 100.percent() - 30.pixels()
            height = 1.pixel()
        } childOf this
    }

    init {
        constrain {
            width = 100.percent()
            height = 100.percent()
        }
    }
}
