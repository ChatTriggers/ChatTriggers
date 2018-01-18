package com.chattriggers.ctjs.minecraft.wrappers;

public class Inventory {
    /**
     * Gets the slot of the player's currently held item.
     * @return {@link InventorySlot} of currently selected hotbar position.
     */
    public static InventorySlot getHeldItem() {
        return new InventorySlot(Player.getPlayer().inventory.currentItem, false);
    }

    public static InventorySlot getHelmet() {
        return new InventorySlot(3, true);
    }

    public static InventorySlot getChestplate() {
        return new InventorySlot(2, true);
    }

    public static InventorySlot getLeggings() {
        return new InventorySlot(1, true);
    }

    public static InventorySlot getBoots() {
        return new InventorySlot(0, true);
    }

    public static InventorySlot getItemInSlot(int slot) {
        return new InventorySlot(slot, false);
    }

}
