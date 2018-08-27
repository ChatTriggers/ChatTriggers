// package com.chattriggers.ctjs.minecraft.wrappers.objects.block;
//
// import com.chattriggers.ctjs.minecraft.wrappers.Player;
// import com.chattriggers.ctjs.minecraft.wrappers.World;
// import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item;
// import lombok.Getter;
// import net.minecraft.block.state.IBlockState;
//
// //#if MC<=10809
// import net.minecraft.util.BlockPos;
// //#else
// //$$ import net.minecraft.util.math.BlockPos;
// //#endif
//
// public class  Block {
//     protected net.minecraft.block.Block block;
//     @Getter
//     private BlockPos blockPos = new BlockPos(0, 0, 0);
//
//     /* Constructors */
//     Block() {}
//
//     public net.minecraft.block.Block getBlock() {
//         return block;
//     }
//
//     /**
//      * Creates a Block object from a minecraft block input.<br>
//      * This method is not meant for public use.
//      *
//      * @param block the minecraft block input
//      */
//     public Block(net.minecraft.block.Block block) {
//         this.block = block;
//     }
//
//     /**
//      * Creates a Block object from a string name input.
//      *
//      * @param blockName the name of the block
//      */
//     public Block(String blockName) {
//         this.block = net.minecraft.block.Block.getBlockFromName(blockName);
//     }
//
//     /**
//      * Creates a Block object from an integer ID input.
//      *
//      * @param blockID the ID of the block
//      */
//     public Block(int blockID) {
//         this.block = net.minecraft.block.Block.getBlockById(blockID);
//     }
//
//     /**
//      * Creates a Block object from an {@link Item} object input.
//      *
//      * @param item the {@link Item}
//      */
//     public Block(Item item) {
//         this.block = net.minecraft.block.Block.getBlockFromItem(item.getItem());
//     }
//     /* End of constructors */
//
//     /**
//      * Sets the block position in the world.<br>
//      * This is automatically set by {@link Player#lookingAt()}.<br>
//      * This method is not meant for public use.
//      *
//      * @param blockPos the block position
//      * @return the Block object
//      */
//     public Block setBlockPos(BlockPos blockPos) {
//         this.blockPos = blockPos;
//         return this;
//     }
//
//     /**
//      * Gets the block's ID.
//      *
//      * @return the block's ID
//      */
//     public int getID() {
//         return net.minecraft.block.Block.getIdFromBlock(this.block);
//     }
//
//     /**
//      * Gets the block's registry name.<br>
//      * Example: <code>minecraft:planks</code>
//      *
//      * @return the block's registry name
//      */
//     public String getRegistryName() {
//         //#if MC<=10809
//         return this.block.getRegistryName();
//         //#else
//         //$$ return this.block.getRegistryName().toString();
//         //#endif
//     }
//
//     /**
//      * Gets the block's unlocalized name.<br>
//      * Example: <code>tile.wood</code>
//      *
//      * @return the block's unlocalized name
//      */
//     public String getUnlocalizedName() {
//         return this.block.getUnlocalizedName();
//     }
//
//     /**
//      * Gets the block's localized name.<br>
//      * Example: <code>Wooden Planks</code>
//      *
//      * @return the block's localized name
//      */
//     public String getName() {
//         return this.block.getLocalizedName();
//     }
//
//     /**
//      * Gets the block's light value.<br>
//      * The level of light the block gives off.
//      *
//      * @return the block's light value
//      */
//     public int getLightValue() {
//         //#if MC<=10809
//         return this.block.getLightValue();
//         //#else
//         //$$ return this.block.getLightValue(
//         //$$         World.getWorld().getBlockState(this.blockPos),
//         //$$         World.getWorld(),
//         //$$         this.getBlockPos()
//         //$$ );
//         //#endif
//     }
//
//     /**
//      * Gets the block's block state.
//      *
//      * @return the block's block state
//      */
//     public IBlockState getState() {
//         return World.getWorld().getBlockState(this.getBlockPos());
//     }
//
//     /**
//      * Gets the block's default state.
//      *
//      * @return the block's default state
//      */
//     public IBlockState getDefaultState() {
//         return this.block.getDefaultState();
//     }
//
//     /**
//      * Gets the block's x position.<br>
//      * 0 by default.
//      *
//      * @return the block's x position
//      */
//     public int getX() {
//         return this.getBlockPos().getX();
//     }
//
//     /**
//      * Gets the block's y position.<br>
//      * 0 by default.
//      *
//      * @return the block's y position
//      */
//     public int getY() {
//         return this.getBlockPos().getY();
//     }
//
//     /**
//      * Gets the block's z position.<br>
//      * 0 by default.
//      *
//      * @return the block's z position
//      */
//     public int getZ() {
//         return this.getBlockPos().getZ();
//     }
//
//     /**
//      * Gets the current metadata of the block
//      *
//      * @return the metadata
//      */
//     public int getMetadata() {
//         return this.block.getMetaFromState(this.getState());
//     }
//
//     /**
//      * Gets the block's meta from the default state.
//      *
//      * @return the block's meta
//      */
//     public int getDefaultMetadata() {
//         return this.block.getMetaFromState(getDefaultState());
//     }
//
//     /**
//      * Checks whether this block can output redstone power
//      *
//      * @return whether the block provides power
//      */
//     public boolean canProvidePower() {
//         //#if MC<=10809
//         return this.block.canProvidePower();
//         //#else
//         //$$ return this.block.canProvidePower(
//         //$$         World.getWorld().getBlockState(this.blockPos)
//         //$$ );
//         //#endif
//     }
//
//     /**
//      * Checks whether the block is receiving power
//      *
//      * @return whether the block is receiving power
//      */
//     public boolean isPowered() {
//         return World.getWorld().isBlockPowered(this.blockPos);
//     }
//
//     /**
//      * Gets the redstone power level of the block
//      *
//      * @return the redstone strength
//      */
//     public int getRedstoneStength() {
//         return World.getWorld().getStrongPower(this.blockPos);
//     }
//
//     /**
//      * Checks whether the block can be mined with the tool in the player's hand
//      *
//      * @return whether the block can be mined
//      */
//     public boolean canBeHarvested() {
//         return this.block.canHarvestBlock(World.getWorld(), this.blockPos, Player.getPlayer());
//     }
//
//     /**
//      * Checks whether the block can be mined with a certain item
//      *
//      * @param item the item to use when checking if the block can be broken
//      * @return whether the block can be mined
//      */
//     public boolean canBeHarvestedWith(Item item) {
//         return item.canHarvest(this);
//     }
//
//     /**
//      * Gets whether this block is translucent, i.e. is see through.
//      * Any translucent block will not suffocate entities.
//      *
//      * @return whether the block is translucent.
//      */
//     public boolean isTranslucent() {
//         //#if MC<=10809
//         return this.block.isTranslucent();
//         //#else
//         //$$ return this.block.isTranslucent(
//         //$$         World.getWorld().getBlockState(this.blockPos)
//         //$$ );
//         //#endif
//     }
//
//     @Override
//     public String toString() {
//         return "Block{"
//                 + "name=" + this.block.getRegistryName()
//                 + ", x=" + getX()
//                 + ", y=" + getY()
//                 + ", z=" + getZ()
//                 + "}";
//     }
// }
