package com.chattriggers.ctjs.browser

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedMaxSizeConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.ScissorEffect
import gg.essential.elementa.utils.withAlpha
import gg.essential.vigilance.gui.VigilancePalette
import java.awt.Color

object Panel : UIContainer() {
    private val container by UIContainer().constrain {
        width = 24.pixels()
        height = 100.percent() - 8.pixels()
    } effect ScissorEffect() childOf this

    init {
        constrain {
            width = ChildBasedSizeConstraint()
            height = 100.percent()
        }

        onMouseEnter {
            container.animate {
                setWidthAnimation(Animations.OUT_EXP, 0.5f, 100.pixels())
            }
        }

        onMouseLeave {
            container.animate {
                setWidthAnimation(Animations.OUT_EXP, 0.5f, 24.pixels())
            }
        }

        Entry("/images/modules.png", "Modules") {
            ModuleBrowser.showPage(ModuleBrowser.Page.Modules)
        } childOf container

        Entry("/images/account.png", "Account") {
            ModuleBrowser.showPage(ModuleBrowser.Page.Account)
        } childOf container
    }

    class Entry(
        resourcePath: String,
        text: String,
        action: () -> Unit,
    ) : UIBlock(VigilancePalette.getBrightHighlight().withAlpha(0)) {
        private val image by UIImage.ofResource(resourcePath).constrain {
            y = 4.pixels()
            x = 4.pixels()
            width = 16.pixels()
            height = 16.pixels()
        } childOf this

        private val text by UIText(text).constrain {
            x = NearestSiblingConstraint(8f)
            y = CenterConstraint() boundTo image
            color = VigilancePalette.getMidText().toConstraint()
        } childOf this

        init {
            constrain {
                y = NearestSiblingConstraint(0f)
                width = 100.percent()
                height = ChildBasedMaxSizeConstraint() + 8.pixels()
            }

            onMouseEnter {
                animate {
                    setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.getBrightHighlight().toConstraint())
                }
            }

            onMouseLeave {
                animate {
                    setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.getBrightHighlight().withAlpha(0).toConstraint())
                }
            }

            onMouseClick { action() }
        }
    }
}
