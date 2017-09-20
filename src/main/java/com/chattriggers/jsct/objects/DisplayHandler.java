package com.chattriggers.jsct.objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
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

    /**
     * gets the current resolution width scaled to guiScale.
     * @return scaled width
     */
    public static int getRenderWidth() {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        return res.getScaledWidth();
    }

    /**
     * gets the current resolution height scaled to guiScale.
     * @return scaled height
     */
    public static int getRenderHeight() {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        return res.getScaledHeight();
    }

    /**
     * Gets a color int based on 0-255 rgba values.
     * This can be used in settings background and text color.
     * TODO: maybe move to somewhere more useful
     * @param red value between 0 and 255
     * @param green value between 0 and 255
     * @param blue value between 0 and 255
     * @param alpha value between 0 and 255
     * @return integer color
     */
    public static int color(int red, int green, int blue, int alpha) {
        if (red > 255) red = 255;
        if (green > 255) green = 255;
        if (blue > 255) blue = 255;
        if (red < 0) red = 0;
        if (green < 0) green = 0;
        if (blue < 0) blue = 0;
        return (alpha * 0x1000000) + (red * 0x10000) + (green * 0x100) + blue;
    }
}
