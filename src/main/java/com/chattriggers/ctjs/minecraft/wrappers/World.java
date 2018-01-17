package com.chattriggers.ctjs.minecraft.wrappers;

import net.minecraft.client.multiplayer.WorldClient;

public class World {
    /**
     * Gets the world object.
     * @return the world object
     */
    public static WorldClient getWorld() {
        return Client.getMinecraft().theWorld;
    }
}
