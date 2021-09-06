package com.chattriggers.ctjs.browser.pages

import com.chattriggers.ctjs.browser.BrowserEntry
import com.chattriggers.ctjs.browser.NearestSiblingConstraint
import com.chattriggers.ctjs.browser.components.ModuleRelease
import com.chattriggers.ctjs.browser.components.Tag
import com.chattriggers.ctjs.minecraft.wrappers.Player
import gg.essential.elementa.components.*
import gg.essential.elementa.constraints.*
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.markdown.MarkdownComponent
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.utils.onLeftClick
import java.awt.Color

interface BrowserModuleProvider {
    val name: String?
    val creator: String?
    val description: String?
    val tags: List<String>
    val releases: List<BrowserReleaseProvider>
}

interface BrowserReleaseProvider {
    val releaseVersion: String
    val modVersion: String
    val changelog: String
}

class ModulePage(private val module: BrowserModuleProvider, onBack: () -> Unit) : UIContainer() {
    private val header by UIContainer().constrain {
        width = 100.percent()
    } childOf this

    private val backButtonContainer by UIContainer().constrain {
        x = 20.pixels()
        y = CenterConstraint()
        width = ChildBasedRangeConstraint()
        height = ChildBasedMaxSizeConstraint()
    }.onLeftClick {
        onBack()
    } childOf header

    private val backButtonIcon by UIImage.ofResource("/vigilance/arrow-left.png").constrain {
        y = CenterConstraint()
        width = 4.pixels()
        height = 7.pixels()
        color = Color.WHITE.darker().toConstraint()
    } childOf backButtonContainer

    private val backButtonText by UIText("Back").constrain {
        x = NearestSiblingConstraint(10f)
        y = CenterConstraint()
        color = Color.WHITE.darker().toConstraint()
    } childOf backButtonContainer

    private val title by UIText(module.name ?: "UNNAMED MODULE", shadow = false).constrain {
        x = CenterConstraint()
        y = 10.pixels()
        textScale = 2.pixels()
        color = VigilancePalette.getAccent().toConstraint()
    } childOf header

    private val authorContainer by UIContainer().constrain {
        x = CenterConstraint()
        y = NearestSiblingConstraint(5f)
        height = ChildBasedMaxSizeConstraint()
    } childOf header

    private val authorBy by UIText("by ", shadow = false).constrain {
        y = NearestSiblingConstraint(5f)
        textScale = 1.2.pixels()
        color = VigilancePalette.getDarkText().toConstraint()
    } childOf authorContainer

    private val author by UIText(module.creator ?: Player.getName(), shadow = false).constrain {
        x = NearestSiblingConstraint() boundTo authorBy
        y = CopyConstraintFloat() boundTo authorBy
        textScale = 1.2.pixels()
    } childOf authorContainer

    private val tagContainer by UIContainer().constrain {
        x = CenterConstraint()
        y = NearestSiblingConstraint(5f)
        width = basicWidthConstraint { container ->
            (container.children.sumOf { it.getWidth().toDouble() } + (container.children.size - 1) * 5.0).toFloat()
        }
        height = ChildBasedMaxSizeConstraint()
    }

    init {
        if (module.tags.isNotEmpty()) {
            tagContainer childOf header

            module.tags.forEach {
                Tag(it).constrain {
                    x = NearestSiblingConstraint(5f)
                    y = CenterConstraint()
                } childOf tagContainer
            }

            header.constrain {
                height = basicHeightConstraint {
                    tagContainer.getBottom() - title.getTop()
                }
            }
        } else {
            header.constrain {
                height = basicHeightConstraint {
                    authorContainer.getBottom() - title.getTop()
                }
            }
        }

        authorContainer.constrain {
            width = basicWidthConstraint {
                authorBy.getWidth() + author.getWidth()
            }
        }

        backButtonContainer.onMouseEnter {
            backButtonIcon.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, Color.WHITE.toConstraint())
            }
            backButtonText.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, Color.WHITE.toConstraint())
            }
        }.onMouseLeave {
            backButtonIcon.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, Color.WHITE.darker().toConstraint())
            }
            backButtonText.animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, Color.WHITE.darker().toConstraint())
            }
        }
    }

    private val settings by UIImage.ofResource("/images/settings.png").constrain {
        x = 10.pixels(alignOpposite = true)
        y = 10.pixels()
        width = 16.pixels()
        height = 16.pixels()
        color = Color.WHITE.toConstraint()
    } childOf header

    init {
        UIBlock(VigilancePalette.getDivider()).constrain {
            x = 15.pixels()
            y = NearestSiblingConstraint(15f)
            width = 100.percent() - 30.pixels()
            height = 1.pixel()
        } childOf this
    }

    private val container by ScrollComponent().constrain {
        x = 20.pixels()
        y = NearestSiblingConstraint(20f)
        width = 100.percent() - 40.pixels()
        height = FillConstraint()
    } childOf this

    private val descriptionTitle by UIText("Description").constrain {
        textScale = 1.5.pixels()
        color = VigilancePalette.getBrightText().toConstraint()
    } childOf container

    private val description by MarkdownComponent(
        module.description.let { if (it?.isBlank() != false) "No description" else it },
        BrowserEntry.markdownConfig,
    ).constrain {
        x = 35.pixels()
        y = NearestSiblingConstraint(15f)
        width = 100.percent() - 70.pixels()
    } childOf container

    init {
        if (module.releases.isNotEmpty()) {
            UIBlock(VigilancePalette.getDivider()).constrain {
                x = 0.pixels()
                y = NearestSiblingConstraint(15f)
                width = 100.percent()
                height = 1.pixel()
            } childOf container

            UIText("Releases").constrain {
                y = NearestSiblingConstraint(15f)
                textScale = 1.5.pixels()
                color = VigilancePalette.getBrightText().toConstraint()
            } childOf container

            module.releases.forEach {
                ModuleRelease(it).constrain {
                    x = 30.pixels()
                    y = NearestSiblingConstraint(15f)
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
