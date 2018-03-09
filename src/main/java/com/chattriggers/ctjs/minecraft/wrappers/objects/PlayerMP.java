package com.chattriggers.ctjs.minecraft.wrappers.objects;

import com.chattriggers.ctjs.minecraft.objects.message.ChatComponent;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import lombok.Getter;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class PlayerMP extends Entity {
    @Getter
    private EntityOtherPlayerMP player;

    public PlayerMP(EntityPlayer player) throws IllegalArgumentException {
        super(player);

        if (player instanceof EntityOtherPlayerMP) {
            this.player = ((EntityOtherPlayerMP) player);
        } else {
            throw new IllegalArgumentException("The player provided is not valid!");
        }
    }

    public PlayerMP(EntityOtherPlayerMP player) {
        super(player);

        this.player = player;
    }

    @Override
    public String getName() {
        return this.player.getName();
    }

    /**
     * Returns whether the player is currently a spectator or not
     *
     * @return whether the player is a spectator
     */
    public boolean isSpectator() {
        return this.player.isSpectator();
    }

    /**
     * Gets the player's current ping
     *
     * @return the ping
     */
    public int getPing() {
        return getPlayerInfo().getResponseTime();
    }

    /**
     * Gets the item currently in the player's specified inventory slot.
     * 0 for main hand, 1-4 for armor.
     *
     * @param slot the slot to access
     * @return the item in said slot
     */
    public Item getItemInSlot(int slot) {
        return new Item(player.getEquipmentInSlot(slot));
    }

    /**
     * Gets the display name for this player,
     * i.e. the name shown in tab list and in the player's nametag.
     * @return the display name
     */
    public ChatComponent getDisplayName() {
        return new ChatComponent(getPlayerName(getPlayerInfo()));
    }

    /**
     * Sets the name for this player shown in tab list
     *
     * @param chatComponent the new name to display
     */
    public void setTabDisplayName(ChatComponent chatComponent) {
        getPlayerInfo().setDisplayName(chatComponent.getChatComponentText());
    }

    /**
     * Sets the name for this player shown above their head,
     * in their name tag
     *
     * @param chatComponent the new name to display
     */
    public void setNametagName(ChatComponent chatComponent) {
        ReflectionHelper.setPrivateValue(
                EntityPlayer.class,
                player,
                chatComponent.getChatComponentText().getFormattedText(),
                "displayname"
        );
    }

    private String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn)
    {
        return networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
    }

    public NetworkPlayerInfo getPlayerInfo() {
        return Client.getMinecraft().getNetHandler().getPlayerInfo(this.player.getUniqueID());
    }

    @Override
    public String toString() {
        return "PlayerMP{name:" + getName() + ",ping:" + getPing() + ",entity:" + super.toString() + "}";
    }
}
