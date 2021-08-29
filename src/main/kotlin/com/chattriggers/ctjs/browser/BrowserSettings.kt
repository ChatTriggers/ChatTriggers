package com.chattriggers.ctjs.browser

import com.chattriggers.ctjs.browser.components.TextInput
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.elementa.state.state
import gg.essential.vigilance.gui.VigilancePalette

class BrowserSettings(private val reloadModules: () -> Unit) : UIContainer() {
    var search by state("")
    var filteredTags by state(emptyList<String>())
    var sort by state(Sort.DateNewestToOldest)
    var filter by state(Filter.All)
    var page by state(1)

    private val content by UIContainer().constrain {
        x = CenterConstraint()
        width = ChildBasedSizeConstraint()
        height = 100.percent()
    } childOf this

    private val searchSetting by Setting("Module Filter") childOf content
    private val searchInput by TextInput().constrain {
        width = 20.percentOfWindow().coerceAtLeast(100.pixels())
    } childOf searchSetting

    // TODO: Combobox for filteredTagsInput

    // private val sortSetting by Setting("Module Sorting") childOf content
    // private val sortInput by SelectorComponent(0, Sort.values().map { it.display }) childOf sortSetting
    //
    // // TODO: Checkbox selector component for filter
    //
    // private val pageSetting by Setting("Page") childOf content
    // private val pageInput by NumberComponent(1, 1, 10000 /* TODO */, 1) childOf pageSetting

    init {
        var stopSearchDelay: () -> Unit = {}

        searchInput.onChange {
            search = it
            stopSearchDelay.invoke()
            stopSearchDelay = searchInput.delay(1000L, reloadModules)
        }
    }

    private class Setting(name: String) : UIContainer() {
        init {
            constrain {
                x = SiblingConstraint(20f)
                width = ChildBasedMaxSizeConstraint()
                height = 100.percent()
            }
        }

        private val title by UIText(name, shadow = false).constrain {
            color = VigilancePalette.getMidText().toConstraint()
        } childOf this

        override fun addChild(component: UIComponent) = apply {
            if (component !is UIText) {
                component.constrain {
                    x = 1.pixel()
                    y = SiblingConstraint(5f)
                }
            }

            super.addChild(component)
        }
    }

    enum class Sort(val display: String, val apiValue: String) {
        DateNewestToOldest("Date (Newest to Oldest)", "DATE_CREATED_DESC"),
        DateOldestToNewest("Date (Oldest to Newest)", "DATE_CREATED_ASC"),
        DownloadsHighToLow("Downloads (High to Low)", "DOWNLOADS_DESC"),
        DownloadsLowToHigh("Downloads (Low to High)", "DOWNLOADS_ASC"),
    }

    enum class Filter {
        All,
        Trusted,
    }
}
