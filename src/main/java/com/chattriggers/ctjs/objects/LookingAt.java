package com.chattriggers.ctjs.objects;

import com.google.gson.Gson;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.EnumSkyBlock;

import java.util.HashMap;

public class LookingAt {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static MovingObjectPosition mop;

    private static Entity entity;
    private static BlockPos pos;
    private static Block block;

    public static void update() {
        if (mc.thePlayer == null || mc.theWorld == null || mc.objectMouseOver == null) return;

        mop = mc.objectMouseOver;

        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            pos = mop.getBlockPos();
            block = mc.theWorld.getBlockState(pos).getBlock();
            entity = null;
        } else if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
            entity = mop.entityHit;
            pos = null; block = null;
        } else {
            pos = null; block = null; entity = null;
        }
    }

    /**
     * Gets the type of the object the player is looking at.
     *
     * If the player is looking at an entity, returns "entity". If
     * the player is looking at a block, return "block". If the player
     * is not looking at anything, returns "null".
     *
     * @return The object type.
     */
    public static String getType() {
        if (entity != null) return "entity";
        else if (block != null) return "block";
        else return "null";
    }

    /**
     * Gets the name of the object the player is looking at.
     *
     * Example return values: "Sandstone", "Wolf", "Oak Fence", "Zombie Pigman".
     * Returns "null" if the player is not looking at anything.
     *
     * @return The object name.
     */
    public static String getName() {
        if (entity != null) return entity.getName();
        else if (block != null) return block.getLocalizedName();
        else return "null";
    }

    /**
     * Gets the distance of the object from the player. Returns -1
     * if the player is not looking at anything.
     *
     * @return The distance of the object from the player.
     */
    public static double getDistanceFromPlayer() {
        if (block != null) {
            return Math.sqrt(Math.pow(pos.getX() - mc.thePlayer.posX, 2) + Math.pow(pos.getY() - mc.thePlayer.posY, 2) + Math.pow(pos.getZ() - mc.thePlayer.posZ, 2));
        } else if (entity != null) {
            return (double) entity.getDistanceToEntity(mc.thePlayer);
        } else {
            return -1d;
        }
    }

    /**
     * Gets the X coordinate of the object the player is looking at.
     *
     * @return A float number representing the object's X coordinate.
     */
    public static double getPosX() {
        if (entity != null) return entity.posX;
        else if (pos != null) return (double) pos.getX();
        else return 0d;
    }

    /**
     * Gets the Y coordinate of the object the player is looking at.
     *
     * @return A float number representing the object's Y coordinate.
     */
    public static double getPosY() {
        if (entity != null) return entity.posY;
        else if (pos != null) return (double) pos.getY();
        else return 0d;
    }

    /**
     * Gets the Z coordinate of the object the player is looking at.
     *
     * @return A float number representing the object's Z coordinate.
     */
    public static double getPosZ() {
        if (entity != null) return entity.posZ;
        else if (pos != null) return (double) pos.getY();
        else return 0d;
    }

    /**
     * Gets the metadata of the block the player is looking at, or -1
     * if the player is looking at either an entity or nothing.
     *
     * @return The block's metadata.
     */
    public static int getBlockMetadata() {
        if (block == null) return -1;
        return block.getMetaFromState(mc.theWorld.getBlockState(pos));
    }

    /**
     * Gets the unlocalized name of the block the player is looking at,
     * or "null" if the player is looking at either an entity or nothing.
     *
     * @return The block's unlocalized name.
     */
    public static String getBlockUnlocalizedName() {
        if (block == null) return "null";
        return block.getUnlocalizedName().replace("tile.", "");
    }

    /**
     * Gets the registry name of the block the player is looking at,
     * or "null" if the player is looking at either an entity or nothing.
     *
     * @return The block's registry name.
     */
    public static String getBlockRegistryName() {
        if (block == null) return "null";

        String rn = block.getRegistryName().replace("minecraft:", "");

        if (rn.startsWith("double_") && rn.endsWith("_slab") || rn.endsWith("slab2")) rn = rn.substring(7);
        else if (rn.startsWith("lit_") && !rn.endsWith("pumpkin")) rn = rn.substring(4);
        else if (rn.contains("daylight_detector_inverted")) rn = "daylight_detector";
        else if (rn.equals("standing_sign") || rn.equals("wall_sign")) rn = "sign";
        else if (rn.endsWith("_redstone_torch")) rn = "redstone_torch";
        else if (rn.equals("pumpkin_stem")) rn = "pumpkin_seeds";
        else if (rn.endsWith("comparator")) rn = "comparator";
        else if (rn.equals("melon_stem")) rn = "melon_seeds";
        else if (rn.equals("redstone_wire")) rn = "redstone";
        else if (rn.equals("double_plant")) rn = "tallgrass";
        else if (rn.endsWith("_repeater")) rn = "repeater";
        else if (rn.equals("piston_head")) rn = "piston";
        else if (rn.endsWith("_banner")) rn = "banner";
        else if (rn.equals("potatoes")) rn = "potato";
        else if (rn.equals("tripwire")) rn = "string";
        else if (rn.equals("carrots")) rn = "carrot";
        else if (rn.equals("farmland")) rn = "dirt";

        return "minecraft:" + rn;
    }

    /**
     * Gets the id of the block the player is looking at, or -1 if
     * the player is looking at either an entity or nothing.
     *
     * @return The block's id.
     */
    public static int getBlockId() {
        if (block == null) return -1;
        else return Block.getIdFromBlock(block);
    }

    /**
     * Gets the light level of the block the player is looking at, or
     * -1 if the player is looking at either an entity or nothing.
     *
     * @return The block's light level.
     */
    public static int getBlockLightLevel() {
        BlockPos newPos = getBlockOnFace();
        if (pos == null || newPos == null) return -1;

        if (block.getLightValue() != 0) return block.getLightValue();
        else return mc.theWorld.getLightFor(EnumSkyBlock.BLOCK, newPos);
    }

    /**
     * Gets whether or not the block the player is looking at is on fire.
     *
     * @return True if the block is on fire, false otherwise.
     */
    public static boolean isBlockOnFire() {
        return pos != null && mc.theWorld.getBlockState(pos.up()).getBlock().getUnlocalizedName().equals("tile.fire");
    }

    /**
     * Gets whether or not the entity the player is looking at is human
     * (a player type).
     *
     * @return True if the entity is human, false otherwise.
     */
    public static boolean isEntityHuman() {
        return entity != null && entity instanceof EntityPlayer;
    }

    /**
     * Gets the display name of the entity the player is looking at,
     * or "null" if the player is looking at either a block or nothing.
     *
     * @return The entity's display name.
     */
    public static String getEntityDisplayName() {
        if (entity == null) return "null";
        return entity.getCustomNameTag();
    }

    /**
     * Gets the X motion of the entity the player is looking at.
     *
     * @return The X motion of the entity.
     */
    public static double getEntityMotionX() {
        if (entity == null) return 0d;
        return entity.motionX;
    }

    /**
     * Gets the Y motion of the entity the player is looking at.
     *
     * @return The Y motion of the entity.
     */
    public static double getEntityMotionY() {
        if (entity == null) return 0d;
        return entity.motionY;
    }

    /**
     * Gets the Z motion of the entity the player is looking at.
     *
     * @return The Z motion of the entity.
     */
    public static double getEntityMotionZ() {
        if (entity == null) return 0d;
        return entity.motionZ;
    }

    /**
     * Gets the team name of the entity the player is looking at, or "null"
     * if there is no team, or if the player is looking at either a block
     * or nothing.
     *
     * @return The entity's team name, or null if nonexistent.
     */
    public static String getEntityTeamName() {
        if (entity == null) return "null";
        if (!(entity instanceof EntityLivingBase) || ((EntityLivingBase) entity).getTeam() == null) return null;

        return ((EntityLivingBase) entity).getTeam().getRegisteredName();
    }

    /**
     * Gets the NBT data of the entity the player is looking at, or "null"
     * if the player is looking at either a block or nothing.
     *
     * @return A JSON string containing the entity NBT data.
     */
    public static String getEntityNBTData() {
        if (entity == null) return "null";

        NBTTagCompound tags = new NBTTagCompound();
        entity.writeToNBT(tags);
        HashMap<Object, Object> metadata = new HashMap<>();

        for (String key : tags.getKeySet()) {
            if (!tags.getTag(key).toString().startsWith("[") && !tags.getTag(key).toString().startsWith("{")) {
                if (key.equalsIgnoreCase("healf") || key.equalsIgnoreCase("health") || key.equalsIgnoreCase("absorbtionamount")) {
                    continue;
                }

                String val = tags.getTag(key).toString();
                Object targetVal;

                if (val.equals("0b")) {
                    targetVal = false;
                } else if (val.equals("1b")) {
                    targetVal = true;
                } else if (val.matches("-?\\d+L")) {
                    targetVal = Long.valueOf(val.substring(0, val.length() - 1));
                } else if (val.matches("-?\\d+\\.\\d+f")) {
                    targetVal = Float.valueOf(val);
                } else {
                    targetVal = val;
                }

                metadata.put(key, targetVal);
            }
        }

        return (new Gson()).toJsonTree(metadata).getAsJsonObject().toString();
    }

    private static BlockPos getBlockOnFace() {
        switch (mop.sideHit) {
            case UP:
                return pos.up();
            case DOWN:
                return pos.down();
            case NORTH:
                return pos.north();
            case SOUTH:
                return pos.south();
            case EAST:
                return pos.east();
            case WEST:
                return pos.west();
            default:
                return null;
        }
    }
}
