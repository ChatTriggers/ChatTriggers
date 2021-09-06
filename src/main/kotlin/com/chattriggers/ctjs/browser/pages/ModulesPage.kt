package com.chattriggers.ctjs.browser.pages

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.browser.BrowserEntry
import com.chattriggers.ctjs.browser.BrowserSettings
import com.chattriggers.ctjs.browser.NearestSiblingConstraint
import com.chattriggers.ctjs.browser.WebsiteResponse
import com.chattriggers.ctjs.utils.kotlin.fromJson
import gg.essential.elementa.components.ScrollComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.Window
import gg.essential.elementa.dsl.*
import gg.essential.elementa.transitions.SlideToTransition
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.utils.onLeftClick
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.concurrent.thread

object ModulesPage : UIContainer() {
    private const val WEBSITE_MODULE_API = "https://chattriggers.com/api/modules/"
    private var moduleOffset = 0

    private val modulesPageContainer by UIContainer().constrain {
        width = 100.percent()
        height = 100.percent()
    } childOf this

    private val settings by BrowserSettings { loadModules(true) }.constrain {
        width = 100.percent()
        height = 30.pixels()
    } childOf modulesPageContainer

    init {
        UIBlock(VigilancePalette.getDivider()).constrain {
            x = 15.pixels()
            y = NearestSiblingConstraint(15f) boundTo settings
            width = 100.percent() - 30.pixels()
            height = 1.pixel()
        } childOf modulesPageContainer
    }

    private val moduleContentContainer by UIContainer().constrain {
        x = 45.pixels()
        y = NearestSiblingConstraint(15f)
        width = 100.percent() - 90.pixels()
        height = basicHeightConstraint { modulesPageContainer.getBottom() - it.getTop() }
    } childOf modulesPageContainer

    private val moduleContent by ScrollComponent("Loading...").constrain {
        width = 100.percent()
        height = 100.percent()
    } childOf moduleContentContainer

    init {
        constrain {
            width = 100.percent()
            height = 100.percent()
        }

        loadModules()

        moduleContent.addScrollAdjustEvent(isHorizontal = false) { scrollPercentage, _ ->
            if (scrollPercentage >= 0.9) {
                moduleOffset++
                loadModules()
            }
        }
    }

    // Null indicates going back to the previous screen
    private fun viewEntry(entry: BrowserEntry?) {
        if (entry == null) {
            val page = children[0]
            modulesPageContainer childOf this
            modulesPageContainer.constrain {
                x = NearestSiblingConstraint(alignOpposite = true)
            }
            SlideToTransition.Right(time = 0.5f, restoreConstraints = true).transition(page) {
                page.hide(instantly = true)
                modulesPageContainer.constrain {
                    x = 0.pixels()
                }
            }
        } else {
            val page = ModulePage(entry.module) {
                viewEntry(null)
            }.constrain {
                x = NearestSiblingConstraint()
                y = 0.pixels()
            } childOf this

            SlideToTransition.Left(time = 0.5f, restoreConstraints = true).transition(modulesPageContainer) {
                modulesPageContainer.hide(instantly = true)
                page.constrain {
                    x = 0.pixels()
                }
            }
        }
    }

    private fun loadModules(clearModules: Boolean = false) {
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
                if (clearModules)
                    moduleContent.clearChildren()

                response.modules.forEach {
                    val entry = BrowserEntry(it)
                    entry.onLeftClick {
                        viewEntry(entry)
                    } childOf moduleContent
                }
            }
        }
    }
}