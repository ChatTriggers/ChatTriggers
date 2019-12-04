package com.chattriggers.ctjs.minecraft.wrappers.settings

import com.chattriggers.ctjs.minecraft.wrappers.Client
import net.minecraft.entity.player.EnumPlayerModelParts

/**
 * Used from [Settings.skin] to get skin settings
 */
class SkinSettings {
    private val modelParts = Client.settings.getSettings().modelParts

    fun getCape() = modelParts.contains(EnumPlayerModelParts.CAPE)

    fun setCape(toggled: Boolean) {
        setModelPart(toggled, EnumPlayerModelParts.CAPE)
    }

    fun getJacket() = modelParts.contains(EnumPlayerModelParts.JACKET)

    fun setJacket(toggled: Boolean) {
        setModelPart(toggled, EnumPlayerModelParts.JACKET)
    }

    fun getLeftSleeve() = modelParts.contains(EnumPlayerModelParts.LEFT_SLEEVE)

    fun setLeftSleeve(toggled: Boolean) {
        setModelPart(toggled, EnumPlayerModelParts.LEFT_SLEEVE)
    }

    fun getRightSleeve() = modelParts.contains(EnumPlayerModelParts.RIGHT_SLEEVE)

    fun setRightSleeve(toggled: Boolean) {
        setModelPart(toggled, EnumPlayerModelParts.RIGHT_SLEEVE)
    }

    fun getLeftPantsLeg() = modelParts.contains(EnumPlayerModelParts.LEFT_PANTS_LEG)

    fun setLeftPantsLef(toggled: Boolean) {
        setModelPart(toggled, EnumPlayerModelParts.LEFT_PANTS_LEG)
    }

    fun getRightPantsLeg() = modelParts.contains(EnumPlayerModelParts.RIGHT_PANTS_LEG)

    fun setRightPantsLef(toggled: Boolean) {
        setModelPart(toggled, EnumPlayerModelParts.RIGHT_PANTS_LEG)
    }

    fun getHat() = modelParts.contains(EnumPlayerModelParts.HAT)

    fun setHat(toggled: Boolean) {
        setModelPart(toggled, EnumPlayerModelParts.HAT)
    }

    private fun setModelPart(toggled: Boolean, modelPart: EnumPlayerModelParts) {
        if (toggled) modelParts.add(modelPart)
        else modelParts.remove(modelPart)
    }
}