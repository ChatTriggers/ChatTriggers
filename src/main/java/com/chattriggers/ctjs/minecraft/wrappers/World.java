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
     * Returns true if world is currently raining.
     * @return true if world is raining, false otherwise
     */
    public static Boolean isRaining() {
        return getWorld().getWorldInfo().isRaining();
    }

    /**
     * Gets the raining strength.
     * @return the raining strength
     */
    public static float getRainingStrength() {
        return getWorld().rainingStrength;
    }

    /**
     * Gets the world time.
     * @return the world time
     */
    public static long getTime() {
        return getWorld().getWorldTime();
    }

    /**
     * Gets the world difficulty.
     * @return the world difficulty
     */
    public static String getDifficulty() {
        return getWorld().getDifficulty().toString();
    }

    /**
     * Gets the moon phase.
     * @return the moon phase
     */
    public static int getMoonPhase() {
        return getWorld().getMoonPhase();
    }

    /**
     * World spawn object for getting spawn location.
     */
    public static class spawn {
        /**
         * Gets the spawn x location.
         * @return the spawn x location.
         */
        public static int getX() {
            return getWorld().getSpawnPoint().getX();
        }

        /**
         * Gets the spawn y location.
         * @return the spawn y location.
         */
        public static int getY() {
            return getWorld().getSpawnPoint().getY();
        }

        /**
         * Gets the spawn z location.
         * @return the spawn z location.
         */
        public static int getZ() {
            return getWorld().getSpawnPoint().getZ();
        }
    }

    /**
     * Gets the world seed.
     * @return the world seed
     */
    public static long getSeed() {
        return getWorld().getSeed();
    }

    /**
     * Gets the world type.
     * @return the world type
     */
    public static String getType() {
        return getWorld().getWorldType().getWorldTypeName();
    }

    /**
     * World border object to get border parameters
     */
    public static class border {
        /**
         * Gets the border center x location.
         * @return the border center x location
         */
        public static double getCenterX() {
            return getWorld().getWorldBorder().getCenterX();
        }

        /**
         * Gets the border center z location.
         * @return the border center z location
         */
        public static double getCenterZ() {
            return getWorld().getWorldBorder().getCenterZ();
        }

        /**
         * Gets the border size.
         * @return the border size
         */
        public static int getSize() {
            return getWorld().getWorldBorder().getSize();
        }

        /**
         * Gets the border target size.
         * @return the border target size
         */
        public static double getTargetSize() {
            return getWorld().getWorldBorder().getTargetSize();
        }

        /**
         * Gets the border time until the target size is met.
         * @return the border time until target
         */
        public static long getTimeUntilTarget() {
            return getWorld().getWorldBorder().getTimeUntilTarget();
        }
>>>>>>> upstream/master
    }
}
