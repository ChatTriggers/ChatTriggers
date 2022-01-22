package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.utils.kotlin.External

@External
object Server {
    /**
     * Gets the current server's IP, or "localhost" if the player
     * is in a single-player world.
     *
     * @return The IP of the current server
     */
    @JvmStatic
    fun getIP(): String {
        if (Client.getMinecraft().isInSingleplayer) return "localhost"

        return Client.getMinecraft().server?.serverIp ?: ""
    }

    /**
     * Gets the current server's name, or "SinglePlayer" if the player
     * is in a single-player world.
     *
     * @return The name of the current server
     */
    @JvmStatic
    fun getName(): String {
        if (Client.getMinecraft().isInSingleplayer) return "SinglePlayer"

        return Client.getMinecraft().server?.name ?: ""
    }

    /**
     * Gets the current server's MOTD, or "SinglePlayer" if the player
     * is in a single-player world.
     *
     * @return The MOTD of the current server
     */
    @JvmStatic
    fun getMOTD(): String {
        if (Client.getMinecraft().isInSingleplayer) return "SinglePlayer"

        return Client.getMinecraft().server?.serverMotd ?: ""
    }

    /**
     * Gets the ping to the current server, or 5 if the player
     * is in a single-player world.
     *
     * @return The ping to the current server
     */
    @JvmStatic
    fun getPing(): Long {
        val player = Player.getPlayer()

        if (player == null
            || Client.getMinecraft().isInSingleplayer
            || Client.getMinecraft().server == null
        ) {
            return 5L
        }

        return Client.getConnection()?.getPlayerListEntry(player.uuid)?.latency?.toLong() ?: 5L
    }
}
