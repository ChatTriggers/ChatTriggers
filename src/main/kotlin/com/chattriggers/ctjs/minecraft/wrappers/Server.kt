package com.chattriggers.ctjs.minecraft.wrappers

object Server {
    /**
     * Gets the current server's IP.
     *
     * @return The IP of the current server, or "localhost" if the player
     * is in a single player world.
     */
    @JvmStatic
    fun getIP(): String {
        if (Client.getMinecraft().isSingleplayer) return "localhost"

        return Client.getMinecraft().currentServerData?.serverIP ?: ""
    }

    /**
     * Gets the current server's name.
     *
     * @return The name of the current server, or "SinglePlayer" if the player
     * is in a single player world.
     */
    fun getName(): String {
        if (Client.getMinecraft().isSingleplayer) return "SinglePlayer"

        return Client.getMinecraft().currentServerData?.serverName ?: ""
    }

    /**
     * Gets the current server's MOTD.
     *
     * @return The MOTD of the current server, or "SinglePlayer" if the player
     * is in a single player world.
     */
    fun getMOTD(): String {
        if (Client.getMinecraft().isSingleplayer) return "SinglePlayer"

        return Client.getMinecraft().currentServerData?.serverMOTD ?: ""
    }

    /**
     * Gets the ping to the current server.
     *
     * @return The ping to the current server, or 5 if the player
     * is in a single player world.
     */
    fun getPing(): Long {
        val player = Player.getPlayer()

        if (player == null
                || Client.getMinecraft().isSingleplayer
                || Client.getMinecraft().currentServerData == null) {
            return 5L
        }

        return Client.getConnection().getPlayerInfo(player.uniqueID)?.responseTime?.toLong()
                ?: Client.getMinecraft().currentServerData.pingToServer
    }
}