package com.chattriggers.ctjs.minecraft.objects.display;

import com.chattriggers.ctjs.minecraft.libs.EventLib;
import lombok.Getter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class DisplayHandler {
    private static DisplayHandler instance;

    public static DisplayHandler getInstance() {
        return instance;
    }

    private ArrayList<Display> displays;

    public DisplayHandler() {
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);

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
            displays.forEach(Display::render);
        }

        GlStateManager.popMatrix();
    }

    /**
     * The background type of a display.
     */
    public enum Background {
        NONE, FULL, PER_LINE;

        public static Background getBackgroundByName(String background) {
            return Background.valueOf(background.toUpperCase().replace(" ", "_"));
        }
    }

    /**
     * The alignment of the text in a display.
     */
    public enum Align {
        NONE, LEFT, CENTER, RIGHT;

        public static Align getAlignByName(String align) {
            return Align.valueOf(align.toUpperCase());
        }
    }

    /**
     * The order that a display should be drawn in.
     */
    public enum Order {
        UP, DOWN;

        public static Order getOrderByName(String order) {
            return Order.valueOf(order.toUpperCase());
        }
    }
}
