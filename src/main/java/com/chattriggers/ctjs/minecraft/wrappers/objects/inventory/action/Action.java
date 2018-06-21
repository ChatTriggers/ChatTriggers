package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action;

import com.chattriggers.ctjs.minecraft.wrappers.Client;
import com.chattriggers.ctjs.minecraft.wrappers.Player;
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Inventory;
import lombok.Getter;
import lombok.Setter;

public abstract class Action {
    @Getter @Setter
    protected int slot;
    @Getter @Setter
    protected int windowId;

    public Action(int slot, int windowId) {
        this.slot = slot;
        this.windowId = windowId;
    }

    public abstract void complete();

    protected void doClick(int button, int mode) {
        Client.getMinecraft().playerController.windowClick(
                this.windowId,
                this.slot,
                button,
                mode,
                Player.getPlayer()
        );
    }

    /**
     * Creates a new action.
     * The Inventory must be a container, see {@link Inventory#isContainer()}.
     * The slot can be -999 for outside of the gui
     *
     * @param inventory the inventory to complete the action on
     * @param slot the slot to complete the action on
     * @param typeString the type of action to do (CLICK, DRAG, KEY)
     * @return the new action
     */
    public static Action of(Inventory inventory, int slot, String typeString) {
        Type type = Type.valueOf(typeString.toUpperCase());

        if (type == Type.CLICK) {
            return new ClickAction(slot, inventory.getWindowId());
        } else if (type == Type.DRAG) {
            return new DragAction(slot, inventory.getWindowId());
        } else if (type == Type.KEY) {
            return new KeyAction(slot, inventory.getWindowId());
        }

        throw new UnsupportedOperationException("Action of type " + typeString + " is not supported yet!");
    }

    public enum Type {
        CLICK, DRAG, KEY
    }
}
