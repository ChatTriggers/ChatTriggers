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

    /**
     * Gets the world time
     * @return the world time
     */
    public static long getWorldTime() {
        return Client.getMinecraft().theWorld.getWorldTime();
    }
}
