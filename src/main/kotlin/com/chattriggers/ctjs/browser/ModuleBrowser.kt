package com.chattriggers.ctjs.browser

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.utils.kotlin.fromJson
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.*
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.ScissorEffect
import gg.essential.elementa.transitions.SlideToTransition
import gg.essential.elementa.transitions.Transition
import gg.essential.elementa.utils.withAlpha
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.utils.onLeftClick
import java.awt.Color
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.concurrent.thread

object ModuleBrowser : WindowScreen(restoreCurrentGuiOnClose = true) {
    private const val WEBSITE_MODULE_API = "https://chattriggers.com/api/modules/"
    private var moduleOffset = 0

    init {
        UIBlock(VigilancePalette.getBackground()).constrain {
            width = 100.percent()
            height = 100.percent()
        } childOf window
    }

    private val content by UIContainer().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 85.percent()
        height = 85.percent()
    } childOf window

    private val backButton by UIImage.ofResource("/vigilance/arrow-left.png").constrain {
        x = 10.pixels(alignOutside = true) boundTo content
        y = 10.pixels() boundTo content
        width = 4.pixels()
        height = 7.pixels()
        color = Color.WHITE.darker().toConstraint()
    }.onMouseEnter {
        animate {
            setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.getAccent().toConstraint())
        }
    }.onMouseLeave {
        animate {
            setColorAnimation(Animations.OUT_EXP, 0.5f, Color.WHITE.darker().toConstraint())
        }
    }.onLeftClick {
        view(null)
    }

    init {
        UIBlock(VigilancePalette.getDivider()).constrain {
            width = 1.pixel()
            height = 100.percent()
        } childOf content
    }

    init {
        UIBlock(VigilancePalette.getDivider()).constrain {
            width = 1.pixel()
            height = 100.percent()
        } childOf content
    }

    private val main by UIContainer().constrain {
        width = 100.percent() - 2.pixels()
        height = 100.percent()
    } effect ScissorEffect() childOf content

    private val modulesPage by UIContainer().constrain {
        width = 100.percent()
        height = 100.percent()
    } childOf main

    init {
        UIBlock(VigilancePalette.getDivider()).constrain {
            width = 1.pixel()
            height = 100.percent()
        } childOf content
    }

    private val settings by BrowserSettings().constrain {
        width = 100.percent()
        height = 30.pixels()
    } childOf modulesPage

    init {
        UIBlock(VigilancePalette.getDivider()).constrain {
            x = 15.pixels()
            y = SiblingConstraint(15f)
            width = 100.percent() - 30.pixels()
            height = 1.pixel()
        } childOf modulesPage
    }

    private val moduleContentContainer by UIContainer().constrain {
        x = 45.pixels()
        y = SiblingConstraint(15f)
        width = 100.percent() - 90.pixels()
        height = basicHeightConstraint { modulesPage.getBottom() - it.getTop() }
    } childOf modulesPage

    private val moduleContent by ScrollComponent("Loading...").constrain {
        width = 100.percent()
        height = 100.percent()
    } childOf moduleContentContainer

    init {
        // val scrollBar = ScrollComponent.DefaultScrollBar(isHorizontal = false) childOf main
        // moduleContent.setVerticalScrollBarComponent(scrollBar.grip)

        UIBlock(VigilancePalette.getDivider()).constrain {
            x = SiblingConstraint()
            width = 1.pixel()
            height = 100.percent()
        } childOf content

        loadModules()

        moduleContent.addScrollAdjustEvent(isHorizontal = false) { scrollPercentage, _ ->
            if (scrollPercentage >= 0.9) {
                moduleOffset++
                loadModules()
            }
        }
    }

    private fun loadModules() {
        thread {
            var url = WEBSITE_MODULE_API

            url += "?limit=10"
            url += "&offset=${moduleOffset * 10}"
            url += "&sort=${settings.sort.apiValue}"

            if (settings.search.isNotBlank())
                url += "&q=${URLEncoder.encode(settings.search.trim(), StandardCharsets.UTF_8.name())}"

            val result = URL(url).openConnection().apply {
                setRequestProperty("User-Agent", "Mozilla/5.0")
            }.getInputStream().bufferedReader().readText()

            val response = CTJS.gson.fromJson<WebsiteResponse>(result)

            Window.enqueueRenderOperation {
                response.modules.forEach {
                    val entry = BrowserEntry(it)
                    entry.onLeftClick {
                        view(entry)
                    } childOf moduleContent
                }
            }
        }
    }

    // Null indicates the general modules screen
    private fun view(entry: BrowserEntry?) {
        if (entry == null) {
            backButton.hide(instantly = true)
            val page = main.children[0]
            modulesPage childOf main
            modulesPage.constrain {
                x = SiblingConstraint(alignOpposite = true)
            }
            SlideToTransition.Right(time = 0.5f, restoreConstraints = true).transition(page) {
                page.hide(instantly = true)
                modulesPage.constrain {
                    x = 0.pixels()
                }
            }
        } else {
            backButton childOf window
            val page = ModulePage(entry.module).constrain {
                x = SiblingConstraint()
                y = 0.pixels()
            } childOf main
            SlideToTransition.Left(time = 0.5f, restoreConstraints = true).transition(modulesPage) {
                modulesPage.hide(instantly = true)
                page.constrain {
                    x = 0.pixels()
                }
            }
        }
    }

    init {
        // Inspector(window) childOf window
    }
}
