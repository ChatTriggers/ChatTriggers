package com.chattriggers.ctjs.minecraft.wrappers.objects;

import net.minecraft.client.entity.EntityOtherPlayerMP;

public class PlayerMP extends Entity {
    private EntityOtherPlayerMP player;

    public PlayerMP(EntityOtherPlayerMP player) {
        super(player);

        this.player = player;
    }

    @Override
    public String getName() {
        return this.player.getName();
    }

    public boolean isSpectator() {
        return this.player.isSpectator();
    }
}
