package com.chattriggers.ctjs.minecraft.wrappers;

import com.chattriggers.ctjs.minecraft.wrappers.objects.Item;

public class Inventory {
    /**
     * Gets the slot of the player's currently held item.
     * @return {@link Item} of currently selected hotbar position.
     */
    public static Item getHeldItem() {
        return new Item(Player.getPlayer().inventory.getCurrentItem());
    }

    public static Item getItemInSlot(int slot) {
        return new Item(Player.getPlayer().getInventory()[slot]);
    }
}
