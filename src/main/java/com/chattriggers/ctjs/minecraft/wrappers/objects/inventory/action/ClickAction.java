// package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action;
//
// import com.chattriggers.ctjs.minecraft.wrappers.Player;
// import lombok.Getter;
// import lombok.Setter;
// import lombok.experimental.Accessors;
//
// @Accessors(chain = true)
// public class ClickAction extends Action {
//     /**
//      * The type of click (REQUIRED)
//      *
//      * @param clickType the new click type
//      */
//     @Setter @Getter
//     private ClickType clickType;
//
//     /**
//      * Whether the click should act as if shift is being held (defaults to false)
//      *
//      * @param holdingShift to hold shift or not
//      */
//     @Setter @Getter
//     private boolean holdingShift = false;
//
//     /**
//      * Whether the click should act as if an item is being held
//      * (defaults to whether there actually is an item in the hand)
//      *
//      * @param itemInHand to be holding an item or not
//      */
//     @Setter @Getter
//     private boolean itemInHand = Player.getPlayer().inventory.getCurrentItem() == null;
//
//     //#if MC>10809
//     //$$ /**
//     //$$  * Whether the click should try to pick up all items of said type in the inventory (essentially double clicking)
//     //$$  * (defaults to whether there actually is an item in the hand)
//     //$$  *
//     //$$  * @param pickupAll to pickup all items of the same type
//     //$$  */
//     //$$ @Setter @Getter
//     //$$ private boolean pickupAll = false;
//     //#endif
//
//     /**
//      * The slot to click on (-999 if outside gui)
//      *
//      * @param slot the slot
//      * @param windowId the current window's id
//      */
//     public ClickAction(int slot, int windowId) {
//         super(slot, windowId);
//     }
//
//     /**
//      * Sets the type of click.
//      * Possible values are: LEFT, RIGHT, MIDDLE
//      *
//      * @param clickType the click type
//      * @return the current Action for method chaining
//      */
//     public ClickAction setClickString(String clickType) {
//         this.clickType = ClickType.valueOf(clickType.toUpperCase());
//         return this;
//     }
//
//     @Override
//     public void complete() {
//         //#if MC<=10809
//         int mode = 0;
//
//         if (this.clickType == ClickType.MIDDLE) {
//             mode = 3;
//         } else if (slot == -999 && !this.itemInHand) {
//             mode = 4;
//         } else if (this.holdingShift) {
//             mode = 1;
//         }
//         //#else
//         //$$ net.minecraft.inventory.ClickType mode;
//         //$$ if (this.clickType == ClickType.MIDDLE) {
//         //$$     mode = net.minecraft.inventory.ClickType.CLONE;
//         //$$ } else if (slot == -999 && !this.itemInHand) {
//         //$$     mode = net.minecraft.inventory.ClickType.THROW;
//         //$$ } else if (this.holdingShift) {
//         //$$     mode = net.minecraft.inventory.ClickType.QUICK_MOVE;;
//         //$$ } else if (pickupAll) {
//         //$$     mode = net.minecraft.inventory.ClickType.PICKUP_ALL;
//         //$$ } else {
//         //$$     mode = net.minecraft.inventory.ClickType.PICKUP;
//         //$$ }
//         //#endif
//
//         doClick(this.clickType.getButton(), mode);
//     }
//
//     public enum ClickType {
//         LEFT(0), RIGHT(1), MIDDLE(2);
//
//         @Getter
//         private int button;
//
//         ClickType(int button) {
//             this.button = button;
//         }
//     }
// }
