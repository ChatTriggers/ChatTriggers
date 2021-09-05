package com.chattriggers.ctjs.browser

import com.chattriggers.ctjs.browser.components.ModuleRelease
import com.chattriggers.ctjs.browser.components.Tag
import gg.essential.elementa.components.*
import gg.essential.elementa.components.inspector.Inspector
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.elementa.markdown.MarkdownComponent
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

    private val container by ScrollComponent().constrain {
        x = 20.pixels()
        y = SiblingConstraint(20f)
        width = 100.percent() - 40.pixels()
        height = FillConstraint()
    } childOf this

    private val descriptionTitle by UIText("Description").constrain {
        textScale = 1.5.pixels()
        color = VigilancePalette.getBrightText().toConstraint()
    } childOf container

    private val description by MarkdownComponent(
        module.description.ifBlank { "No description" },
        BrowserEntry.markdownConfig,
    ).constrain {
        x = 35.pixels()
        y = SiblingConstraint(15f)
        width = 100.percent() - 70.pixels()
    } childOf container

    init {
        if (module.releases.isNotEmpty()) {
            UIBlock(VigilancePalette.getDivider()).constrain {
                x = 0.pixels()
                y = SiblingConstraint(15f)
                width = 100.percent()
                height = 1.pixel()
            } childOf container

            UIText("Releases").constrain {
                y = SiblingConstraint(15f)
                textScale = 1.5.pixels()
                color = VigilancePalette.getBrightText().toConstraint()
            } childOf container

            module.releases.forEach {
                ModuleRelease(it).constrain {
                    x = 30.pixels()
                    y = SiblingConstraint(15f)
                    width = 100.percent() - 60.pixels()
                } childOf container
            }
        }
    }

    init {
        constrain {
            width = 100.percent()
            height = 100.percent()
        }
    }
}
