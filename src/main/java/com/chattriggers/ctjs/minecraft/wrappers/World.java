package com.chattriggers.ctjs.minecraft.wrappers;

import com.chattriggers.ctjs.minecraft.libs.ChatLib;
import com.chattriggers.ctjs.minecraft.mixins.MixinSoundHandler;
import com.chattriggers.ctjs.minecraft.mixins.MixinSoundManager;
import com.chattriggers.ctjs.minecraft.wrappers.objects.*;
import com.chattriggers.ctjs.utils.console.Console;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import paulscode.sound.SoundSystem;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
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
     * Play a sound at the player location.
     *
     * @param name   the name of the sound
     * @param volume the volume of the sound
     * @param pitch  the pitch of the sound
     */
    public static void playSound(String name, float volume, float pitch) {
        Player.getPlayer().playSound(name, volume, pitch);
    }

    public static void playCustomSound() {
        SoundManager sndManager = ((MixinSoundHandler) Client.getMinecraft().getSoundHandler()).getSndManager();
        SoundSystem sndSystem = ((MixinSoundManager) sndManager).getSndSystem();
        sndSystem.newSource(
                false,
                "test",
                "./config/ChatTriggers/images/White_noise.ogg",
                false,
                (float)Player.getX(),
                (float)Player.getY(),
                (float)Player.getZ(),
                0,
                16
        );
    }

    /**
     * Play a record at location x, y, and z.<br>
     * Use "null" as name in the same location to stop record.
     *
     * @param name  the name of the sound/record
     * @param x     the x location
     * @param y     the y location
     * @param z     the z location
     */
    public static void playRecord(String name, float x, float y, float z) {
        getWorld().playRecord(new BlockPos(x, y, z), name);
    }

    /**
     * Display a title.
     *
     * @param title    title text
     * @param subtitle subtitle text
     * @param fadeIn   time to fade in
     * @param time     time to stay on screen
     * @param fadeOut  time to fade out
     */
    public static void showTitle(String title, String subtitle, int fadeIn, int time, int fadeOut) {
        Client.getMinecraft().ingameGUI.displayTitle(ChatLib.addColor(title), null, fadeIn, time, fadeOut);
        Client.getMinecraft().ingameGUI.displayTitle(null, ChatLib.addColor(subtitle), 0, 0, 0);
        Client.getMinecraft().ingameGUI.displayTitle(null, null, fadeIn, time, fadeOut);
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
     * Gets all of the players in the world, and returns their wrapped versions.
     *
     * @return the players
     */
    public static List<PlayerMP> getAllPlayers() {
        List<PlayerMP> players = new ArrayList<>();

        if (getWorld() != null) {
            for (EntityPlayer player : getWorld().playerEntities) {
                players.add(new PlayerMP(player));
            }
        }

        return players;
    }

    /**
     * Gets a player by their username, must be in the currently loaded world!
     *
     * @param name the username
     * @return the player with said username
     * @throws IllegalArgumentException if the player is not valid
     */
    public static PlayerMP getPlayerByName(String name) throws IllegalArgumentException {
        return new PlayerMP(getWorld().getPlayerEntityByName(name));
    }

    /**
     * Gets the chunk that contains certain coordinates
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return the chunk
     */
    public static Chunk getChunk(int x, int y, int z) {
        return new Chunk(getWorld().getChunkFromBlockCoords(new BlockPos(x, y, z)));
    }

    /**
     * Gets every entity loaded in the world
     *
     * @return the entity list
     */
    public static List<Entity> getAllEntities() {
        return getWorld().loadedEntityList
                .stream()
                .map(Entity::new)
                .collect(Collectors.toList());
    }

    /**
     * Gets every entity loaded in the world of a certain class
     *
     * @param clazz the class to filter for (Use {@code Java.type().class} to get this)
     * @return the entity list
     */
    public static List<Entity> getAllEntitiesOfType(Class clazz) {
        System.out.println(clazz);
        return getAllEntities()
                .stream()
                .filter(entity -> entity.getEntity().getClass().equals(clazz))
                .collect(Collectors.toList());
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

        /**
         * Spawns a particle into the world with the given attributes,
         * which can be configured further with the returned {@link Particle}
         *
         * @param particle the name of the particle to spawn, see {@link #getParticleNames()}
         * @param x the x coordinate to spawn the particle at
         * @param y the y coordinate to spawn the particle at
         * @param z the z coordinate to spawn the particle at
         * @param xSpeed the motion the particle should have in the x direction
         * @param ySpeed the motion the particle should have in the y direction
         * @param zSpeed the motion the particle should have in the z direction
         * @return the actual particle for further configuration
         */
        public static Particle spawnParticle(String particle, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
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
                        x, y, z, xSpeed, ySpeed, zSpeed, new int[]{}
                );

                return new Particle(fx);
            } catch (Exception e) {
                Console.getInstance().printStackTrace(e);
            }

            return null;
        }

        public static void spawnParticle(EntityFX particle) {
            Client.getMinecraft().effectRenderer.addEffect(particle);
        }
    }
}
