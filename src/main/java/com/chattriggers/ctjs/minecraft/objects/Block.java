package com.chattriggers.ctjs.minecraft.objects;

import lombok.Getter;
import net.minecraft.util.BlockPos;

public class Block {
    @Getter
    private net.minecraft.block.Block block;
    private BlockPos blockPos = new BlockPos(0, 0, 0);

    /* Constructors */
    public Block(net.minecraft.block.Block block) {
        this.block = block;
    }

    public Block(String blockName) {
        this.block = net.minecraft.block.Block.getBlockFromName(blockName);
    }

    public Block(int blockID) {
        this.block = net.minecraft.block.Block.getBlockById(blockID);
    }

    public Block(net.minecraft.item.Item item) {
        this.block = net.minecraft.block.Block.getBlockFromItem(item);
    }

    public Block(Item item) {
        this.block = net.minecraft.block.Block.getBlockFromItem(item.getItem());
    }
    /* End of constructors */

    public Block setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
        return this;
    }

    public int getID() {
        return net.minecraft.block.Block.getIdFromBlock(this.block);
    }

    public String getRegistryName() {
        return this.block.getRegistryName();
    }

    public String getUnlocalizedName() {
        return this.block.getUnlocalizedName();
    }

    public int getX() {
        return this.blockPos.getX();
    }

    public int getY() {
        return this.blockPos.getY();
    }

    public int getZ() {
        return this.blockPos.getZ();
    }
}
