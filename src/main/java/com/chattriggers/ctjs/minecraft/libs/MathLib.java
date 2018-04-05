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

    /**
     * Clamps a number between two values.
     *
     * @param number the number to clamp
     * @param min    the minimum
     * @param max    the maximum
     * @return the clamped number
     */
    public static float clampFloat(float number, float min, float max) {
        return number < min ? min : number > max ? max : number;
    }

    /**
     * Clamps a number between two values.
     *
     * @param number the number to clamp
     * @param min    the minimum
     * @param max    the maximum
     * @return the clamped number
     */
    public static int clamp(int number, int min, int max) {
        return number < min ? min : number > max ? max : number;
    }
}
