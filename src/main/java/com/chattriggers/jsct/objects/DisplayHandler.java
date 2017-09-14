package com.chattriggers.jsct.objects;

import java.util.ArrayList;

public class DisplayHandler {
    private ArrayList<Display> displays;

    public DisplayHandler() {
        displays = new ArrayList<>();
    }

    public void registerDisplay(Display display) {
        this.displays.add(display);
    }

    public void renderDisplays() {
        for (Display display : displays) {
            display.render();
        }
    }
}
