package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class KeyAction extends Action {
    /**
     * Which key to act as if has been clicked (REQUIRED).
     * Options currently are 0-8, representing the hotbar keys
     *
     * @param key which key to "click"
     */
    @Setter @Getter
    private int key;

    public KeyAction(int slot, int windowId) {
        super(slot, windowId);
    }

    @Override
    public void complete() {
        //#if MC<=10809
        //$$ doClick(key, 2);
        //#else
        doClick(key, net.minecraft.inventory.ClickType.SWAP);
        //#endif
    }
}
