package com.chattriggers.ctjs.minecraft.wrappers.entity

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import net.minecraft.scoreboard.ScorePlayerTeam

//#if MC>=11701
//$$ import gg.essential.universal.wrappers.message.UTextComponent
//#endif

class Team(val team: ScorePlayerTeam) {
    /**
     * Gets the registered name of the team
     */
    fun getRegisteredName(): String {
        //#if MC<=11202
        return team.registeredName
        //#else
        //$$ return team.name
        //#endif
    }

    /**
     * Gets the display name of the team
     */
    fun getName(): String {
        //#if MC<=11202
        return team.teamName
        //#else
        //$$ return UTextComponent(team.displayName).formattedText
        //#endif
    }

    /**
     * Sets the display name of the team
     * @param name the new display name
     * @return the team for method chaining
     */
    fun setName(name: String) = apply {
        //#if MC<=11202
        team.teamName = ChatLib.addColor(name)
        //#else
        //$$ team.displayName = UTextComponent(ChatLib.addColor(name))
        //#endif
    }

    /**
     * Gets the list of names on the team
     */
    fun getMembers(): List<String> {
        //#if MC<=11202
        return team.membershipCollection.toList()
        //#else
        //$$ return team.players.toList()
        //#endif
    }

    /**
     * Gets the team prefix
     */
    fun getPrefix(): String {
        //#if MC<=11202
        return team.colorPrefix
        //#else
        //$$ return UTextComponent(team.playerPrefix).formattedText
        //#endif
    }

    /**
     * Sets the team prefix
     * @param prefix the prefix to set
     * @return the team for method chaining
     */
    fun setPrefix(prefix: String) = apply {
        //#if MC<=11202
        team.setNamePrefix(ChatLib.addColor(prefix))
        //#else
        //$$ team.playerPrefix = UTextComponent(ChatLib.addColor(prefix))
        //#endif
    }

    /**
     * Gets the team suffix
     */
    fun getSuffix(): String {
        //#if MC<=11202
        return team.colorSuffix
        //#else
        //$$ return UTextComponent(team.playerSuffix).formattedText
        //#endif
    }

    /**
     * Sets the team suffix
     * @param suffix the suffix to set
     * @return the team for method chaining
     */
    fun setSuffix(suffix: String) = apply {
        //#if MC<=11202
        team.setNameSuffix(ChatLib.addColor(suffix))
        //#else
        //$$ team.playerSuffix = UTextComponent(ChatLib.addColor(suffix))
        //#endif
    }

    /**
     * Gets the team's friendly fire setting
     */
    fun getFriendlyFire(): Boolean {
        //#if MC<=11202
        return team.allowFriendlyFire
        //#else
        //$$ return team.isAllowFriendlyFire
        //#endif
    }

    /**
     * Gets whether the team can see invisible players on the same team
     */
    fun canSeeInvisibleTeammates(): Boolean {
        //#if MC<=11202
        return team.seeFriendlyInvisiblesEnabled
        //#else
        //$$ return team.canSeeFriendlyInvisibles()
        //#endif
    }

    /**
     * Gets the team's name tag visibility
     */
    fun getNameTagVisibility(): String {
        //#if MC<=11202
        return team.nameTagVisibility.internalName
        //#else
        //$$ return team.nameTagVisibility.name
        //#endif
    }

    /**
     * Gets the team's death message visibility
     */
    fun getDeathMessageVisibility(): String {
        //#if MC<=11202
        return team.deathMessageVisibility.internalName
        //#else
        //$$ return team.deathMessageVisibility.name
        //#endif
    }
}
