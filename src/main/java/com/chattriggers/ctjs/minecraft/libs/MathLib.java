package com.chattriggers.ctjs.minecraft.libs;

public class MathLib {
    /**
     * Maps a number from one range to another.
     *
     * @param number  the number to map
     * @param in_min  the original range min
     * @param in_max  the original range max
     * @param out_min the final range min
     * @param out_max the final range max
     * @return the re-mapped number
     */
    public static float map(float number, float in_min, float in_max, float out_min, float out_max) {
        return (number - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}
