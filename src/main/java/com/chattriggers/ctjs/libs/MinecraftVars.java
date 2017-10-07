package com.chattriggers.ctjs.libs;

import com.chattriggers.ctjs.objects.KeyBind;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class MinecraftVars {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static KeyBinding keyLeftArrow = new KeyBinding("left", Keyboard.KEY_LEFT, "CT Controls");
    public static KeyBinding keyRightArrow = new KeyBinding("right", Keyboard.KEY_RIGHT, "CT Controls");
    public static KeyBinding keyUpArrow = new KeyBinding("up", Keyboard.KEY_UP, "CT Controls");
    public static KeyBinding keyDownArrow = new KeyBinding("down", Keyboard.KEY_DOWN, "CT Controls");

    public static Boolean isLeftArrowDown() {
        return keyLeftArrow.isKeyDown();
    }
    public static Boolean isRightArrowDown() {
        return keyRightArrow.isKeyDown();
    }
    public static Boolean isUpArrowDown() {
        return keyUpArrow.isKeyDown();
    }
    public static Boolean isDownArrowDown() {
        return keyDownArrow.isKeyDown();
    }

    /**
     * Gets the player's username.
     * In an import, accessible via the {@code playerName} variable.
     * @return The username of the user.
     */
    public static String getPlayerName() {
        return mc.getSession().getUsername();
    }

    /**
     * Gets the player's UUID.
     * In an import, accessible via the {@code playerUUID} variable.
     * @return The UUID of the user.
     */
    public static String getPlayerUUID() {
        return mc.getSession().getPlayerID();
    }

    /**
     * Gets the player's HP.
     * In an import, accessible via the {@code hp} variable.
     * @return The player's HP.
     */
    public static Float getPlayerHP() {
        return mc.thePlayer == null ? null : mc.thePlayer.getHealth();
    }

    /**
     * Gets the player's hunger level.
     * In an import, accessible via the {@code hunger} variable.
     * @return The player's hunger level.
     */
    public static Integer getPlayerHunger() {
        return mc.thePlayer == null ? null : mc.thePlayer.getFoodStats().getFoodLevel();
    }

    /**
     * Gets the player's saturation level.
     * In an import, accessible via the {@code saturation} variable.
     * @return The player's saturation level.
     */
    public static Float getPlayerSaturation() {
        return mc.thePlayer == null ? null : mc.thePlayer.getFoodStats().getSaturationLevel();
    }

    /**
     * Gets the player's armor points.
     * In an import, accessible via the {@code armorPoints} variable.
     * @return The player's armor points.
     */
    public static Integer getPlayerArmorPoints() {
        return mc.thePlayer == null ? null : mc.thePlayer.getTotalArmorValue();
    }

    /**
     * Gets the player's air level.
     * The returned value will be an integer. If the player is not taking damage, it
     * will be between 300 (not in water) and 0. If the player is taking damage, it
     * will be between -20 and 0, getting reset to 0 every time the player takes damage.
     *
     * In an import, accessible via the {@code airLevel} variable.
     * @return An integer corresponding to the player's air level.
     */
    public static Integer getPlayerAirLevel() {
        return mc.thePlayer == null ? null : mc.thePlayer.getAir();
    }

    /**
     * Gets the player's XP level.
     * In an import, accessible via the {@code xpLevel} variable.
     * @return The player's XP level.
     */
    public static Integer getXPLevel() {
        return mc.thePlayer == null ? null : mc.thePlayer.experienceLevel;
    }

    /**
     * Gets the player's XP progress towards the next XP level.
     * In an import, accessible via the {@code xpProgress} variable.
     * @return The player's xp progress.
     */
    public static Float getXPProgress() {
        return mc.thePlayer == null ? null : mc.thePlayer.experience;
    }

    /**
     * Gets the biome the player is currently in.
     * In an import, accessible via the {@code biome} variable.
     * @return The biome the player is in.
     */
    public static String getPlayerBiome() {
        if (mc.thePlayer == null) {
            return null;
        }
        Chunk chunk = mc.theWorld.getChunkFromBlockCoords(mc.thePlayer.getPosition());
        BiomeGenBase biome = chunk.getBiome(mc.thePlayer.getPosition(), mc.theWorld.getWorldChunkManager());

        return biome.biomeName;
    }

    /**
     * Returns true if the player has the chat open.
     * In an import, accessible via the {@code inChat} variable.
     * @return True if the player has the chat open, false otherwise.
     */
    public static boolean isInChat() {
        return mc.currentScreen instanceof GuiChat;
    }

    /**
     * Returns true if the player has the tab list open.
     * In an import, accessible via the {@code inTab} variable.
     * @return True if the player has the tab list open, false otherwise.
     */
    public static boolean isInTab() {
        return mc.gameSettings.keyBindPlayerList.isKeyDown();
    }

    /**
     * Returns true if the player is sneaking.
     * In an import, accessible via the {@code isSneaking} variable.
     * @return True if the player is sneaking, false otherwise.
     */
    public static boolean isSneaking() {
        return mc.thePlayer != null && mc.thePlayer.isSneaking();
    }

    /**
     * Returns true if the player is sprinting.
     * In an import, accessible via the {@code isSprinting} variable.
     * @return True if the player is sprinting, false otherwise.
     */
    public static boolean isSprinting() {
        return mc.thePlayer != null && mc.thePlayer.isSprinting();
    }

    /**
     * Gets the current server's IP.
     * In an import, accessible via the {@code serverIP} variable.
     * @return The IP of the current server, or "localhost" if the player
     *          is in a single player world.
     */
    public static String getServerIP() {
        if (mc.isSingleplayer()) return "localhost";

        return mc.getCurrentServerData() == null ? null : mc.getCurrentServerData().serverIP;
    }

    /**
     * Gets the current server's name.
     * In an import, accessible via the {@code server} variable.
     * @return The name of the current server, or "SinglePlayer" if the player
     *          is in a single player world.
     */
    public static String getServerName() {
        if (mc.isSingleplayer()) return "SinglePlayer";

        return mc.getCurrentServerData() == null ? null : mc.getCurrentServerData().serverName;
    }

    /**
     * Gets the current server's MOTD.
     * In an import, accessible via the {@code serverMOTD} variable.
     * @return The MOTD of the current server, or "SinglePlayer" if the player
     *          is in a single player world.
     */
    public static String getServerMOTD() {
        if (mc.isSingleplayer()) return "SinglePlayer";

        return mc.getCurrentServerData() == null ? null : mc.getCurrentServerData().serverMOTD;
    }

    /**
     * Gets the ping to the current server.
     * In an import, accessible via the {@code ping} variable.
     * @return The ping to the current server, or 5 if the player
     *          is in a single player world.
     */
    public static Long getPing() {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        if (player == null || mc.isSingleplayer()) {
            return 5L;
        }

        if(Minecraft.getMinecraft().getNetHandler().getPlayerInfo(UUID.fromString(Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString())) != null) {
            return (long) Minecraft.getMinecraft().getNetHandler().getPlayerInfo(
                    UUID.fromString(Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString())
            ).getResponseTime();
        }

        return Minecraft.getMinecraft().getCurrentServerData().pingToServer;
    }

    /**
     * Gets whether or not the minecraft window is active
     * and in the foreground of the user's screen
     * @return whether or not the game is active
     */
    public static boolean isUserTabbedIn() {
        return Display.isActive();
    }

    /**
     * Get the {@link KeyBind} from an already existing
     * Minecraft KeyBinding.
     * @param keyCode the keycode to search for, see Keyboard below. Ex. Keyboard.KEY_A
     * @return the {@link KeyBind} from a Minecraft KeyBinding, or null if one doesn't exist
     * @see <a href="http://legacy.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html">Keyboard</a>
     */
    public static KeyBind getKeyBindFromKey(int keyCode) {
        for (KeyBinding keyBinding : Minecraft.getMinecraft().gameSettings.keyBindings) {
            if (keyBinding.getKeyCode() == keyCode) {
                return new KeyBind(keyBinding);
            }
        }

        return null;
    }

    /**
     * Gets a list of the players in tabs list.
     * @return A string array containing the names of the players in the tabs list.
     *          If the player is in a single player world, returns an array containing
     *          the player's name.
     */
    public static ArrayList<String> getTabList() {
        if (mc.isSingleplayer()) return new ArrayList<>(Collections.singletonList(getPlayerName()));
        if (mc.getNetHandler() == null || mc.getNetHandler().getPlayerInfoMap() == null) return null;

        ArrayList<String> playerNames = new ArrayList<>();

        for (NetworkPlayerInfo playerInfo : mc.getNetHandler().getPlayerInfoMap()) {
            playerNames.add(playerInfo.getGameProfile().getName());
        }

        return playerNames;
    }

    /**
     * Gets the player's X position.
     * In an import, accessible via the {@code posX} variable.
     * @return The player's X position.
     */
    public static Double getPlayerPosX() {
        return mc.thePlayer == null ? null : mc.thePlayer.posX;
    }

    /**
     * Gets the player's Y position.
     * In an import, accessible via the {@code posY} variable.
     * @return The player's Y position.
     */
    public static Double getPlayerPosY() {
        return mc.thePlayer == null ? null : mc.thePlayer.posY;
    }

    /**
     * Gets the player's Z position.
     * In an import, accessible via the {@code posZ} variable.
     * @return The player's Z position.
     */
    public static Double getPlayerPosZ() {
        return mc.thePlayer == null ? null : mc.thePlayer.posZ;
    }

    /**
     * Gets the player's camera pitch.
     * In an import, accessible via the {@code cameraPitch} variable.
     * @return The player's camera pitch.
     */
    public static Float getPlayerPitch() {
        return mc.thePlayer == null ? null : MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch);
    }

    /**
     * Gets the player's camera yaw.
     * In an import, accessible via the {@code cameraYaw} variable.
     * @return The player's camera yaw.
     */
    public static Float getPlayerYaw() {
        return mc.thePlayer == null ? null : MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw);
    }

    /**
     * Gets the direction the player is facing.
     * In an import, accessible via the {@code facing} variable.
     * @return The direction the player is facing, one of the four cardinal directions.
     */
    public static String getPlayerFacing() {
        if (mc.thePlayer == null) {
            return null;
        }

        Float yaw = getPlayerYaw();
        String direction = "";

        if(yaw < 22.5 && yaw > -22.5) {
            direction = "South";
        } else if (yaw < 67.5 && yaw > 22.5) {
            direction = "South West";
        } else if (yaw < 112.5 && yaw > 67.5) {
            direction = "West";
        } else if (yaw < 157.5 && yaw > 112.5) {
            direction = "North West";
        } else if (yaw < -157.5 || yaw > 157.5) {
            direction = "North";
        } else if (yaw > -157.5 && yaw < -112.5) {
            direction = "North East";
        } else if (yaw > -112.5 && yaw < -67.5) {
            direction = "East";
        } else if (yaw > -67.5 && yaw < -22.5) {
            direction = "South East";
        }

        return direction;
    }

    /**
     * Gets the game's FPS count.
     * In an import, accessible via the {@code fps} variable.
     * @return The game's FPS count.
     */
    public static int getPlayerFPS() {
        return Minecraft.getDebugFPS();
    }
}