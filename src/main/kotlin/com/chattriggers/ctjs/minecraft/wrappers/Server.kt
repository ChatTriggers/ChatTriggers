package com.chattriggers.ctjs.minecraft.wrappers

import gg.essential.universal.wrappers.message.UTextComponent
import net.minecraft.client.multiplayer.ServerData

object Server {
    /**
     * Returns whether the current world is a single-player world
     *
     * @return True if the current world is single-player, false otherwise.
     */
    fun isSinglePlayer(): Boolean {
        //#if MC<=11202
        return Client.getMinecraft().isSingleplayer
        //#else
        //$$ return Client.getMinecraft().hasSingleplayerServer()
        //#endif
    }

    /**
     * Gets the current server's IP, or "localhost" if the player
     * is in a single-player world.
     *
     * @return The IP of the current server
     */
    @JvmStatic
    fun getIP(): String {
        if (isSinglePlayer())
            return "localhost"

        //#if MC<=11202
        return getServerData()?.serverIP ?: ""
        //#else
        //$$ return getServerData()?.ip ?: ""
        //#endif
    }

    /**
     * Gets the current server's name, or "SinglePlayer" if the player
     * is in a single-player world.
     *
     * @return The name of the current server
     */
    @JvmStatic
    fun getName(): String {
        if (isSinglePlayer())
            return "SinglePlayer"

        //#if MC<=11202
        return getServerData()?.serverName ?: ""
        //#else
        //$$ return getServerData()?.name ?: ""
        //#endif
    }

    /**
     * Gets the current server's MOTD, or "SinglePlayer" if the player
     * is in a single-player world.
     *
     * @return The MOTD of the current server
     */
    // TODO(BREAKING): Return UTextComponent
    @JvmStatic
    fun getMOTD(): UTextComponent {
        if (isSinglePlayer())
            return UTextComponent("SinglePlayer")

        //#if MC<=11202
        return UTextComponent(getServerData()?.serverMOTD ?: "")
        //#else
        //$$ return UTextComponent.from(getServerData()?.motd ?: "")!!
        //#endif
    }

    /**
     * Gets the ping to the current server, or 5 if the player
     * is in a single-player world.
     *
     * @return The ping to the current server
     */
    @JvmStatic
    fun getPing(): Long {
        if (Player.getPlayer() == null || isSinglePlayer() || getServerData() == null)
            return 5L

        //#if MC<=11202
        return Client.getConnection()?.getPlayerInfo(Player.getUUID())?.responseTime?.toLong()
            ?: getServerData()?.pingToServer ?: -1L
        //#else
        //$$ return Client.getConnection()?.getPlayerInfo(Player.getUUID())?.latency?.toLong()
        //$$    ?: getServerData()?.ping ?: -1L
        //#endif
    }

    private fun getServerData(): ServerData? =
        //#if MC<=11202
        Client.getMinecraft().currentServerData
        //#else
        //$$ Client.getMinecraft().currentServer
        //#endif
}
