package com.chattriggers.ctjs.minecraft.wrappers;

import com.chattriggers.ctjs.minecraft.libs.MinecraftVars;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InventorySlot {
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
        if (this.isArmor) return MinecraftVars.getPlayer().inventory.armorInventory[this.id];
        else return MinecraftVars.getPlayer().inventory.getStackInSlot(this.id);
    }
}