package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class DragAction extends Action {
    /**
     * The type of click (REQUIRED)
     *
     * @param clickType the new click type
     */
    @Setter @Getter
    private ClickType clickType;

    /**
     * The stage of this drag (REQUIRED)
     * BEGIN is when beginning the drag
     * SLOT is for each slot being dragged into
     * END is for ending the drag
     *
     * @param stage the stage
     */
    @Setter @Getter
    private Stage stage;

    public DragAction(int slot, int windowId) {
        super(slot, windowId);
    }

    /**
     * Sets the type of click.
     * Possible values are: LEFT, RIGHT, MIDDLE
     *
     * @param clickType the click type
     * @return the current Action for method chaining
     */
    public DragAction setClickString(String clickType) {
        this.clickType = ClickType.valueOf(clickType.toUpperCase());
        return this;
    }

    /**
     * Sets the stage of this drag.
     * Possible values are: BEGIN, SLOT, END {@link #stage}
     *
     * @param stage the stage
     * @return the current Action for method chaining
     */
    public DragAction setStageString(String stage) {
        this.stage = Stage.valueOf(stage.toUpperCase());
        return this;
    }

    @Override
    public void complete() {
        int button = clickType.getButton() & 3 | (stage.getStage() & 3) << 2;

        doClick(button, 5);
    }

    public enum ClickType {
        LEFT(0), RIGHT(1), MIDDLE(2);

        @Getter
        private int button;

        ClickType(int button) {
            this.button = button;
        }
    }

    public enum Stage {
        BEGIN(0), SLOT(1), END(2);

        @Getter
        private int stage;

        Stage(int stage) {
            this.stage = stage;
        }
    }
}
