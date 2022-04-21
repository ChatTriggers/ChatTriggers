package com.chattriggers.ctjs.minecraft.wrappers.entity

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import net.minecraft.scoreboard.ScorePlayerTeam

class Team(val team: ScorePlayerTeam) {
    /**
     * Gets the registered name of the team
     */
    fun getRegisteredName(): String = team.registeredName

    /**
     * Gets the display name of the team
     */
    fun getName(): String = team.teamName

    /**
     * Sets the display name of the team
     * @param name the new display name
     * @return the team for method chaining
     */
    fun setName(name: String) = apply {
        team.teamName = ChatLib.addColor(name)
    }

    /**
     * Gets the list of names on the team
     */
    fun getMembers(): List<String> = team.membershipCollection.toList()

    /**
     * Gets the team prefix
     */
    fun getPrefix(): String = team.colorPrefix

    /**
     * Sets the team prefix
     * @param prefix the prefix to set
     * @return the team for method chaining
     */
    fun setPrefix(prefix: String) = apply {
        team.setNamePrefix(ChatLib.addColor(prefix))
    }

    /**
     * Gets the team suffix
     */
    fun getSuffix(): String = team.colorSuffix

    /**
     * Sets the team suffix
     * @param suffix the suffix to set
     * @return the team for method chaining
     */
    fun setSuffix(suffix: String) = apply {
        team.setNameSuffix(ChatLib.addColor(suffix))
    }

    /**
     * Gets the team's friendly fire setting
     */
    fun getFriendlyFire(): Boolean = team.allowFriendlyFire

    /**
     * Gets whether the team can see invisible players on the same team
     */
    fun canSeeInvisibleTeammates(): Boolean = team.seeFriendlyInvisiblesEnabled

    /**
     * Gets the team's name tag visibility
     */
    fun getNameTagVisibility(): String = team.nameTagVisibility.internalName

    /**
     * Gets the team's death message visibility
     */
    fun getDeathMessageVisibility(): String = team.deathMessageVisibility.internalName
}
