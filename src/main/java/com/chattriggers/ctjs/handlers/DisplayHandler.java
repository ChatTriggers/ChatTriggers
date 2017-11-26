package com.chattriggers.ctjs.handlers;

import com.chattriggers.ctjs.objects.Display;
import lombok.Getter;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class DisplayHandler {
    private ArrayList<Display> displays;


    public DisplayHandler() {
        displays = new ArrayList<>();
    }

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

    /**
     * The background type of a display.
     */
    public enum Background {
        NONE, FULL, PER_LINE
    }

    /**
     * The alignment of the text in a display.
     */
    public enum Align {
        LEFT, CENTER, RIGHT;

        public static Align getAlignByName(String align) {
            switch (align.toUpperCase()) {
                case("RIGHT"):
                    return RIGHT;
                case("CENTER"):
                    return CENTER;
                default:
                    return LEFT;
            }
        }
    }

    /**
     * The order that a display should be drawn in.
     */
    public enum Order {
        UP, DOWN
    }
}
