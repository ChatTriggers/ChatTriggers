package com.chattriggers.ctjs.browser

import com.chattriggers.ctjs.browser.components.HighlightedBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.ScissorEffect
import gg.essential.elementa.markdown.MarkdownComponent
import gg.essential.elementa.markdown.MarkdownConfig
import gg.essential.elementa.markdown.TextConfig
import gg.essential.vigilance.gui.VigilancePalette

class BrowserEntry(val module: WebsiteModule) : UIContainer() {
    private val block by HighlightedBlock(
        backgroundColor = VigilancePalette.getBackground(),
        highlightColor = VigilancePalette.getBrightHighlight(),
        backgroundHoverColor = VigilancePalette.getLightBackground(),
        highlightHoverColor = VigilancePalette.getAccent(),
        outlineWidth = 1f
    ) effect ScissorEffect() childOf this

    private val title by UIText(module.name, shadow = false).constrain {
        x = 10.pixels()
        y = 10.pixels()
        textScale = 1.5.pixels()
        color = VigilancePalette.getBrightText().toConstraint()
    } childOf block

    private val owner by UIText("by ${module.owner.name}", shadow = false).constrain {
        x = SiblingConstraint(10f)
        y = 13.pixels()
        color = VigilancePalette.getDarkText().toConstraint()
    } childOf block

    private val descriptionContainer: UIContainer by UIContainer().constrain {
        x = 10.pixels()
        y = SiblingConstraint(10f)
        width = 100.percent() - 20.pixels()
        height = ChildBasedSizeConstraint().coerceAtMost(100.pixels())
    } childOf block

    private val description by MarkdownComponent(module.description, markdownConfig).constrain {
        width = 100.percent()
    } childOf descriptionContainer

    init {
        constrain {
            y = SiblingConstraint(20f)
            width = 100.percent()
            height = ChildBasedSizeConstraint()
        }

        block.constrain {
            height = basicHeightConstraint {
                val top = title.getTop()
                val bottom = title.getBottom() + 10f + descriptionContainer.getHeight()
                bottom - top + 20f
            }
        }.onHighlight {
            title.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.getAccent().toConstraint())
            }
        }.onUnhighlight {
            title.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.getBrightText().toConstraint())
            }
        }
    }

    companion object {
        val markdownConfig = MarkdownConfig(
            textConfig = TextConfig(
                color = VigilancePalette.getMidText(),
                hasShadow = false,
            ),
        )
    }
}
