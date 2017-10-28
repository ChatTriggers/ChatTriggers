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
     * is not looking at anything, returns null.
     *
     * @return The object type.
     */
    public static String getType() {
        if (entity != null) return "entity";
        else if (block != null) return "block";
        else return null;
    }

    /**
     * Gets the name of the object the player is looking at.
     *
     * Example return values: "Sandstone", "Wolf", "Oak Fence", "Zombie Pigman".
     * Returns null if the player is not looking at anything.
     *
     * @return The object name.
     */
    public static String getName() {
        if (entity != null) return entity.getName();
        else if (block != null) return block.getLocalizedName();
        else return null;
    }

    /**
     * Gets the distance of the object from the player.
     *
     * @return The distance of the object from the player.
     */
    public static Double getDistanceFromPlayer() {
        if (block != null) {
            return Math.sqrt(Math.pow(pos.getX() - mc.thePlayer.posX, 2) + Math.pow(pos.getY() - mc.thePlayer.posY, 2) + Math.pow(pos.getZ() - mc.thePlayer.posZ, 2));
        } else if (entity != null) {
            return (double) entity.getDistanceToEntity(mc.thePlayer);
        } else {
            return null;
        }
    }

    /**
     * Gets the X coordinate of the object the player is looking at.
     *
     * @return A float number representing the object's X coordinate.
     */
    public static Double getPosX() {
        if (entity != null) return entity.posX;
        else if (pos != null) return (double) pos.getX();
        else return null;
    }

    /**
     * Gets the Y coordinate of the object the player is looking at.
     *
     * @return A float number representing the object's Y coordinate.
     */
    public static Double getPosY() {
        if (entity != null) return entity.posY;
        else if (pos != null) return (double) pos.getY();
        else return null;
    }

    /**
     * Gets the Z coordinate of the object the player is looking at.
     *
     * @return A float number representing the object's Z coordinate.
     */
    public static Double getPosZ() {
        if (entity != null) return entity.posZ;
        else if (pos != null) return (double) pos.getY();
        else return null;
    }

    /**
     * Gets the metadata of the block the player is looking at, or null
     * if the player is looking at either an entity or nothing.
     *
     * @return The block's metadata.
     */
    public static Integer getBlockMetadata() {
        if (block == null) return null;
        return block.getMetaFromState(mc.theWorld.getBlockState(pos));
    }

    /**
     * Gets the unlocalized name of the block the player is looking at,
     * or null if the player is looking at either an entity or nothing.
     *
     * @return The block's unlocalized name.
     */
    public static String getBlockUnlocalizedName() {
        if (block == null) return null;
        return block.getUnlocalizedName().replace("tile.", "");
    }

    /**
     * Gets the registry name of the block the player is looking at,
     * or null if the player is looking at either an entity or nothing.
     *
     * @return The block's registry name.
     */
    public static String getBlockRegistryName() {
        if (block == null) return null;

        String registryName = block.getRegistryName().replace("minecraft:", "");

        if (registryName.startsWith("double_") && registryName.endsWith("_slab")) registryName = registryName.substring(7);
        if (registryName.startsWith("lit_") && !registryName.endsWith("pumpkin")) registryName = registryName.substring(4);

        return registryName;
    }

    /**
     * Gets the id of the block the player is looking at, or null if
     * the player is looking at either an entity or nothing.
     *
     * @return The block's id.
     */
    public static Integer getBlockId() {
        if (block == null) return null;
        else return Block.getIdFromBlock(block);
    }

    /**
     * Gets the light level of the block the player is looking at, or
     * null if the player is looking at either an entity or nothing.
     *
     * @return The block's light level.
     */
    public static Integer getBlockLightLevel() {
        BlockPos newPos = getBlockOnFace();
        if (pos == null || newPos == null) return null;

        if (block.getLightValue() != 0) return block.getLightValue();
        else return mc.theWorld.getLightFor(EnumSkyBlock.BLOCK, newPos);
    }

    /**
     * Gets whether or not the block the player is looking at is on fire,
     * or null if the player is looking at either an entity or nothing.
     *
     * @return True if the block is on fire.
     */
    public static Boolean isBlockOnFire() {
        if (pos == null) return null;
        return mc.theWorld.getBlockState(pos.up()).getBlock().getUnlocalizedName().equals("tile.fire");
    }

    /**
     * Gets whether or not the entity the player is looking at is human
     * (a player type), or null if the player is looking at either a
     * block or nothing.
     *
     * @return True if the entity is human.
     */
    public static Boolean isEntityHuman() {
        if (entity == null) return null;
        return entity instanceof EntityPlayer;
    }

    /**
     * Gets the display name of the entity the player is looking at,
     * or null if the player is looking at either a block or nothing.
     *
     * @return The entity's display name.
     */
    public static String getEntityDisplayName() {
        if (entity == null) return null;
        return entity.getCustomNameTag();
    }

    /**
     * Gets the X motion of the entity the player is looking at, or
     * null if the player is looking at either a block or nothing.
     *
     * @return The X motion of the entity.
     */
    public static Double getEntityMotionX() {
        if (entity == null) return null;
        return entity.motionX;
    }

    /**
     * Gets the Y motion of the entity the player is looking at, or
     * null if the player is looking at either a block or nothing.
     *
     * @return The Y motion of the entity.
     */
    public static Double getEntityMotionY() {
        if (entity == null) return null;
        return entity.motionY;
    }

    /**
     * Gets the Z motion of the entity the player is looking at, or
     * null if the player is looking at either a block or nothing.
     *
     * @return The Z motion of the entity.
     */
    public static Double getEntityMotionZ() {
        if (entity == null) return null;
        return entity.motionZ;
    }

    /**
     * Gets the team name of the entity the player is looking at, or null
     * if there is no team, or if the player is looking at either a block
     * or nothing.
     *
     * @return The entity's team name, or null if nonexistent.
     */
    public static String getEntityTeamName() {
        if (entity == null) return null;
        if (!(entity instanceof EntityLivingBase) || ((EntityLivingBase) entity).getTeam() == null) return null;

        return ((EntityLivingBase) entity).getTeam().getRegisteredName();
    }

    /**
     * Gets the NBT data of the entity the player is looking at, or null
     * if the player is looking at either a block or nothing.
     *
     * @return A JSON string containing the entity NBT data.
     */
    public static String getEntityNBTData() {
        if (entity == null) return null;

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

    private static String getBlockOnFaceName() {
        switch (mop.sideHit) {
            case UP:
                return mc.theWorld.getBlockState(pos.up()).getBlock().getUnlocalizedName();
            case DOWN:
                return mc.theWorld.getBlockState(pos.down()).getBlock().getUnlocalizedName();
            case NORTH:
                return mc.theWorld.getBlockState(pos.north()).getBlock().getUnlocalizedName();
            case SOUTH:
                return mc.theWorld.getBlockState(pos.south()).getBlock().getUnlocalizedName();
            case EAST:
                return mc.theWorld.getBlockState(pos.east()).getBlock().getUnlocalizedName();
            case WEST:
                return mc.theWorld.getBlockState(pos.west()).getBlock().getUnlocalizedName();
            default:
                return null;
        }
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
