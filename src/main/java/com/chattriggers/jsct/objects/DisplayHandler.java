package com.chattriggers.jsct.objects;

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

    /**
     * Render all of the display currently registered.
     */
    public void renderDisplays() {
        for (Display display : displays) {
            display.render();
        }
    }
}
