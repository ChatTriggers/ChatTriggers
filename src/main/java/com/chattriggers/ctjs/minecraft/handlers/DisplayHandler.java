package com.chattriggers.ctjs.minecraft.handlers;

import com.chattriggers.ctjs.minecraft.libs.EventLib;
import com.chattriggers.ctjs.minecraft.objects.Display;
import net.minecraft.client.renderer.GlStateManager;
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
        GlStateManager.pushMatrix();

        if (EventLib.getType(event) == RenderGameOverlayEvent.ElementType.TEXT) {
            for (Display display : displays) {
                display.render();
            }
        }

        GlStateManager.popMatrix();
    }

    /**
     * The background type of a display.
     */
    public enum Background {
        NONE, FULL, PER_LINE;

        public static Background getBackgroundByName(String background) {
            switch(background.toUpperCase()) {
                case("FULL"):
                    return FULL;
                case("PER LINE"):
                case("PER_LINE"):
                    return PER_LINE;
                default:
                    return NONE;
            }
        }
    }

    /**
     * The alignment of the text in a display.
     */
    public enum Align {
        NONE, LEFT, CENTER, RIGHT;

        public static Align getAlignByName(String align) {
            switch (align.toUpperCase()) {
                case("LEFT"):
                    return LEFT;
                case("RIGHT"):
                    return RIGHT;
                case("CENTER"):
                    return CENTER;
                default:
                    return NONE;
            }
        }
    }

    /**
     * The order that a display should be drawn in.
     */
    public enum Order {
        UP, DOWN;

        public static Order getOrderByName(String order) {
            switch (order.toUpperCase()) {
                case("UP"):
                    return UP;
                default:
                    return DOWN;
            }
        }
    }
}
