//package com.chattriggers.ctjs.minecraft.wrappers;
//
//import com.chattriggers.ctjs.minecraft.objects.message.TextComponent;
//import com.chattriggers.ctjs.minecraft.wrappers.objects.Entity;
//import com.chattriggers.ctjs.minecraft.wrappers.objects.PotionEffect;
//import com.chattriggers.ctjs.minecraft.wrappers.objects.block.Block;
//import com.chattriggers.ctjs.minecraft.wrappers.objects.block.Sign;
//import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Inventory;
//import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item;
//import net.minecraft.block.BlockSign;
//import net.minecraft.client.entity.EntityPlayerSP;
//import net.minecraft.client.network.NetworkPlayerInfo;
//import net.minecraft.scoreboard.ScorePlayerTeam;
//import net.minecraft.world.chunk.Chunk;
//
////#if MC<=10809
//import net.minecraft.util.BlockPos;
//import net.minecraft.util.MathHelper;
//import net.minecraft.util.MovingObjectPosition;
//import net.minecraft.world.biome.BiomeGenBase;
////#else
////$$ import net.minecraft.util.math.BlockPos;
////$$ import net.minecraft.util.math.MathHelper;
////$$ import net.minecraft.util.math.RayTraceResult;
////$$ import net.minecraft.world.biome.Biome;
////#endif
//
//import java.util.ArrayList;
//
//public class Player {
//
//    /**
//     * Gets the player object.
//     *
//     * @return the player object
//     */
//    public static EntityPlayerSP getPlayer() {
//        //#if MC<=10809
//        return Client.getMinecraft().thePlayer;
//        //#else
//        //$$ return Client.getMinecraft().player;
//        //#endif
//    }
//
//    /**
//     * Gets the player's x position.
//     *
//     * @return the player's x position
//     */
//    public static double getX() {
//        return getPlayer() == null ? 0 : getPlayer().posX;
//    }
//
//    /**
//     * Gets the player's y position.
//     *
//     * @return the player's y position
//     */
//    public static double getY() {
//        return getPlayer() == null ? 0 : getPlayer().posY;
//    }
//
//    /**
//     * Gets the player's z position.
//     *
//     * @return the player's z position
//     */
//    public static double getZ() {
//        return getPlayer() == null ? 0 : getPlayer().posZ;
//    }
//
//    /**
//     * Gets the player's x motion.
//     * This is the amount the player will move in the x direction next tick.
//     *
//     * @return the player's x motion
//     */
//    public static double getMotionX() {
//        return getPlayer() == null ? 0 : getPlayer().motionX;
//    }
//
//    /**
//     * Gets the player's y motion.
//     * This is the amount the player will move in the y direction next tick.
//     *
//     * @return the player's y motion
//     */
//    public static double getMotionY() {
//        return getPlayer() == null ? 0 : getPlayer().motionY;
//    }
//
//    /**
//     * Gets the player's z motion.
//     * This is the amount the player will move in the z direction next tick.
//     *
//     * @return the player's z motion
//     */
//    public static double getMotionZ() {
//        return getPlayer() == null ? 0 : getPlayer().motionZ;
//    }
//
//    /**
//     * Gets the player's camera pitch.
//     *
//     * @return the player's camera pitch
//     */
//    public static float getPitch() {
//        return getPlayer() == null
//                ? 0
//                //#if MC<=10809
//                : MathHelper.wrapAngleTo180_float(getPlayer().rotationPitch);
//                //#else
//                //$$ : MathHelper.wrapDegrees(getPlayer().rotationPitch);
//                //#endif
//    }
//
//    /**
//     * Gets the player's camera yaw.
//     *
//     * @return the player's camera yaw
//     */
//    public static float getYaw() {
//        return getPlayer() == null
//                ? 0
//                //#if MC<=10809
//                : MathHelper.wrapAngleTo180_float(getPlayer().rotationYaw);
//                //#else
//                //$$ : MathHelper.wrapDegrees(getPlayer().rotationYaw);
//                //#endif
//    }
//
//    /**
//     * Gets the player's yaw rotation without wrapping.
//     *
//     * @return the yaw
//     */
//    public static float getRawYaw() {
//        return getPlayer() == null ? 0 : getPlayer().rotationYaw;
//    }
//
//    /**
//     * Gets the player's username.
//     *
//     * @return the player's username
//     */
//    public static String getName() {
//        return Client.getMinecraft().getSession().getUsername();
//    }
//
//    /**
//     * Gets the player's uuid.
//     *
//     * @return the player's uuid
//     */
//    public static String getUUID() {
//        return Client.getMinecraft().getSession().getProfile().getId().toString();
//    }
//
//    /**
//     * Gets the player's hp.
//     *
//     * @return the player's hp
//     */
//    public static float getHP() {
//        return getPlayer() == null ? 0 : getPlayer().getHealth();
//    }
//
//    /**
//     * Gets the player's hunger.
//     *
//     * @return the player's hunger
//     */
//    public static int getHunger() {
//        return getPlayer() == null ? 0 : getPlayer().getFoodStats().getFoodLevel();
//    }
//
//    /**
//     * Gets the player's saturation.
//     *
//     * @return the player's saturation
//     */
//    public static float getSaturation() {
//        return getPlayer() == null ? 0 : getPlayer().getFoodStats().getSaturationLevel();
//    }
//
//    /**
//     * Gets the player's armor points.
//     *
//     * @return the player's armor points
//     */
//    public static int getArmorPoints() {
//        return getPlayer() == null ? 0 : getPlayer().getTotalArmorValue();
//    }
//
//    /**
//     * Gets the player's air level.<br>
//     * The returned value will be an integer. If the player is not taking damage, it
//     * will be between 300 (not in water) and 0. If the player is taking damage, it
//     * will be between -20 and 0, getting reset to 0 every time the player takes damage.
//     *
//     * @return the player's air level
//     */
//    public static int getAirLevel() {
//        return getPlayer() == null ? 0 : getPlayer().getAir();
//    }
//
//    /**
//     * Gets the player's xp level.
//     *
//     * @return the player's xp level
//     */
//    public static int getXPLevel() {
//        return getPlayer() == null ? 0 : getPlayer().experienceLevel;
//    }
//
//    /**
//     * Gets the player's xp progress.
//     *
//     * @return the player's xp progress
//     */
//    public static float getXPProgress() {
//        return getPlayer() == null ? 0 : getPlayer().experience;
//    }
//
//    /**
//     * Gets the biome the player is currently in.
//     *
//     * @return the biome name
//     */
//    public static String getBiome() {
//        if (getPlayer() == null)
//            return "";
//
//        Chunk chunk = World.getWorld().getChunkFromBlockCoords(getPlayer().getPosition());
//
//        //#if MC<=10809
//        BiomeGenBase biome = chunk.getBiome(getPlayer().getPosition(), World.getWorld().getWorldChunkManager());
//        //#else
//        //$$ Biome biome = chunk.getBiome(getPlayer().getPosition(), World.getWorld().getBiomeProvider());
//        //#endif
//
//        //#if MC<=10809
//        return biome.biomeName;
//        //#else
//        //$$ return biome.getBiomeName();
//        //#endif
//    }
//
//    /**
//     * Gets the light level at the player's current position.
//     *
//     * @return the light level at the player's current position
//     */
//    public static int getLightLevel() {
//        if (getPlayer() == null || World.getWorld() == null) return 0;
//
//        return World.getWorld().getLight(getPlayer().getPosition());
//    }
//
//    /**
//     * Checks if if the player is sneaking.
//     *
//     * @return true if the player is sneaking, false otherwise
//     */
//    public static boolean isSneaking() {
//        return getPlayer() != null && getPlayer().isSneaking();
//    }
//
//    /**
//     * Checks if the player is sprinting.
//     *
//     * @return true if the player is sprinting, false otherwise
//     */
//    public static boolean isSprinting() {
//        return getPlayer() != null && getPlayer().isSprinting();
//    }
//
//    /**
//     * Checks if player can be pushed by water.
//     *
//     * @return true if the player is flying, false otherwise
//     */
//    public static boolean isFlying() {
//        return !(getPlayer() != null && getPlayer().isPushedByWater());
//    }
//
//    /**
//     * Checks if player is sleeping.
//     *
//     * @return true if the player is sleeping, false otherwise
//     */
//    public static boolean isSleeping() {
//        return getPlayer() != null && getPlayer().isPlayerSleeping();
//    }
//
//    /**
//     * Gets the direction the player is facing.
//     *
//     * @return The direction the player is facing, one of the four cardinal directions
//     */
//    public static String facing() {
//        if (getPlayer() == null) {
//            return "";
//        }
//
//        Float yaw = getYaw();
//
//        if (yaw < 22.5 && yaw > -22.5) {
//            return "South";
//        } else if (yaw < 67.5 && yaw > 22.5) {
//            return "South West";
//        } else if (yaw < 112.5 && yaw > 67.5) {
//            return "West";
//        } else if (yaw < 157.5 && yaw > 112.5) {
//            return "North West";
//        } else if (yaw < -157.5 || yaw > 157.5) {
//            return "North";
//        } else if (yaw > -157.5 && yaw < -112.5) {
//            return "North East";
//        } else if (yaw > -112.5 && yaw < -67.5) {
//            return "East";
//        } else if (yaw > -67.5 && yaw < -22.5) {
//            return "South East";
//        }
//
//        return "";
//    }
//
//    /**
//     * Gets the player's active potion effects.
//     *
//     * @return The player's active potion effects.
//     */
//    public static ArrayList<PotionEffect> getActivePotionEffects() {
//        if (getPlayer() == null) return new ArrayList<>();
//
//        ArrayList<PotionEffect> effects = new ArrayList<>();
//
//        for (net.minecraft.potion.PotionEffect effect : getPlayer().getActivePotionEffects()) {
//            effects.add(new PotionEffect(effect));
//        }
//
//        return effects;
//    }
//
//    /**
//     * Gets the current object that the player is looking at,
//     * whether that be a block or an entity. Returns an air block when not looking
//     * at anything.
//     *
//     * @return the {@link Block}, {@link Sign}, or {@link Entity} being looked at
//     */
//    public static Object lookingAt() {
//        if (getPlayer() == null
//                || World.getWorld() == null
//                || Client.getMinecraft().objectMouseOver == null)
//            return new Block(0);
//
//        //#if MC<=10809
//        MovingObjectPosition mop = Client.getMinecraft().objectMouseOver;
//        boolean isBlock = mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK;
//        boolean isEntity = mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY;
//        //#else
//        //$$ RayTraceResult mop = Client.getMinecraft().objectMouseOver;
//        //$$ boolean isBlock = mop.typeOfHit == RayTraceResult.Type.BLOCK;
//        //$$ boolean isEntity = mop.typeOfHit == RayTraceResult.Type.ENTITY;
//        //#endif
//
//        if (isBlock) {
//            BlockPos pos = mop.getBlockPos();
//            Block block = new Block(World.getWorld().getBlockState(pos).getBlock()).setBlockPos(pos);
//
//            if (block.getBlock() instanceof BlockSign) {
//                return new Sign(block);
//            }
//
//            return block;
//        } else if (isEntity) {
//            return new Entity(mop.entityHit);
//        } else {
//            return new Block(0);
//        }
//    }
//
//    /**
//     * Gets the player's currently held item.
//     *
//     * @return the item
//     */
//    public static Item getHeldItem() {
//        return new Item(Player.getPlayer().inventory.getCurrentItem());
//    }
//
//    /**
//     * Gets the inventory of the player, i.e. the inventory accessed by 'e'.
//     *
//     * @return the player's inventory
//     */
//    public static Inventory getInventory() {
//        return new Inventory(getPlayer().inventory);
//    }
//
//    /**
//     * Gets the display name for the player,
//     * i.e. the name shown in tab list and in the player's nametag.
//     * @return the display name
//     */
//    public static TextComponent getDisplayName() {
//        return new TextComponent(getPlayerName(getPlayerInfo()));
//    }
//
//    /**
//     * Sets the name for this player shown in tab list
//     *
//     * @param textComponent the new name to display
//     */
//    public static void setTabDisplayName(TextComponent textComponent) {
//        getPlayerInfo().setDisplayName(textComponent.getChatComponentText());
//    }
//
//    private static String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn) {
//        return networkPlayerInfoIn.getDisplayName() != null
//                ? networkPlayerInfoIn.getDisplayName().getFormattedText()
//                : ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
//    }
//
//    public static NetworkPlayerInfo getPlayerInfo() {
//        //#if MC<=10809
//        return Client.getMinecraft().getNetHandler().getPlayerInfo(getPlayer().getUniqueID());
//        //#else
//        //$$ return Client.getConnection().getPlayerInfo(getPlayer().getUniqueID());
//        //#endif
//    }
//
//    /**
//     * Gets the inventory the user currently has open, i.e. a chest.
//     *
//     * @return the currently opened inventory
//     */
//    public static Inventory getOpenedInventory() {
//        return new Inventory(getPlayer().openContainer);
//    }
//
//    public static class armor {
//        /**
//         * @return the item in the player's helmet slot
//         */
//        public static Item getHelmet() {
//            return getInventory().getStackInSlot(39);
//        }
//
//        /**
//         * @return the item in the player's chestplate slot
//         */
//        public static Item getChestplate() {
//            return getInventory().getStackInSlot(38);
//        }
//
//        /**
//         * @return the item in the player's leggings slot
//         */
//        public static Item getLeggings() {
//            return getInventory().getStackInSlot(37);
//        }
//
//        /**
//         * @return the item in the player's boots slot
//         */
//        public static Item getBoots() {
//            return getInventory().getStackInSlot(36);
//        }
//    }
//}
