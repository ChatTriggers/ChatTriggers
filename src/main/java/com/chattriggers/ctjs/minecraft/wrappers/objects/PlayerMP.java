package com.chattriggers.ctjs.minecraft.wrappers.objects;

import com.chattriggers.ctjs.minecraft.wrappers.Client;
import lombok.Getter;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;

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

    public NetworkPlayerInfo getPlayerInfo() {
        return Client.getMinecraft().getNetHandler().getPlayerInfo(this.player.getUniqueID());
    }

    @Override
    public String toString() {
        return "PlayerMP{name:" + getName() + ",ping:" + getPing() + ",entity:" + super.toString() + "}";
    }
}
