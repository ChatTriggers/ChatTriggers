package com.chattriggers.ctjs.minecraft.objects;

import com.chattriggers.ctjs.minecraft.wrappers.Player;
import lombok.Getter;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;

public class Block {
    @Getter
    private net.minecraft.block.Block block;
    private BlockPos blockPos = new BlockPos(0, 0, 0);

    /* Constructors */

    /**
     * Creates a Block object from a minecraft block input.<br>
     * This method is not meant for public use.
     * @param block the minecraft block input
     */
    public Block(net.minecraft.block.Block block) {
        this.block = block;
    }

    /**
     * Creates a Block object from a string name input.
     * @param blockName the name of the block
     */
    public Block(String blockName) {
        this.block = net.minecraft.block.Block.getBlockFromName(blockName);
    }

    /**
     * Creates a Block object from an integer ID input.
     * @param blockID the ID of the block
     */
    public Block(int blockID) {
        this.block = net.minecraft.block.Block.getBlockById(blockID);
    }

    /**
     * Creates a Block object from an {@link Item} object input.
     * @param item the {@link Item}
     */
    public Block(Item item) {
        this.block = net.minecraft.block.Block.getBlockFromItem(item.getItem());
    }
    /* End of constructors */

    /**
     * Sets the block position in the world.<br>
     * This is automatically set by {@link Player#lookingAt()}.<br>
     * This method is not meant for public use.
     * @param blockPos the block position
     * @return the Block object
     */
    public Block setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
        return this;
    }

    /**
     * Gets the block's ID.
     * @return the block's ID
     */
    public int getID() {
        return net.minecraft.block.Block.getIdFromBlock(this.block);
    }

    /**
     * Gets the block's registry name.<br>
     * Example: <code>minecraft:planks</code>
     * @return the block's registry name
     */
    public String getRegistryName() {
        return this.block.getRegistryName();
    }

    /**
     * Gets the block's unlocalized name.<br>
     * Example: <code>tile.wood</code>
     * @return the block's unlocalized name
     */
    public String getUnlocalizedName() {
        return this.block.getUnlocalizedName();
    }

    /**
     * Gets the block's localized name.<br>
     * Example: <code>Wooden Planks</code>
     * @return the block's localized name
     */
    public String getName() {
        return this.block.getLocalizedName();
    }

    /**
     * Gets the block's light value.<br>
     * The level of light the block gives off.
     * @return the block's light value
     */
    public int getLightValue() {
        return this.block.getLightValue();
    }

    /**
     * Gets the block's block state.
     * @return the block's block state
     */
    public BlockState getState() {
        return this.block.getBlockState();
    }

    /**
     * Gets the block's default state.
     * @return the block's default state
     */
    public IBlockState getDefaultState() {
        return this.block.getDefaultState();
    }

    /**
     * Gets the block's meta from the default state.
     * @return the block's meta
     */
    public int getMeta() {
        return this.block.getMetaFromState(getDefaultState());
    }

    /**
     * Gets the block's x position.<br>
     * 0 by default.
     * @return the block's x position
     */
    public int getX() {
        return this.blockPos.getX();
    }

    /**
     * Gets the block's y position.<br>
     * 0 by default.
     * @return the block's y position
     */
    public int getY() {
        return this.blockPos.getY();
    }

    /**
     * Gets the block's z position.<br>
     * 0 by default.
     * @return the block's z position
     */
    public int getZ() {
        return this.blockPos.getZ();
    }
}
