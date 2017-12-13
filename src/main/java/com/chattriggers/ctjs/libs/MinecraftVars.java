package com.chattriggers.ctjs.libs;

import com.chattriggers.ctjs.objects.KeyBind;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
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
     * Gets the Minecraft object.
     * @return the Minecraft object
     */
    public static Minecraft getMinecraft() {
        return mc;
    }

    /**
     * Gets the world object.
     * @return the world object
     */
    public static WorldClient getWorld() {
        try {
            return (WorldClient) ReflectionHelper.findField(Minecraft.class,
                    "theWorld", "world" /* add obfuscated names too*/)
                    .get(getMinecraft());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return getMinecraft().theWorld;
        }
    }

    /**
     * Gets the player object.
     * @return the player object
     */
    public static EntityPlayerSP getPlayer() {
        try {
            return (EntityPlayerSP) ReflectionHelper.findField(Minecraft.class,
                    "thePlayer", "player" /* add obfuscated names too*/)
                    .get(getMinecraft());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return getMinecraft().thePlayer;
        }
    }

    /**
     * Gets the connection object.
     * @return the connection object
     */
    public static NetHandlerPlayClient getConnection() {
        return getMinecraft().getNetHandler();
    }

    /**
     * Gets the player's username.
     * In an import, accessible via the {@code playerName} variable.
     * @return The username of the user.
     */
    public static String getPlayerName() {
        return getMinecraft().getSession().getUsername();
    }

    /**
     * Gets the player's UUID.
     * In an import, accessible via the {@code playerUUID} variable.
     * @return The UUID of the user.
     */
    public static String getPlayerUUID() {
        return getMinecraft().getSession().getPlayerID();
    }

    /**
     * Gets the player's HP.
     * In an import, accessible via the {@code hp} variable.
     * @return The player's HP.
     */
    public static Float getPlayerHP() {
        return getPlayer() == null ? null : getPlayer().getHealth();
    }

    /**
     * Gets the player's hunger level.
     * In an import, accessible via the {@code hunger} variable.
     * @return The player's hunger level.
     */
    public static Integer getPlayerHunger() {
        return getPlayer() == null ? null : getPlayer().getFoodStats().getFoodLevel();
    }

    /**
     * Gets the player's saturation level.
     * In an import, accessible via the {@code saturation} variable.
     * @return The player's saturation level.
     */
    public static Float getPlayerSaturation() {
        return getPlayer() == null ? null : getPlayer().getFoodStats().getSaturationLevel();
    }

    /**
     * Gets the player's armor points.
     * In an import, accessible via the {@code armorPoints} variable.
     * @return The player's armor points.
     */
    public static Integer getPlayerArmorPoints() {
        return getPlayer() == null ? null : getPlayer().getTotalArmorValue();
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
        return getPlayer() == null ? null : getPlayer().getAir();
    }

    /**
     * Gets the player's XP level.
     * In an import, accessible via the {@code xpLevel} variable.
     * @return The player's XP level.
     */
    public static Integer getXPLevel() {
        return getPlayer() == null ? null : getPlayer().experienceLevel;
    }

    /**
     * Gets the player's XP progress towards the next XP level.
     * In an import, accessible via the {@code xpProgress} variable.
     * @return The player's xp progress.
     */
    public static Float getXPProgress() {
        return getPlayer() == null ? null : getPlayer().experience;
    }

    /**
     * Gets the biome the player is currently in.
     * In an import, accessible via the {@code biome} variable.
     * @return The biome the player is in.
     */
    public static String getPlayerBiome() {
        if (getPlayer() == null) {
            return null;
        }
        Chunk chunk = getWorld().getChunkFromBlockCoords(getPlayer().getPosition());
        BiomeGenBase biome = chunk.getBiome(getPlayer().getPosition(), getWorld().getWorldChunkManager());

        return biome.biomeName;
    }

    /**
     * Gets the light level at the player's current position.
     * In an import, accessible via the {@code lightLevel} variable.
     * @return The light level at the player's current position.
     */
    public static Integer getPlayerLightLevel() {
        if (getPlayer() == null || getWorld() == null) return null;

        return getWorld().getLight(getPlayer().getPosition());
    }

    /**
     * Returns true if the player has the chat open.
     * In an import, accessible via the {@code inChat} variable.
     * @return True if the player has the chat open, false otherwise.
     */
    public static boolean isInChat() {
        return getMinecraft().currentScreen instanceof GuiChat;
    }

    /**
     * Returns true if the player has the tab list open.
     * In an import, accessible via the {@code inTab} variable.
     * @return True if the player has the tab list open, false otherwise.
     */
    public static boolean isInTab() {
        return getMinecraft().gameSettings.keyBindPlayerList.isKeyDown();
    }

    /**
     * Returns true if the player is sneaking.
     * In an import, accessible via the {@code isSneaking} variable.
     * @return True if the player is sneaking, false otherwise.
     */
    public static boolean isSneaking() {
        return getPlayer() != null && getPlayer().isSneaking();
    }

    /**
     * Returns true if the player is sprinting.
     * In an import, accessible via the {@code isSprinting} variable.
     * @return True if the player is sprinting, false otherwise.
     */
    public static boolean isSprinting() {
        return getPlayer() != null && getPlayer().isSprinting();
    }

    /**
     * Gets the current server's IP.
     * In an import, accessible via the {@code serverIP} variable.
     * @return The IP of the current server, or "localhost" if the player
     *          is in a single player world.
     */
    public static String getServerIP() {
        if (getMinecraft().isSingleplayer()) return "localhost";

        return getMinecraft().getCurrentServerData() == null ? null : getMinecraft().getCurrentServerData().serverIP;
    }

    /**
     * Gets the current server's name.
     * In an import, accessible via the {@code server} variable.
     * @return The name of the current server, or "SinglePlayer" if the player
     *          is in a single player world.
     */
    public static String getServerName() {
        if (getMinecraft().isSingleplayer()) return "SinglePlayer";

        return getMinecraft().getCurrentServerData() == null ? null : getMinecraft().getCurrentServerData().serverName;
    }

    /**
     * Gets the current server's MOTD.
     * In an import, accessible via the {@code serverMOTD} variable.
     * @return The MOTD of the current server, or "SinglePlayer" if the player
     *          is in a single player world.
     */
    public static String getServerMOTD() {
        if (getMinecraft().isSingleplayer()) return "SinglePlayer";

        return getMinecraft().getCurrentServerData() == null ? null : getMinecraft().getCurrentServerData().serverMOTD;
    }

    /**
     * Gets the ping to the current server.
     * In an import, accessible via the {@code ping} variable.
     * @return The ping to the current server, or 5 if the player
     *          is in a single player world.
     */
    public static Long getPing() {
        EntityPlayer player = getPlayer();

        if (player == null || getMinecraft().isSingleplayer()) {
            return 5L;
        }

        if(getConnection().getPlayerInfo(UUID.fromString(getPlayer().getGameProfile().getId().toString())) != null) {
            return (long) getConnection().getPlayerInfo(
                    UUID.fromString(getPlayer().getGameProfile().getId().toString())
            ).getResponseTime();
        }

        return getMinecraft().getCurrentServerData().pingToServer;
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
        for (KeyBinding keyBinding : getMinecraft().gameSettings.keyBindings) {
            if (keyBinding.getKeyCode() == keyCode) {
                return new KeyBind(keyBinding);
            }
        }

        return null;
    }

    /**
     * Get the {@link KeyBind} from an already existing
     * Minecraft KeyBinding, else, return a new one.
     * @param keyCode the keycode to search for, see Keyboard below. Ex. Keyboard.KEY_A
     * @return the {@link KeyBind} from a Minecraft KeyBinding, or null if one doesn't exist
     * @see <a href="http://legacy.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html">Keyboard</a>
     */
    public static KeyBind getKeyBindFromKey(int keyCode, String description) {
        for (KeyBinding keyBinding : getMinecraft().gameSettings.keyBindings) {
            if (keyBinding.getKeyCode() == keyCode) {
                return new KeyBind(keyBinding);
            }
        }

        return new KeyBind(description, keyCode);
    }

    /**
     * Gets the player's X position.
     * In an import, accessible via the {@code posX} variable.
     * @return The player's X position.
     */
    public static Double getPlayerPosX() {
        return getPlayer() == null ? null : getPlayer().posX;
    }

    /**
     * Gets the player's Y position.
     * In an import, accessible via the {@code posY} variable.
     * @return The player's Y position.
     */
    public static Double getPlayerPosY() {
        return getPlayer() == null ? null : getPlayer().posY;
    }

    /**
     * Gets the player's Z position.
     * In an import, accessible via the {@code posZ} variable.
     * @return The player's Z position.
     */
    public static Double getPlayerPosZ() {
        return getPlayer() == null ? null : getPlayer().posZ;
    }

    /**
     * Gets the player's X motion.
     * In an import, accessible via the {@code motionX} variable.
     * @return The player's X motion.
     */
    public static Double getPlayerMotionX() {
        return getPlayer() == null ? null : getPlayer().motionX;
    }

    /**
     * Gets the player's Y motion.
     * In an import, accessible via the {@code motionY} variable.
     * @return The player's Y motion.
     */
    public static Double getPlayerMotionY() {
        return getPlayer() == null ? null : getPlayer().motionY;
    }

    /**
     * Gets the player's Z motion.
     * In an import, accessible via the {@code motionZ} variable.
     * @return The player's Z motion.
     */
    public static Double getPlayerMotionZ() {
        return getPlayer() == null ? null : getPlayer().motionZ;
    }

    /**
     * Gets the player's camera pitch.
     * In an import, accessible via the {@code cameraPitch} variable.
     * @return The player's camera pitch.
     */
    public static Float getPlayerPitch() {
        return getPlayer() == null ? null : MathHelper.wrapAngleTo180_float(getPlayer().rotationPitch);
    }

    /**
     * Gets the player's camera yaw.
     * In an import, accessible via the {@code cameraYaw} variable.
     * @return The player's camera yaw.
     */
    public static Float getPlayerYaw() {
        return getPlayer() == null ? null : MathHelper.wrapAngleTo180_float(getPlayer().rotationYaw);
    }

    /**
     * Gets the direction the player is facing.
     * In an import, accessible via the {@code facing} variable.
     * @return The direction the player is facing, one of the four cardinal directions.
     */
    public static String getPlayerFacing() {
        if (getPlayer() == null) {
            return null;
        }

        Float yaw = getPlayerYaw();
        if (yaw == null) return null;

        if(yaw < 22.5 && yaw > -22.5) {
            return "South";
        } else if (yaw < 67.5 && yaw > 22.5) {
            return "South West";
        } else if (yaw < 112.5 && yaw > 67.5) {
            return "West";
        } else if (yaw < 157.5 && yaw > 112.5) {
            return "North West";
        } else if (yaw < -157.5 || yaw > 157.5) {
            return "North";
        } else if (yaw > -157.5 && yaw < -112.5) {
            return "North East";
        } else if (yaw > -112.5 && yaw < -67.5) {
            return "East";
        } else if (yaw > -67.5 && yaw < -22.5) {
            return "South East";
        }

        return null;
    }

    /**
     * Gets the game's FPS count.
     * In an import, accessible via the {@code fps} variable.
     * @return The game's FPS count.
     */
    public static int getPlayerFPS() {
        return Minecraft.getDebugFPS();
    }

    /**
     * Gets the player's active potion effects.
     * In an import, accessible via the {@code potEffects} variable.
     * @return The player's active potion effects.
     */
    public static String[] getActivePotionEffects(){
        if (getPlayer() == null) return null;
        
        ArrayList<String> effects = new ArrayList<>();
        for(PotionEffect effect : getPlayer().getActivePotionEffects()){
            effects.add(effect.toString());
        }
        return effects.toArray(new String[effects.size()]);
    }

    /**
     * Gets the player's minecraft version.
     * In an import, accessible via the {@code mcVersion} variable.
     * @return The player's minecraft version.
     */
    public static String getMinecraftVersion() {
        return getMinecraft().getVersion();
    }

    /**
     * Gets the player's max memory.
     * In an import, accessible via the {@code maxMem} variable.
     * @return The player's max memory.
     */
    public static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    /**
     * Gets the player's total memory.
     * In an import, accessible via the {@code totalMem} variable.
     * @return The player's total memory.
     */
    public static long getTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    /**
     * Gets the player's free memory.
     * In an import, accessible via the {@code freeMem} variable.
     * @return The player's free memory.
     */
    public static long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    /**
     * Gets the player's memory usage.
     * In an import, accessible via the {@code memUsage} variable.
     * @return The player's memory usage.
     */
    public static int getMemoryUsage() {
        return Math.round((getTotalMemory() - getFreeMemory()) * 100 / getMaxMemory());
    }

    /**
     * Checks if player is pushed by water currently.
     * In an import, accessible via the {@code isFlying} variable.
     * @return If the player is flying (and false if the player does not exist)
     */
    public static boolean isFlying(){
        return !(getPlayer() != null && getPlayer().isPushedByWater());
    }

    /**
     * Checks if player is sleeping.
     * In an import, accessible via the {@code isSleeping} variable.
     * @return If the player is sleeping (and false if the player does not exist)
     */
    public static boolean isSleeping(){
        return getPlayer() != null && getPlayer().isPlayerSleeping();
    }

    /**
     * Gets the system time.
     * @return the system time
     */
    public static Long getSystemTime() {
        return Minecraft.getSystemTime();
    }
}