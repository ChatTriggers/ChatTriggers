package com.chattriggers.ctjs.browser.pages

import com.chattriggers.ctjs.browser.ModuleBrowser
import com.chattriggers.ctjs.browser.NearestSiblingConstraint
import com.chattriggers.ctjs.browser.WebsiteAPI
import com.chattriggers.ctjs.browser.WebsiteModule
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.mojang.authlib.GameProfile
import gg.essential.api.EssentialAPI
import gg.essential.api.gui.buildEmulatedPlayer
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.FillConstraint
import gg.essential.elementa.dsl.*
import gg.essential.vigilance.gui.VigilancePalette
import java.util.*

object AccountPage : UIContainer() {
    private var userModules = emptyList<WebsiteModule>()

    private val container by UIContainer().constrain {
        width = 100.percent()
        height = 100.percent()
    } childOf this

    private val header by UIContainer().constrain {
        width = 100.percent()
        height = 30.pixels()
    } childOf container

    private val username by UIText(ModuleBrowser.username.get() ?: "").constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        textScale = 2.pixels()
        color = VigilancePalette.getAccent().toConstraint()
    } childOf header

    init {
        UIBlock(VigilancePalette.getDivider()).constrain {
            y = NearestSiblingConstraint(10f)
            width = 100.percent()
            height = 1.pixel()
        } childOf container
    }

    private val content by UIContainer().constrain {
        y = NearestSiblingConstraint(10f)
        width = 90.percent()
        height = FillConstraint()
    } childOf container

    private val lhs by UIContainer().constrain {
        width = 40.percent()
        height = 100.percent()
    } childOf content

    private val rhs by UIContainer().constrain {
        x = NearestSiblingConstraint()
        width = 60.percent()
        height = 100.percent()
    } childOf content

    private val userModel by EssentialAPI.getEssentialComponentFactory().buildEmulatedPlayer {
        profile = GameProfile(UUID.fromString(Player.getUUID()), Player.getName())
    }.constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 100.pixels()
        height = 300.pixels()
    } childOf lhs

    init {
        constrain {
            x = CenterConstraint()
            width = 90.percent()
            height = 100.percent()
        }

        ModuleBrowser.username.onSetValue {
            if (it != null)
                this.username.setText(it)
        }

        ModuleBrowser.id.onSetValue {
            if (it != null)
                userModules = WebsiteAPI.getUserModules(it)
        }
    }
}
