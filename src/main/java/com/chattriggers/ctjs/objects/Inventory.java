package com.chattriggers.ctjs.objects;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class Inventory {
    private static final Minecraft mc = Minecraft.getMinecraft();

    /**
     * Gets the slot of the player's currently held item.
     *
     * @return {@link InventorySlot} of currently selected hotbar position.
     */
    public static InventorySlot getHeldItem() {
        for (InventorySlot slot : InventorySlot.values()) {
            if (slot.id == mc.thePlayer.inventory.currentItem) return slot;
        }
        return null;
    }

    /**
     * Contains objects that represent common inventory slots.
     */
    public enum InventorySlot {
        hotbar1(0, false),  hotbar2(1, false),  hotbar3(2, false), hotbar4(3, false),  hotbar5(4, false),
        hotbar6(5, false), hotbar7(6, false),  hotbar8(7, false),  hotbar9(8, false),
        helmet(3, true), chestplate(2, true), leggings(1, true), boots(0, true);

        private int id;
        private boolean isArmor;

        InventorySlot(int id, boolean isArmor) {
            this.id = id;
            this.isArmor = isArmor;
        }

        /**
         * Gets whether or not the InventorySlot is empty.
         *
         * @return True if the InventorySlot is empty, otherwise
         *          returns true.
         */
        public boolean isEmpty() {
            return this.getItem() == null;
        }

        /**
         * Returns true if the item passed in is a block. Otherwise,
         * returns false.
         *
         * @return True if the item in the slot is a block.
         */
        public boolean isBlock() {
            if (this.isEmpty()) return false;
            return this.getItem().getMaxDamage() == 0;
        }

        /**
         * Gets the display name of {@code slot}.
         *
         * @return The display name of {@code slot}
         */
        public String getDisplayName() {
            if (this.isEmpty()) return null;
            return this.getItem().getDisplayName();
        }

        /**
         * Gets the registry name of the {@code slot}.
         *
         * @return The registry name of {@code slot}
         */
        public String getRegistryName() {
            if (this.isEmpty()) return null;
            return this.getItem().getItem().getRegistryName().replace("minecraft:", "");
        }

        /**
         * Gets the maximum durability of the {@code slot}, or null
         * if the content of {@code slot} is a block.
         *
         * @return The maximum durability of {@code slot}
         */
        public Integer getMaxDurability() {
            if (this.isEmpty() || this.isBlock()) return null;
            return this.getItem().getMaxDamage() + 1;
        }

        /**
         * Gets the currently durability of {@code slot}, or null if
         * the content of {@code slot} is a block.
         *
         * @return The current durability of {@code slot}
         */
        public Integer getDurability() {
            if (this.isEmpty() || this.isBlock()) return null;
            return this.getMaxDurability() - this.getItem().getItemDamage();
        }

        /**
         * Gets the stack size of {@code slot}.
         *
         * @return The stack size of {@code slot}
         */
        public Integer getStackSize() {
            if (this.isEmpty()) return null;
            return this.getItem().stackSize;
        }

        /**
         * Gets the metadata of {@code slot} as a string.
         *
         * @return The metadata of {@code slot}
         */
        public String getMetadata() {
            if (this.isEmpty()) return null;
            ItemStack item = this.getItem();

            String data = String.valueOf(item.getMetadata());
            NBTTagCompound armorNBT = item.getTagCompound();

            if (armorNBT != null && armorNBT.hasKey("display")) {
                String armorNBTBase = armorNBT.getTag("display").toString();
                String armorColor = null;
                if (armorNBTBase.startsWith("{color:")) {
                    armorColor = armorNBTBase.substring(7, armorNBTBase.indexOf("}"));
                    if (armorColor.contains(",")) {
                        armorColor = armorColor.substring(0, armorColor.indexOf(","));
                    }
                }

                if (data.equals("0") && armorColor != null) {
                    data = "#" + armorColor;
                }
            }

            return data;
        }

        private ItemStack getItem() {
            if (this.isArmor) return mc.thePlayer.inventory.armorInventory[this.id];
            else return mc.thePlayer.inventory.getStackInSlot(this.id);
        }
    }
}
