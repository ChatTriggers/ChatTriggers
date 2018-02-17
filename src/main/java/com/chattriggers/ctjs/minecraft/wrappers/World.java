package com.chattriggers.ctjs.minecraft.wrappers;

import com.chattriggers.ctjs.minecraft.wrappers.objects.Block;
import com.chattriggers.ctjs.minecraft.wrappers.objects.Particle;
import com.chattriggers.ctjs.utils.console.Console;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Method;

public class World {
    /**
     * Gets the world object.
     *
     * @return the world object
     */
    public static WorldClient getWorld() {
        return Client.getMinecraft().theWorld;
    }

    /**
     * Returns true if world is currently raining.
     *
     * @return true if world is raining, false otherwise
     */
    public static Boolean isRaining() {
        return getWorld().getWorldInfo().isRaining();
    }

    /**
     * Gets the raining strength.
     *
     * @return the raining strength
     */
    public static float getRainingStrength() {
        return getWorld().rainingStrength;
    }

    /**
     * Gets the world time.
     *
     * @return the world time
     */
    public static long getTime() {
        return getWorld().getWorldTime();
    }

    /**
     * Gets the world difficulty.
     *
     * @return the world difficulty
     */
    public static String getDifficulty() {
        return getWorld().getDifficulty().toString();
    }

    /**
     * Gets the moon phase.
     *
     * @return the moon phase
     */
    public static int getMoonPhase() {
        return getWorld().getMoonPhase();
    }

    /**
     * Gets the world seed.
     *
     * @return the world seed
     */
    public static long getSeed() {
        return getWorld().getSeed();
    }

    /**
     * Gets the world type.
     *
     * @return the world type
     */
    public static String getType() {
        return getWorld().getWorldType().getWorldTypeName();
    }

    /**
     * Gets the {@link Block} at a location in the world.
     *
     * @param x the x position
     * @param y the y position
     * @param z the z position
     * @return the {@link Block} at the location
     */
    public static Block getBlockAt(int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        IBlockState blockState = World.getWorld().getBlockState(blockPos);

        return new Block(blockState.getBlock()).setBlockPos(blockPos);
    }

    /**
     * World border object to get border parameters
     */
    public static class border {
        /**
         * Gets the border center x location.
         *
         * @return the border center x location
         */
        public static double getCenterX() {
            return getWorld().getWorldBorder().getCenterX();
        }

        /**
         * Gets the border center z location.
         *
         * @return the border center z location
         */
        public static double getCenterZ() {
            return getWorld().getWorldBorder().getCenterZ();
        }

        /**
         * Gets the border size.
         *
         * @return the border size
         */
        public static int getSize() {
            return getWorld().getWorldBorder().getSize();
        }

        /**
         * Gets the border target size.
         *
         * @return the border target size
         */
        public static double getTargetSize() {
            return getWorld().getWorldBorder().getTargetSize();
        }

        /**
         * Gets the border time until the target size is met.
         *
         * @return the border time until target
         */
        public static long getTimeUntilTarget() {
            return getWorld().getWorldBorder().getTimeUntilTarget();
        }
    }

    /**
     * World spawn object for getting spawn location.
     */
    public static class spawn {
        /**
         * Gets the spawn x location.
         *
         * @return the spawn x location.
         */
        public static int getX() {
            return getWorld().getSpawnPoint().getX();
        }

        /**
         * Gets the spawn y location.
         *
         * @return the spawn y location.
         */
        public static int getY() {
            return getWorld().getSpawnPoint().getY();
        }

        /**
         * Gets the spawn z location.
         *
         * @return the spawn z location.
         */
        public static int getZ() {
            return getWorld().getSpawnPoint().getZ();
        }
    }

    public static class particle {
        /**
         * Gets an array of all the different particle names you can pass
         * to {@link #spawnParticle(String, double, double, double, double, double, double)}
         *
         * @return the array of name strings
         */
        public static String[] getParticleNames() {
            String[] names = new String[EnumParticleTypes.values().length];

            for (int i = 0; i < EnumParticleTypes.values().length; i++) {
                names[i] = EnumParticleTypes.values()[i].name();
            }

            return names;
        }

        public static Particle spawnParticle(String particle, double x, double y, double z, double dx, double dy, double dz) {
            EnumParticleTypes particleType = EnumParticleTypes.valueOf(particle);

            try {
                Method method = ReflectionHelper.findMethod(
                        RenderGlobal.class,
                        Client.getMinecraft().renderGlobal,
                        new String[]{
                                "spawnEntityFX",
                                "func_174974_b"
                        },
                        int.class,
                        boolean.class,
                        double.class,
                        double.class,
                        double.class,
                        double.class,
                        double.class,
                        double.class,
                        int[].class
                );

                EntityFX fx = (EntityFX) method.invoke(Client.getMinecraft().renderGlobal,
                        particleType.getParticleID(),
                        particleType.getShouldIgnoreRange(),
                        x, y, z, dx, dy, dz, new int[]{}
                );

                return new Particle(fx);
            } catch (Exception e) {
                Console.getConsole().printStackTrace(e);
            }

            return null;
        }
    }
}
