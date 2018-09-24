//package com.chattriggers.ctjs.minecraft.wrappers;
//
//import net.minecraft.entity.player.EntityPlayer;
//
//public class Server {
//    /**
//     * Gets the current server's IP.
//     *
//     * @return The IP of the current server, or "localhost" if the player
//     * is in a single player world.
//     */
//    public static String getIP() {
//        if (Client.getMinecraft().isSingleplayer()) return "localhost";
//
//        return Client.getMinecraft().getCurrentServerData() == null ? "" : Client.getMinecraft().getCurrentServerData().serverIP;
//    }
//
//    /**
//     * Gets the current server's name.
//     *
//     * @return The name of the current server, or "SinglePlayer" if the player
//     * is in a single player world.
//     */
//    public static String getName() {
//        if (Client.getMinecraft().isSingleplayer()) return "SinglePlayer";
//
//        return Client.getMinecraft().getCurrentServerData() == null ? "" : Client.getMinecraft().getCurrentServerData().serverName;
//    }
//
//    /**
//     * Gets the current server's MOTD.
//     *
//     * @return The MOTD of the current server, or "SinglePlayer" if the player
//     * is in a single player world.
//     */
//    public static String getMOTD() {
//        if (Client.getMinecraft().isSingleplayer()) return "SinglePlayer";
//
//        return Client.getMinecraft().getCurrentServerData() == null ? "" : Client.getMinecraft().getCurrentServerData().serverMOTD;
//    }
//
//    /**
//     * Gets the ping to the current server.
//     *
//     * @return The ping to the current server, or 5 if the player
//     * is in a single player world.
//     */
//    public static Long getPing() {
//        EntityPlayer player = Player.getPlayer();
//
//        if (player == null
//                || Client.getMinecraft().isSingleplayer()
//                || Client.getMinecraft().getCurrentServerData() == null) {
//            return 5L;
//        }
//
//        if (Client.getConnection().getPlayerInfo(player.getUniqueID()) != null) {
//            return (long) Client.getConnection().getPlayerInfo(player.getUniqueID()).getResponseTime();
//        }
//
//        return Client.getMinecraft().getCurrentServerData().pingToServer;
//    }
//}
