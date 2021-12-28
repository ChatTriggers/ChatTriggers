package com.chattriggers.ctjs.minecraft.wrappers.objects.entity

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.client.network.OtherClientPlayerEntity
import net.minecraft.client.network.PlayerListEntry

@External
class PlayerMP(player: OtherClientPlayerEntity) : PlayerEntity(player) {
    // TODO("fabric")
    // fun getPing(): Int {
    //     return getPlayerInfo()?.responseTime ?: -1
    // }

    /**
     * Sets the name for this player shown above their head,
     * in their name tag
     *
     * @param textComponent the new name to display
     */
    fun setNametagName(textComponent: TextComponent) {
        // TODO("fabric")
        // displayNameField.set(player, textComponent.textComponent.formattedText)
    }

    override fun toString(): String {
        return "PlayerMP{name=${getName()}, ping=${/*TODO("fabric"): getPing()*/""}, entityLivingBase=${super.toString()}}"
    }
}
