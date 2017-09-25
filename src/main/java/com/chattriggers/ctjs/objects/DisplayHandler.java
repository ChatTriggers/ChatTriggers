package com.chattriggers.ctjs.objects;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class DisplayHandler {
    private ArrayList<Display> displays;


    public DisplayHandler() {
        displays = new ArrayList<>();
    }

    /**
     * Registers a display to be rendered every tick.
     * @param display the display to be rendered
     */
    public void registerDisplay(Display display) {
        this.displays.add(display);
    }

    public void clearDisplays() {
        displays.clear();
    }

    @SubscribeEvent
    public void renderDisplays(RenderGameOverlayEvent event) {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            for (Display display : displays) {
                display.render();
            }
        }
    }

    // background type
    public enum Background {
        NONE, FULL, PER_LINE
    }

    // text align
    public enum Align {
        LEFT, CENTER, RIGHT
    }

    // line order
    public enum Order {
        UP, DOWN
    }
}
