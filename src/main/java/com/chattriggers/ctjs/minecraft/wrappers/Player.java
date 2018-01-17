package com.chattriggers.ctjs.minecraft.wrappers;

import com.chattriggers.ctjs.minecraft.libs.MinecraftVars;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;

public class Player {

    /**
     * Gets the player object.
     * @return the player object
     */
    public static EntityPlayerSP getPlayer() {
        return Client.getMinecraft().thePlayer;
    }

    /**
     * Gets the player's x position.
     * @return the player's x position
     */
    public static double getX() {
        return getPlayer() == null ? 0 : getPlayer().posX;
    }

    /**
     * Gets the player's y position.
     * @return the player's y position
     */
    public static double getY() {
        return getPlayer() == null ? 0 : getPlayer().posY;
    }

    /**
     * Gets the player's z position.
     * @return the player's z position
     */
    public static double getZ() {
        return getPlayer() == null ? 0 : getPlayer().posZ;
    }

    /**
     * Gets the player's x motion.
     * @return the player's x motion
     */
    public static Double getMotionX() {
        return getPlayer() == null ? 0 : getPlayer().motionX;
    }

    /**
     * Gets the player's y motion.
     * @return the player's y motion
     */
    public static Double getMotionY() {
        return getPlayer() == null ? 0 : getPlayer().motionY;
    }

    /**
     * Gets the player's z motion.
     * @return the player's z motion
     */
    public static Double getMotionZ() {
        return getPlayer() == null ? 0 : getPlayer().motionZ;
    }

    /**
     * Gets the player's camera pitch.
     * @return the player's camera pitch
     */
    public static Float getPitch() {
        return getPlayer() == null ? 0 : MathHelper.wrapAngleTo180_float(getPlayer().rotationPitch);
    }

    /**
     * Gets the player's camera yaw.
     * @return the player's camera yaw
     */
    public static Float getYaw() {
        return getPlayer() == null ? 0 : MathHelper.wrapAngleTo180_float(getPlayer().rotationYaw);
    }

    /**
     * Gets the player's username.
     * @return the player's username
     */
    public static String getName() {
        return Client.getMinecraft().getSession().getUsername();
    }

    /**
     * Gets the player's uuid.
     * @return the player's uuid
     */
    public static String getUUID() {
        return Client.getMinecraft().getSession().getPlayerID();
    }

    /**
     * Gets the player's hp.
     * @return the player's hp
     */
    public static Float getHP() {
        return getPlayer() == null ? 0 : getPlayer().getHealth();
    }

    /**
     * Gets the player's hunger.
     * @return the player's hunger
     */
    public static Integer getHunger() {
        return getPlayer() == null ? 0 : getPlayer().getFoodStats().getFoodLevel();
    }

    /**
     * Gets the player's saturation.
     * @return the player's saturation
     */
    public static Float getSaturation() {
        return getPlayer() == null ? 0 : getPlayer().getFoodStats().getSaturationLevel();
    }

    /**
     * Gets the player's armor points.
     * @return the player's armor points
     */
    public static Integer getArmorPoints() {
        return getPlayer() == null ? 0 : getPlayer().getTotalArmorValue();
    }

    /**
     * Gets the player's air level.<br>
     *
     * The returned value will be an integer. If the player is not taking damage, it
     * will be between 300 (not in water) and 0. If the player is taking damage, it
     * will be between -20 and 0, getting reset to 0 every time the player takes damage.
     *
     * @return the player's air level
     */
    public static Integer getAirLevel() {
        return getPlayer() == null ? 0 : getPlayer().getAir();
    }

    /**
     * Gets the player's xp level.
     * @return the player's xp level
     */
    public static Integer getXPLevel() {
        return getPlayer() == null ? 0 : getPlayer().experienceLevel;
    }

    /**
     * Gets the player's xp progress.
     * @return the player's xp progress
     */
    public static Float getXPProgress() {
        return getPlayer() == null ? 0 : getPlayer().experience;
    }

    /**
     * Gets the biome the player is currently in.
     * @return the biome name
     */
    public static String getBiome() {
        if (getPlayer() == null)
            return "";

        Chunk chunk = MinecraftVars.getWorld().getChunkFromBlockCoords(getPlayer().getPosition());
        BiomeGenBase biome = chunk.getBiome(getPlayer().getPosition(),
                MinecraftVars.getWorld().getWorldChunkManager());

        return biome.biomeName;
    }

    /**
     * Gets the light level at the player's current position.
     * @return the light level at the player's current position
     */
    public static Integer getLightLevel() {
        if (getPlayer() == null || MinecraftVars.getWorld() == null) return 0;

        return MinecraftVars.getWorld().getLight(getPlayer().getPosition());
    }

    /**
     * Checks if if the player is sneaking.
     * @return true if the player is sneaking, false otherwise
     */
    public static boolean isSneaking() {
        return getPlayer() != null && getPlayer().isSneaking();
    }

    /**
     * Checks if the player is sprinting.
     * @return true if the player is sprinting, false otherwise
     */
    public static boolean isSprinting() {
        return getPlayer() != null && getPlayer().isSprinting();
    }

    /**
     * Checks if player can be pushed by water.
     * @return true if the player is flying, false otherwise
     */
    public static boolean isFlying(){
        return !(getPlayer() != null && getPlayer().isPushedByWater());
    }

    /**
     * Checks if player is sleeping.
     * @return true if the player is sleeping, false otherwise
     */
    public static boolean isSleeping(){
        return getPlayer() != null && getPlayer().isPlayerSleeping();
    }

    /**
     * Gets the direction the player is facing.
     * @return The direction the player is facing, one of the four cardinal directions
     */
    public static String facing() {
        if (getPlayer() == null) {
            return "";
        }

        Float yaw = getYaw();

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

        return "";
    }

    /**
     * Gets the player's active potion effects.\
     * @return The player's active potion effects.
     */
    public static String[] getActivePotionEffects() {
        if (getPlayer() == null) return new String[]{};

        ArrayList<String> effects = new ArrayList<>();
        for(PotionEffect effect : getPlayer().getActivePotionEffects()){
            effects.add(effect.toString());
        }
        return effects.toArray(new String[effects.size()]);
    }
}
